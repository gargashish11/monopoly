package com.ashish.monopoly.repository;

import com.ashish.monopoly.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query("select p.id,p.name from #{#entityName} p")
    Set<Integer> getAllIds();

    Set<GameProjection> findAllProjectedByIdNotNull();

}
