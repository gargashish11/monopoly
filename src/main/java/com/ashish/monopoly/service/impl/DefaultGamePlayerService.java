package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
    public Boolean updateBalance(GamePlayer gamePlayer,
                                 Integer txnAmount, CreditDebit creditDebit) {
        switch (creditDebit) {
            case CREDIT -> {
                gamePlayer.setBalance(gamePlayer.getBalance() + txnAmount);
            }
            case DEBIT -> {
                if (txnAmount <= gamePlayer.getBalance()) {
                    gamePlayer.setBalance(gamePlayer.getBalance() - txnAmount);
                } else {
                    throw new InsufficientBalanceException("Insufficient Balance");
                }
            }
        }
        gamePlayerRepository.save(gamePlayer);
        return Boolean.TRUE;
    }

    @Override
    public GamePlayer findByGame_IdAndPlayer(Integer game_id, Player player){
        return gamePlayerRepository.findByGame_IdAndPlayer(game_id, player);
    }
}
