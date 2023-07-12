package com.loco.transaction.management.controllers;

import com.loco.transaction.management.pojo.TransactionDTO;
import com.loco.transaction.management.pojo.response.Response;
import com.loco.transaction.management.pojo.response.TransactionSummation;
import com.loco.transaction.management.services.base.TransactionService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;

    private Validator validator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testCreateTransaction() {

        Long transactionId = 1L;
        TransactionDTO transactionDTO = getTransactionDto(400.0, "cars", null);

        ResponseEntity<Response> expectedResponse = new ResponseEntity<>(
                Response.builder().status(HttpStatus.OK.name()).build(),
                HttpStatus.OK
        );

        when(service.createTransaction(transactionId, transactionDTO)).thenReturn(null);

        ResponseEntity<Response> actualResponse = controller.createTransaction(transactionId, transactionDTO);
        System.out.println(actualResponse);
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
        TransactionDTO expectedTransaction = getTransactionDto(400.0, "cars", null);

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

    TransactionDTO getTransactionDto(Double amount, String type, Long parentId) {
        return TransactionDTO.builder()
                .amount(amount)
                .type(type)
                .parentId(parentId)
                .build();
    }

}