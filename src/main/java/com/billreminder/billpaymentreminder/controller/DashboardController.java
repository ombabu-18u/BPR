package com.billreminder.billpaymentreminder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.billreminder.billpaymentreminder.entity.Bill;
import com.billreminder.billpaymentreminder.entity.BillCategory;
import com.billreminder.billpaymentreminder.entity.User;
import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;
import com.billreminder.billpaymentreminder.service.BillService;
import com.billreminder.billpaymentreminder.service.UserService;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
private BillCategoryRepository billCategoryRepository;

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
            // In DashboardController - replace the upcomingBills line:
var upcomingBills = billService.getUpcomingBillsForUser(user, 7);
            
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

@GetMapping("/bills")
public String bills(Model model, Principal principal) {
    if (principal != null) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        var userBills = billService.getUserBills(user);
        
        // Add category names to each bill
        for (Bill bill : userBills) {
            BillCategory category = billCategoryRepository.findById(bill.getCategoryId())
                .orElse(null);
            if (category != null) {
                bill.setCategoryName(category.getName());
            }
        }
        
        model.addAttribute("bills", userBills);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "My Bills");
    }
    return "bills";
}

@GetMapping("/profile")
public String profile(Model model, Principal principal) {
    if (principal != null) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Profile");
    }
    return "profile";
}

@PostMapping("/profile/update")
public String updateProfile(@RequestParam String name, 
                          @RequestParam(required = false) String contactNo,
                          Principal principal, Model model) {
    if (principal != null) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create user object with updated fields
        User userDetails = new User();
        userDetails.setName(name);
        userDetails.setContactNo(contactNo);
        
        User updatedUser = userService.updateUserProfile(user.getId(), userDetails);
        model.addAttribute("user", updatedUser);
        model.addAttribute("pageTitle", "Profile");
        model.addAttribute("success", "Profile updated successfully!");
    }
    return "profile";
}

@GetMapping("/bills/new")
public String addBill(Model model, Principal principal) {
    if (principal != null) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Get categories for the dropdown
        var categories = billCategoryRepository.findAll();
        
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Add Bill");
    }
    return "add-bill";
}
}