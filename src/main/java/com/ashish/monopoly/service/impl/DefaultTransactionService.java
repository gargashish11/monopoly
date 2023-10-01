package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.repository.TransactionRepository;
import com.ashish.monopoly.service.TransactionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransactionService implements TransactionService {

    @Resource
    private TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteById(Integer id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction findById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }
}
