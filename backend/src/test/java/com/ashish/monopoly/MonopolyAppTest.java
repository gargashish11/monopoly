package com.ashish.monopoly;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.PlayerType;
import com.ashish.monopoly.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonopolyAppTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private Monopoly monopoly;

    @Test
    public void testMainMethod() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(eq(Monopoly.class), any(String[].class)))
                    .thenReturn(null);
            Monopoly.main(new String[]{"--server.port=0"});
            mocked.verify(() -> SpringApplication.run(eq(Monopoly.class), any(String[].class)));
        }
    }

    @Test
    public void testRunWhenBankDoesNotExist() throws Exception {
        when(playerService.findByName("Bank")).thenReturn(Optional.empty());

        monopoly.run();

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(playerService).save(captor.capture());
        assertEquals("Bank", captor.getValue().getName());
        assertEquals(PlayerType.BANK, captor.getValue().getType());
    }

    @Test
    public void testRunWhenBankAlreadyExists() throws Exception {
        Player existing = new Player();
        existing.setName("Bank");
        when(playerService.findByName("Bank")).thenReturn(Optional.of(existing));

        monopoly.run();

        verify(playerService, never()).save(any());
    }
}
