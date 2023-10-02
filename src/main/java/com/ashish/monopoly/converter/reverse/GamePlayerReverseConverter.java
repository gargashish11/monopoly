package com.ashish.monopoly.converter.reverse;

import com.ashish.monopoly.data.GamePlayerData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GamePlayerReverseConverter implements Converter<GamePlayerData, GamePlayer> {

    @Override
    public GamePlayer convert(GamePlayerData gamePlayerData) {
        Game game = new Game();
        game.setId(gamePlayerData.getGame_id());
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(game);

        Player player = new Player();
        player.setId(gamePlayerData.getPlayer_id());
        player.setName(gamePlayerData.getPlayer_name());
        gamePlayer.setGame(game);
        gamePlayer.setPlayer(player);
        gamePlayer.setBalance(gamePlayerData.getBalance());
        return gamePlayer;
    }
}
