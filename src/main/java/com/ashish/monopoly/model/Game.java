package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game extends AbstractEntity {

    @Nonnull
    private String name;

    @JsonManagedReference(value = "game")
    @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @JsonManagedReference(value = "transactions")
    @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setGame(this);
    }

    public void removeGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        gamePlayer.setGame(null);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setGame(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setGame(null);
    }

    @PreUpdate
    @PrePersist
    public void prePersist() {
        for (GamePlayer gamePlayer : this.gamePlayers) {
            if (Objects.isNull(gamePlayer.getPlayer())) {
                gamePlayer.setGame(this);
            }
        }

        for (Transaction txn : this.transactions) {
            if (Objects.isNull(txn.getGame())) {
                txn.setGame(this);
            }
        }

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
                "name='" + name + "'" +
                '}';
    }
}

