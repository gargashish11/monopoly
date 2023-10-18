package com.ashish.monopoly.data;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class GameData {

    private Integer id;

    private String name;

    private Set<GamePlayerData> gamePlayers = new LinkedHashSet<>();

    private Set<TransactionData> transactions = new LinkedHashSet<>();

}
