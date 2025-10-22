package com.billreminder.billpaymentreminder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billreminder.billpaymentreminder.entity.BillCategory;
import com.billreminder.billpaymentreminder.repository.BillCategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class BillCategoryController {

    @Autowired
    private BillCategoryRepository billCategoryRepository;

    @GetMapping
    public ResponseEntity<List<BillCategory>> getAllCategories() {
        List<BillCategory> categories = billCategoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }
}