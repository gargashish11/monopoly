package com.ashish.monopoly.controllers.ajax;

import com.ashish.monopoly.data.TransactionData;
import com.ashish.monopoly.facade.TransactionFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView addTransaction(@RequestBody TransactionData transactionData,
                                       RedirectAttributes redirectAttributes) {
        TransactionData savedTransactionData = transactionFacade.save(transactionData);
        redirectAttributes
                .addAttribute("id", savedTransactionData.getGame_id())
                .addAttribute("txnSuccess", savedTransactionData.getIsSuccess());
        return new RedirectView("/game/{id}", true);
    }

}
