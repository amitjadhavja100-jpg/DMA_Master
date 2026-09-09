package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.AutoManipalRetainerMaster;
import com.icici.dma.slabEntity.PersonalLoanMST;

@Repository
public interface PersonalAndEduLoanSlabRepository extends JpaRepository<PersonalLoanMST, Long> {
	 // Fetch slabs by loan category (e.g., 'EL Generic', 'PL & Doctor Loans')
    List<PersonalLoanMST> findByCategory(String category);
 
    @Query("SELECT p FROM PersonalLoanMST p WHERE p.status = 'Pending'")
    List<PersonalLoanMST> findPendingSummary();
    // Fetch slabs by category and approval status
    List<PersonalLoanMST> findByCategoryAndStatus(String category, String status);
 
    // Delete existing slabs for a specific category before inserting new ones
    void deleteByCategory(String category);
}