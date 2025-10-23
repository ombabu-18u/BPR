package com.billreminder.billpaymentreminder.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billreminder.billpaymentreminder.entity.Bill;
import com.billreminder.billpaymentreminder.entity.User;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    // Find all bills by user
    List<Bill> findByUser(User user);
    
    // Find bills by user and payment status
    List<Bill> findByUserAndPaid(User user, Boolean paid);
    
    // Find overdue bills (due date passed and not paid)
    List<Bill> findByDueDateBeforeAndPaidFalse(LocalDate date);

    List<Bill> findByUserAndDueDateBetweenAndPaidFalse(User user, LocalDate startDate, LocalDate endDate);
    
    // Find bills due soon (within next 3 days and not paid)
    List<Bill> findByDueDateBetweenAndPaidFalse(LocalDate startDate, LocalDate endDate);
    
    // Find bills by category ID and user
    List<Bill> findByUserAndCategoryId(User user, Long categoryId);
    
    // Custom query for bills summary by user
    @Query("SELECT COUNT(b), SUM(b.amount) FROM Bill b WHERE b.user = :user AND b.paid = false")
    Object[] findBillSummaryByUser(User user);
}