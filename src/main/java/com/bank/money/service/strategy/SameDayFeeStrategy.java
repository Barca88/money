package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Fee strategy for same-day transactions with amounts up to $1,000.
 * <p>
 * Fee: 3% + $3 flat fee
 * </p>
 */
public class SameDayFeeStrategy implements FeeStrategy {
    
    /**
     * Constructs a new SameDayFeeStrategy.
     */
    public SameDayFeeStrategy() {
    }
    
    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(0.03);
    private static final BigDecimal FLAT_FEE = BigDecimal.valueOf(3);
    private static final BigDecimal AMOUNT_THRESHOLD = BigDecimal.valueOf(1000);
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(PERCENTAGE).add(FLAT_FEE);
    }
    
    @Override
    public boolean isApplicable(BigDecimal amount, long daysBetween) {
        return amount.compareTo(AMOUNT_THRESHOLD) <= 0 && daysBetween == 0;
    }
}
