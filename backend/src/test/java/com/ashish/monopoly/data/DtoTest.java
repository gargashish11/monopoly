package com.ashish.monopoly.data;

import com.ashish.monopoly.controllers.form.GameFormData;
import com.ashish.monopoly.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DtoTest {

    @Test
    public void testGameFormData() {
        GameFormData form = new GameFormData();
        assertEquals(2000, form.getInitialBalance());
        assertTrue(form.getPlayers().isEmpty());

        form.setInitialBalance(3000);
        Player p = new Player();
        form.setPlayers(List.of(p));

        assertEquals(3000, form.getInitialBalance());
        assertEquals(1, form.getPlayers().size());
        assertNotNull(form.toString());
        assertEquals(form, form);
        assertNotEquals(form, null);
    }

    @Test
    public void testGameData() {
        GameData data = new GameData();
        data.setId(1);
        data.setName("Test Game");
        Set<GamePlayerData> gps = new LinkedHashSet<>();
        Set<TransactionData> txs = new LinkedHashSet<>();
        data.setGamePlayers(gps);
        data.setTransactions(txs);

        assertEquals(1, data.getId());
        assertEquals("Test Game", data.getName());
        assertEquals(gps, data.getGamePlayers());
        assertEquals(txs, data.getTransactions());
        assertNotNull(data.toString());
        assertEquals(data, data);
        assertNotEquals(data, null);
    }

    @Test
    public void testGamePlayerData() {
        GamePlayerData data = new GamePlayerData();
        data.setId(10);
        data.setGame_id(1);
        data.setGame_name("Game 1");
        data.setPlayer_id(2);
        data.setPlayer_name("Player 2");
        data.setBalance(1500);

        assertEquals(10, data.getId());
        assertEquals(1, data.getGame_id());
        assertEquals("Game 1", data.getGame_name());
        assertEquals(2, data.getPlayer_id());
        assertEquals("Player 2", data.getPlayer_name());
        assertEquals(1500, data.getBalance());
        assertNotNull(data.toString());
        assertEquals(data, data);
        assertNotEquals(data, null);
    }

    @Test
    public void testTransactionData() {
        TransactionData data = new TransactionData();
        Date now = new Date();
        data.setId(100);
        data.setGame_id(1);
        data.setPayer_id(2);
        data.setPayer_name("Payer");
        data.setPayee_id(3);
        data.setPayee_name("Payee");
        data.setAmount(250);
        data.setIsSuccess(true);
        data.setCreatedDate(now);

        assertEquals(100, data.getId());
        assertEquals(1, data.getGame_id());
        assertEquals(2, data.getPayer_id());
        assertEquals("Payer", data.getPayer_name());
        assertEquals(3, data.getPayee_id());
        assertEquals("Payee", data.getPayee_name());
        assertEquals(250, data.getAmount());
        assertTrue(data.getIsSuccess());
        assertEquals(now, data.getCreatedDate());
        assertNotNull(data.toString());
        assertEquals(data, data);
        assertNotEquals(data, null);
    }
}
