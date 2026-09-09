package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ChannelMaster;
import com.icici.dma.model.ModelMaster;

@Repository
public interface ModelMasterRepository extends JpaRepository<ModelMaster, Integer> {

	boolean existsByManufacturerIdAndModelId(Integer manufacturerId,Integer modelId);
	
	Optional<ModelMaster> findByManufacturerIdAndModelId(Integer manufacturerId, Integer modelId);
	
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_MODEL_MST t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ModelMaster> findAllByStatus(@Param("status") String status);
	
	boolean existsBy();
	
}
