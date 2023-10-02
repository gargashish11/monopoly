package com.ashish.monopoly.facade.impl;

import com.ashish.monopoly.converter.TransactionConverter;
import com.ashish.monopoly.converter.reverse.TransactionReverseConverter;
import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.TransactionFacade;
import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import com.ashish.monopoly.service.TransactionService;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import com.ashish.monopoly.service.exception.NegativeTransactionAmountException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransactionFacade implements TransactionFacade {
    @Resource
    private GameService gameService;

    @Resource
    private PlayerService playerService;

    @Resource
    private TransactionService transactionService;

    @Resource
    private TransactionConverter transactionConverter;

    @Resource
    private TransactionReverseConverter transactionReverseConverter;

    @Override
    public TransactionData save(TransactionData transactionData) {
        try {
            Transaction transaction = transactionService.save(
                    transactionReverseConverter.convert(transactionData));
            return transactionConverter.convert(transaction);
        } catch (InsufficientBalanceException | NegativeTransactionAmountException exception) {
            transactionData.setIsSuccess(Boolean.FALSE);
            return transactionData;
        }
    }

    @Override
    public TransactionData findById(Integer id) {
        return transactionConverter.convert(transactionService.findById(id));
    }
}
