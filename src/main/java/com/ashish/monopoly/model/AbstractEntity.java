package com.ashish.monopoly.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
}
