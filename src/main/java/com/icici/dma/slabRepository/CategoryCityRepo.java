package com.icici.dma.slabRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.CategoryCityMaster;
@Repository
public interface CategoryCityRepo extends JpaRepository<CategoryCityMaster, Long> {
	
    List<CategoryCityMaster> findByCategory(String category);
    
    //new requirement
    Optional<CategoryCityMaster> findByCategoryAndCity(String category, String city);
    List<CategoryCityMaster> findAllByOrderByCategoryAscCityAsc();
    
    @Query(value=
            "SELECT DISTINCT CATEGORY " +
            "FROM TS_CCR_SLAB_CAT_CITY_MST " +
            "WHERE STATUS='Y' " +
            "ORDER BY CATEGORY",
            nativeQuery=true)
    List<String> findDistinctCategories();
    
    @Query(value=
    		"SELECT CITY " +
    		"FROM TS_CCR_SLAB_CAT_CITY_MST " +
    		"WHERE CATEGORY=:category " +
    		"ORDER BY CITY",
    		nativeQuery=true)
    		List<String> findCities(

    		@Param("category")
    		String category);
    
    List<CategoryCityMaster> findByStatusOrderByCategoryAsc(String status);

    List<CategoryCityMaster> findByStatusOrderByCategoryAscCityAsc(String status);

    List<CategoryCityMaster> findByCategoryAndStatus(String category,String status);
    
    @Query(value=
    		"SELECT DISTINCT CATEGORY " +
    		"FROM TS_CCR_SLAB_CAT_CITY_MST " +
    		"WHERE STATUS='Y' " +
    		"ORDER BY CATEGORY",
    		nativeQuery=true)
    		List<String> findActiveCategories();
    
    @Query(value=
    		"SELECT CITY " +
    		"FROM TS_CCR_SLAB_CAT_CITY_MST " +
    		"WHERE CATEGORY=:category " +
    		"AND STATUS='Y' " +
    		"ORDER BY CITY",
    		nativeQuery=true)
    		List<String> findActiveCities(
    		        @Param("category") String category);
    
    List<CategoryCityMaster> findAllByOrderByCategoryAsc();
    List<CategoryCityMaster> findAllByOrderByDisplayOrderAscIdAsc();
    List<CategoryCityMaster> findByStatusOrderByDisplayOrderAscIdAsc(String status);
    
    Optional<CategoryCityMaster> findByDisplayOrder(Integer displayOrder);

    Optional<CategoryCityMaster> findByCategoryAndDisplayOrder(
            String category,
            Integer displayOrder);
    
	/*
	 * @Query("SELECT c FROM CategoryCityMaster c " + "WHERE c.category=:category "
	 * + "AND c.displayOrder=:displayOrder " + "AND c.city IS NOT NULL")
	 * Optional<CategoryCityMaster> findCityByCategoryAndDisplayOrder(
	 * 
	 * @Param("category") String category,
	 * 
	 * @Param("displayOrder") Integer displayOrder);
	 */
    
    @Query(value =
    	    "SELECT * " +
    	    "FROM TS_CCR_SLAB_CAT_CITY_MST " +
    	    "WHERE DISPLAY_ORDER = :displayOrder " +
    	    "AND CITY <> 'NA'",
    	    nativeQuery = true)
    	Optional<CategoryCityMaster> findCityByDisplayOrder(
    	        @Param("displayOrder") Integer displayOrder);
    
    Optional<CategoryCityMaster>
    findByCategoryAndCityIsNull(
            String category);
    
    Optional<CategoryCityMaster> findFirstByCategory(String category);
}