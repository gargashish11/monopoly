package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.TransactionFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Resource
    private TransactionFacade transactionFacade;

    @GetMapping("{id}")
    public TransactionData get(@PathVariable Integer id) {
        return transactionFacade.findById(id);
    }

    @PutMapping("/add")
    public Map<String, Integer> addTransaction(@RequestBody TransactionData transactionData) {
        TransactionData savedTransactionData = transactionFacade.save(transactionData);
        Map<String, Integer> response = new HashMap<>();
        response.put("id", savedTransactionData.getGame_id());
        response.put("txnSuccess", savedTransactionData.getIsSuccess() ? 1 : 0);
        return response;
    }

}
