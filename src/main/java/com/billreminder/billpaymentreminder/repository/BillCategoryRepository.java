package com.billreminder.billpaymentreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billreminder.billpaymentreminder.entity.BillCategory;

@Repository
public interface BillCategoryRepository extends JpaRepository<BillCategory, Long> {
    BillCategory findByName(String name);
    boolean existsByName(String name);
}