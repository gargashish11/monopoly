package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameService {
    Game save(Set<Player> players);

    Optional<Game> findById(Integer id);

    List<Game> findAll();
}
