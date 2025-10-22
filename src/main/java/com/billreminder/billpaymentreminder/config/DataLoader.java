package com.billreminder.billpaymentreminder.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.billreminder.billpaymentreminder.entity.BillCategory;
import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final BillCategoryRepository billCategoryRepository;

    public DataLoader(BillCategoryRepository billCategoryRepository) {
        this.billCategoryRepository = billCategoryRepository;
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
    }
}