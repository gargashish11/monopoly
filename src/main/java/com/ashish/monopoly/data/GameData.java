package com.ashish.monopoly.data;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Data
public class GameData {

    private Integer id;

    private String name;

    private Set<GamePlayerData> gamePlayers = new HashSet<>();

    private Set<TransactionData> transactions = new HashSet<>();

}
