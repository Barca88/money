package com.bank.money.service.strategy;

import java.math.BigDecimal;

/**
 * Default fee strategy when no other strategy applies.
 * <p>
 * Returns zero fee.
 * </p>
 */
public class NoFeeStrategy implements FeeStrategy {
    
    /**
     * Constructs a new NoFeeStrategy.
     */
    public NoFeeStrategy() {
    }
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
    
    @Override
    public boolean isApplicable(BigDecimal amount, long daysBetween) {
        // This is the default strategy, always applicable as fallback
        return true;
    }
}
