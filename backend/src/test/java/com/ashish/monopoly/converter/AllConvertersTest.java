package com.ashish.monopoly.converter;

import com.ashish.monopoly.converter.reverse.GamePlayerReverseConverter;
import com.ashish.monopoly.converter.reverse.GameReverseConverter;
import com.ashish.monopoly.converter.reverse.TransactionReverseConverter;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.data.GamePlayerData;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AllConvertersTest {

    @Test
    public void testConvertersUtilityClass() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Converters> constructor = Converters.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Converters instance = constructor.newInstance();
        assertNotNull(instance);

        assertThrows(IllegalArgumentException.class, () -> Converters.convertAll(List.of("A"), null));
        assertTrue(Converters.convertAll(null, s -> s).isEmpty());
        assertTrue(Converters.convertAll(Collections.emptyList(), s -> s).isEmpty());

        Set<String> converted = Converters.convertAll(List.of("hello", "world"), String::toUpperCase);
        assertEquals(Set.of("HELLO", "WORLD"), converted);
    }

    @Test
    public void testGameConverter() {
        GameConverter gameConverter = new GameConverter();
        GamePlayerConverter gamePlayerConverter = new GamePlayerConverter();
        TransactionConverter transactionConverter = new TransactionConverter();

        ReflectionTestUtils.setField(gameConverter, "gamePlayerConverter", gamePlayerConverter);
        ReflectionTestUtils.setField(gameConverter, "transactionConverter", transactionConverter);

        assertNull(gameConverter.convert(null));

        Game game = new Game();
        game.setId(1);
        game.setName("Game 1");

        // Without players or transactions
        GameData data = gameConverter.convert(game);
        assertEquals(1, data.getId());
        assertEquals("Game 1", data.getName());
        assertTrue(data.getGamePlayers().isEmpty());
        assertTrue(data.getTransactions().isEmpty());

        // With players and transactions
        Player player = new Player();
        player.setId(10);
        player.setName("Alice");

        GamePlayer gp = new GamePlayer();
        gp.setId(100);
        gp.setGame(game);
        gp.setPlayer(player);
        gp.setBalance(1500);
        game.getGamePlayers().add(gp);

        Transaction tx = new Transaction();
        tx.setId(200);
        tx.setGame(game);
        tx.setPayer(player);
        tx.setPayee(player);
        tx.setAmount(100);
        game.getTransactions().add(tx);

        GameData fullData = gameConverter.convert(game);
        assertEquals(1, fullData.getGamePlayers().size());
        assertEquals(1, fullData.getTransactions().size());
    }

    @Test
    public void testGamePlayerConverter() {
        GamePlayerConverter converter = new GamePlayerConverter();
        assertNull(converter.convert(null));

        // Missing game and player
        GamePlayer gpEmpty = new GamePlayer();
        gpEmpty.setId(1);
        gpEmpty.setBalance(500);
        GamePlayerData emptyData = converter.convert(gpEmpty);
        assertEquals(1, emptyData.getId());
        assertEquals(500, emptyData.getBalance());
        assertNull(emptyData.getGame_id());
        assertNull(emptyData.getPlayer_id());

        // Full game and player
        Game game = new Game();
        game.setId(2);
        game.setName("Test Game");
        Player player = new Player();
        player.setId(3);
        player.setName("Bob");

        GamePlayer gpFull = new GamePlayer();
        gpFull.setId(4);
        gpFull.setGame(game);
        gpFull.setPlayer(player);
        gpFull.setBalance(1000);

        GamePlayerData fullData = converter.convert(gpFull);
        assertEquals(4, fullData.getId());
        assertEquals(2, fullData.getGame_id());
        assertEquals("Test Game", fullData.getGame_name());
        assertEquals(3, fullData.getPlayer_id());
        assertEquals("Bob", fullData.getPlayer_name());
        assertEquals(1000, fullData.getBalance());
    }

    @Test
    public void testTransactionConverter() {
        TransactionConverter converter = new TransactionConverter();
        assertNull(converter.convert(null));

        Transaction txEmpty = new Transaction();
        txEmpty.setId(1);
        txEmpty.setAmount(50);
        txEmpty.setIsSuccess(true);
        Date now = new Date();
        txEmpty.setCreatedDate(now);

        TransactionData emptyData = converter.convert(txEmpty);
        assertEquals(1, emptyData.getId());
        assertEquals(50, emptyData.getAmount());
        assertTrue(emptyData.getIsSuccess());
        assertEquals(now, emptyData.getCreatedDate());
        assertNull(emptyData.getGame_id());
        assertNull(emptyData.getPayer_id());
        assertNull(emptyData.getPayee_id());

        Game game = new Game();
        game.setId(5);
        Player payer = new Player();
        payer.setId(6);
        payer.setName("Payer");
        Player payee = new Player();
        payee.setId(7);
        payee.setName("Payee");

        Transaction txFull = new Transaction();
        txFull.setId(2);
        txFull.setGame(game);
        txFull.setPayer(payer);
        txFull.setPayee(payee);
        txFull.setAmount(200);
        txFull.setIsSuccess(false);

        TransactionData fullData = converter.convert(txFull);
        assertEquals(2, fullData.getId());
        assertEquals(5, fullData.getGame_id());
        assertEquals(6, fullData.getPayer_id());
        assertEquals("Payer", fullData.getPayer_name());
        assertEquals(7, fullData.getPayee_id());
        assertEquals("Payee", fullData.getPayee_name());
        assertEquals(200, fullData.getAmount());
        assertFalse(fullData.getIsSuccess());
    }

    @Test
    public void testGameReverseConverter() {
        GameReverseConverter converter = new GameReverseConverter();
        GamePlayerReverseConverter gpConverter = new GamePlayerReverseConverter();
        ReflectionTestUtils.setField(converter, "gamePlayerReverseConverter", gpConverter);

        assertNull(converter.convert(null));

        GameData data = new GameData();
        data.setId(1);
        data.setName("Game Reverse");

        Game game = converter.convert(data);
        assertEquals(1, game.getId());
        assertEquals("Game Reverse", game.getName());

        GamePlayerData gpData = new GamePlayerData();
        gpData.setId(2);
        gpData.setBalance(500);
        data.getGamePlayers().add(gpData);

        Game gameWithPlayers = converter.convert(data);
        assertEquals(1, gameWithPlayers.getGamePlayers().size());
    }

    @Test
    public void testGamePlayerReverseConverter() {
        GamePlayerReverseConverter converter = new GamePlayerReverseConverter();
        assertNull(converter.convert(null));

        GamePlayerData data = new GamePlayerData();
        data.setBalance(300);

        GamePlayer gp = converter.convert(data);
        assertNull(gp.getGame());
        assertNull(gp.getPlayer());
        assertEquals(300, gp.getBalance());

        // player_id null but name present
        data.setPlayer_id(null);
        data.setPlayer_name("Bob");
        GamePlayer gpNameOnly = converter.convert(data);
        assertNotNull(gpNameOnly.getPlayer());
        assertEquals("Bob", gpNameOnly.getPlayer().getName());

        data.setGame_id(10);
        data.setPlayer_id(20);
        data.setPlayer_name("Alice");

        GamePlayer gpFull = converter.convert(data);
        assertEquals(10, gpFull.getGame().getId());
        assertEquals(20, gpFull.getPlayer().getId());
        assertEquals("Alice", gpFull.getPlayer().getName());
        assertEquals(300, gpFull.getBalance());
    }

    @Test
    public void testTransactionReverseConverterAllBranches() {
        TransactionReverseConverter converter = new TransactionReverseConverter();
        assertNull(converter.convert(null));

        // When ID is null
        TransactionData d1 = new TransactionData();
        d1.setAmount(100);
        Transaction tx1 = converter.convert(d1);
        assertNull(tx1.getId());
        assertFalse(tx1.getIsSuccess());
        assertEquals(100, tx1.getAmount());
        assertNull(tx1.getGame());
        assertNull(tx1.getPayer());
        assertNull(tx1.getPayee());

        // When ID is present and isSuccess is null
        TransactionData d2 = new TransactionData();
        d2.setId(5);
        d2.setAmount(200);
        Transaction tx2 = converter.convert(d2);
        assertEquals(5, tx2.getId());
        assertFalse(tx2.getIsSuccess());

        // When ID is present and isSuccess is true, and names only
        TransactionData dNamesOnly = new TransactionData();
        dNamesOnly.setPayer_id(null);
        dNamesOnly.setPayer_name("P1");
        dNamesOnly.setPayee_id(null);
        dNamesOnly.setPayee_name("P2");
        Transaction txNames = converter.convert(dNamesOnly);
        assertNotNull(txNames.getPayer());
        assertEquals("P1", txNames.getPayer().getName());
        assertNotNull(txNames.getPayee());
        assertEquals("P2", txNames.getPayee().getName());

        // Full data
        TransactionData d3 = new TransactionData();
        d3.setId(6);
        d3.setIsSuccess(true);
        d3.setGame_id(1);
        d3.setPayer_id(2);
        d3.setPayer_name("P1");
        d3.setPayee_id(3);
        d3.setPayee_name("P2");
        Transaction tx3 = converter.convert(d3);
        assertEquals(6, tx3.getId());
        assertTrue(tx3.getIsSuccess());
        assertEquals(1, tx3.getGame().getId());
        assertEquals(2, tx3.getPayer().getId());
        assertEquals("P1", tx3.getPayer().getName());
        assertEquals(3, tx3.getPayee().getId());
        assertEquals("P2", tx3.getPayee().getName());
    }
}
