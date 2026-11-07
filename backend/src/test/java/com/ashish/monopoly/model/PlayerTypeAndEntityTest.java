package com.ashish.monopoly.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTypeAndEntityTest {

    @Test
    public void testPlayerTypeEnum() {
        assertEquals(3, PlayerType.values().length);
        assertEquals(PlayerType.HUMAN, PlayerType.valueOf("HUMAN"));
        assertEquals(PlayerType.BANK, PlayerType.valueOf("BANK"));
        assertEquals(PlayerType.BOT, PlayerType.valueOf("BOT"));
    }

    @Test
    public void testAbstractEntityProperties() {
        Player player = new Player();
        Date now = new Date();
        player.setId(123);
        player.setCreatedDate(now);
        player.setLastModifiedDate(now);

        assertEquals(123, player.getId());
        assertEquals(now, player.getCreatedDate());
        assertEquals(now, player.getLastModifiedDate());
    }
}
