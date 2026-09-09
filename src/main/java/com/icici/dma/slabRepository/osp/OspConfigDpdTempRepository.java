package com.icici.dma.slabRepository.osp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigDpdTemp;


@Repository
public interface OspConfigDpdTempRepository extends JpaRepository<OspConfigDpdTemp, Long> {

    List<OspConfigDpdTemp> findByStatusOrderByCreatedDateDesc(String status);

    Optional<OspConfigDpdTemp> findByTempId(Long tempId);

	boolean existsByDpdNameIgnoreCaseAndStatus(String dpdName, String status);

	boolean existsByDpdIdAndStatus(Long dpdId, String status);
	
	List<OspConfigDpdTemp> findByStatusAndCreatedByNotOrderByCreatedDateDesc(
	        String status,
	        String createdBy);

}