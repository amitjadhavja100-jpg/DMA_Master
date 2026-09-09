package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("SELECT DISTINCT p.productName " + "FROM Product p " + "ORDER BY p.productName")
	List<String> getProducts();


	@Query("SELECT DISTINCT p.subProduct "
	        + "FROM Product p "
	        + "WHERE p.productName = :product "
	        + "AND p.subProduct IS NOT NULL "
	        + "ORDER BY p.subProduct")
	List<String> getSubProducts(@Param("product") String product);
	
	@Query("SELECT DISTINCT p.masterName "
	        + "FROM Product p "
	        + "WHERE p.productName = :product "
	        + "AND p.subProduct = :subProduct "
	        + "AND p.masterName IS NOT NULL "
	        + "ORDER BY p.masterName")
	List<String> getMasters(@Param("product") String product,
	                        @Param("subProduct") String subProduct);
	
	
	@Query("SELECT COUNT(p) FROM Product p " +
		       "WHERE p.productName = :productName " +
		       "AND ((:subProduct IS NULL AND p.subProduct IS NULL) OR p.subProduct = :subProduct) " +
		       "AND ((:masterName IS NULL AND p.masterName IS NULL) OR p.masterName = :masterName)")
	Long countProduct(
	        @Param("productName") String productName,
		    @Param("subProduct") String subProduct,
		    @Param("masterName") String masterName);

}