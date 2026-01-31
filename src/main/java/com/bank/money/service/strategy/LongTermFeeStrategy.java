package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Fee strategy for transactions with amounts over $2,000 scheduled 21-30 days ahead.
 * <p>
 * Fee: 6.9%
 * </p>
 */
public class LongTermFeeStrategy implements FeeStrategy {
    
    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(0.069);
    private static final BigDecimal AMOUNT_THRESHOLD = BigDecimal.valueOf(2000);
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENTAGE);
    }
    
    @Override
    public boolean isApplicable(BigDecimal amount, long daysBetween) {
        return amount.compareTo(AMOUNT_THRESHOLD) > 0 
            && daysBetween >= 21 
            && daysBetween <= 30;
    }
}
