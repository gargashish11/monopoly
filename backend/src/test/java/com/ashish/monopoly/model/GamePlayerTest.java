package com.ashish.monopoly.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayerTest {

    @Test
    public void testEqualsAndHashCodeAndToString() {
        GamePlayer gp1 = new GamePlayer();
        gp1.setId(1);
        gp1.setBalance(1500);

        GamePlayer gp2 = new GamePlayer();
        gp2.setId(1);
        gp2.setBalance(2000);

        GamePlayer gp3 = new GamePlayer();
        gp3.setId(2);

        // Same object
        assertEquals(gp1, gp1);
        // Null
        assertNotEquals(gp1, null);
        // Different type
        assertNotEquals(gp1, "String");
        // Same ID
        assertEquals(gp1, gp2);
        assertEquals(gp1.hashCode(), gp2.hashCode());
        // Different ID
        assertNotEquals(gp1, gp3);

        // Null IDs
        GamePlayer gpNull1 = new GamePlayer();
        GamePlayer gpNull2 = new GamePlayer();
        assertNotEquals(gpNull1, gpNull2);

        // toString
        assertTrue(gp1.toString().contains("GamePlayer{"));
        assertTrue(gp1.toString().contains("balance=1500"));
    }

    @Test
    public void testAllArgsConstructor() {
        Game game = new Game();
        Player player = new Player();
        GamePlayer gp = new GamePlayer(game, player, 1000);
        assertEquals(game, gp.getGame());
        assertEquals(player, gp.getPlayer());
        assertEquals(1000, gp.getBalance());
    }
}
