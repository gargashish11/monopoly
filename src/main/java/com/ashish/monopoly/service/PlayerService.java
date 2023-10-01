package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player save(Player player);

    List<Player> findAll();

    Boolean deleteById(Integer id);

    Player findById(Integer id);
}
