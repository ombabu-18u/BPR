package com.billreminder.billpaymentreminder.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billreminder.billpaymentreminder.service.BillReminderService;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Autowired
    private BillReminderService billReminderService;

    @PostMapping("/test")
    public ResponseEntity<?> testReminder() {
        billReminderService.sendTestReminder();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test reminder triggered - check application logs");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-now")
    public ResponseEntity<?> checkNow() {
        billReminderService.checkDueBills();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Manual bill check triggered - check application logs");
        return ResponseEntity.ok(response);
    }
}