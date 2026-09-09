package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icici.dma.model.GSTStateMasterError;


public interface GSTStateMasterErrorRepository extends JpaRepository<GSTStateMasterError, Long> {

	// Fetch all error records created by a given user (latest first)
	List<GSTStateMasterError> findByCreatedByOrderByCreatedDateDesc(String createdBy);

	// Fetch error records for a specific upload
	List<GSTStateMasterError> findByUploadId(String uploadId);
}

