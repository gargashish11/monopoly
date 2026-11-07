package com.ashish.monopoly.service;

import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import com.ashish.monopoly.service.exception.NegativeTransactionAmountException;
import com.ashish.monopoly.service.exception.SameAccountTransaction;
import com.ashish.monopoly.service.impl.CreditDebit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceHelperAndExceptionTest {

    @Test
    public void testCreditDebitEnum() {
        assertEquals(2, CreditDebit.values().length);
        assertEquals(CreditDebit.CREDIT, CreditDebit.valueOf("CREDIT"));
        assertEquals(CreditDebit.DEBIT, CreditDebit.valueOf("DEBIT"));
    }

    @Test
    public void testExceptions() {
        InsufficientBalanceException e1 = new InsufficientBalanceException("insufficient");
        assertEquals("insufficient", e1.getMessage());

        NegativeTransactionAmountException e2 = new NegativeTransactionAmountException("negative");
        assertEquals("negative", e2.getMessage());

        SameAccountTransaction e3 = new SameAccountTransaction("same account");
        assertEquals("same account", e3.getMessage());
    }
}
