package com.ashish.monopoly.repository;

import com.ashish.monopoly.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

  Optional<Player> findByName(String name);

  Optional<Player> findByNameIgnoreCase(String name);

  Set<PlayerProjection> findAllProjectedByIdNotNull();

  List<Player> findByNameIn(Collection<String> names);

  @Query("SELECT p FROM Player p WHERE LOWER(p.name) IN :names")
  List<Player> findByNamesIgnoreCase(@Param("names") Collection<String> names);
}
