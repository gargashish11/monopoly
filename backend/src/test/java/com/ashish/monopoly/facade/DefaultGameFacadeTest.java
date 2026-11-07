package com.ashish.monopoly.facade;

import com.ashish.monopoly.converter.GameConverter;
import com.ashish.monopoly.converter.reverse.GameReverseConverter;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.impl.DefaultGameFacade;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultGameFacadeTest {

    @Mock
    private GameService gameService;

    @Mock
    private GameConverter gameConverter;

    @Mock
    private GameReverseConverter gameReverseConverter;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private DefaultGameFacade facade;

    @Test
    public void testSaveAndCreateGame() {
        GameData inputData = new GameData();
        Game game = new Game();
        Game savedGame = new Game();
        GameData resultData = new GameData();

        when(gameReverseConverter.convert(inputData)).thenReturn(game);
        when(gameService.save(game)).thenReturn(savedGame);
        when(gameConverter.convert(savedGame)).thenReturn(resultData);

        assertEquals(resultData, facade.save(inputData));

        List<Player> players = List.of(new Player());
        when(gameService.createGame(players, 2000)).thenReturn(savedGame);
        assertEquals(resultData, facade.createGame(players, 2000));
    }

    @Test
    public void testGetGameDataNotFoundThrows() {
        when(gameService.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> facade.getGameData(99));
    }

    @Test
    public void testGetGameDataWhenConvertedIsNull() {
        Game game = new Game();
        game.setId(1);

        when(gameService.findById(1)).thenReturn(Optional.of(game));
        when(gameConverter.convert(game)).thenReturn(null);

        assertNull(facade.getGameData(1));
    }

    @Test
    public void testGetGameDataWithoutTransactions() {
        Game game = new Game();
        game.setId(1);
        GameData data = new GameData();
        data.setId(1);
        data.setTransactions(null);

        when(gameService.findById(1)).thenReturn(Optional.of(game));
        when(gameConverter.convert(game)).thenReturn(data);

        GameData result = facade.getGameData(1);
        assertEquals(1, result.getId());
        assertNull(result.getTransactions());
    }

    @Test
    public void testGetGameDataSortsTransactionsAndLimits() {
        Game game = new Game();
        game.setId(1);

        GameData data = new GameData();
        data.setId(1);

        TransactionData tx1 = new TransactionData();
        tx1.setId(1);
        tx1.setCreatedDate(new Date(1000));

        TransactionData tx2 = new TransactionData();
        tx2.setId(2);
        tx2.setCreatedDate(new Date(3000));

        TransactionData txNullDate = new TransactionData();
        txNullDate.setId(3);
        txNullDate.setCreatedDate(null);

        data.setTransactions(new LinkedHashSet<>(List.of(tx1, tx2, txNullDate)));

        when(gameService.findById(1)).thenReturn(Optional.of(game));
        when(gameConverter.convert(game)).thenReturn(data);

        GameData result = facade.getGameData(1);
        assertEquals(3, result.getTransactions().size());
        Iterator<TransactionData> it = result.getTransactions().iterator();
        assertEquals(2, it.next().getId()); // newest date first
        assertEquals(1, it.next().getId());
    }

    @Test
    public void testFindAllProjectedByIdNotNull() {
        GameProjection proj = mock(GameProjection.class);
        when(gameService.findAllProjectedByIdNotNull()).thenReturn(Set.of(proj));
        assertEquals(Set.of(proj), facade.findAllProjectedByIdNotNull());
    }

    @Test
    public void testUpdateGameName() {
        // null data or null id
        assertThrows(IllegalArgumentException.class, () -> facade.updateGameName(null));
        assertThrows(IllegalArgumentException.class, () -> facade.updateGameName(new GameData()));

        // not found
        GameData d = new GameData();
        d.setId(99);
        when(gameService.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> facade.updateGameName(d));

        // success
        Game existing = new Game();
        existing.setId(10);
        existing.setName("Old");
        d.setId(10);
        d.setName("New");
        when(gameService.findById(10)).thenReturn(Optional.of(existing));

        assertTrue(facade.updateGameName(d));
        assertEquals("New", existing.getName());
        verify(gameService).save(existing);
    }

    @Test
    public void testDeleteGame() {
        doNothing().when(gameService).deleteGame(1);
        facade.deleteGame(1);
        verify(gameService).deleteGame(1);
    }
}
