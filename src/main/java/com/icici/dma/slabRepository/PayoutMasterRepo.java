package com.icici.dma.slabRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.PayoutMaster;
@Repository
public interface PayoutMasterRepo extends JpaRepository<PayoutMaster, Long> {

    Optional<PayoutMaster> findTopByCategoryAndCityAndDpdAndStatusOrderByVersionDesc(
            String category, String city, String dpd, String status);

    List<PayoutMaster> findByCategoryAndCityAndDpdOrderByVersionDesc(
            String category, String city, String dpd);
    
    Optional<PayoutMaster>
    findByCategoryAndCityAndDpdAndFromDateAndToDateAndStatus(String category,String city, String dpd,
    		LocalDate fromDate,LocalDate toDate,String status);
    
    Optional<PayoutMaster>
    findTopByCategoryAndCityAndDpdAndFromDateAndToDateOrderByCreatedDateDesc(
            String category,
            String city,
            String dpd,
            LocalDate fromDate,
            LocalDate toDate);
    
    List<PayoutMaster>
    findByStatusOrderByCreatedDateDesc(
    String status);
    
    List<PayoutMaster>
    findByStatusAndCreatedByNot(
            String status,
            String createdBy
    );
    
    Optional<PayoutMaster>
    findTopByCategoryAndCityAndDpdAndFromDateAndToDateAndStatusOrderByVersionDesc(
            String category,
            String city,
            String dpd,
            LocalDate fromDate,
            LocalDate toDate,
            String status);
    
    List<PayoutMaster> findByCategory(String category);
    List<PayoutMaster> findByCity(String city);
    List<PayoutMaster> findByDpd(String dpd);
    
}


