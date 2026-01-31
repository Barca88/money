package com.bank.money.service.strategy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Factory for creating appropriate fee calculation strategies.
 * <p>
 * This factory implements the Factory design pattern to select and return
 * the appropriate FeeStrategy implementation based on transaction parameters.
 * </p>
 */
@Component
public class FeeStrategyFactory {
    
    private final List<FeeStrategy> strategies;
    
    /**
     * Constructs a FeeStrategyFactory with all available strategies.
     * <p>
     * Strategies are checked in order, so more specific strategies should come first.
     * </p>
     */
    public FeeStrategyFactory() {
        this.strategies = Arrays.asList(
            new SameDayFeeStrategy(),
            new ShortTermFeeStrategy(),
            new MediumTermFeeStrategy(),
            new LongTermFeeStrategy(),
            new ExtraLongTermFeeStrategy(),
            new VeryLongTermFeeStrategy(),
            new NoFeeStrategy() // Default fallback - should be last
        );
    }
    
    /**
     * Retrieves the appropriate fee strategy based on amount and days.
     * <p>
     * Iterates through available strategies and returns the first one that
     * is applicable for the given parameters.
     * </p>
     *
     * @param amount the transaction amount
     * @param daysBetween the number of days between creation and schedule date
     * @return the appropriate FeeStrategy implementation
     */
    public FeeStrategy getStrategy(BigDecimal amount, long daysBetween) {
        return strategies.stream()
            .filter(strategy -> strategy.isApplicable(amount, daysBetween))
            .findFirst()
            .orElse(new NoFeeStrategy()); // Fallback, though NoFeeStrategy should always match
    }
}
