package com.ashish.monopoly.facade.impl;

import com.ashish.monopoly.converter.GameConverter;
import com.ashish.monopoly.converter.reverse.GameReverseConverter;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.GameFacade;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultGameFacade implements GameFacade {

    private final GameService gameService;
    private final GameConverter gameConverter;
    private final GameReverseConverter gameReverseConverter;
    private final TransactionService transactionService;

    @Override
    public GameData save(GameData gameData) {
        Game game = gameReverseConverter.convert(gameData);
        Game savedGame = gameService.save(game);
        return gameConverter.convert(savedGame);
    }

    @Override
    public GameData createGame(List<Player> players, Integer initialBalance) {
        return gameConverter.convert(gameService.createGame(players, initialBalance));
    }

    @Override
    @Transactional(readOnly = true)
    public GameData getGameData(Integer gameId) {
        Game game = gameService.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with ID: " + gameId));

        GameData gameData = gameConverter.convert(game);

        if (gameData != null && gameData.getTransactions() != null) {
            Comparator<TransactionData> byDate = Comparator.comparing(TransactionData::getCreatedDate);
            Set<TransactionData> filteredTxns = gameData.getTransactions().stream()
                    .sorted(byDate.reversed()) // Newest IDs first
                    .limit(20)
                    .collect(Collectors.toCollection(LinkedHashSet::new)); // Preserves insertion order
            gameData.setTransactions(filteredTxns);
        }

        return gameData;
    }

    @Override
    public Set<GameProjection> findAllProjectedByIdNotNull() {
        return gameService.findAllProjectedByIdNotNull();
    }

    @Override
    @Transactional
    public Boolean updateGameName(GameData gameData) {
        Game existingGame = gameService.findById(gameData.getId())
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));
        existingGame.setName(gameData.getName());
        gameService.save(existingGame);
        return Boolean.TRUE;
    }

    @Override
    public void deleteGame(Integer id) {
        gameService.deleteGame(id);
    }
}