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
    if (gamePlayer == null) {
      return null;
    }
    GamePlayerData gamePlayerData = new GamePlayerData();
    gamePlayerData.setId(gamePlayer.getId());

    if (gamePlayer.getGame() != null) {
      gamePlayerData.setGame_id(gamePlayer.getGame().getId());
      gamePlayerData.setGame_name(gamePlayer.getGame().getName());
    }

    Player player = gamePlayer.getPlayer();
    if (player != null) {
      gamePlayerData.setPlayer_id(player.getId());
      gamePlayerData.setPlayer_name(player.getName());
    }

    gamePlayerData.setBalance(gamePlayer.getBalance());
    return gamePlayerData;
  }
}
