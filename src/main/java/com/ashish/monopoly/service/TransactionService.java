package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Transaction;

public interface TransactionService {

    Transaction save(Transaction transaction);

    void deleteById(Integer id);

    Transaction findById(Integer id);
}
