package com.billreminder.billpaymentreminder.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billreminder.billpaymentreminder.dto.BillRequest;
import com.billreminder.billpaymentreminder.entity.Bill;
import com.billreminder.billpaymentreminder.entity.BillCategory;
import com.billreminder.billpaymentreminder.entity.User;
import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;
import com.billreminder.billpaymentreminder.service.BillService;
import com.billreminder.billpaymentreminder.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private UserService userService;

    @Autowired
private BillCategoryRepository billCategoryRepository;

    @PostMapping
public ResponseEntity<?> createBill(@Valid @RequestBody BillRequest billRequest, Principal principal) {
    try {
        // Get the logged-in user
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify category exists and get its name
        BillCategory category = billCategoryRepository.findById(billRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + billRequest.getCategoryId()));
        
        // Create new Bill entity
        Bill bill = new Bill();
        bill.setBillName(billRequest.getBillName());
        bill.setAmount(billRequest.getAmount());
        bill.setDueDate(billRequest.getDueDate());
        bill.setPaid(billRequest.getPaid());
        bill.setUser(user);
        bill.setCategoryId(billRequest.getCategoryId()); // Set category ID directly
        bill.setCategoryName(category.getName()); // Set category name for response
        
        Bill createdBill = billService.createBill(bill);
        
        return ResponseEntity.ok(createdBill);
    } catch (RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}

    @GetMapping
public ResponseEntity<?> getUserBills(Principal principal) {
    try {
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Bill> bills = billService.getUserBills(user);
        
        // Add category names to each bill
        for (Bill bill : bills) {
            BillCategory category = billCategoryRepository.findById(bill.getCategoryId())
                .orElse(null);
            if (category != null) {
                bill.setCategoryName(category.getName());
            }
        }
        
        return ResponseEntity.ok(bills);
    } catch (RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueBills() {
        List<Bill> overdueBills = billService.getOverdueBills();
        return ResponseEntity.ok(overdueBills);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingBills(@RequestParam(defaultValue = "3") int days) {
        List<Bill> upcomingBills = billService.getUpcomingBills(days);
        return ResponseEntity.ok(upcomingBills);
    }

    @PutMapping("/{id}/pay")
public ResponseEntity<?> markBillAsPaid(@PathVariable Long id) {
    try {
        Bill paidBill = billService.markAsPaid(id);
        
        // Create a simple response object to avoid serialization issues
        Map<String, Object> response = new HashMap<>();
        response.put("id", paidBill.getId());
        response.put("billName", paidBill.getBillName());
        response.put("amount", paidBill.getAmount());
        response.put("dueDate", paidBill.getDueDate());
        response.put("paid", paidBill.getPaid());
        response.put("message", "Bill marked as paid successfully");
        
        return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {
        try {
            billService.deleteBill(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Bill deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/summary")
public ResponseEntity<?> getBillSummary(Principal principal) {
    try {
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        BigDecimal totalPending = billService.getTotalPendingAmount(user);
        List<Bill> pendingBills = billService.getUserBills(user).stream()
            .filter(bill -> !bill.getPaid())
            .toList();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPendingAmount", totalPending);
        summary.put("pendingBillsCount", pendingBills.size());
        summary.put("user", user.getName());
        
        return ResponseEntity.ok(summary);
    } catch (RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
}