package com.ashish.monopoly.controllers;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.TransactionFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionFacade transactionFacade;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void testGetTransactionFoundAndNotFound() {
        TransactionData data = new TransactionData();
        data.setId(1);
        when(transactionFacade.findById(1)).thenReturn(data);

        ResponseEntity<TransactionData> res1 = transactionController.getTransaction(1);
        assertEquals(HttpStatus.OK, res1.getStatusCode());
        assertEquals(data, res1.getBody());

        when(transactionFacade.findById(2)).thenReturn(null);
        ResponseEntity<TransactionData> res2 = transactionController.getTransaction(2);
        assertEquals(HttpStatus.NOT_FOUND, res2.getStatusCode());
    }

    @Test
    public void testCreateTransaction() {
        TransactionData input = new TransactionData();
        TransactionData saved = new TransactionData();
        saved.setId(10);
        when(transactionFacade.save(input)).thenReturn(saved);

        ResponseEntity<TransactionData> response = transactionController.createTransaction(input);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
    }
}
