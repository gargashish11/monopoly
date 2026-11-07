package com.ashish.monopoly.converter;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter implements Converter<Transaction, TransactionData> {

  @Override
  public TransactionData convert(Transaction transaction) {
    if (transaction == null) {
      return null;
    }
    TransactionData transactionData = new TransactionData();
    transactionData.setId(transaction.getId());

    if (transaction.getGame() != null) {
      transactionData.setGame_id(transaction.getGame().getId());
    }

    Player payer = transaction.getPayer();
    if (payer != null) {
      transactionData.setPayer_id(payer.getId());
      transactionData.setPayer_name(payer.getName());
    }

    Player payee = transaction.getPayee();
    if (payee != null) {
      transactionData.setPayee_id(payee.getId());
      transactionData.setPayee_name(payee.getName());
    }

    transactionData.setAmount(transaction.getAmount());
    transactionData.setIsSuccess(transaction.getIsSuccess());
    transactionData.setCreatedDate(transaction.getCreatedDate());
    return transactionData;
  }
}
