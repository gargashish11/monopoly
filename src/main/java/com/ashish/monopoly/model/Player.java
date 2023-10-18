package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player extends AbstractEntity {

    @Column(unique = true)
    private String name;

    @JsonManagedReference(value = "player")
    @OneToMany(mappedBy = "player", cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Set<GamePlayer> gamePlayers = new LinkedHashSet<>();

    @JsonManagedReference(value = "payer")
    @OneToMany(mappedBy = "payer", cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Set<Transaction> paid = new LinkedHashSet<>();

    @JsonManagedReference(value = "payee")
    @OneToMany(mappedBy = "payee", cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Set<Transaction> received = new LinkedHashSet<>();

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setPlayer(this);
    }

    public void removeGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        gamePlayer.setPlayer(null);
    }

    public void addPaid(Transaction transaction) {
        paid.add(transaction);
        transaction.setPayer(this);
    }

    public void removePaid(Transaction transaction) {
        paid.remove(transaction);
        transaction.setPayer(null);
    }

    public void addReceived(Transaction transaction) {
        received.add(transaction);
        transaction.setPayee(this);
    }

    public void removeReceived(Transaction transaction) {
        received.remove(transaction);
        transaction.setPayee(null);
    }

    @PreUpdate
    @PrePersist
    public void prePersist() {
        for (GamePlayer gamePlayer : this.gamePlayers) {
            if (Objects.isNull(gamePlayer.getPlayer())) {
                gamePlayer.setPlayer(this);
            }
        }

        for(Transaction payer : this.paid) {
            if(Objects.isNull(payer.getPayer())) {
                payer.setPayer(this);
            }
        }
        for(Transaction payee : this.paid) {
            if(Objects.isNull(payee.getPayee())) {
                payee.setPayee(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player )) return false;
        return this.getId() != null && this.getId().equals(((Player) o).getId());
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
