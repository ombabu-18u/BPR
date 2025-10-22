package com.billreminder.billpaymentreminder.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billreminder.billpaymentreminder.entity.Bill;
import com.billreminder.billpaymentreminder.entity.User;
import com.billreminder.billpaymentreminder.repository.BillRepository;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public Bill createBill(Bill bill) {
        validateBill(bill);
        return billRepository.save(bill);
    }

    public Bill updateBill(Long billId, Bill billDetails) {
        Bill bill = billRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        if (billDetails.getBillName() != null) {
            bill.setBillName(billDetails.getBillName());
        }
        if (billDetails.getAmount() != null) {
            bill.setAmount(billDetails.getAmount());
        }
        if (billDetails.getDueDate() != null) {
            bill.setDueDate(billDetails.getDueDate());
        }
        if (billDetails.getCategory() != null) {
            bill.setCategory(billDetails.getCategory());
        }

        validateBill(bill);
        return billRepository.save(bill);
    }

    public Bill markAsPaid(Long billId) {
        Bill bill = billRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        bill.setPaid(true);
        return billRepository.save(bill);
    }

    public void deleteBill(Long billId) {
        Bill bill = billRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));
        
        billRepository.delete(bill);
    }

    public List<Bill> getUserBills(User user) {
        return billRepository.findByUser(user);
    }

    public List<Bill> getOverdueBills() {
        return billRepository.findByDueDateBeforeAndPaidFalse(LocalDate.now());
    }

    public List<Bill> getUpcomingBills(int days) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days);
        return billRepository.findByDueDateBetweenAndPaidFalse(start, end);
    }

    public BigDecimal getTotalPendingAmount(User user) {
        List<Bill> pendingBills = billRepository.findByUserAndPaid(user, false);
        return pendingBills.stream()
            .map(Bill::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateBill(Bill bill) {
        if (bill.getDueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Due date cannot be in the past");
        }
        if (bill.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
    }

    // Add this method to BillService class
    public Bill updateReminderStatus(Long billId, Boolean reminderSent) {
        Bill bill = billRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));
        
        bill.setReminderSent(reminderSent);
        return billRepository.save(bill);
    }
}