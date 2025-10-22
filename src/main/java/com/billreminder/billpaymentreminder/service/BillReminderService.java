package com.billreminder.billpaymentreminder.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.billreminder.billpaymentreminder.entity.Bill;
import com.billreminder.billpaymentreminder.entity.BillCategory;
import com.billreminder.billpaymentreminder.entity.User;

@Service
public class BillReminderService {

    private static final Logger logger = LoggerFactory.getLogger(BillReminderService.class);

    @Autowired
    private BillService billService;

    // Run every day at 8:00 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkDueBills() {
        logger.info("🔔 Starting daily bill reminder check...");
        
        // Get bills due in the next 3 days
        List<Bill> upcomingBills = billService.getUpcomingBills(3);
        
        if (upcomingBills.isEmpty()) {
            logger.info("✅ No upcoming bills found for the next 3 days");
        } else {
            logger.info("📅 Found {} upcoming bills:", upcomingBills.size());
            for (Bill bill : upcomingBills) {
                if (!bill.getReminderSent()) {
                    sendReminder(bill);
                    // Mark reminder as sent (in real app, we'd update this in database)
                    logger.info("   💡 Reminder for: {} - ${} due on {}", 
                        bill.getBillName(), bill.getAmount(), bill.getDueDate());
                }
            }
        }
        
        // Check overdue bills
        checkOverdueBills();
    }

    // Run every hour to check for overdue bills
    @Scheduled(cron = "0 0 * * * ?")
    public void checkOverdueBills() {
        List<Bill> overdueBills = billService.getOverdueBills();
        
        if (!overdueBills.isEmpty()) {
            logger.warn("🚨 Found {} OVERDUE bills:", overdueBills.size());
            for (Bill bill : overdueBills) {
                logger.warn("   ❗ OVERDUE: {} - ${} was due on {}", 
                    bill.getBillName(), bill.getAmount(), bill.getDueDate());
                sendOverdueAlert(bill);
            }
        }
    }

    private void sendReminder(Bill bill) {
        // In a real application, this would send email/SMS/push notifications
        // For now, we'll just log it and you can implement actual notifications later
        
        String message = String.format(
            "Reminder: Bill '%s' for $%.2f is due on %s. Category: %s",
            bill.getBillName(),
            bill.getAmount().doubleValue(),
            bill.getDueDate(),
            bill.getCategory().getName()
        );
        
        logger.info("📧 [SIMULATED EMAIL] To: {} - {}", 
            bill.getUser().getEmail(), message);
        
        // Here you would integrate with:
        // - Email service (JavaMailSender, SendGrid, etc.)
        // - SMS service (Twilio, etc.)
        // - Push notification service
    }

    private void sendOverdueAlert(Bill bill) {
        String message = String.format(
            "URGENT: Bill '%s' for $%.2f was due on %s and is now OVERDUE!",
            bill.getBillName(),
            bill.getAmount().doubleValue(),
            bill.getDueDate()
        );
        
        logger.error("🔥 [SIMULATED ALERT] To: {} - {}", 
            bill.getUser().getEmail(), message);
        
        // In production, this could trigger:
        // - Urgent email/SMS alerts
        // - Push notifications with high priority
        // - Dashboard alerts
    }

    // Additional method for manual reminder testing
    // Additional method for manual reminder testing
public void sendTestReminder() {
    logger.info("🧪 Sending test reminder...");
    
    try {
        // Get a real category from the database for testing
        BillCategory utilitiesCategory = new BillCategory();
        utilitiesCategory.setId(1L);
        utilitiesCategory.setName("Utilities");
        
        // Get the test user
        User testUser = new User();
        testUser.setId(1L); // Assuming test user has ID 1
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        
        // Create a more realistic test bill
        Bill testBill = new Bill();
        testBill.setBillName("Test Electricity Bill");
        testBill.setAmount(java.math.BigDecimal.valueOf(99.99));
        testBill.setDueDate(LocalDate.now().plusDays(2));
        testBill.setCategory(utilitiesCategory);
        testBill.setUser(testUser);
        
        sendReminder(testBill);
        logger.info("✅ Test reminder sent successfully!");
    } catch (Exception e) {
        logger.error("❌ Error sending test reminder: {}", e.getMessage());
        throw new RuntimeException("Test reminder failed: " + e.getMessage());
    }
}
}