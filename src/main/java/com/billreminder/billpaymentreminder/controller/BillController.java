package com.billreminder.billpaymentreminder.controller;

import java.math.BigDecimal;
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

import com.billreminder.billpaymentreminder.entity.Bill;
import com.billreminder.billpaymentreminder.entity.User;
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

    @PostMapping
    public ResponseEntity<?> createBill(@Valid @RequestBody Bill bill) {
        try {
            // For now, we'll use the test user. In Phase 6, we'll add proper authentication
            User testUser = userService.findByEmail("test@example.com")
                .orElseThrow(() -> new RuntimeException("Test user not found"));
            
            bill.setUser(testUser);
            Bill createdBill = billService.createBill(bill);
            
            return ResponseEntity.ok(createdBill);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserBills() {
        try {
            User testUser = userService.findByEmail("test@example.com")
                .orElseThrow(() -> new RuntimeException("Test user not found"));
            
            List<Bill> bills = billService.getUserBills(testUser);
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
            return ResponseEntity.ok(paidBill);
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
    public ResponseEntity<?> getBillSummary() {
        try {
            User testUser = userService.findByEmail("test@example.com")
                .orElseThrow(() -> new RuntimeException("Test user not found"));
            
            BigDecimal totalPending = billService.getTotalPendingAmount(testUser);
            List<Bill> pendingBills = billService.getUserBills(testUser).stream()
                .filter(bill -> !bill.getPaid())
                .toList();
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalPendingAmount", totalPending);
            summary.put("pendingBillsCount", pendingBills.size());
            summary.put("user", testUser.getName());
            
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}