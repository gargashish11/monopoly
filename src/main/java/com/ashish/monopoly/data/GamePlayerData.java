package com.ashish.monopoly.data;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class GamePlayerData {
    private Integer id;

    private Integer game_id;

    private Integer player_id;

    private String player_name;

    private Integer balance;
}
