package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultGamePlayerService implements GamePlayerService {

  @Resource
  private GamePlayerRepository gamePlayerRepository;

  @Override
  public Optional<GamePlayer> findById(Integer id) {
    return gamePlayerRepository.findById(id);
  }

  @Override
  public GamePlayer save(GamePlayer gamePlayer) {
    return gamePlayerRepository.save(gamePlayer);
  }

  @Override
  public List<GamePlayer> saveAll(List<GamePlayer> gamePlayers) {
    return gamePlayerRepository.saveAll(gamePlayers);
  }

  @Override
  public void updateBalance(GamePlayer gamePlayer,
                               Integer txnAmount, CreditDebit creditDebit) {
    if (gamePlayer == null) {
      throw new IllegalArgumentException("GamePlayer cannot be null");
    }
    int currentBalance = gamePlayer.getBalance() != null ? gamePlayer.getBalance() : 0;
    if (creditDebit == CreditDebit.CREDIT) {
      gamePlayer.setBalance(currentBalance + txnAmount);
    } else {
      if (txnAmount <= currentBalance) {
        gamePlayer.setBalance(currentBalance - txnAmount);
      } else {
        throw new InsufficientBalanceException("Insufficient Balance");
      }
    }
    gamePlayerRepository.save(gamePlayer);
  }

  @Override
  public GamePlayer findByGame_IdAndPlayer(Integer game_id, Player player) {
    if (game_id == null || player == null) {
      return null;
    }
    if (player.getId() != null) {
      return gamePlayerRepository.findByGame_IdAndPlayer(game_id, player);
    }
    if (player.getName() != null && !player.getName().trim().isEmpty()) {
      return gamePlayerRepository.findByGame_IdAndPlayer_NameIgnoreCase(game_id, player.getName().trim());
    }
    return null;
  }
}
