package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AbstractEntity {

    private Integer amount;

    @Nonnull
    private Boolean isSuccess = Boolean.FALSE;

    @JsonBackReference(value = "payer")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private Player payer;

    @JsonBackReference(value = "payee")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "payee_id", referencedColumnName = "id")
    private Player payee;

    @JsonBackReference(value = "transactions")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

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
                '}';
    }
}
