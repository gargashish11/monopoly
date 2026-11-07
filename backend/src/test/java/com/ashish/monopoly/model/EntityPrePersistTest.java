package com.ashish.monopoly.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityPrePersistTest {

    @Test
    public void testGamePrePersistAssociatesGame() {
        Game game = new Game();
        game.setName("Test Game");

        Player player = new Player();
        player.setName("Alice");

        GamePlayer gp = new GamePlayer();
        gp.setPlayer(player);
        // gp.getGame() is initially null

        game.getGamePlayers().add(gp);

        game.prePersist();

        // Game should now be set on gp
        assertEquals(game, gp.getGame());
    }

    @Test
    public void testPlayerPrePersistAssociatesReceivedTransactions() {
        Player player = new Player();
        player.setName("Bob");

        Transaction txReceived = new Transaction();
        // payee is null
        player.getReceived().add(txReceived);

        Transaction txPaid = new Transaction();
        Player recipient = new Player();
        recipient.setName("Charlie");
        txPaid.setPayee(recipient);
        player.getPaid().add(txPaid);

        player.prePersist();

        // Received transaction should have payee set to player
        assertEquals(player, txReceived.getPayee());
        // Paid transaction payee should NOT be overwritten with player
        assertEquals(recipient, txPaid.getPayee());
    }
}
