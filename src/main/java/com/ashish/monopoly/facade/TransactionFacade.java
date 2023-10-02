package com.ashish.monopoly.facade;

import com.ashish.monopoly.data.TransactionData;

public interface TransactionFacade {
    TransactionData save(TransactionData transactionData);

    TransactionData findById(Integer id);
}
