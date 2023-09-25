package com.ashish.monopoly.repository;

import com.ashish.monopoly.model.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Integer> {
}
