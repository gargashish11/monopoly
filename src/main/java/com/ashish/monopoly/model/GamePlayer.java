package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_player")
public class GamePlayer extends AbstractEntity {

    @JsonBackReference(value = "game")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "game_id")
    private Game game;

    @JsonBackReference(value = "player")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "player_id")
    private Player player;

    @PositiveOrZero
    private Integer balance;

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + this.getId() +
                ", game=" + game +
                ", player=" + player +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePlayer)) return false;
        return this.getId() != null && this.getId().equals(((GamePlayer) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
