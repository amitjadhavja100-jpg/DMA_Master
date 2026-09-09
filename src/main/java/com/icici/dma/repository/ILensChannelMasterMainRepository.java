package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ILensChannelMaster;
@Repository
public interface ILensChannelMasterMainRepository extends JpaRepository<ILensChannelMaster, String> {
	
	@Query(value = "SELECT m.* " +
	        "FROM TM_VHL_ILENS_CHANNEL_MST m " +
	        "WHERE m.STATUS_A = :statusA " +
	        "ORDER BY GREATEST( " +
	        "NVL(m.MODIFIED_DATE, DATE '1900-01-01'), " +
	        "NVL(m.CREATED_DATE, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ILensChannelMaster> findAllByStatusA(@Param("statusA") String statusA);
	
	boolean existsByUserID(String userID);
	
	Optional<ILensChannelMaster> findByUserID(String userID);
	
	boolean existsBy();

}
