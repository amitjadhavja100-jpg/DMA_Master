package com.icici.dma.service;

import java.util.List;

import com.icici.dma.slabEntity.PersonalLoanMST;


public interface PersonalAndEduLoanService {

   List<PersonalLoanMST> saveSlabs(List<PersonalLoanMST> slabList);

   List<PersonalLoanMST> getSlabsByCategory(String category);

   void replaceCategorySlabs(String category, List<PersonalLoanMST> newSlabs);

   List<PersonalLoanMST> getPendingRecords();
   
   boolean updateStatus(Long id, String status);
   
}