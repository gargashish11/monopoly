package com.ashish.monopoly;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class Monopoly implements CommandLineRunner {

    @Resource
    PlayerService playerService;

    public static void main(String[] args) {
        SpringApplication.run(Monopoly.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Player> optionalPlayer = playerService.findByName("Bank");
        if (optionalPlayer.isEmpty()) {
            Player player = new Player();
            player.setName("Bank");
            playerService.save(player);
        }
    }
}
