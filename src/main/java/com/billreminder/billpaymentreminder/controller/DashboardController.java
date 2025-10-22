package com.billreminder.billpaymentreminder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.billreminder.billpaymentreminder.entity.User;
import com.billreminder.billpaymentreminder.service.BillService;
import com.billreminder.billpaymentreminder.service.UserService;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Dashboard");
            
            // Get bill statistics
            var pendingBills = billService.getUserBills(user).stream()
                .filter(bill -> !bill.getPaid())
                .toList();
            
            var totalPending = billService.getTotalPendingAmount(user);
            var upcomingBills = billService.getUpcomingBills(7);
            
            model.addAttribute("pendingBillsCount", pendingBills.size());
            model.addAttribute("totalPendingAmount", totalPending);
            model.addAttribute("upcomingBills", upcomingBills);
        }
        
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Register");
        return "register";
    }

    @PostMapping("/register")
public String registerUser(@ModelAttribute User user, Model model) {
    try {
        User registeredUser = userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please login.");
        return "login";
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Register");
        return "register";
    }
}
}