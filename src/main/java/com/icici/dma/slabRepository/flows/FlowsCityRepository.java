package com.icici.dma.slabRepository.flows;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsCityMaster;

@Repository
public interface FlowsCityRepository extends JpaRepository<FlowsCityMaster, Long> {

	List<FlowsCityMaster> findByCategoryIdAndStatusOrderByCityName(Long categoryId, String status);

	Optional<FlowsCityMaster> findByCategoryIdAndCityName(Long categoryId, String cityName);
}