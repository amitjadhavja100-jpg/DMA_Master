package com.icici.dma.configMasterRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icici.dma.slabEntity.ConfigHistory;

public interface ConfigHistoryRepository
extends JpaRepository<ConfigHistory, Long>{
	
	List<ConfigHistory>
	findByConfigIdOrderByHistoryIdDesc(
	Long configId);
	
	ConfigHistory
	findTopByConfigIdOrderByHistoryIdDesc(
	Long configId);

}