package com.ashish.monopoly.service;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.service.impl.CreditDebit;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GamePlayerService {

    Optional<GamePlayer> findById(Integer id);

    GamePlayer save(GamePlayer gamePlayer);

    List<GamePlayer> saveAll(List<GamePlayer> gamePlayers);

    Boolean updateBalance(GamePlayer gamePlayer, Integer txnAmount, CreditDebit creditDebit);

    GamePlayer findByGame_IdAndPlayer(Integer game_id, Player player);
}
