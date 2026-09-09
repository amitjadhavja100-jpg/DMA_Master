package com.icici.dma.serviceImpl;
 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.repository.PersonalAndEduLoanSlabRepository;
import com.icici.dma.service.PersonalAndEduLoanService;
import com.icici.dma.slabEntity.PersonalLoanMST;
 
@Service
public class PersonalAndEduLoanServiceImpl implements PersonalAndEduLoanService {
 
    @Autowired
    private PersonalAndEduLoanSlabRepository personalAndEduLoanSlabRepository;
 
    @Override
    @Transactional
    public List<PersonalLoanMST> saveSlabs(List<PersonalLoanMST> slabList) {
        // Business logic check: Ensure status is set
        for (PersonalLoanMST slab : slabList) {
            if (slab.getStatus() == null || slab.getStatus().isEmpty()) {
                slab.setStatus("Pending"); // Fixed typo: 'Pendinh' -> 'Pending'
            }
        }
        return personalAndEduLoanSlabRepository.saveAll(slabList);
    }
 
    @Override
    public List<PersonalLoanMST> getPendingRecords() {
        return personalAndEduLoanSlabRepository.findPendingSummary();
    }
 
    @Override
    public void replaceCategorySlabs(String category, List<PersonalLoanMST> newSlabs) {
        // TODO Auto-generated method stub
    }
 
    
    @Override
    public boolean updateStatus(Long id, String status) {
        Optional<PersonalLoanMST> optionalSlab = personalAndEduLoanSlabRepository.findById(id);
        if (optionalSlab.isPresent()) {
            PersonalLoanMST slab = optionalSlab.get();
            slab.setStatus(status); // Ensure your PersonalLoanMST entity has setStatus(String status)
            personalAndEduLoanSlabRepository.save(slab);
            return true;
        }
        return false;
    }
    
    
    @Override
    public List<PersonalLoanMST> getSlabsByCategory(String category) {
        // TODO Auto-generated method stub
        return null;
    }
}
 