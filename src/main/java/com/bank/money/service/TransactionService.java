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

/**
 * Service layer for managing transaction business logic.
 * <p>
 * This service provides methods for CRUD operations on transactions and implements
 * the business rules for calculating transaction fees based on amount and schedule date.
 * </p>
 * <p>
 * Fee Calculation Rules:
 * </p>
 * <ul>
 *   <li>For amounts up to $1,000 scheduled for the same day (0 days): 3% + $3 flat fee</li>
 *   <li>For amounts up to $2,000 scheduled 1-10 days ahead: 9%</li>
 *   <li>For amounts over $2,000 scheduled 11-20 days ahead: 8.2%</li>
 *   <li>For amounts over $2,000 scheduled 21-30 days ahead: 6.9%</li>
 *   <li>For amounts over $2,000 scheduled 31-40 days ahead: 4.7%</li>
 *   <li>For amounts over $2,000 scheduled more than 40 days ahead: 1.7%</li>
 *   <li>If no condition matches, fee is $0</li>
 * </ul>
 *
 */
@Service
public class TransactionService {

    private final TransactionRepository repository;

    /**
     * Constructs a new TransactionService with the specified repository.
     *
     * @param repository the transaction repository for data access
     */
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all transactions from the database.
     *
     * @return a list of all transactions
     */
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    /**
     * Finds a transaction by its unique identifier.
     *
     * @param id the transaction ID
     * @return an Optional containing the transaction if found, or empty if not found
     */
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Saves a new transaction to the database.
     * <p>
     * This method automatically sets the creation date to the current date
     * and calculates the transaction fee based on the amount and schedule date.
     * </p>
     *
     * @param transaction the transaction to save
     * @return the saved transaction with ID, creation date, and calculated fee
     */
    public Transaction save(Transaction transaction) {
        transaction.setCreationDate(LocalDate.now());

        BigDecimal fee = calculateFee(transaction.getAmount(), transaction.getScheduleDate());
        System.out.println("This is the calculated fee = " + fee);
        transaction.setFee(fee);
        return repository.save(transaction);
    }

    /**
     * Updates an existing transaction.
     * <p>
     * This method updates the modifiable fields of the transaction and recalculates
     * the fee based on the new amount and schedule date. The creation date is preserved.
     * </p>
     *
     * @param id the ID of the transaction to update
     * @param transaction the transaction object containing the updated values
     * @return the updated transaction
     * @throws BusinessException if the transaction with the specified ID is not found
     */
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

    /**
     * Deletes a transaction by its ID.
     *
     * @param id the ID of the transaction to delete
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /**
     * Calculates the transaction fee based on the amount and schedule date.
     * <p>
     * The fee is determined by the amount and the number of days between the current
     * date and the scheduled date according to the business rules defined in the class documentation.
     * </p>
     *
     * @param amount the transaction amount
     * @param scheduleDate the date when the transaction is scheduled to occur
     * @return the calculated fee, or BigDecimal.ZERO if no fee rules apply
     */
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
