package com.loco.transaction.management.services;

import com.loco.transaction.management.exceptions.BadRequestException;
import com.loco.transaction.management.exceptions.ResourceNotFoundException;
import com.loco.transaction.management.models.Transactions;
import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.TransactionSummation;
import com.loco.transaction.management.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {

        //Success case
        Long transactionId = 1L;
        TransactionDTO transactionDTO = getTransactionDto(400.0, "cars", null);
        Transactions transaction = getTransaction(transactionId, transactionDTO);
        Transactions savedTransaction = getTransactionCreated(transactionId, transactionDTO);

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());
        when(repository.save(transaction)).thenReturn(savedTransaction);

        Transactions createdTransaction = transactionService.createTransaction(transactionId, transactionDTO);

        Assertions.assertEquals(savedTransaction, createdTransaction);


        //Failure case (Already transaction exist)

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.of(savedTransaction));

        Assertions.assertThrows(BadRequestException.class, () -> {
            transactionService.createTransaction(transactionId, transactionDTO);
        });

        //Failure case (Parent not found)
        Long parentId = 2L;
        transactionDTO.setParentId(parentId);
        when(repository.findByTransactionId(parentId)).thenReturn(Optional.empty());
        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(transactionId, transactionDTO);
        });

        //

    }

    @Test
    void testFindTransactionsByType() {
        List<Long> expectedTransactionIds = new ArrayList<>();
        expectedTransactionIds.add(1L);
        expectedTransactionIds.add(2L);

        when(repository.findAllByType("cars")).thenReturn(expectedTransactionIds);

        List<Long> actualTransactionIds = transactionService.findTransactionsByType("cars");

        Assertions.assertEquals(expectedTransactionIds, actualTransactionIds);

    }

    @Test
    void testFindByTransactionId() {

        //Success case
        Long transactionId = 1L;
        Transactions transaction = getTransactionCreated(transactionId, getTransactionDto(400.0, "cars", null));

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.of(transaction));

        TransactionDTO foundTransactionDTO = transactionService.findByTransactionId(transactionId);

        Assertions.assertNotNull(foundTransactionDTO);
        Assertions.assertEquals(transaction.getAmount(), foundTransactionDTO.getAmount());
        Assertions.assertEquals(transaction.getType(), foundTransactionDTO.getType());
        Assertions.assertEquals(transaction.getParentId(), foundTransactionDTO.getParentId());

        //Failure case
        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.findByTransactionId(transactionId);
        });

    }

    @Test
    void testFindSumOfTransitivelyLinkedTransactions() {
        //Success case
        Long transactionId = 1L;
        Transactions transaction = getTransactionCreated(transactionId, getTransactionDto(400.0, "cars", null));

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.of(transaction));
        when(repository.findAllByParentId(transactionId)).thenReturn(new ArrayList<>());

        TransactionSummation expectedSummation = new TransactionSummation(transaction.getAmount());

        TransactionSummation actualSummation = transactionService.findSumOfTransitivelyLinkedTransactions(transactionId);

        Assertions.assertEquals(expectedSummation, actualSummation);

        //Failure case

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.findSumOfTransitivelyLinkedTransactions(transactionId);
        });
    }

    TransactionDTO getTransactionDto(Double amount, String type, Long parentId) {
        return TransactionDTO.builder()
                .amount(amount)
                .type(type)
                .parentId(parentId)
                .build();
    }

    Transactions getTransaction(Long transactionId, TransactionDTO transactionDTO) {
        return Transactions.builder()
                .transactionId(transactionId)
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .parentId(transactionDTO.getParentId())
                .build();
    }

    Transactions getTransactionCreated(Long transactionId, TransactionDTO transactionDTO) {
        return Transactions.builder()
                .transactionId(transactionId)
                .amount(transactionDTO.getAmount())
                .type(transactionDTO.getType())
                .parentId(transactionDTO.getParentId())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

}