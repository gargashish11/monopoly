package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Game;

import java.util.List;

public interface GameService {
    Game save(Game game);

    List<Game> findAll();
}
