package com.ashish.monopoly.converter.reverse;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionReverseConverter implements Converter<TransactionData, Transaction> {

    @Override
    public Transaction convert(TransactionData transactionData) {
        Transaction transaction = new Transaction();
        if (transactionData.getId() != null) {
            transaction.setId(transactionData.getId());
            transaction.setIsSuccess(transactionData.getIsSuccess());
        }
        transaction.setIsSuccess(Boolean.FALSE);
        transaction.setAmount(transactionData.getAmount());

        //set Game
        Game game = new Game();
        game.setId(transactionData.getGame_id());

        //set payer
        Player payer = new Player();
        payer.setId(transactionData.getPayer_id());
        payer.setName(transactionData.getPayer_name());

        //set payee
        Player payee = new Player();
        payee.setId(transactionData.getPayee_id());
        payee.setName(transactionData.getPayee_name());

        //finally create transaction
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transaction.setGame(game);

        //set id only if an old transaction
        return transaction;
    }
}
