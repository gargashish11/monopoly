package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.controllers.form.GameFormData;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.facade.GameFacade;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/game")
public class GameController {

  @Resource
  private GameFacade gameFacade;

  @GetMapping("/{id}")
  public GameData getGame(@PathVariable Integer id) {
    return gameFacade.getGameData(id);
  }

  @GetMapping("/all")
  public Set<GameProjection> getAll() {
    return gameFacade.findAllProjectedByIdNotNull();
  }

  @PostMapping(value = "/new")
  public Integer createGame(@RequestBody GameFormData gameFormData) {
    return gameFacade.createGame(gameFormData.getPlayers(), gameFormData.getInitialBalance()).getId();
  }

  @PutMapping(value = "/save")
  public Boolean updateGameName(@RequestBody GameData gameData) {
    return gameFacade.updateGameName(gameData);
  }
}
