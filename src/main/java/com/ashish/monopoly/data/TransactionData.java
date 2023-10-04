package com.ashish.monopoly.data;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionData {

    private Integer id;

    private Integer game_id;

    private Integer payer_id;

    private String payer_name;

    private Integer payee_id;

    private String payee_name;

    private Integer amount;

    private Boolean isSuccess;

    private Date createdDate;

}
