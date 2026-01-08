package com.ashish.monopoly.controllers;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.PlayerProjection;
import com.ashish.monopoly.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/player")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/all")
    public ResponseEntity<Set<PlayerProjection>> getAllPlayers() {
        return ResponseEntity.ok(playerService.findAllProjectedByIdNotNull());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        Boolean deleted = playerService.deleteById(id);
        if (Boolean.TRUE.equals(deleted)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player savedPlayer = playerService.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Player> editPlayer(@PathVariable Integer id, @RequestBody Player playerUpdate) {
        try {
            Player updatedPlayer = playerService.updateName(id, playerUpdate.getName());
            return ResponseEntity.ok(updatedPlayer);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
