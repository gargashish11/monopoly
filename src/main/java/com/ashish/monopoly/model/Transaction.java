package com.ashish.monopoly.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Transaction extends AbstractEntity {

    private Integer amount;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private Player payer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payee_id", referencedColumnName = "id")
    private Player payee;

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;
}
