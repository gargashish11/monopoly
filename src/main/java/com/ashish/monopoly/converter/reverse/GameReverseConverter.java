package com.ashish.monopoly.converter.reverse;

import com.ashish.monopoly.converter.Converters;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.service.GameService;
import jakarta.annotation.Resource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class GameReverseConverter implements Converter<GameData, Game> {

    @Resource
    private GamePlayerReverseConverter gamePlayerReverseConverter;

    @Override
    public Game convert(GameData gameData) {
        Game game = new Game();
        game.setId(gameData.getId());
        game.setName(gameData.getName());
        if (!CollectionUtils.isEmpty(gameData.getGamePlayers())) {
            game.setGamePlayers(Converters.convertAll(gameData.getGamePlayers(), gamePlayerReverseConverter));
        }
        return game;
    }
}
