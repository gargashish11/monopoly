package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Game add(@RequestBody List<Player> players) {
        Set<Player> savedPlayers = players.stream()
                .map(player -> {
                    return playerService.save(player);
                }).collect(Collectors.toSet());

        Game game = new Game();
        game.setName("New Game");
        savedPlayers.forEach(player -> {
            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setGame(game);
            gamePlayer.setPlayer(player);
            gamePlayerRepository.save(gamePlayer);
        });

//        gamePlayerRepository.save(gamePlayer);
        return new Game();
    }

}
