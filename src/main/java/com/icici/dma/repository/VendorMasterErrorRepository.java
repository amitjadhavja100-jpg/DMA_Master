package com.icici.dma.repository;

import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.icici.dma.model.VendorMasterError;
 
@Repository
public interface VendorMasterErrorRepository
        extends JpaRepository<VendorMasterError, Long> {
 
    List<VendorMasterError>
    findByUploadIdOrderByRowNumber(
            String uploadId);
}
 