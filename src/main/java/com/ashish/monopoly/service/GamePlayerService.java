package com.ashish.monopoly.service;

import com.ashish.monopoly.model.GamePlayer;

import java.util.Optional;

public interface GamePlayerService {

    Optional<GamePlayer> findById(Integer id);

}
