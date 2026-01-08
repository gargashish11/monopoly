package com.ashish.monopoly.controllers;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.TransactionFacade;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import com.ashish.monopoly.service.exception.NegativeTransactionAmountException;
import com.ashish.monopoly.service.exception.SameAccountTransaction;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionFacade transactionFacade;

    @GetMapping("{id}")
    public ResponseEntity<TransactionData> getTransaction(@PathVariable Integer id) {
        TransactionData data = transactionFacade.findById(id);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/add")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionData transactionData) {
        try {
            TransactionData savedTransaction = transactionFacade.save(transactionData);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
        } catch (InsufficientBalanceException | NegativeTransactionAmountException | SameAccountTransaction e) {
            // 3. Error Handling: Map business logic failures to 400 Bad Request
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Transaction Failed",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
