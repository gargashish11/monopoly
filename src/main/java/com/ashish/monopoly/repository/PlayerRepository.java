package com.ashish.monopoly.repository;

import com.ashish.monopoly.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Override
    List<Player> findAll();
}
