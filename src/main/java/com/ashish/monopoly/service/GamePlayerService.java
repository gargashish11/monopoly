package com.ashish.monopoly.service;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.impl.CreditDebit;

import java.util.Optional;

public interface GamePlayerService {

    Optional<GamePlayer> findById(Integer id);

    GamePlayer save(GamePlayer gamePlayer);

    Boolean updateBalance(GamePlayer gamePlayer, Integer txnAmount, CreditDebit creditDebit);

    GamePlayer findByGame_IdAndPlayer(Integer game_id, Player player);
}
