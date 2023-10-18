package com.ashish.monopoly.service;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Transaction;

import java.util.Set;

public interface TransactionService {

    Transaction save(Transaction transaction);

    void deleteById(Integer id);

    Transaction findById(Integer id);

    Set<Transaction> filter(Set<Transaction> transactionSet, int maxSize);
}
