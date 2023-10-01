package com.ashish.monopoly.converter.reverse;

import com.ashish.monopoly.data.GamePlayerData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GamePlayerReverseConverter implements Converter<GamePlayerData, GamePlayer> {

    @Resource
    private GamePlayerService gamePlayerService;

    @Resource
    private PlayerService playerService;

    @Resource
    private GameService gameService;

    @Override
    public GamePlayer convert(GamePlayerData gamePlayerData) {
        Game game = gameService.findById(gamePlayerData.getGame_id()).orElse(null);
        GamePlayer gamePlayer = gamePlayerService.findById(gamePlayerData.getId()).orElse(null);
        if (gamePlayer != null && game != null) {
            Player player = playerService.findById(gamePlayerData.getPlayer_id());
            player.setName(gamePlayerData.getPlayer_name());
            gamePlayer.setGame(game);
            gamePlayer.setPlayer(player);
            gamePlayer.setBalance(gamePlayerData.getBalance());
        }
        return gamePlayer;
    }
}
