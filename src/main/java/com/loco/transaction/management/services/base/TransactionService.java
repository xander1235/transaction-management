package com.loco.transaction.management.services.base;

import com.loco.transaction.management.exceptions.ResourceNotFoundException;
import com.loco.transaction.management.exceptions.BadRequestException;
import com.loco.transaction.management.models.Transactions;
import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.TransactionSummation;

import java.util.List;

public interface TransactionService {

    /**'
     * Creates a new transaction
     *
     * @param transactionId transaction id to create the transaction
     * @param transactionDTO transaction details to create the transaction
     * @return the created transaction
     * @throws ResourceNotFoundException if parent transaction not found
     * @throws BadRequestException if the transaction already exist with the id.
     */
    Transactions createTransaction(Long transactionId, TransactionDTO transactionDTO);

    /**
     * Retrieves a list of transaction IDs for transactions of a specific type.
     *
     * @param type The transaction type.
     * @return A list of transaction IDs.
     */
    List<Long> findTransactionsByType(String type);

    /**
     * Retrieves a transaction by ID.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return The retrieved transaction.
     * @throws ResourceNotFoundException if the transaction is not found.
     */
    TransactionDTO findByTransactionId(Long transactionId);

    /**
     * Calculates the aggregation of linked transactions for a given transaction ID.
     *
     * @param transactionId The ID of the transaction.
     * @return The aggregation of linked transactions.
     */
    TransactionSummation findSumOfTransitivelyLinkedTransactions(Long transactionId);

}
