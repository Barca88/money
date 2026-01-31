package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Fee strategy for transactions with amounts between $1,000 and $2,000 scheduled 1-10 days ahead.
 * <p>
 * Fee: 9%
 * </p>
 */
public class ShortTermFeeStrategy implements FeeStrategy {
    
    /**
     * Constructs a new ShortTermFeeStrategy.
     */
    public ShortTermFeeStrategy() {
    }
    
    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(0.09);
    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(1000);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(2000);
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENTAGE);
    }
    
    @Override
    public boolean isApplicable(BigDecimal amount, long daysBetween) {
        return amount.compareTo(MIN_AMOUNT) > 0 
            && amount.compareTo(MAX_AMOUNT) <= 0 
            && daysBetween >= 1 
            && daysBetween <= 10;
    }
}
