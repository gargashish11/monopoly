package com.ashish.monopoly.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testRelationships() {
        Game game = new Game();
        game.setId(1);
        game.setName("Monopoly World");

        GamePlayer gp = new GamePlayer();
        game.addGamePlayer(gp);
        assertTrue(game.getGamePlayers().contains(gp));
        assertEquals(game, gp.getGame());

        game.removeGamePlayer(gp);
        assertFalse(game.getGamePlayers().contains(gp));
        assertNull(gp.getGame());

        Transaction tx = new Transaction();
        game.addTransaction(tx);
        assertTrue(game.getTransactions().contains(tx));
        assertEquals(game, tx.getGame());

        game.removeTransaction(tx);
        assertFalse(game.getTransactions().contains(tx));
        assertNull(tx.getGame());
    }

    @Test
    public void testPrePersist() {
        Game game = new Game();
        game.setName("Game 1");

        GamePlayer gp1 = new GamePlayer(); // null game
        Game otherGame = new Game();
        otherGame.setId(99);
        GamePlayer gp2 = new GamePlayer();
        gp2.setGame(otherGame); // non-null game

        game.getGamePlayers().add(gp1);
        game.getGamePlayers().add(gp2);

        Transaction tx1 = new Transaction(); // null game
        Transaction tx2 = new Transaction();
        tx2.setGame(otherGame); // non-null game

        game.getTransactions().add(tx1);
        game.getTransactions().add(tx2);

        game.prePersist();

        assertEquals(game, gp1.getGame());
        assertEquals(otherGame, gp2.getGame());
        assertEquals(game, tx1.getGame());
        assertEquals(otherGame, tx2.getGame());
    }

    @Test
    public void testEqualsAndHashCodeAndToString() {
        Game g1 = new Game();
        g1.setId(10);
        g1.setName("Game 1");

        Game g2 = new Game();
        g2.setId(10);
        g2.setName("Game 2");

        Game g3 = new Game();
        g3.setId(20);

        // Same object
        assertEquals(g1, g1);
        // Null
        assertNotEquals(g1, null);
        // Different class
        assertNotEquals(g1, "String");
        // Same id
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
        // Different id
        assertNotEquals(g1, g3);

        // Null IDs
        Game gNull1 = new Game();
        Game gNull2 = new Game();
        assertNotEquals(gNull1, gNull2);

        // toString
        assertTrue(g1.toString().contains("Game{"));
        assertTrue(g1.toString().contains("name='Game 1'"));
    }

    @Test
    public void testAllArgsConstructor() {
        Set<GamePlayer> gps = new LinkedHashSet<>();
        Set<Transaction> txs = new LinkedHashSet<>();
        Game game = new Game("Custom Game", gps, txs);
        assertEquals("Custom Game", game.getName());
        assertEquals(gps, game.getGamePlayers());
        assertEquals(txs, game.getTransactions());
    }
}
