package com.bank.money.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionRequestDTO {

    @NotNull
    private String accountOrigin;

    @NotNull
    private String accountDestination;

    @NotNull
    private LocalDate scheduleDate;

    @NotNull
    private BigDecimal amount;
}
