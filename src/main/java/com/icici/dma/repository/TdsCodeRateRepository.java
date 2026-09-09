package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.TdsCodeRate;



@Repository
public interface TdsCodeRateRepository
       extends JpaRepository<TdsCodeRate, Long> {

   boolean existsById(long id);

   List<TdsCodeRate> findAllByStatus(String status);
}