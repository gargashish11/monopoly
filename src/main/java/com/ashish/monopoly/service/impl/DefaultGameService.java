package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class DefaultGameService implements GameService {

    private final Integer INITIAL_BALANCE = 1500;

    @Resource
    private GameRepository gameRepository;

    @Resource
    private PlayerService playerService;


    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }
    @Override
    public Game createGame(Set<Player> players) {
        Game game = new Game();
        game.setName(generateName());
        players.forEach(player -> {
            Player savedPlayer = playerService.save(player);
            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setPlayer(savedPlayer);
            gamePlayer.setBalance(INITIAL_BALANCE);
            game.addGamePlayer(gamePlayer);
        });
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> findById(Integer id) {
        return gameRepository.findById(id);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    private String generateName() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a custom format pattern and separators
        String customFormat = "yyyy_MM_dd_HH_mm_ss";

        // Create a DateTimeFormatter with the custom format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(customFormat);

        // Format the LocalDateTime object using the formatter
        String formattedDateTime = currentDateTime.format(formatter);

        // Replace default separators with custom separators
        return formattedDateTime;
    }
}




