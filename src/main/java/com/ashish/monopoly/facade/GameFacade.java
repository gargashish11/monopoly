package com.ashish.monopoly.facade;

import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;

import java.util.List;
import java.util.Set;

public interface GameFacade {

  GameData save(GameData gameData);

  GameData createGame(List<Player> players, Integer initialBalance);

  GameData getGameData(Integer gameId);

  Set<GameProjection> findAllProjectedByIdNotNull();

  Boolean updateGameName(GameData gameData);
}
