package com.billreminder.billpaymentreminder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;

@SpringBootApplication
public class BillPaymentReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillPaymentReminderApplication.class, args);
    }

    @Bean
    public CommandLineRunner testRepositories(BillCategoryRepository billCategoryRepository) {
        return args -> {
            System.out.println("=== Testing Repositories ===");
            long categoryCount = billCategoryRepository.count();
            System.out.println("✅ Bill categories in database: " + categoryCount);
            System.out.println("✅ All repositories are working correctly!");
        };
    }
}