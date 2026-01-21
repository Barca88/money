package com.bank.money.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a financial transaction between two accounts.
 * <p>
 * This entity stores all information about a scheduled money transfer,
 * including origin and destination accounts, amounts, dates, and calculated fees.
 * Transactions are persisted to the {@code transactions} table in the database.
 * </p>
 * <p>
 * The fee is automatically calculated based on the transaction amount and the
 * number of days between creation date and scheduled date according to the
 * following rules:
 * </p>
 * <ul>
 *   <li>Amount ≤ $1,000 on same day (0 days): 3% + $3</li>
 *   <li>Amount ≤ $2,000 and 1-10 days: 9%</li>
 *   <li>Amount > $2,000 and 11-20 days: 8.2%</li>
 *   <li>Amount > $2,000 and 21-30 days: 6.9%</li>
 *   <li>Amount > $2,000 and 31-40 days: 4.7%</li>
 *   <li>Amount > $2,000 and 40+ days: 1.7%</li>
 * </ul>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Account number from which the money will be transferred.
     */
    @NotNull
    private String accountOrigin;

    /**
     * Account number to which the money will be transferred.
     */
    @NotNull
    private String accountDestination;

    /**
     * Date when the transaction was created in the system.
     * This date is automatically set when the transaction is saved.
     */
    @NotNull
    private LocalDate creationDate;

    /**
     * Date when the transaction is scheduled to be executed.
     * This date affects the fee calculation.
     */
    @NotNull
    private LocalDate scheduleDate;

    /**
     * Amount of money to be transferred (excluding fees).
     */
    @NotNull
    private BigDecimal amount;

    /**
     * Calculated transaction fee based on amount and schedule date.
     * This value is automatically calculated and set by the service layer.
     */
    private BigDecimal fee;
}
