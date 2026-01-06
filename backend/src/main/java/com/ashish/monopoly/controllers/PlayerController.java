package com.ashish.monopoly.controllers;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.PlayerProjection;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/player")
public class PlayerController {

  @Resource
  private PlayerService playerService;

  @GetMapping("/all")
  public Set<PlayerProjection> getAllPlayers() {
    return playerService.findAllProjectedByIdNotNull();
  }

  @DeleteMapping("/delete/{id}")
  public Boolean deletePlayer(@PathVariable Integer id) {
    return playerService.deleteById(id);
  }

  @PutMapping("/add")
  public Player addPlayer(@RequestBody Player player) {
    return playerService.save(player);
  }

}
