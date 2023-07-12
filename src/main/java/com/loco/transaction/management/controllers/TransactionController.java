package com.loco.transaction.management.controllers;

import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.Response;
import com.loco.transaction.management.pojo.response.TransactionSummation;
import com.loco.transaction.management.services.base.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.loco.transaction.management.constants.EndpointConstants.*;
import static com.loco.transaction.management.constants.LocoExceptionConstants.TRANSACTION_ID_0;

@RestController
@Validated
@RequestMapping(value = TRANSACTION_ROUTE)
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionId  The ID of the transaction to create.
     * @param transactionDTO The transaction data.
     * @return A response entity indicating the status of the operation.
     */
    @PutMapping(value = TRANSACTION_TRANSACTION_ID)
    public ResponseEntity<Response> createTransaction(
            @PathVariable(value = "transactionId") @Min(value = 1, message = TRANSACTION_ID_0) Long transactionId,
            @RequestBody @Valid TransactionDTO transactionDTO) {
        service.createTransaction(transactionId, transactionDTO);
        Response response = Response.builder()
                .status(HttpStatus.OK.name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves a list of transaction IDs for transactions of a specific type.
     *
     * @param type The transaction type.
     * @return A response entity containing the list of transaction IDs.
     */
    @GetMapping(value = TRANSACTION_TYPES)
    public ResponseEntity<List<Long>> getTransactionIdsByType(@PathVariable(value = "type") String type) {
        List<Long> transactionIds = service.findTransactionsByType(type);
        return new ResponseEntity<>(transactionIds, HttpStatus.OK);
    }

    /**
     * Retrieves a transaction by ID.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return A response entity containing the retrieved transaction.
     */
    @GetMapping(value = TRANSACTION_TRANSACTION_ID)
    public ResponseEntity<TransactionDTO> getTransactionByTransactionId(@PathVariable(value = "transactionId") Long transactionId) {
        TransactionDTO transaction = service.findByTransactionId(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    /**
     * Calculates the sum of linked transactions for a given transaction ID.
     *
     * @param transactionId The ID of the transaction.
     * @return A response entity containing the summation of linked transactions.
     */
    @GetMapping(value = TRANSACTION_SUM)
    public ResponseEntity<TransactionSummation> getSumOfTransitivelyLinkedTransactions(@PathVariable(value = "transactionId") Long transactionId) {
        TransactionSummation transactionSummation = service.findSumOfTransitivelyLinkedTransactions(transactionId);
        return new ResponseEntity<>(transactionSummation, HttpStatus.OK);
    }

}
