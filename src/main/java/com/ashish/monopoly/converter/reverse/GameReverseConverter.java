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
    private GameService gameService;
    @Resource
    private GamePlayerReverseConverter gamePlayerReverseConverter;

    @Resource
    private TransactionReverseConverter transactionReverseConverter;

    @Override
    public Game convert(GameData gameData) {
        Game game = gameService.findById(gameData.getId()).orElse(null);
        if (game != null) {
            if (!CollectionUtils.isEmpty(gameData.getGamePlayers())) {
                game.setGamePlayers(Converters.convertAll(gameData.getGamePlayers(), gamePlayerReverseConverter));
            }

            if (!CollectionUtils.isEmpty(gameData.getTransactions())) {
                game.setTransactions(Converters.convertAll(gameData.getTransactions(), transactionReverseConverter));
            }
        }
        return game;
    }
}
