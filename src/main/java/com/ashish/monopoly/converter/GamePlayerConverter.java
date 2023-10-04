package com.ashish.monopoly.converter;

import com.ashish.monopoly.data.GamePlayerData;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GamePlayerConverter implements Converter<GamePlayer, GamePlayerData> {

    @Override
    public GamePlayerData convert(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        GamePlayerData gamePlayerData = new GamePlayerData();

        gamePlayerData.setId(gamePlayer.getId());

        gamePlayerData.setGame_id(gamePlayer.getGame().getId());
        gamePlayerData.setGame_name(gamePlayer.getGame().getName());

        gamePlayerData.setPlayer_id(player.getId());
        gamePlayerData.setPlayer_name(player.getName());

        gamePlayerData.setBalance(gamePlayer.getBalance());
        return gamePlayerData;
    }
}
