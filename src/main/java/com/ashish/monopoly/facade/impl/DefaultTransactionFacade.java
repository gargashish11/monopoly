package com.ashish.monopoly.facade.impl;

import com.ashish.monopoly.facade.TransactionFacade;
import com.ashish.monopoly.service.GameService;
import com.ashish.monopoly.service.PlayerService;
import com.ashish.monopoly.service.TransactionService;
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

}
