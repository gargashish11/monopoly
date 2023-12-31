package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.repository.TransactionRepository;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.TransactionService;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import com.ashish.monopoly.service.exception.NegativeTransactionAmountException;
import com.ashish.monopoly.service.exception.SameAccountTransaction;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultTransactionService implements TransactionService {

    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    private GamePlayerService gamePlayerService;

    @Transactional(rollbackOn = {
            InsufficientBalanceException.class,
            NegativeTransactionAmountException.class,
            SameAccountTransaction.class
    })
    @Override
    public Transaction save(Transaction transaction) {
        updateGamePlayerBalance(transaction);
        return transactionRepository.save(transaction);
    }

    private void updateGamePlayerBalance(Transaction transaction) {
        Integer txnAmount = transaction.getAmount();
        if (txnAmount < 0) {
            transaction.setIsSuccess(false);
            throw new NegativeTransactionAmountException("Negative Transaction Amount");
        }

        if(transaction.getPayee().equals(transaction.getPayer())) {
            transaction.setIsSuccess(false);
            throw new SameAccountTransaction("Same Account Transaction");
        }

        GamePlayer payee = gamePlayerService.findByGame_IdAndPlayer(transaction.getGame().getId(), transaction.getPayee());

        Boolean creditSuccess = gamePlayerService.updateBalance(payee, txnAmount, CreditDebit.CREDIT);

        GamePlayer payer = gamePlayerService.findByGame_IdAndPlayer(transaction.getGame().getId(), transaction.getPayer());

        Boolean debitSuccess = gamePlayerService.updateBalance(payer, txnAmount, CreditDebit.DEBIT);
        transaction.setIsSuccess(creditSuccess && debitSuccess);
    }

    @Override
    public void deleteById(Integer id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction findById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Set<Transaction> filter(
            Set<Transaction> transactionSet, int maxSize) {
        return transactionSet.stream()
                .sorted(Comparator.comparing(Transaction::getCreatedDate).reversed())
                .limit(maxSize)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

