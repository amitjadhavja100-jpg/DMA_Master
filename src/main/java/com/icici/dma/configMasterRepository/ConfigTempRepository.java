package com.icici.dma.configMasterRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icici.dma.slabEntity.ConfigTemp;

public interface ConfigTempRepository
extends JpaRepository<ConfigTemp, Long>{

   // List<ConfigTemp> findByStatus(String status);
	List<ConfigTemp>
	findByStatusAndMakerIdNotOrderByMakerDateDesc(
	        String status,
	        String makerId);
	
    Optional<ConfigTemp> findByTempId(Long tempId);

}