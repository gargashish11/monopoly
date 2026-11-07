package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.PlayerType;
import com.ashish.monopoly.repository.PlayerProjection;
import com.ashish.monopoly.repository.PlayerRepository;
import com.ashish.monopoly.service.impl.DefaultPlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultPlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private DefaultPlayerService playerService;

    @Test
    public void testSaveAssignsTypes() {
        // Bank
        Player bank = new Player();
        bank.setName("bank");
        when(playerRepository.save(bank)).thenReturn(bank);
        playerService.save(bank);
        assertEquals(PlayerType.BANK, bank.getType());

        // Bot
        Player bot = new Player();
        bot.setName("BOT");
        when(playerRepository.save(bot)).thenReturn(bot);
        playerService.save(bot);
        assertEquals(PlayerType.BOT, bot.getType());

        // Human with null type
        Player human = new Player();
        human.setName("Charlie");
        human.setType(null);
        when(playerRepository.save(human)).thenReturn(human);
        playerService.save(human);
        assertEquals(PlayerType.HUMAN, human.getType());

        // Human with pre-set type
        Player custom = new Player();
        custom.setName("Dave");
        custom.setType(PlayerType.HUMAN);
        when(playerRepository.save(custom)).thenReturn(custom);
        playerService.save(custom);
        assertEquals(PlayerType.HUMAN, custom.getType());

        // Null name
        Player noName = new Player();
        noName.setName(null);
        noName.setType(null);
        when(playerRepository.save(noName)).thenReturn(noName);
        playerService.save(noName);
        assertNull(noName.getType());
    }

    @Test
    public void testSaveAll() {
        Player p1 = new Player();
        p1.setName("Alice");
        Player p2 = new Player();
        p2.setName("Bank");
        List<Player> list = List.of(p1, p2);
        when(playerRepository.saveAll(list)).thenReturn(list);

        List<Player> result = playerService.saveAll(list);
        assertEquals(2, result.size());
        assertEquals(PlayerType.HUMAN, p1.getType());
        assertEquals(PlayerType.BANK, p2.getType());
    }

    @Test
    public void testFindByNameAndByIdAndNamesAndProjections() {
        Player p = new Player();
        p.setId(1);
        p.setName("Alice");

        when(playerRepository.findByNameIgnoreCase("Alice")).thenReturn(Optional.of(p));
        when(playerRepository.findById(1)).thenReturn(Optional.of(p));
        when(playerRepository.findByNamesIgnoreCase(List.of("alice"))).thenReturn(List.of(p));

        PlayerProjection proj = mock(PlayerProjection.class);
        when(playerRepository.findAllProjectedByIdNotNull()).thenReturn(Set.of(proj));

        assertEquals(p, playerService.findByName("Alice").orElse(null));
        assertTrue(playerService.findByName(null).isEmpty());
        assertEquals(p, playerService.findById(1).orElse(null));
        assertEquals(List.of(p), playerService.findByNames(List.of(" Alice ")));
        assertTrue(playerService.findByNames(null).isEmpty());
        assertTrue(playerService.findByNames(List.of()).isEmpty());
        assertTrue(playerService.findByNames(java.util.Arrays.asList((String) null, "  ")).isEmpty());
        assertEquals(Set.of(proj), playerService.findAllProjectedByIdNotNull());
    }

    @Test
    public void testDeleteByIdBranches() {
        // 1. Not found
        when(playerRepository.findById(1)).thenReturn(Optional.empty());
        assertFalse(playerService.deleteById(1));

        // 2. Bank player
        Player bank = new Player();
        bank.setType(PlayerType.BANK);
        when(playerRepository.findById(2)).thenReturn(Optional.of(bank));
        assertFalse(playerService.deleteById(2));

        // 3. Bot player
        Player bot = new Player();
        bot.setType(PlayerType.BOT);
        when(playerRepository.findById(3)).thenReturn(Optional.of(bot));
        assertFalse(playerService.deleteById(3));

        // 4. Human player success
        Player human = new Player();
        human.setType(PlayerType.HUMAN);
        when(playerRepository.findById(4)).thenReturn(Optional.of(human));
        doNothing().when(playerRepository).deleteById(4);
        assertTrue(playerService.deleteById(4));

        // 5. Human player throws exception
        when(playerRepository.findById(5)).thenReturn(Optional.of(human));
        doThrow(new RuntimeException("DB error")).when(playerRepository).deleteById(5);
        assertFalse(playerService.deleteById(5));
    }

    @Test
    public void testUpdateNameFound() {
        Player p = new Player();
        p.setId(10);
        p.setName("OldName");
        when(playerRepository.findById(10)).thenReturn(Optional.of(p));
        when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArgument(0));

        Player updated = playerService.updateName(10, "NewName");
        assertEquals("NewName", updated.getName());
    }

    @Test
    public void testUpdateNameNotFoundThrows() {
        when(playerRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> playerService.updateName(99, "NewName"));
    }
}
