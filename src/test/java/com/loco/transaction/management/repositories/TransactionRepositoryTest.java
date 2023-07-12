package com.loco.transaction.management.repositories;

import com.loco.transaction.management.models.Transactions;
import com.loco.transaction.management.utils.LocoTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository repository;

    Transactions parent;
    Transactions child1;
    Transactions child2;

    @BeforeEach
    public void setup() {
        parent = LocoTestUtils.getTransaction(1L, 400.0, "cars", null);
        child1 = LocoTestUtils.getTransaction(2L, 500.0, "mobiles", 1L);
        child2 = LocoTestUtils.getTransaction(3L, 500.0, "mobiles", 1L);

    }

    @Test
    public void testSave() {

        // Save the transaction
        Transactions savedTransaction = repository.save(parent);

        // Assert the saved transaction
        Assertions.assertNotNull(savedTransaction.getTransactionId());
    }

    @Test
    void testFindAllByType() {
        // Save test transactions

        repository.save(parent);
        repository.save(child1);
        repository.save(child2);

        // Retrieve transactions by type
        List<Long> transactionIds = repository.findAllByType("mobiles");

        Assertions.assertEquals(2, transactionIds.size());
        Assertions.assertTrue(transactionIds.contains(child1.getTransactionId()));
        Assertions.assertTrue(transactionIds.contains(child2.getTransactionId()));

    }

    @Test
    void testFindByTransactionId() {
        // Save a test transaction
        repository.save(parent);

        // Retrieve transaction by ID
        Optional<Transactions> optionalTransaction = repository.findByTransactionId(parent.getTransactionId());

        // Assert the result
        Assertions.assertTrue(optionalTransaction.isPresent());
        Assertions.assertEquals(parent.getTransactionId(), optionalTransaction.get().getTransactionId());

    }

    @Test
    void testFindAllByParentId() {
        // Save test transactions with the same parent ID
        repository.save(parent);

        repository.save(child1);

        repository.save(child2);

        // Retrieve transactions by parent ID
        List<Transactions> transactions = repository.findAllByParentId(parent.getTransactionId());

        // Assert the result
        Assertions.assertEquals(2, transactions.size());
        Assertions.assertTrue(transactions.stream().allMatch(t -> t.getParentId().equals(parent.getTransactionId())));
    }

}