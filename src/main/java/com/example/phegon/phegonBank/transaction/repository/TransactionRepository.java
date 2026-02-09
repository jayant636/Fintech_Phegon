package com.example.phegon.phegonBank.transaction.repository;

import com.example.phegon.phegonBank.account.entity.AccountEntity;
import com.example.phegon.phegonBank.transaction.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
    Page<TransactionEntity> findByAccount_AccountNumber(String accountNumber, Pageable pageable);
    List<TransactionEntity> findByAccount_AccountNumber(String accountNumber);
}
