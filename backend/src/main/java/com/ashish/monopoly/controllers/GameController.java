package com.ashish.monopoly.controllers;

import com.ashish.monopoly.controllers.form.GameFormData;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.facade.GameFacade;
import com.ashish.monopoly.repository.GameProjection;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {

    private final GameFacade gameFacade;

    @GetMapping("/{id}")
    public ResponseEntity<GameData> getGame(@PathVariable Integer id) {
        GameData gameData = gameFacade.getGameData(id);
        return ResponseEntity.ok(gameData);
    }

    @GetMapping("/all")
    public ResponseEntity<Set<GameProjection>> getAll() {
        Set<GameProjection> games = gameFacade.findAllProjectedByIdNotNull();
        return ResponseEntity.ok(games);
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Integer> createGame(@RequestBody GameFormData gameFormData) {
        Integer gameId = gameFacade.createGame(
                gameFormData.getPlayers(),
                gameFormData.getInitialBalance()
        ).getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(gameId);
    }

    @PutMapping(value = "/save")
    public ResponseEntity<Boolean> updateGameName(@RequestBody GameData gameData) {
        Boolean success = gameFacade.updateGameName(gameData);
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@Valid @PathVariable Integer id) {
        gameFacade.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}