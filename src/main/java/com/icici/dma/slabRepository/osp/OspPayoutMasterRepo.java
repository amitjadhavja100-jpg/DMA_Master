package com.icici.dma.slabRepository.osp;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;
import com.icici.dma.slabEntity.osp.OspPayoutMaster;

@Repository
public interface OspPayoutMasterRepo
        extends JpaRepository<OspPayoutMaster,Long>{

	List<OspPayoutMaster> findByStatusOrderByCreatedDateDesc(String status);

	Optional<OspPayoutMaster> findTopByStatusOrderByVersionDesc(String status);
    
	List<OspPayoutMaster> findByStatusAndCreatedByNotOrderByCreatedDateDesc(String status, String createdBy);
 
	Optional<OspPayoutMaster> findTopByProductAndSubProductAndDpdAndStatusOrderByVersionDesc(
			String product, String subProduct, OspConfigDpdMaster dpd, String status);

	
	Optional<OspPayoutMaster> findTopByProductAndSubProductAndDpdAndFromDateAndToDateAndStatusOrderByVersionDesc(
			String product, String subProduct, OspConfigDpdMaster dpd, LocalDate fromDate, LocalDate toDate, String status);
	
	@EntityGraph(attributePaths = {"dpd", "details", "details.dpd"})
	Optional<OspPayoutMaster> findById(Long id);
	
	Optional<OspPayoutMaster> findTopByProductAndSubProductAndDpdAndFromDateAndToDateOrderByCreatedDateDesc(
			String product, String subProduct, OspConfigDpdMaster dpd, LocalDate fromDate, LocalDate toDate);
	
	Optional<OspPayoutMaster> findByProductAndSubProductAndDpdAndFromDateAndToDateAndStatus(
			String product,
			String subProduct,
			OspConfigDpdMaster dpd,
			LocalDate fromDate,
			LocalDate toDate,
			String status);
    
}
