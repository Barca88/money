package com.bank.money.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.money.domain.Transaction;
import com.bank.money.dto.TransactionRequestDTO;
import com.bank.money.exception.BusinessException;
import com.bank.money.service.TransactionService;

import jakarta.validation.Valid;

/**
 * REST controller for managing financial transactions.
 * <p>
 * This controller provides endpoints for CRUD operations on scheduled transactions.
 * All endpoints are mapped under the base path {@code /api/transactions}.
 * </p>
 * <p>
 * Supported operations:
 * </p>
 * <ul>
 *   <li>GET /api/transactions - Retrieve all transactions</li>
 *   <li>GET /api/transactions/{id} - Retrieve a specific transaction by ID</li>
 *   <li>POST /api/transactions - Create a new scheduled transaction</li>
 *   <li>PUT /api/transactions/{id} - Update an existing transaction</li>
 *   <li>DELETE /api/transactions/{id} - Delete a transaction</li>
 * </ul>
 *
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    /**
     * Constructs a new TransactionController with the specified service.
     *
     * @param service the transaction service to handle business logic
     */
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * Retrieves all transactions in the system.
     *
     * @return a list of all transactions
     */
    @GetMapping
    public List<Transaction> getAll() {
        return service.findAll();
    }

    /**
     * Retrieves a specific transaction by its ID.
     *
     * @param id the unique identifier of the transaction
     * @return a ResponseEntity containing the transaction if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new scheduled transaction.
     * <p>
     * The transaction fee will be automatically calculated based on the amount
     * and the number of days between today and the scheduled date.
     * </p>
     *
     * @param dto the transaction data transfer object containing transaction details
     * @return a ResponseEntity containing the created transaction with calculated fee
     */
    @PostMapping
    public ResponseEntity<Transaction> create(@Valid @RequestBody TransactionRequestDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAccountOrigin(dto.getAccountOrigin());
        transaction.setAccountDestination(dto.getAccountDestination());
        transaction.setScheduleDate(dto.getScheduleDate());
        transaction.setAmount(dto.getAmount());

        Transaction saved = service.save(transaction);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an existing transaction.
     * <p>
     * The transaction fee will be recalculated based on the updated amount
     * and schedule date.
     * </p>
     *
     * @param id the unique identifier of the transaction to update
     * @param dto the transaction data transfer object containing updated details
     * @return a ResponseEntity containing the updated transaction, or 404 Not Found if the transaction doesn't exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> update(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAccountOrigin(dto.getAccountOrigin());
        transaction.setAccountDestination(dto.getAccountDestination());
        transaction.setScheduleDate(dto.getScheduleDate());
        transaction.setAmount(dto.getAmount());

        try {
            Transaction updated = service.update(id, transaction);
            return ResponseEntity.ok(updated);
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id the unique identifier of the transaction to delete
     * @return a ResponseEntity with status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
