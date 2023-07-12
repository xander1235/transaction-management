package com.loco.transaction.management.repositories;

import com.loco.transaction.management.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    /**
     * Retrieves a list of transaction IDs for a specific type.
     *
     * @param type The transaction type.
     * @return A list of transaction IDs.
     */
    @Query(value = "select transaction_id from transactions where type = :type", nativeQuery = true)
    List<Long> findAllByType(@Param(value = "type") String type);

    /**
     * Retrieves a unique transaction with given transactionId.
     *
     * @param transactionId The transaction ID.
     * @return Optional Transaction.
     */
    @Query(value = "select * from transactions where transaction_id = :transactionId", nativeQuery = true)
    Optional<Transactions> findByTransactionId(@Param(value = "transactionId") Long transactionId);

    /**
     * Retrieves a list of transactions that have a specific parent ID.
     *
     * @param parentId The parent ID.
     * @return A list of Transactions.
     */
    @Query(value = "select * from transactions where parent_id = :parentId", nativeQuery = true)
    List<Transactions> findAllByParentId(@Param(value = "parentId") Long parentId);
}
