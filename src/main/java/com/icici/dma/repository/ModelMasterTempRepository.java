package com.icici.dma.repository;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ModelMasterTemp;
@Repository
public interface ModelMasterTempRepository extends JpaRepository<ModelMasterTemp, Integer> {

	boolean existsByManufacturerIdAndStatus(Integer manufacturerId,String Status);
	
	ModelMasterTemp findByManufacturerIdAndModelId(Integer manufacturerId,Integer modelId);
	
	Optional<ModelMasterTemp> findByTempIdAndStatus(Integer tempId,String Status);
	
	
	@Query(value = "SELECT t.* " +
	        "FROM tm_vhl_model_mst_temp t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.action_date, DATE '1900-01-01'), " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ModelMasterTemp> findAllByStatus(@Param("status") String status);
	
	
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_MODEL_MST_TEMP t " +
	        "WHERE t.STATUS = :status " +
	    	"AND t.ACTION_TYPE IN ('I', 'U') " +
	        "AND t.ACTION_USER IS NOT NULL " +
	    	"AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.ACTION_DATE, DATE '1900-01-01'), " +
	        "NVL(t.MODIFIED_DATE, DATE '1900-01-01'), " +
	        "NVL(t.CREATED_DATE, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ModelMasterTemp> findAllByStatusAndCreatedByNot(@Param("status") String status, @Param("actionUser") String actionUser);
	
	@Query("SELECT m FROM ModelMaster m " +
		       "WHERE m.manufacturerId IN :manufacturerIds " +
		       "AND m.modelId IN :modelIds")
		List<ModelMasterTemp> findPossibleMatches(
		        @Param("manufacturerIds") List<Integer> manufacturerIds,
		        @Param("modelIds") List<Integer> modelIds);
	
	boolean existsByStatus(String status);
}
