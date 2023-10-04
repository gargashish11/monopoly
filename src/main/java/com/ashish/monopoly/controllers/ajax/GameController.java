package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.facade.GameFacade;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.*;
import com.ashish.monopoly.service.GameService;
import jakarta.annotation.Resource;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private GameFacade gameFacade;

    @Resource
    private GameRepository gameRepository;

    @GetMapping("/{id}")
    public GameData getGame(@PathVariable Integer id) {
        return gameFacade.getGameData(id);
    }

    @GetMapping("/all")
    public Set<GameProjection> getAll() {
        return gameFacade.findAllProjectedByIdNotNull();
    }

    @PostMapping(value = "/new")
    public Integer createGame(@RequestBody Set<Player> players) {
        return gameFacade.createGame(players).getId();
    }

    @PutMapping(value = "/save")
    public Boolean updateGameName(@RequestBody GameData gameData) {
        return gameFacade.updateGameName(gameData);
    }
}
