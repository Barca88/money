package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Fee strategy for transactions with amounts over $2,000 scheduled 11-20 days ahead.
 * <p>
 * Fee: 8.2%
 * </p>
 */
public class MediumTermFeeStrategy implements FeeStrategy {
    
    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(0.082);
    private static final BigDecimal AMOUNT_THRESHOLD = BigDecimal.valueOf(2000);
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENTAGE);
    }
    
    @Override
    public boolean isApplicable(BigDecimal amount, long daysBetween) {
        return amount.compareTo(AMOUNT_THRESHOLD) > 0 
            && daysBetween >= 11 
            && daysBetween <= 20;
    }
}
