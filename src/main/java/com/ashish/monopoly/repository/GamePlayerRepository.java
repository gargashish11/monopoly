package com.ashish.monopoly.repository;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Integer> {
    GamePlayer findByGame_IdAndPlayer(Integer game_id, Player player);
}
