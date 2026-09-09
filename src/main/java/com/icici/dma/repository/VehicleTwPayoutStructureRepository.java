package com.icici.dma.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.VehicleTwPayoutStructure;



@Repository
public interface VehicleTwPayoutStructureRepository extends JpaRepository<VehicleTwPayoutStructure, Long> {

	@Query("select v from VehicleTwPayoutStructure v where v.productType =:productType and v.structureType =:structureType and "
			+ "v.fromDate =:fromDate and v.toDate =:toDate and v.status =:status")
	List<VehicleTwPayoutStructure> findDataForCheckertw(@Param("productType") String productType,
			@Param("structureType") String structureType, @Param("fromDate") LocalDate cycleFrom,
			@Param("toDate") LocalDate cycleTo, @Param("status") String status);

	List<VehicleTwPayoutStructure> findByIdIn(List<Long> ids);

	List<VehicleTwPayoutStructure> findByStatus(String status);

	

}
