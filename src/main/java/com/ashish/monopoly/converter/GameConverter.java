package com.ashish.monopoly.converter;

import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Game;
import jakarta.annotation.Resource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;


@Component
public class GameConverter implements Converter<Game, GameData> {

    @Resource
    private GamePlayerConverter gamePlayerConverter;

    @Resource
    private TransactionConverter transactionConverter;


    @Override
    public GameData convert(Game game) {
        GameData gameData = new GameData();
        gameData.setId(game.getId());
        gameData.setName(game.getName());
        if (!CollectionUtils.isEmpty(game.getGamePlayers())) {
            gameData.setGamePlayers(Converters.convertAll(game.getGamePlayers(), gamePlayerConverter));
        }
        if (!CollectionUtils.isEmpty(game.getTransactions())) {
            gameData.setTransactions(Converters.convertAll(game.getTransactions(),transactionConverter));
        }
        return gameData;
    }
}
