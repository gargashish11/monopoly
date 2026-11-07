package com.ashish.monopoly.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testEqualsAndHashCode() {
        Transaction tx1 = new Transaction();
        tx1.setId(10);
        tx1.setAmount(100);

        Transaction tx2 = new Transaction();
        tx2.setId(10);
        tx2.setAmount(200);

        Transaction tx3 = new Transaction();
        tx3.setId(20);

        Game game = new Game();
        game.setId(10);

        // Same object
        assertEquals(tx1, tx1);

        // Equals null
        assertNotEquals(tx1, null);

        // Different type
        assertNotEquals(tx1, "string");
        assertNotEquals(tx1, game);

        // Same ID
        assertEquals(tx1, tx2);
        assertEquals(tx1.hashCode(), tx2.hashCode());

        // Different ID
        assertNotEquals(tx1, tx3);

        // Null IDs
        Transaction txNull1 = new Transaction();
        Transaction txNull2 = new Transaction();
        assertNotEquals(txNull1, txNull2);

        // toString
        assertTrue(tx1.toString().contains("Transaction{"));
        assertTrue(tx1.toString().contains("id=10"));
    }

    @Test
    public void testConstructorsAndGettersSetters() {
        Player payer = new Player();
        Player payee = new Player();
        Game game = new Game();

        Transaction tx = new Transaction(500, true, payer, payee, game);
        assertEquals(500, tx.getAmount());
        assertTrue(tx.getIsSuccess());
        assertEquals(payer, tx.getPayer());
        assertEquals(payee, tx.getPayee());
        assertEquals(game, tx.getGame());

        tx.setAmount(300);
        tx.setIsSuccess(false);
        assertEquals(300, tx.getAmount());
        assertFalse(tx.getIsSuccess());
    }
}
