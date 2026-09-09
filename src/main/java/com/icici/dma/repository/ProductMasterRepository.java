package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Integer> {
	
	@Query("SELECT p.productName FROM ProductMaster p")
	Optional<List<String>> FindAllProductNames();

}
