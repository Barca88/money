package com.bank.money.exception;

/**
 * Custom runtime exception for business logic errors.
 * <p>
 * This exception is thrown when business rules are violated or when
 * business operations cannot be completed. It extends RuntimeException,
 * making it an unchecked exception that doesn't need to be declared in method signatures.
 * </p>
 * <p>
 * Common use cases include:
 * </p>
 * <ul>
 *   <li>Transaction not found during update operations</li>
 *   <li>Business rule violations</li>
 *   <li>Invalid business state conditions</li>
 * </ul>
 *
 */
public class BusinessException extends RuntimeException {
    
    /**
     * Constructs a new BusinessException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public BusinessException(String message) {
        super(message);
    }
}
