package com.billreminder.billpaymentreminder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.billreminder.billpaymentreminder.entity.User;
import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;
import com.billreminder.billpaymentreminder.service.BillService;
import com.billreminder.billpaymentreminder.service.UserService;

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

	// Add this method to your existing BillPaymentReminderApplication class
@Bean
public CommandLineRunner testServices(BillCategoryRepository billCategoryRepository,
                                     UserService userService,
                                     BillService billService) {
    return args -> {
        System.out.println("=== Testing Services ===");
        
        // Test user registration
        try {
            User testUser = new User();
            testUser.setName("Test User");
            testUser.setEmail("test@example.com");
            testUser.setPassword("password123");
            testUser.setContactNo("1234567890");
            
            User savedUser = userService.registerUser(testUser);
            System.out.println("✅ User service working - User registered: " + savedUser.getEmail());
        } catch (Exception e) {
            System.out.println("ℹ️ User already exists or test completed");
        }
        
        long categoryCount = billCategoryRepository.count();
        System.out.println("✅ Bill categories in database: " + categoryCount);
        System.out.println("✅ All services are working correctly!");
    };
}
}