package com.loco.transaction.management.services;

import com.loco.transaction.management.exceptions.BadRequestException;
import com.loco.transaction.management.exceptions.ResourceNotFoundException;
import com.loco.transaction.management.models.Transactions;
import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.TransactionSummation;
import com.loco.transaction.management.repositories.TransactionRepository;
import com.loco.transaction.management.utils.LocoTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    TransactionDTO transactionDTO;
    Transactions transaction;
    Transactions savedTransaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        transactionDTO = LocoTestUtils.getTransactionDto(400.0, "cars", null);

        transaction = LocoTestUtils.getTransaction(1L, transactionDTO);
        savedTransaction = LocoTestUtils.getTransactionCreated(1L, transactionDTO);
    }

    @Test
    void testCreateTransaction() {

        //Success case
        Long transactionId  = 1L;

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());
        when(repository.save(transaction)).thenReturn(savedTransaction);

        TransactionDTO createdTransactionDto = transactionService.createTransaction(transactionId, transactionDTO);

        Assertions.assertEquals(transactionDTO, createdTransactionDto);

    }

    @Test
    void testCreateTransaction_AlreadyExists() {
        //Failure case (Already transaction exist)
        Long transactionId  = 1L;

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.of(savedTransaction));

        Assertions.assertThrows(BadRequestException.class, () -> {
            transactionService.createTransaction(transactionId, transactionDTO);
        });
    }

    @Test
    void testCreateTransaction_ParentNotFound() {
        //Failure case (Parent not found)
        Long transactionId  = 1L;

        Long parentId = 2L;
        TransactionDTO transactionDTO2 = LocoTestUtils.getTransactionDto(400.0, "cars", parentId);

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());
        when(repository.findByTransactionId(parentId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(transactionId, transactionDTO2);
        });

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

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.of(savedTransaction));

        TransactionDTO foundTransactionDTO = transactionService.findByTransactionId(transactionId);

        Assertions.assertNotNull(foundTransactionDTO);
        Assertions.assertEquals(savedTransaction.getAmount(), foundTransactionDTO.getAmount());
        Assertions.assertEquals(savedTransaction.getType(), foundTransactionDTO.getType());
        Assertions.assertEquals(savedTransaction.getParentId(), foundTransactionDTO.getParentId());

    }

    @Test
    void testFindByTransactionId_NotFound() {
        //Failure case
        Long transactionId = 1L;

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.findByTransactionId(transactionId);
        });
    }

    @Test
    void testFindSumOfTransitivelyLinkedTransactions() {
        //Success case
        Long transactionId = 1L;

        TransactionDTO transactionDto1 = LocoTestUtils.getTransactionDto(400.0, "cars", 1L);
        Transactions savedTransaction1 = LocoTestUtils.getTransactionCreated(2L, transactionDto1);

        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(savedTransaction1);

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.of(savedTransaction));
        when(repository.findAllByParentId(transactionId)).thenReturn(transactionsList);
        when(repository.findAllByParentId(savedTransaction1.getTransactionId())).thenReturn(new ArrayList<>());

        TransactionSummation expectedSummation = new TransactionSummation(transaction.getAmount() + savedTransaction1.getAmount());

        TransactionSummation actualSummation = transactionService.findSumOfTransitivelyLinkedTransactions(transactionId);

        Assertions.assertEquals(expectedSummation, actualSummation);

    }

    @Test
    void testFindSumOfTransitivelyLinkedTransactions_NotFound() {
        //Failure case
        Long transactionId = 1L;

        when(repository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.findSumOfTransitivelyLinkedTransactions(transactionId);
        });
    }

}