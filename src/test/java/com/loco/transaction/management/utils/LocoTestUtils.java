package com.loco.transaction.management.utils;

import com.loco.transaction.management.models.Transactions;
import com.loco.transaction.management.pojo.TransactionDTO;

import java.time.LocalDateTime;

public class LocoTestUtils {

    public static Transactions getTransaction(Long transactionId, Double amount, String type, Long parentId) {
        return Transactions.builder()
                .transactionId(transactionId)
                .amount(amount)
                .type(type)
                .parentId(parentId)
                .build();
    }

    public static Transactions getTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return Transactions.builder()
                .transactionId(transactionId)
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .parentId(transactionDTO.getParentId())
                .build();
    }


    public static Transactions getTransactionCreated(Long transactionId, TransactionDTO transactionDTO) {
        return Transactions.builder()
                .transactionId(transactionId)
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .parentId(transactionDTO.getParentId())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static TransactionDTO getTransactionDto(Double amount, String type, Long parentId) {
        return TransactionDTO.builder()
                .amount(amount)
                .type(type)
                .parentId(parentId)
                .build();
    }
}
