package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.repository.TransactionRepository;
import com.ashish.monopoly.service.GamePlayerService;
import com.ashish.monopoly.service.TransactionService;
import com.ashish.monopoly.service.exception.NegativeTransactionAmountException;
import com.ashish.monopoly.service.exception.SameAccountTransaction;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

  @Transactional(rollbackFor = Exception.class)
  @Override
  public Transaction save(Transaction transaction) {
    updateGamePlayerBalance(transaction);
    transaction.setIsSuccess(true);
    return transactionRepository.save(transaction);
  }

  private void updateGamePlayerBalance(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }
    Integer txnAmount = transaction.getAmount();
    if (txnAmount == null || txnAmount <= 0) {
      transaction.setIsSuccess(false);
      throw new NegativeTransactionAmountException("Transaction amount must be greater than zero");
    }

    if (transaction.getGame() == null || transaction.getGame().getId() == null) {
      throw new IllegalArgumentException("Game ID must be specified for the transaction");
    }
    if (transaction.getPayer() == null || transaction.getPayee() == null) {
      throw new IllegalArgumentException("Both payer and payee must be specified");
    }

    if (transaction.getPayer().equals(transaction.getPayee())) {
      transaction.setIsSuccess(false);
      throw new SameAccountTransaction("Payer and payee cannot be the same player");
    }

    GamePlayer payer = gamePlayerService.findByGame_IdAndPlayer(transaction.getGame().getId(), transaction.getPayer());
    if (payer == null) {
      throw new EntityNotFoundException("Payer is not enrolled in this game");
    }

    GamePlayer payee = gamePlayerService.findByGame_IdAndPlayer(transaction.getGame().getId(), transaction.getPayee());
    if (payee == null) {
      throw new EntityNotFoundException("Payee is not enrolled in this game");
    }

    if (payer.equals(payee)) {
      transaction.setIsSuccess(false);
      throw new SameAccountTransaction("Payer and payee cannot be the same player");
    }

    transaction.setPayer(payer.getPlayer());
    transaction.setPayee(payee.getPlayer());

    gamePlayerService.updateBalance(payer, txnAmount, CreditDebit.DEBIT);
    gamePlayerService.updateBalance(payee, txnAmount, CreditDebit.CREDIT);
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
  public Set<Transaction> filter(Set<Transaction> transactionSet, int maxSize) {
    if (transactionSet == null || transactionSet.isEmpty() || maxSize <= 0) {
      return Collections.emptySet();
    }
    return transactionSet.stream()
            .sorted(Comparator.comparing(Transaction::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(maxSize)
            .collect(Collectors.toCollection(LinkedHashSet::new));
  }
}

