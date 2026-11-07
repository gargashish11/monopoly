package com.ashish.monopoly.integration;

import com.ashish.monopoly.controllers.form.GameFormData;
import com.ashish.monopoly.data.GameData;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MonopolyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Integer createdPlayerId1;
    private static Integer createdPlayerId2;
    private static Integer createdGameId;
    private static Integer createdTransactionId;

    @Test
    @Order(1)
    public void testBankPlayerCreatedOnStartup() throws Exception {
        mockMvc.perform(get("/player/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Bank")));
    }

    @Test
    @Order(2)
    public void testCreateAndEditPlayers() throws Exception {
        Player player1 = new Player();
        player1.setName("Alice");

        MvcResult res1 = mockMvc.perform(post("/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andReturn();

        Player saved1 = objectMapper.readValue(res1.getResponse().getContentAsString(), Player.class);
        createdPlayerId1 = saved1.getId();

        Player player2 = new Player();
        player2.setName("Bob");

        MvcResult res2 = mockMvc.perform(post("/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andReturn();

        Player saved2 = objectMapper.readValue(res2.getResponse().getContentAsString(), Player.class);
        createdPlayerId2 = saved2.getId();

        // Edit player
        Player update = new Player();
        update.setName("Alice Cooper");

        mockMvc.perform(patch("/player/" + createdPlayerId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Cooper"));
    }

    @Test
    @Order(3)
    public void testCreateGameAndFetch() throws Exception {
        GameFormData formData = new GameFormData();
        formData.setInitialBalance(2500);

        Player p1 = new Player();
        p1.setId(createdPlayerId1);
        p1.setName("Alice Cooper");

        Player p2 = new Player();
        p2.setId(createdPlayerId2);
        p2.setName("Bob");

        formData.setPlayers(List.of(p1, p2));

        MvcResult res = mockMvc.perform(post("/game/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formData)))
                .andExpect(status().isCreated())
                .andReturn();

        createdGameId = Integer.parseInt(res.getResponse().getContentAsString());

        // Fetch game details
        mockMvc.perform(get("/game/" + createdGameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdGameId))
                .andExpect(jsonPath("$.gamePlayers", hasSize(2)))
                .andExpect(jsonPath("$.gamePlayers[0].balance").value(2500));

        // Fetch all games projection
        mockMvc.perform(get("/game/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(createdGameId)));

        // Update game name
        GameData updateData = new GameData();
        updateData.setId(createdGameId);
        updateData.setName("Champions League Monopoly");

        mockMvc.perform(put("/game/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @Order(4)
    public void testExecuteTransactionsAndBalances() throws Exception {
        // Successful transaction from Alice to Bob: 500
        TransactionData tx = new TransactionData();
        tx.setGame_id(createdGameId);
        tx.setPayer_id(createdPlayerId1);
        tx.setPayer_name("Alice Cooper");
        tx.setPayee_id(createdPlayerId2);
        tx.setPayee_name("Bob");
        tx.setAmount(500);

        MvcResult res = mockMvc.perform(post("/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tx)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();

        TransactionData savedTx = objectMapper.readValue(res.getResponse().getContentAsString(), TransactionData.class);
        createdTransactionId = savedTx.getId();

        // Check balances after transaction: Alice should have 2000, Bob should have 3000
        mockMvc.perform(get("/game/" + createdGameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gamePlayers[?(@.player_id == " + createdPlayerId1 + ")].balance").value(hasItem(2000)))
                .andExpect(jsonPath("$.gamePlayers[?(@.player_id == " + createdPlayerId2 + ")].balance").value(hasItem(3000)))
                .andExpect(jsonPath("$.transactions", hasSize(1)));

        // Retrieve transaction by id
        mockMvc.perform(get("/transaction/" + createdTransactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTransactionId))
                .andExpect(jsonPath("$.amount").value(500));

        // Failed transaction: Insufficient balance (Alice tries to send 10,000)
        TransactionData excessiveTx = new TransactionData();
        excessiveTx.setGame_id(createdGameId);
        excessiveTx.setPayer_id(createdPlayerId1);
        excessiveTx.setPayee_id(createdPlayerId2);
        excessiveTx.setAmount(10000);

        mockMvc.perform(post("/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(excessiveTx)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient Balance"));
    }

    @Test
    @Order(5)
    public void testDeleteGameAndPlayer() throws Exception {
        mockMvc.perform(delete("/game/" + createdGameId))
                .andExpect(status().isNoContent());

        // Game should now return 404
        mockMvc.perform(get("/game/" + createdGameId))
                .andExpect(status().isNotFound());

        // Deleting non-existent player returns 404
        mockMvc.perform(delete("/player/99999"))
                .andExpect(status().isNotFound());
    }
}
