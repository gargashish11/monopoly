package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameService {

    Game save(Game game);
    Game createGame(Set<Player> players);

    Optional<Game> findById(Integer id);

    List<Game> findAll();
}
