package com.ashish.monopoly.controllers;

import com.ashish.monopoly.controllers.form.GameFormData;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.facade.GameFacade;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GameProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    @Mock
    private GameFacade gameFacade;

    @InjectMocks
    private GameController gameController;

    @Test
    public void testGetGame() {
        GameData data = new GameData();
        data.setId(1);
        when(gameFacade.getGameData(1)).thenReturn(data);

        ResponseEntity<GameData> response = gameController.getGame(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(data, response.getBody());
    }

    @Test
    public void testGetAll() {
        GameProjection proj = mock(GameProjection.class);
        when(gameFacade.findAllProjectedByIdNotNull()).thenReturn(Set.of(proj));

        ResponseEntity<Set<GameProjection>> response = gameController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Set.of(proj), response.getBody());
    }

    @Test
    public void testCreateGame() {
        GameFormData formData = new GameFormData();
        formData.setInitialBalance(1500);
        List<Player> players = List.of(new Player());
        formData.setPlayers(players);

        GameData created = new GameData();
        created.setId(42);
        when(gameFacade.createGame(players, 1500)).thenReturn(created);

        ResponseEntity<Integer> response = gameController.createGame(formData);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(42, response.getBody());
    }

    @Test
    public void testUpdateGameName() {
        GameData data = new GameData();
        when(gameFacade.updateGameName(data)).thenReturn(true);

        ResponseEntity<Boolean> response = gameController.updateGameName(data);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void testDeleteGame() {
        doNothing().when(gameFacade).deleteGame(10);
        ResponseEntity<Void> response = gameController.deleteGame(10);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(gameFacade).deleteGame(10);
    }
}
