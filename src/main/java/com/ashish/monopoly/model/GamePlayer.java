package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true, includeFieldNames = false)
@Table(name = "game_player")
public class GamePlayer extends AbstractEntity {

    @JsonBackReference(value = "game")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private Game game;

    @JsonBackReference(value = "player")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;

    private Integer balance;

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + this.getId() +
                ", game=" + game.getId() +
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
