package com.bank.money.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.money.domain.Transaction;

/**
 * Repository interface for Transaction entity persistence operations.
 * <p>
 * This interface extends Spring Data JPA's JpaRepository, providing standard
 * CRUD operations and query methods for the Transaction entity.
 * No custom query methods are currently defined.
 * </p>
 *
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
