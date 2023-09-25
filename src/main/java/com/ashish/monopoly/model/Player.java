package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true, includeFieldNames = false)
public class Player extends AbstractEntity {

    @Nonnull
    private String name;

    @JsonManagedReference(value = "player")
    @OneToMany(mappedBy = "player", cascade = CascadeType.PERSIST)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
        gamePlayer.setPlayer(this);
    }

    public void removeGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer);
        gamePlayer.setPlayer(null);
    }

    @PreUpdate
    @PrePersist
    public void prePersist() {
        for (GamePlayer gamePlayer : this.gamePlayers) {
            if (Objects.isNull(gamePlayer.getPlayer())) {
                gamePlayer.setPlayer(this);
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

}
