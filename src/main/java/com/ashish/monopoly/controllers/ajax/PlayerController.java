package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Resource
    private PlayerService playerService;

    @GetMapping("/all")
    public List<Player> getAllPlayers() {
        return playerService.findAll();
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
