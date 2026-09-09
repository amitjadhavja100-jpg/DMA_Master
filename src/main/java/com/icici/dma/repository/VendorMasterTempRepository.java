package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.icici.dma.model.VendorMasterTemp;
 
@Repository
public interface VendorMasterTempRepository
        extends JpaRepository<VendorMasterTemp, String> {
 
    boolean existsByVendorNoAndStatus(
            String vendorNo,
            String status);
 
    Optional<VendorMasterTemp>
    findByVendorNoAndStatus(
            String vendorNo,
            String status);
 
    List<VendorMasterTemp>
    findByCreatedByAndStatus(
            String createdBy,
            String status);
 
    List<VendorMasterTemp>
    findAllByStatusAndCreatedByNot(
            String status,
            String createdBy);
 
    List<VendorMasterTemp>
    findAllByStatus(
            String status);
}