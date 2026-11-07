package com.ashish.monopoly.converter;

import com.ashish.monopoly.converter.reverse.TransactionReverseConverter;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterTest {

    @Test
    public void testTransactionReverseConverterPreservesSuccess() {
        TransactionReverseConverter reverseConverter = new TransactionReverseConverter();

        TransactionData data = new TransactionData();
        data.setId(101);
        data.setAmount(500);
        data.setIsSuccess(true);
        data.setGame_id(1);
        data.setPayer_id(2);
        data.setPayee_id(3);

        Transaction entity = reverseConverter.convert(data);

        assertNotNull(entity);
        assertEquals(101, entity.getId());
        assertEquals(500, entity.getAmount());
        // Crucial: isSuccess must not be overwritten to false
        assertTrue(entity.getIsSuccess());
    }

    @Test
    public void testTransactionConverterNullSafe() {
        TransactionConverter converter = new TransactionConverter();
        assertNull(converter.convert(null));
    }
}
