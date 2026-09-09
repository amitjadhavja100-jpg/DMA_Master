package com.icici.dma.slabRepository.osp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;


@Repository
public interface OspConfigDpdMasterRepository extends JpaRepository<OspConfigDpdMaster, Long> {

    Optional<OspConfigDpdMaster> findByDpdNameIgnoreCase(String dpdName);

    boolean existsByDpdNameIgnoreCase(String dpdName);

    List<OspConfigDpdMaster> findByStatusOrderByCreatedDateDesc(String status);

    Optional<OspConfigDpdMaster> findByIdAndStatus(Long id, String status);

    boolean existsByDpdNameIgnoreCaseAndIdNot(String dpdName, Long id);

}