package com.bank.money;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Money Transaction Management System.
 * <p>
 * This Spring Boot application provides a RESTful API for managing financial transactions,
 * including creating, updating, retrieving, and deleting scheduled money transfers between accounts.
 * The application automatically calculates transaction fees based on the amount and schedule date.
 * </p>
 *
 */
@SpringBootApplication
public class MoneyApplication {

    /**
     * Main entry point for the Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
    }
}
