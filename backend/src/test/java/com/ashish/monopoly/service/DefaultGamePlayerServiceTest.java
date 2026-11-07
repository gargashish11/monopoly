package com.ashish.monopoly.service;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import com.ashish.monopoly.service.impl.CreditDebit;
import com.ashish.monopoly.service.impl.DefaultGamePlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultGamePlayerServiceTest {

    @Mock
    private GamePlayerRepository repository;

    @InjectMocks
    private DefaultGamePlayerService service;

    @Test
    public void testFindById() {
        GamePlayer gp = new GamePlayer();
        gp.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(gp));

        Optional<GamePlayer> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testSaveAndSaveAll() {
        GamePlayer gp = new GamePlayer();
        when(repository.save(gp)).thenReturn(gp);
        when(repository.saveAll(List.of(gp))).thenReturn(List.of(gp));

        assertEquals(gp, service.save(gp));
        assertEquals(List.of(gp), service.saveAll(List.of(gp)));
    }

    @Test
    public void testUpdateBalanceCredit() {
        GamePlayer gp = new GamePlayer();
        gp.setBalance(500);

        service.updateBalance(gp, 200, CreditDebit.CREDIT);
        assertEquals(700, gp.getBalance());
        verify(repository).save(gp);
    }

    @Test
    public void testUpdateBalanceCreditWithNullInitialBalance() {
        GamePlayer gp = new GamePlayer();
        gp.setBalance(null);

        service.updateBalance(gp, 100, CreditDebit.CREDIT);
        assertEquals(100, gp.getBalance());
    }

    @Test
    public void testUpdateBalanceDebitSuccess() {
        GamePlayer gp = new GamePlayer();
        gp.setBalance(500);

        service.updateBalance(gp, 300, CreditDebit.DEBIT);
        assertEquals(200, gp.getBalance());
    }

    @Test
    public void testUpdateBalanceDebitInsufficientThrows() {
        GamePlayer gp = new GamePlayer();
        gp.setBalance(100);

        assertThrows(InsufficientBalanceException.class, () ->
                service.updateBalance(gp, 200, CreditDebit.DEBIT));
    }

    @Test
    public void testUpdateBalanceNullPlayerThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                service.updateBalance(null, 100, CreditDebit.CREDIT));
    }

    @Test
    public void testFindByGame_IdAndPlayer() {
        // 1. Both valid with ID
        Player playerWithId = new Player();
        playerWithId.setId(10);
        GamePlayer gp1 = new GamePlayer();
        when(repository.findByGame_IdAndPlayer(1, playerWithId)).thenReturn(gp1);
        assertEquals(gp1, service.findByGame_IdAndPlayer(1, playerWithId));

        // 2. ID is null, name is valid
        Player playerWithName = new Player();
        playerWithName.setId(null);
        playerWithName.setName("Alice");
        GamePlayer gp2 = new GamePlayer();
        when(repository.findByGame_IdAndPlayer_NameIgnoreCase(1, "Alice")).thenReturn(gp2);
        assertEquals(gp2, service.findByGame_IdAndPlayer(1, playerWithName));

        // 3. ID is null, name is null / empty
        Player playerEmpty = new Player();
        playerEmpty.setId(null);
        playerEmpty.setName("");
        assertNull(service.findByGame_IdAndPlayer(1, playerEmpty));

        Player playerNullName = new Player();
        playerNullName.setId(null);
        playerNullName.setName(null);
        assertNull(service.findByGame_IdAndPlayer(1, playerNullName));

        // 4. Null player or null game_id
        assertNull(service.findByGame_IdAndPlayer(null, playerWithId));
        assertNull(service.findByGame_IdAndPlayer(1, null));
    }
}
