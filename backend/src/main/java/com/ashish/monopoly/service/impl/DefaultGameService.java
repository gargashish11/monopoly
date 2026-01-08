package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.PlayerType;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.repository.GameRepository;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultGameService implements GameService {

    private final Integer INITIAL_BALANCE = 2000;

    private final Integer BANK_INITIAL_BALANCE = 30000;

    @Resource
    private GameRepository gameRepository;

    @Resource
    private PlayerService playerService;

    @Resource
    private GamePlayerService gamePlayerService;


    @Override
    public Set<Integer> getAllIds() {
        return gameRepository.getAllIds();
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    @Override
    @Transactional
    public Game createGame(List<Player> players, Integer initialBalance) {
        Game game = new Game();
        game.setName(generateName());
        Game savedGame = gameRepository.save(game);

        // 1. EXTRACT NAMES & FETCH EXISTING PLAYERS (Batch Select)
        List<String> incomingNames = players.stream()
                .map(Player::getName)
                .toList();

        // Requires: findByNameIn(Collection<String> names) in your Repository
        List<Player> existingPlayers = playerService.findByNames(incomingNames);

        Set<String> existingNames = existingPlayers.stream()
                .map(Player::getName)
                .collect(Collectors.toSet());

        // 2. IDENTIFY & PREPARE NEW PLAYERS
        List<Player> newPlayers = players.stream()
                .filter(p -> !existingNames.contains(p.getName()))
                .peek(p -> {
                    p.setId(null); // Critical: ensure Hibernate treats as NEW

                    // Auto-detect Bank if it doesn't exist yet
                    if ("Bank".equalsIgnoreCase(p.getName())) {
                        p.setType(PlayerType.BANK);
                    } else {
                        p.setType(PlayerType.HUMAN);
                    }
                })
                .toList();

        // 3. SAVE ALL NEW PLAYERS (Batch Insert)
        if (!newPlayers.isEmpty()) {
            List<Player> savedNewPlayers = playerService.saveAll(newPlayers);
            // Combine existing list with the newly saved ones
            existingPlayers = new ArrayList<>(existingPlayers);
            existingPlayers.addAll(savedNewPlayers);
        }

        // 4. CREATE GAME PLAYERS (In Memory)
        List<GamePlayer> gamePlayers = new ArrayList<>();
        int startBalance = (initialBalance != null && initialBalance > 0) ? initialBalance : INITIAL_BALANCE;

        for (Player p : existingPlayers) {
            GamePlayer gp = new GamePlayer();
            gp.setPlayer(p);
            gp.setGame(savedGame);

            if (p.getType() == PlayerType.BANK || "Bank".equalsIgnoreCase(p.getName())) {
                if (p.getType() != PlayerType.BANK) {
                    p.setType(PlayerType.BANK);
                    playerService.save(p); // Persist the fix for future
                }
                gp.setBalance(BANK_INITIAL_BALANCE);
            }
            else {
                gp.setBalance(startBalance);
            }
            gamePlayers.add(gp);
        }
        gamePlayerService.saveAll(gamePlayers);
        return savedGame;
    }

    @Override
    public Optional<Game> findById(Integer id) {
        return gameRepository.findById(id);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Set<GameProjection> findAllProjectedByIdNotNull() {
        return gameRepository.findAllProjectedByIdNotNull();
    }

    private String generateName() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Create a DateTimeFormatter with the custom format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

        // Replace default separators with custom separators
        return currentDateTime.format(formatter);
    }

    private Player getOrCreatePlayer(Player player) {
        Optional<Player> existingPlayer = playerService.findByName(player.getName());
        log.info(String.valueOf(existingPlayer.isPresent()));

        if (existingPlayer.isPresent()) {
            return existingPlayer.get();
        } else {
            log.info("Creating new player: {}", player.getName());
            return playerService.save(player);
        }
    }

    @Override
    @Transactional
    public void deleteGame(Integer id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id); // Cascade removes Transactions & GamePlayers
        } else {
            log.warn("Attempted to delete non-existent game with ID: {}", id);
        }
    }

}




