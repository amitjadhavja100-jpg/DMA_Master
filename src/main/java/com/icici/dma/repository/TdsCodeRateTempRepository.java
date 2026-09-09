
package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.TdsCodeRateTemp;
 
@Repository
public interface TdsCodeRateTempRepository
        extends JpaRepository<TdsCodeRateTemp, Long> {
 
    boolean existsByIdAndStatus(
            long id,
            String status);
 
    Optional<TdsCodeRateTemp>
    findByIdAndStatus(
            Long id,
            String status);
 
    List<TdsCodeRateTemp>
    findByCreatedByAndStatus(
            String createdBy,
            String status);
 
    List<TdsCodeRateTemp>
    findAllByStatusAndCreatedByNot(
            String status,
            String createdBy);
 
    List<TdsCodeRateTemp>
    findAllByStatus(
            String status);
}
