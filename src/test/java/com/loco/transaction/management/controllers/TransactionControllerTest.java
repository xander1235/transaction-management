package com.loco.transaction.management.controllers;

import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.Response;
import com.loco.transaction.management.pojo.response.TransactionSummation;
import com.loco.transaction.management.services.base.TransactionService;
import com.loco.transaction.management.utils.LocoTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {

        Long transactionId = 1L;
        TransactionDTO transactionDTO = LocoTestUtils.getTransactionDto(400.0, "cars", null);

        ResponseEntity<Response> expectedResponse = new ResponseEntity<>(
                Response.builder().status(HttpStatus.OK.name()).build(),
                HttpStatus.OK
        );

        when(service.createTransaction(transactionId, transactionDTO)).thenReturn(transactionDTO);

        ResponseEntity<Response> actualResponse = controller.createTransaction(transactionId, transactionDTO);

        Assertions.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void testGetTransactionIdsByType() {

        String type = "cars";
        List<Long> expectedTransactionIds = new ArrayList<>();
        expectedTransactionIds.add(1L);
        expectedTransactionIds.add(2L);

        when(service.findTransactionsByType(type)).thenReturn(expectedTransactionIds);

        ResponseEntity<List<Long>> expectedResponse = new ResponseEntity<>(expectedTransactionIds, HttpStatus.OK);
        ResponseEntity<List<Long>> actualResponse = controller.getTransactionIdsByType(type);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetTransactionByTransactionId() {
        Long transactionId = 1L;
        TransactionDTO expectedTransaction = LocoTestUtils.getTransactionDto(400.0, "cars", null);

        when(service.findByTransactionId(transactionId)).thenReturn(expectedTransaction);

        ResponseEntity<TransactionDTO> expectedResponse = new ResponseEntity<>(expectedTransaction, HttpStatus.OK);
        ResponseEntity<TransactionDTO> actualResponse = controller.getTransactionByTransactionId(transactionId);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetSumOfTransitivelyLinkedTransactions() {
        Long transactionId = 1L;
        TransactionSummation expectedSummation = new TransactionSummation(250.0);

        when(service.findSumOfTransitivelyLinkedTransactions(transactionId)).thenReturn(expectedSummation);

        ResponseEntity<TransactionSummation> expectedResponse = new ResponseEntity<>(expectedSummation, HttpStatus.OK);
        ResponseEntity<TransactionSummation> actualResponse = controller.getSumOfTransitivelyLinkedTransactions(transactionId);

        Assertions.assertEquals(expectedResponse, actualResponse);

    }

}