package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.PlayerProjection;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerService {

  Player save(Player player);

  Optional<Player> findByName(String name);

  Boolean deleteById(Integer id);

  Optional<Player> findById(Integer id);

  Set<PlayerProjection> findAllProjectedByIdNotNull();

  List<Player> findByNames(List<String> names);

  List<Player> saveAll(List<Player> players);

  Player updateName(Integer id, String name);
}
