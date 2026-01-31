package com.bank.money.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for creating or updating transactions.
 * <p>
 * This DTO is used to transfer transaction data between the client and the API.
 * It contains only the fields that can be set by the user when creating or updating a transaction.
 * Fields like ID, creation date, and fee are not included as they are managed by the system.
 * </p>
 * <p>
 * All fields are validated using Jakarta Bean Validation annotations.
 * </p>
 *
 */
@Data
public class TransactionRequestDTO {
    
    /**
     * Constructs a new empty TransactionRequestDTO.
     */
    public TransactionRequestDTO() {
    }

    /**
     * Account number from which the money will be transferred.
     * Must not be null.
     */
    @NotNull
    private String accountOrigin;

    /**
     * Account number to which the money will be transferred.
     * Must not be null.
     */
    @NotNull
    private String accountDestination;

    /**
     * Date when the transaction is scheduled to be executed.
     * Must not be null. This date affects the fee calculation.
     */
    @NotNull
    private LocalDate scheduleDate;

    /**
     * Amount of money to be transferred (excluding fees).
     * Must not be null.
     */
    @NotNull
    private BigDecimal amount;
}
