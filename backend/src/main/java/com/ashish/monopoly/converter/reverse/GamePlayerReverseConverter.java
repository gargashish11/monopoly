package com.ashish.monopoly.converter.reverse;

import com.ashish.monopoly.data.GamePlayerData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GamePlayerReverseConverter implements Converter<GamePlayerData, GamePlayer> {

  @Override
  public GamePlayer convert(GamePlayerData gamePlayerData) {
    if (gamePlayerData == null) {
      return null;
    }
    GamePlayer gamePlayer = new GamePlayer();
    if (gamePlayerData.getGame_id() != null) {
      Game game = new Game();
      game.setId(gamePlayerData.getGame_id());
      gamePlayer.setGame(game);
    }

    if (gamePlayerData.getPlayer_id() != null || gamePlayerData.getPlayer_name() != null) {
      Player player = new Player();
      player.setId(gamePlayerData.getPlayer_id());
      player.setName(gamePlayerData.getPlayer_name());
      gamePlayer.setPlayer(player);
    }

    gamePlayer.setBalance(gamePlayerData.getBalance());
    return gamePlayer;
  }
}
