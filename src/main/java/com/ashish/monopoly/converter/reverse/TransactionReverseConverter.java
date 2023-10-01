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

    @Resource
    private GameService gameService;

    @Resource
    private PlayerService playerService;


    @Override
    public Transaction convert(TransactionData transactionData) {
        Transaction transaction = new Transaction();

        //set Game
        Game game = gameService.findById(transactionData.getGame_id()).orElse(null);
        if (game != null) {
            //get payer
            Player payer = playerService.findById(transactionData.getPayer_id());
            payer.setName(transactionData.getPayer_name());

            //get payee
            Player payee = playerService.findById(transactionData.getPayee_id());
            payee.setName(transactionData.getPayee_name());

            //finally create transaction
//            transaction.setGame(game);
            transaction.setPayer(payer);
            transaction.setPayee(payee);
            transaction.setAmount(transactionData.getAmount());
//            game.addTransaction(transaction);

            //set id only if an old transaction
            if (transactionData.getId() != null) {
                transaction.setId(transactionData.getId());
            }
        }
        return transaction;
    }
}
