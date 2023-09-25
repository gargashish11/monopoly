package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.repository.PlayerRepository;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private GameService gameService;

    @Resource
    private GameRepository gameRepository;

    @Resource
    private GamePlayerRepository gamePlayerRepository;

    @Resource
    private PlayerService playerService;

    @GetMapping("/all")
    public List<Game> newGame() {
        return  gameService.findAll();
    }

    @GetMapping("/1")
    public GamePlayer getGame() {
        return gamePlayerRepository.findById(1).get();
    }

    @PutMapping(value = "/add")
    public Game add(@RequestBody Game game) {
        return gameService.save(game);
    }

}
