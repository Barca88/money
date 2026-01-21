package com.bank.money.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bank.money.domain.Transaction;
import com.bank.money.exception.BusinessException;
import com.bank.money.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        transaction.setCreationDate(LocalDate.now());

        BigDecimal fee = calculateFee(transaction.getAmount(), transaction.getScheduleDate());
        System.out.println("This is the calculated fee = " + fee);
        transaction.setFee(fee);
        return repository.save(transaction);
    }

    public Transaction update(Long id, Transaction transaction) {
        return repository.findById(id).map(existing -> {
            existing.setAccountOrigin(transaction.getAccountOrigin());
            existing.setAccountDestination(transaction.getAccountDestination());
            existing.setScheduleDate(transaction.getScheduleDate());
            existing.setAmount(transaction.getAmount());
            existing.setFee(calculateFee(transaction.getAmount(), transaction.getScheduleDate()));
            return repository.save(existing);
        }).orElseThrow(() -> new BusinessException("Transaction not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private BigDecimal calculateFee(BigDecimal amount, LocalDate scheduleDate) {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), scheduleDate);
        System.out.println("Days between today and scheduleDate: " + daysBetween);

        if (amount.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            if (daysBetween == 0) { // same day
                return amount.multiply(BigDecimal.valueOf(0.03)).add(BigDecimal.valueOf(3));
            }
        } else if (amount.compareTo(BigDecimal.valueOf(2000)) <= 0) {
            if (daysBetween >= 1 && daysBetween <= 10) {
                return amount.multiply(BigDecimal.valueOf(0.09));
            }
        } else {
            if (daysBetween >= 11 && daysBetween <= 20) {
                return amount.multiply(BigDecimal.valueOf(0.082));
            } else if (daysBetween >= 21 && daysBetween <= 30) {
                return amount.multiply(BigDecimal.valueOf(0.069));
            } else if (daysBetween >= 31 && daysBetween <= 40) {
                return amount.multiply(BigDecimal.valueOf(0.047));
            } else if (daysBetween > 40) {
                return amount.multiply(BigDecimal.valueOf(0.017));
            }
        }
        return BigDecimal.ZERO;
    }
}
