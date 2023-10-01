package com.ashish.monopoly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference(value = "payer")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private Player payer;

    @JsonBackReference(value = "payee")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payee_id", referencedColumnName = "id")
    private Player payee;

    @JsonBackReference(value = "transactions")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
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
