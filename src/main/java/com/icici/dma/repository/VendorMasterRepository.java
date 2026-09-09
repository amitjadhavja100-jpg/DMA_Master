package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.icici.dma.model.VendorMaster;


@Repository
public interface VendorMasterRepository
       extends JpaRepository<VendorMaster, String> {

   boolean existsByVendorNo(String vendorNo);

   List<VendorMaster> findAllByStatus(String status);
}