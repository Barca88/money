package com.bank.money.repository;

import com.bank.money.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    void shouldPersistAndRetrieveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountOrigin("123");
        transaction.setAccountDestination("456");
        transaction.setAmount(new BigDecimal("1000"));
        transaction.setScheduleDate(LocalDate.now());
        transaction.setCreationDate(LocalDate.now());
        transaction.setFee(new BigDecimal("33"));

        Transaction saved = repository.save(transaction);

        assertThat(saved.getId()).isNotNull();

        Transaction found = repository.findById(saved.getId()).orElseThrow();
        assertThat(found.getAmount()).isEqualByComparingTo("1000");
    }
}
