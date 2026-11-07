package com.ashish.monopoly.controllers;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.PlayerProjection;
import com.ashish.monopoly.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @Test
    public void testGetAllPlayers() {
        PlayerProjection proj = mock(PlayerProjection.class);
        when(playerService.findAllProjectedByIdNotNull()).thenReturn(Set.of(proj));

        ResponseEntity<Set<PlayerProjection>> response = playerController.getAllPlayers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Set.of(proj), response.getBody());
    }

    @Test
    public void testDeletePlayerBranches() {
        when(playerService.deleteById(1)).thenReturn(true);
        ResponseEntity<Void> res1 = playerController.deletePlayer(1);
        assertEquals(HttpStatus.NO_CONTENT, res1.getStatusCode());

        when(playerService.deleteById(2)).thenReturn(false);
        ResponseEntity<Void> res2 = playerController.deletePlayer(2);
        assertEquals(HttpStatus.NOT_FOUND, res2.getStatusCode());
    }

    @Test
    public void testAddPlayer() {
        Player input = new Player();
        Player saved = new Player();
        saved.setId(10);
        when(playerService.save(input)).thenReturn(saved);

        ResponseEntity<Player> response = playerController.addPlayer(input);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }

    @Test
    public void testEditPlayerSuccessAndNotFound() {
        Player update = new Player();
        update.setName("NewName");
        Player updated = new Player();
        updated.setId(1);
        updated.setName("NewName");

        when(playerService.updateName(1, "NewName")).thenReturn(updated);
        ResponseEntity<Player> res1 = playerController.editPlayer(1, update);
        assertEquals(HttpStatus.OK, res1.getStatusCode());
        assertEquals(updated, res1.getBody());

        when(playerService.updateName(99, "NewName")).thenThrow(new EntityNotFoundException("not found"));
        ResponseEntity<Player> res2 = playerController.editPlayer(99, update);
        assertEquals(HttpStatus.NOT_FOUND, res2.getStatusCode());
    }
}
