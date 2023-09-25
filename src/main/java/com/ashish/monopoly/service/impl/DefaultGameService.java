package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Resource
    private GamePlayerRepository gamePlayerRepository;

    @Override
    public Game save(Set<Player> players) {
        Game game = new Game();
        game.setName("New Game");
        players.forEach(player -> {
            Player savedPlayer = playerService.save(player);
            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setGame(game);
            gamePlayer.setPlayer(savedPlayer);
            gamePlayer.setBalance(INITIAL_BALANCE);
            gamePlayerRepository.save(gamePlayer);
        });
        return game;
    }

    @Override
    public Optional<Game> findById(Integer id) {
        return gameRepository.findById(id);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }
}
