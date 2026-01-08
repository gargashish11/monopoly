package com.ashish.monopoly.repository;

import com.ashish.monopoly.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends
        PagingAndSortingRepository<Transaction, Integer>, CrudRepository<Transaction, Integer> {
}
