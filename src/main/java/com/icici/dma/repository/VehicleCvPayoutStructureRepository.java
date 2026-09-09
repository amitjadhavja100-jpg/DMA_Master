package com.icici.dma.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.icici.dma.slabEntity.VehicleCvPayoutStructure;



public interface VehicleCvPayoutStructureRepository extends JpaRepository<VehicleCvPayoutStructure, Long> {

	@Query("select v from VehicleCvPayoutStructure v where v.productType =:productType and v.structureType =:structureType and "
			+ "v.fromDate =:fromDate and v.toDate =:toDate and v.status =:status")
	List<VehicleCvPayoutStructure> findDataForCheckercv(@Param("productType") String productType,
			@Param("structureType") String structureType, @Param("fromDate") LocalDate cycleFrom,
			@Param("toDate") LocalDate cycleTo, @Param("status") String status);

	List<VehicleCvPayoutStructure> findByIdIn(List<Long> ids);

	List<VehicleCvPayoutStructure> findByStatus(String status);
	

}
