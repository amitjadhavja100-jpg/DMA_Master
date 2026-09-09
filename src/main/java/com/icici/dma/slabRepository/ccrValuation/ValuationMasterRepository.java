package com.icici.dma.slabRepository.ccrValuation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.ccrValuation.ValuationMaster;

@Repository
public interface ValuationMasterRepository extends JpaRepository<ValuationMaster, Long> {

	List<ValuationMaster> findByStatusOrderById(String status);

	Optional<ValuationMaster> findByIdAndStatus(
			Long id, 
			String status);

	boolean existsByIboxIdAndValuerNameAndProductSubType(
			String iboxId, 
			String valuerName, 
			String productSubType);

	boolean existsByIboxIdAndValuerNameAndProductSubTypeAndIdNot(
			String iboxId, 
			String valuerName,
			String productSubType, 
			Long id);
}
