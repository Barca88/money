package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Fee strategy for transactions with amounts over $2,000 scheduled more than 40 days ahead.
 * <p>
 * Fee: 1.7%
 * </p>
 */
public class VeryLongTermFeeStrategy implements FeeStrategy {
    
    /**
     * Constructs a new VeryLongTermFeeStrategy.
     */
    public VeryLongTermFeeStrategy() {
    }
    
    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(0.017);
    private static final BigDecimal AMOUNT_THRESHOLD = BigDecimal.valueOf(2000);
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENTAGE);
    }
    
    @Override
    public boolean isApplicable(BigDecimal amount, long daysBetween) {
        return amount.compareTo(AMOUNT_THRESHOLD) > 0 && daysBetween > 40;
    }
}
