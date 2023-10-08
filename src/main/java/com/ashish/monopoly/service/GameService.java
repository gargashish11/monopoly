package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameService {

    Set<Integer> getAllIds();

    Game save(Game game);
    Game createGame(List<Player> players);

    Optional<Game> findById(Integer id);

    List<Game> findAll();

    Set<GameProjection> findAllProjectedByIdNotNull();
}
