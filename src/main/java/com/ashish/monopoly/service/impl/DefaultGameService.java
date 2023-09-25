package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.repository.PlayerRepository;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DefaultGameService implements GameService {

    @Resource
    private GameRepository gameRepository;

    @Resource
    private PlayerService playerService;

    @Resource
    private PlayerRepository playerRepository;

    @Override
    public Game save(Game game) {
        var gamePlayers = game.getGamePlayers();
        gamePlayers.forEach(p -> {
//            p.getPlayer().toString();
            System.out.println((p.toString()));
        });

//        game.getGamePlayers().forEach(gamePlayer -> playerService.save(gamePlayer.getPlayer()));
        return new Game();
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }
}
