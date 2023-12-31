package com.loco.transaction.management.services;

import com.loco.transaction.management.constants.LocoConstants;
import com.loco.transaction.management.constants.LocoExceptionConstants;
import com.loco.transaction.management.exceptions.BadRequestException;
import com.loco.transaction.management.exceptions.ResourceNotFoundException;
import com.loco.transaction.management.models.Transactions;
import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.TransactionSummation;
import com.loco.transaction.management.repositories.TransactionRepository;
import com.loco.transaction.management.services.base.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public TransactionDTO createTransaction(Long transactionId, TransactionDTO transactionDTO) {
        log.info(LocoConstants.TRANSACTION_CREATION, transactionId);

        //checking if transaction already exists
        if (repository.findByTransactionId(transactionId).isPresent()) {
            log.error(LocoExceptionConstants.TRANSACTION_EXISTS);
            throw new BadRequestException(LocoExceptionConstants.TRANSACTION_EXISTS);
        }

        if (transactionDTO.getParentId() != null) {
            //checking transaction exist with specific parentId exist, if it doesn't exist throwing an exception
            repository.findByTransactionId(transactionDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(LocoExceptionConstants.PARENT_NOT_FOUND));
        }

        Transactions transactions = Transactions.builder()
                .transactionId(transactionId)
                .type(transactionDTO.getType())
                .amount(transactionDTO.getAmount())
                .parentId(transactionDTO.getParentId())
                .build();
        transactions = repository.save(transactions);
        return TransactionDTO.builder()
                .amount(transactions.getAmount())
                .type(transactions.getType())
                .parentId(transactions.getParentId())
                .build();
    }

    @Override
    public List<Long> findTransactionsByType(String type) {
        log.info(LocoConstants.PROCESSING_TRANSACTION_TYPE, type);

        return repository.findAllByType(type);
    }

    @Override
    public TransactionDTO findByTransactionId(Long transactionId) {
        log.info(LocoConstants.PROCESSING_TRANSACTION_ID, transactionId);

        //If transaction not found throwing an error
        Transactions transactions = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(LocoExceptionConstants.TRANSACTION_NOT_FOUND));

        return TransactionDTO.builder()
                .amount(transactions.getAmount())
                .type(transactions.getType())
                .parentId(transactions.getParentId())
                .build();
    }

    @Override
    public TransactionSummation findSumOfTransitivelyLinkedTransactions(Long transactionId) {
        log.info(LocoConstants.TRANSACTION_AGGREGATION, transactionId);

        //If transaction not found throwing an error
        Transactions transactions = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(LocoExceptionConstants.TRANSACTION_NOT_FOUND));

        Double amount = transactions.getAmount() + aggregateSumOfChildrenTransactions(transactions.getTransactionId());

        return TransactionSummation.builder()
                .sum(amount)
                .build();
    }

    /**
     * Recursively calculates the sum of linked transactions for a given transaction ID.
     *
     * @param parentId The ID of the transaction.
     * @return The sum of children transactions.
     */
    private Double aggregateSumOfChildrenTransactions(Long parentId) {

        double sum = 0.0;
        List<Transactions> children = repository.findAllByParentId(parentId);

        for (Transactions child : children) {
            sum += child.getAmount() + aggregateSumOfChildrenTransactions(child.getTransactionId());
        }

        return sum;
    }

}
