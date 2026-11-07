package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.PlayerType;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.service.impl.DefaultGameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultGameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private GamePlayerService gamePlayerService;

    @InjectMocks
    private DefaultGameService gameService;

    @Test
    public void testGetAllIdsAndSaveAndFind() {
        when(gameRepository.getAllIds()).thenReturn(Set.of(1, 2));
        assertEquals(Set.of(1, 2), gameService.getAllIds());

        Game game = new Game();
        when(gameRepository.save(game)).thenReturn(game);
        assertEquals(game, gameService.save(game));

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        assertEquals(Optional.of(game), gameService.findById(1));

        when(gameRepository.findAll()).thenReturn(List.of(game));
        assertEquals(List.of(game), gameService.findAll());

        GameProjection proj = mock(GameProjection.class);
        when(gameRepository.findAllProjectedByIdNotNull()).thenReturn(Set.of(proj));
        assertEquals(Set.of(proj), gameService.findAllProjectedByIdNotNull());
    }

    @Test
    public void testCreateGameEmptyPlayersThrows() {
        assertThrows(IllegalArgumentException.class, () -> gameService.createGame(null, 1500));
        assertThrows(IllegalArgumentException.class, () -> gameService.createGame(Collections.emptyList(), 1500));

        Player emptyPlayer = new Player();
        emptyPlayer.setName("   ");
        assertThrows(IllegalArgumentException.class, () -> gameService.createGame(List.of(emptyPlayer), 1500));
    }

    @Test
    public void testCreateGameWithExistingAndNewPlayers() {
        Game game = new Game();
        game.setId(10);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        // Player inputs: Alice (existing), Bob (new human), Bot (new bot), Bank (existing but type HUMAN), null player, null name
        Player p1 = new Player(); p1.setName("Alice");
        Player p2 = new Player(); p2.setName("Bob");
        Player p3 = new Player(); p3.setName("Bot");
        Player p4 = new Player(); p4.setName("Bank");
        Player p5 = new Player(); p5.setName(null);

        Player existingAlice = new Player();
        existingAlice.setId(1);
        existingAlice.setName("Alice");
        existingAlice.setType(PlayerType.HUMAN);

        Player existingBank = new Player();
        existingBank.setId(2);
        existingBank.setName("Bank");
        existingBank.setType(PlayerType.HUMAN); // needs fix to BANK

        when(playerService.findByNames(anyList())).thenReturn(new ArrayList<>(List.of(existingAlice, existingBank)));

        when(playerService.saveAll(anyList())).thenAnswer(invocation -> {
            List<Player> toSave = invocation.getArgument(0);
            return toSave;
        });

        when(gamePlayerService.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        Game createdGame = gameService.createGame(Arrays.asList(p1, p2, p3, p4, p5, null), 2500);

        assertNotNull(createdGame);
        assertEquals(10, createdGame.getId());
        assertEquals(4, createdGame.getGamePlayers().size());

        // Verify Bank player balance is 30000 and player type updated
        GamePlayer bankGp = createdGame.getGamePlayers().stream()
                .filter(gp -> "Bank".equalsIgnoreCase(gp.getPlayer().getName()))
                .findFirst().orElseThrow();
        assertEquals(30000, bankGp.getBalance());
        assertEquals(PlayerType.BANK, bankGp.getPlayer().getType());
        verify(playerService).save(existingBank);

        // Verify normal player has initial balance 2500
        GamePlayer aliceGp = createdGame.getGamePlayers().stream()
                .filter(gp -> "Alice".equals(gp.getPlayer().getName()))
                .findFirst().orElseThrow();
        assertEquals(2500, aliceGp.getBalance());
    }

    @Test
    public void testCreateGameWithDefaultInitialBalance() {
        Game game = new Game();
        game.setId(20);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Player p = new Player();
        p.setName("Charlie");

        when(playerService.findByNames(anyList())).thenReturn(Collections.emptyList());
        when(playerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        when(gamePlayerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        // Passing null balance -> should use INITIAL_BALANCE (2000)
        Game createdGame = gameService.createGame(List.of(p), null);
        GamePlayer gp = createdGame.getGamePlayers().iterator().next();
        assertEquals(2000, gp.getBalance());

        // Passing negative balance -> should also fall back to 2000
        Game createdGameNeg = gameService.createGame(List.of(p), -500);
        GamePlayer gpNeg = createdGameNeg.getGamePlayers().iterator().next();
        assertEquals(2000, gpNeg.getBalance());
    }

    @Test
    public void testCreateGameWithPreExistingBankType() {
        Game game = new Game();
        game.setId(30);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Player bank = new Player();
        bank.setName("Bank");
        bank.setType(PlayerType.BANK);

        when(playerService.findByNames(anyList())).thenReturn(new ArrayList<>(List.of(bank)));
        when(gamePlayerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        Game createdGame = gameService.createGame(List.of(bank), 1000);
        GamePlayer gp = createdGame.getGamePlayers().iterator().next();
        assertEquals(30000, gp.getBalance());
        assertEquals(PlayerType.BANK, gp.getPlayer().getType());
        // Should not have called playerService.save since it's already BANK
        verify(playerService, never()).save(bank);
    }

    @Test
    public void testCreateGameWithNewBankPlayer() {
        Game game = new Game();
        game.setId(40);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Player bank = new Player();
        bank.setName("Bank");

        when(playerService.findByNames(anyList())).thenReturn(Collections.emptyList());
        when(playerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        when(gamePlayerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        Game createdGame = gameService.createGame(List.of(bank), 1000);
        GamePlayer gp = createdGame.getGamePlayers().iterator().next();
        assertEquals(PlayerType.BANK, gp.getPlayer().getType());
    }

    @Test
    public void testCreateGameCaseInsensitiveDeduplication() {
        Game game = new Game();
        game.setId(50);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Player p1 = new Player(); p1.setName("Alice");
        Player p2 = new Player(); p2.setName("alice");
        Player p3 = new Player(); p3.setName("ALICE");

        when(playerService.findByNames(List.of("Alice"))).thenReturn(Collections.emptyList());
        when(playerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        when(gamePlayerService.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        Game createdGame = gameService.createGame(List.of(p1, p2, p3), 1000);
        assertEquals(1, createdGame.getGamePlayers().size());
        verify(playerService).saveAll(argThat(list -> list.size() == 1));
    }

    @Test
    public void testDeleteGameBranches() {
        // null id
        gameService.deleteGame(null);
        verify(gameRepository, never()).deleteById(any());

        // non-existent id
        when(gameRepository.existsById(99)).thenReturn(false);
        gameService.deleteGame(99);
        verify(gameRepository, never()).deleteById(99);

        // existing id
        when(gameRepository.existsById(1)).thenReturn(true);
        doNothing().when(gameRepository).deleteById(1);
        gameService.deleteGame(1);
        verify(gameRepository).deleteById(1);
    }
}
