package com.bank.money.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;

import com.bank.money.domain.Transaction;
import com.bank.money.exception.BusinessException;
import com.bank.money.repository.TransactionRepository;
import com.bank.money.service.strategy.FeeStrategyFactory;

class TransactionServiceTest {

    private TransactionRepository repository;
    private FeeStrategyFactory feeStrategyFactory;
    private TransactionService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(TransactionRepository.class);
        feeStrategyFactory = new FeeStrategyFactory();
        service = new TransactionService(repository, feeStrategyFactory);
    }

    @Test
    void shouldCalculateFee_TaxaA_SameDay() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("500"));
        transaction.setScheduleDate(LocalDate.now());

        Mockito.when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction saved = service.save(transaction);

        BigDecimal expectedFee = new BigDecimal("18.00"); // 3% + 3€
        assertEquals(0, expectedFee.compareTo(saved.getFee()));
    }

    @Test
    void shouldCalculateFee_TaxaB_1To10Days() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("1500"));
        transaction.setScheduleDate(LocalDate.now().plusDays(5));

        Mockito.when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction saved = service.save(transaction);

        assertEquals(0, new BigDecimal("135.00").compareTo(saved.getFee()));
    }

    @Test
    void shouldCalculateFee_TaxaC_Over2000_21To30Days() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("3000"));
        transaction.setScheduleDate(LocalDate.now().plusDays(25));

        Mockito.when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction saved = service.save(transaction);

        assertEquals(0, new BigDecimal("207.00").compareTo(saved.getFee()));
    }

    @Test
    void shouldReturnZeroFee_WhenNoRuleMatches() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("500"));
        transaction.setScheduleDate(LocalDate.now().plusDays(3));

        Mockito.when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction saved = service.save(transaction);

        assertEquals(BigDecimal.ZERO, saved.getFee());
    }

    @Test
    void shouldThrowException_WhenUpdatingNonExistingTransaction() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Transaction transaction = new Transaction();

        assertThrows(BusinessException.class,
                () -> service.update(1L, transaction));
    }
}
