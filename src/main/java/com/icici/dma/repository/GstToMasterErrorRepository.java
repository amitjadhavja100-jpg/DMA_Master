package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.GstToMasterError;

@Repository
public interface GstToMasterErrorRepository extends JpaRepository<GstToMasterError, Long> {

	List<GstToMasterError> findByCreatedByOrderByCreatedDateDesc(String createdBy);

	List<GstToMasterError> findByUploadIdOrderByRowNumberAsc(String uploadId);

}
