package com.ashish.monopoly.facade;

import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import com.ashish.monopoly.service.TransactionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

public interface GameFacade {

    Set<Integer> getAllIds();

    GameData save(GameData gameData);

    GameData createGame(Set<Player> players);

    GameData getGameData(Integer gameId);

    GameData getGameData(Game game);

    Set<GameData> findAll();

    Set<GameProjection> findAllProjectedByIdNotNull();
}
