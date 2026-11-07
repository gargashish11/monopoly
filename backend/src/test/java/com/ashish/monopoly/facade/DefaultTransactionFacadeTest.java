package com.ashish.monopoly.facade;

import com.ashish.monopoly.converter.TransactionConverter;
import com.ashish.monopoly.converter.reverse.TransactionReverseConverter;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.impl.DefaultTransactionFacade;
import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultTransactionFacadeTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionConverter transactionConverter;

    @Mock
    private TransactionReverseConverter transactionReverseConverter;

    @InjectMocks
    private DefaultTransactionFacade facade;

    @Test
    public void testSave() {
        TransactionData inputData = new TransactionData();
        Transaction entity = new Transaction();
        Transaction savedEntity = new Transaction();
        TransactionData resultData = new TransactionData();

        when(transactionReverseConverter.convert(inputData)).thenReturn(entity);
        when(transactionService.save(entity)).thenReturn(savedEntity);
        when(transactionConverter.convert(savedEntity)).thenReturn(resultData);

        TransactionData result = facade.save(inputData);
        assertEquals(resultData, result);
    }

    @Test
    public void testFindByIdFound() {
        Transaction entity = new Transaction();
        TransactionData resultData = new TransactionData();

        when(transactionService.findById(1)).thenReturn(entity);
        when(transactionConverter.convert(entity)).thenReturn(resultData);

        assertEquals(resultData, facade.findById(1));
    }

    @Test
    public void testFindByIdNotFoundReturnsNull() {
        when(transactionService.findById(99)).thenReturn(null);
        assertNull(facade.findById(99));
        verify(transactionConverter, never()).convert(any());
    }
}
