package com.ashish.monopoly.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testRelationships() {
        Player player = new Player();
        player.setId(1);
        player.setName("Player 1");

        GamePlayer gp = new GamePlayer();
        player.addGamePlayer(gp);
        assertTrue(player.getGamePlayers().contains(gp));
        assertEquals(player, gp.getPlayer());

        player.removeGamePlayer(gp);
        assertFalse(player.getGamePlayers().contains(gp));
        assertNull(gp.getPlayer());

        Transaction txPaid = new Transaction();
        player.addPaid(txPaid);
        assertTrue(player.getPaid().contains(txPaid));
        assertEquals(player, txPaid.getPayer());

        player.removePaid(txPaid);
        assertFalse(player.getPaid().contains(txPaid));
        assertNull(txPaid.getPayer());

        Transaction txReceived = new Transaction();
        player.addReceived(txReceived);
        assertTrue(player.getReceived().contains(txReceived));
        assertEquals(player, txReceived.getPayee());

        player.removeReceived(txReceived);
        assertFalse(player.getReceived().contains(txReceived));
        assertNull(txReceived.getPayee());
    }

    @Test
    public void testPrePersist() {
        Player player = new Player();
        player.setName("Player PrePersist");

        Player other = new Player();
        other.setId(99);

        GamePlayer gp1 = new GamePlayer(); // player is null
        GamePlayer gp2 = new GamePlayer();
        gp2.setPlayer(other); // player is not null

        player.getGamePlayers().add(gp1);
        player.getGamePlayers().add(gp2);

        Transaction paid1 = new Transaction(); // payer null
        Transaction paid2 = new Transaction();
        paid2.setPayer(other); // payer not null

        player.getPaid().add(paid1);
        player.getPaid().add(paid2);

        Transaction recv1 = new Transaction(); // payee null
        Transaction recv2 = new Transaction();
        recv2.setPayee(other); // payee not null

        player.getReceived().add(recv1);
        player.getReceived().add(recv2);

        player.prePersist();

        assertEquals(player, gp1.getPlayer());
        assertEquals(other, gp2.getPlayer());
        assertEquals(player, paid1.getPayer());
        assertEquals(other, paid2.getPayer());
        assertEquals(player, recv1.getPayee());
        assertEquals(other, recv2.getPayee());
    }

    @Test
    public void testEqualsAndHashCodeAndToString() {
        Player p1 = new Player();
        p1.setId(5);
        p1.setName("Alice");

        Player p2 = new Player();
        p2.setId(5);
        p2.setName("Alice Clone");

        Player p3 = new Player();
        p3.setId(6);

        // Same object
        assertEquals(p1, p1);
        // Null
        assertNotEquals(p1, null);
        // Different type
        assertNotEquals(p1, "not a player");
        // Same ID
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        // Different ID
        assertNotEquals(p1, p3);

        // Null ID with same name (case-insensitive)
        Player pName1 = new Player();
        pName1.setId(null);
        pName1.setName("Alice");

        Player pName2 = new Player();
        pName2.setId(10);
        pName2.setName("alice");

        assertEquals(pName1, pName2);
        assertEquals(pName2, pName1);

        // Null ID with different name
        Player pName3 = new Player();
        pName3.setId(null);
        pName3.setName("Bob");
        assertNotEquals(pName1, pName3);

        // Null name with null ID
        Player pNullName = new Player();
        pNullName.setId(null);
        pNullName.setName(null);
        assertNotEquals(pName1, pNullName);
        assertNotEquals(pNullName, pName1);

        // Both null ID, both null name
        Player pNull1 = new Player();
        Player pNull2 = new Player();
        assertNotEquals(pNull1, pNull2);

        // toString
        assertTrue(p1.toString().contains("Player{"));
        assertTrue(p1.toString().contains("name='Alice'"));
    }

    @Test
    public void testAllArgsConstructor() {
        Set<GamePlayer> gps = new LinkedHashSet<>();
        Set<Transaction> paid = new LinkedHashSet<>();
        Set<Transaction> received = new LinkedHashSet<>();
        Player player = new Player("Custom", PlayerType.HUMAN, gps, paid, received);
        assertEquals("Custom", player.getName());
        assertEquals(PlayerType.HUMAN, player.getType());
        assertEquals(gps, player.getGamePlayers());
        assertEquals(paid, player.getPaid());
        assertEquals(received, player.getReceived());
    }
}
