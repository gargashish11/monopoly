package com.ashish.monopoly.converter.reverse;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionReverseConverter implements Converter<TransactionData, Transaction> {

  @Override
  public Transaction convert(TransactionData transactionData) {
    if (transactionData == null) {
      return null;
    }
    Transaction transaction = new Transaction();
    if (transactionData.getId() != null) {
      transaction.setId(transactionData.getId());
      transaction.setIsSuccess(transactionData.getIsSuccess() != null ? transactionData.getIsSuccess() : Boolean.FALSE);
    } else {
      transaction.setIsSuccess(Boolean.FALSE);
    }
    transaction.setAmount(transactionData.getAmount());

    //set Game
    if (transactionData.getGame_id() != null) {
      Game game = new Game();
      game.setId(transactionData.getGame_id());
      transaction.setGame(game);
    }

    //set payer
    if (transactionData.getPayer_id() != null || transactionData.getPayer_name() != null) {
      Player payer = new Player();
      payer.setId(transactionData.getPayer_id());
      payer.setName(transactionData.getPayer_name());
      transaction.setPayer(payer);
    }

    //set payee
    if (transactionData.getPayee_id() != null || transactionData.getPayee_name() != null) {
      Player payee = new Player();
      payee.setId(transactionData.getPayee_id());
      payee.setName(transactionData.getPayee_name());
      transaction.setPayee(payee);
    }

    return transaction;
  }
}
