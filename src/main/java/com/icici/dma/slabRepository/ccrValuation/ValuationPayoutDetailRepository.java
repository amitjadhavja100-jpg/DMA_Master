package com.icici.dma.slabRepository.ccrValuation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.ccrValuation.ValuationPayoutDetail;

@Repository
public interface ValuationPayoutDetailRepository extends JpaRepository<ValuationPayoutDetail, Long> {

	List<ValuationPayoutDetail> findByMasterId(Long masterId);
}