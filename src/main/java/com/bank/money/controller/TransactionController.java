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

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
