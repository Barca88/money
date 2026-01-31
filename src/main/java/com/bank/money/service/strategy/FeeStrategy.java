package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Strategy interface for calculating transaction fees.
 * <p>
 * Implementations of this interface define specific fee calculation algorithms
 * based on transaction amount and scheduling parameters.
 * </p>
 */
public interface FeeStrategy {
    
    /**
     * Calculates the fee for a transaction.
     *
     * @param amount the transaction amount
     * @return the calculated fee
     */
    BigDecimal calculateFee(BigDecimal amount);
    
    /**
     * Checks if this strategy is applicable for the given amount and days.
     *
     * @param amount the transaction amount
     * @param daysBetween the number of days between creation and schedule date
     * @return true if this strategy should be used, false otherwise
     */
    boolean isApplicable(BigDecimal amount, long daysBetween);
}
