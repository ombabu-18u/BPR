package com.billreminder.billpaymentreminder.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.billreminder.billpaymentreminder.entity.BillCategory;
import com.billreminder.billpaymentreminder.entity.User;
import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;
import com.billreminder.billpaymentreminder.service.UserService;

@Component
public class DataLoader implements CommandLineRunner {

    private final BillCategoryRepository billCategoryRepository;
    private final UserService userService;

    public DataLoader(BillCategoryRepository billCategoryRepository, UserService userService) {
        this.billCategoryRepository = billCategoryRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create default categories if they don't exist
        if (billCategoryRepository.count() == 0) {
            List<BillCategory> categories = Arrays.asList(
                new BillCategory("Utilities", "Electricity, water, gas bills"),
                new BillCategory("Rent/Mortgage", "House rent or mortgage payments"),
                new BillCategory("Insurance", "Health, car, home insurance"),
                new BillCategory("Subscription", "Streaming, software subscriptions"),
                new BillCategory("Loan", "Personal loans, credit cards"),
                new BillCategory("Other", "Other miscellaneous bills")
            );
            
            billCategoryRepository.saveAll(categories);
            System.out.println("Default bill categories loaded successfully!");
        }

        // Create default test user if doesn't exist
        if (userService.findByEmail("test@example.com").isEmpty()) {
            User testUser = new User();
            testUser.setName("Test User");
            testUser.setEmail("test@example.com");
            testUser.setPassword("password123"); // This will be encoded by UserService
            testUser.setContactNo("1234567890");
            
            userService.registerUser(testUser);
            System.out.println("Default test user created: test@example.com / password123");
        }
    }
}