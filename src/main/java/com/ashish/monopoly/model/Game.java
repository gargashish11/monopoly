package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game extends AbstractEntity {

    @Nonnull
    private String name;

    @JsonManagedReference(value = "game")
    @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
    private Set<Transaction> transactions = new HashSet<>();

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setGame(this);
    }

    public void removeGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        gamePlayer.setGame(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        return this.getId() != null && this.getId().equals(((Game) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + this.getId() +
                "name='" + name + '\'' +
                ", gamePlayers=" + gamePlayers +
                '}';
    }
}

