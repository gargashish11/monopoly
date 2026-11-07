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
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Players list cannot be empty");
        }

        Game game = new Game();
        game.setName(generateName());
        Game savedGame = gameRepository.save(game);

        // 1. EXTRACT UNIQUE NAMES & FETCH EXISTING PLAYERS
        List<String> incomingNames = players.stream()
                .filter(Objects::nonNull)
                .map(Player::getName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .map(String::trim)
                .toList();

        if (incomingNames.isEmpty()) {
            throw new IllegalArgumentException("At least one valid player with a name must be provided");
        }

        Map<String, String> uniqueNamesMap = new LinkedHashMap<>();
        for (String name : incomingNames) {
            uniqueNamesMap.putIfAbsent(name.toLowerCase(), name);
        }
        List<String> distinctIncomingNames = new ArrayList<>(uniqueNamesMap.values());

        List<Player> existingPlayers = playerService.findByNames(distinctIncomingNames);

        Set<String> existingNames = existingPlayers.stream()
                .map(Player::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // 2. IDENTIFY & PREPARE NEW PLAYERS
        List<Player> newPlayers = distinctIncomingNames.stream()
                .filter(name -> !existingNames.contains(name.toLowerCase()))
                .map(name -> {
                    Player p = new Player();
                    p.setName(name);
                    p.setId(null);
                    if ("Bank".equalsIgnoreCase(name)) {
                        p.setType(PlayerType.BANK);
                    } else if ("Bot".equalsIgnoreCase(name)) {
                        p.setType(PlayerType.BOT);
                    } else {
                        p.setType(PlayerType.HUMAN);
                    }
                    return p;
                })
                .toList();

        // 3. SAVE ALL NEW PLAYERS (Batch Insert)
        if (!newPlayers.isEmpty()) {
            List<Player> savedNewPlayers = playerService.saveAll(newPlayers);
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
                    playerService.save(p);
                }
                gp.setBalance(BANK_INITIAL_BALANCE);
            } else {
                gp.setBalance(startBalance);
            }
            gamePlayers.add(gp);
        }
        List<GamePlayer> savedGamePlayers = gamePlayerService.saveAll(gamePlayers);
        savedGame.setGamePlayers(new LinkedHashSet<>(savedGamePlayers));
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




