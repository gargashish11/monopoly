package com.ashish.monopoly.facade.impl;

import com.ashish.monopoly.converter.Converters;
import com.ashish.monopoly.converter.GameConverter;
import com.ashish.monopoly.converter.reverse.GameReverseConverter;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.facade.GameFacade;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import com.ashish.monopoly.service.TransactionService;
import jakarta.annotation.Resource;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DefaultGameFacade implements GameFacade {
    @Resource
    private GameService gameService;

    @Resource
    private GameConverter gameConverter;

    @Resource
    private GameReverseConverter gameReverseConverter;


    @Override
    public Set<Integer> getAllIds() {
        return gameService.getAllIds();
    }

    @Override
    public GameData save(GameData gameData) {
        Game game = gameReverseConverter.convert(gameData);
        Game savedGame = gameService.save(game);
        return gameConverter.convert(savedGame);
    }

    @Override
    public GameData createGame(Set<Player> players){
        return gameConverter.convert(gameService.createGame(players));
    }

    @Override
    public GameData getGameData(Integer gameId) {
        Game game = gameService.findById(gameId).orElse(null);
        if (game != null) {
            return gameConverter.convert(game);
        }
        return null;
    }

    @Override
    public GameData getGameData(Game game) {
        try {
            return gameConverter.convert(game);
        } catch (ConversionException | IllegalArgumentException iae) {
            return null;
        }
    }

    @Override
    public Set<GameData> findAll() {
        return Converters.convertAll(gameService.findAll(),gameConverter);
    }

    @Override
    public Set<GameProjection> findAllProjectedByIdNotNull() {
        return gameService.findAllProjectedByIdNotNull();
    }

    @Override
    public Boolean updateGameName(GameData gameData) {
        try {
            gameService.save(gameReverseConverter.convert(gameData));
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
