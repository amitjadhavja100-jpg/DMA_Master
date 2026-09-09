package com.icici.dma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.OutsourceMasterError;

@Repository
public interface OutsourceMasterErrorRepository extends JpaRepository<OutsourceMasterError, Integer> {

}
