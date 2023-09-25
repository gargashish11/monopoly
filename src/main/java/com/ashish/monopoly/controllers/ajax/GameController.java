package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.GameService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private GameService gameService;


    @GetMapping("/all")
    public List<Game> newGame() {
        return  gameService.findAll();
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable Integer id) {
        return gameService.findById(id).get();
    }

    @PutMapping(value = "/add")
    public Game add(@RequestBody Set<Player> players) {
        return gameService.save(players);
    }

}
