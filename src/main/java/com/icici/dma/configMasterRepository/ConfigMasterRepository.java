package com.icici.dma.configMasterRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.icici.dma.slabEntity.ConfigMaster;

public interface ConfigMasterRepository
extends JpaRepository<ConfigMaster, Long>{

    List<ConfigMaster>
    findByActiveFlagOrderByDisplayOrder(
            String activeFlag);

    List<ConfigMaster>
    findByConfigTypeAndActiveFlag(
            String configType,
            String activeFlag);

    Optional<ConfigMaster>
    findByConfigId(Long configId);
    
    Optional<ConfigMaster> findByConfigKey(String configKey);
    
    @Query(
    		"SELECT MAX(c.displayOrder) " +
    		"FROM ConfigMaster c")
    		Integer findMaxDisplayOrder();
    
    //LABEL, BUTTON, TITLE independent load
    List<ConfigMaster> findByConfigTypeAndActiveFlagOrderByDisplayOrder(
            String configType,
            String activeFlag);
    
    Optional<ConfigMaster>
    findByConfigTypeAndConfigKeyAndActiveFlag(
    String configType,
    String configKey,
    String activeFlag);

    List<ConfigMaster>
    findByParentKeyAndActiveFlagOrderByDisplayOrder(
    String parentKey,
    String activeFlag);

    List<ConfigMaster>
    findByScreenNameAndActiveFlagOrderByDisplayOrder(
    String screenName,
    String activeFlag);
    
    List<ConfigMaster> findByConfigTypeInAndActiveFlagOrderByDisplayOrder(
            List<String> configTypes,
            String activeFlag);
    
	
	  Optional<ConfigMaster> findByConfigTypeAndConfigKey( String configType,
	 String configKey);
    

	List<ConfigMaster> findAllByOrderByDisplayOrderAsc();
	
	Optional<ConfigMaster>
	findTopByConfigTypeOrderByConfigIdDesc(
	        String configType);
	
	List<ConfigMaster> findByConfigTypeOrderByConfigIdDesc(String configType);
	
	Optional<ConfigMaster> findByConfigTypeAndCategory(
	        String configType,
	        String category);
	
	Optional<ConfigMaster>
	findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
	        String configType,
	        String configKey);
	
	Optional<ConfigMaster> findTopByConfigKeyOrderByConfigIdDesc(String configKey);
	
	Optional<ConfigMaster>
	findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
	        String configType,
	        String configValue);
	
	 List<ConfigMaster> findByConfigTypeOrderByDisplayOrderAsc(String configType);
	 
	 List<ConfigMaster> findByConfigTypeAndActiveFlagOrderByDisplayOrderAsc(
	         String configType,
	         String activeFlag);
	 
	 Optional<ConfigMaster>
	 findTopByConfigTypeAndCategoryAndConfigValueOrderByConfigIdDesc(
	         String configType,
	         String category,
	         String configValue);

	 Optional<ConfigMaster>
	 findTopByConfigTypeAndCategoryAndDpdAndConfigValueOrderByConfigIdDesc(
	         String configType,
	         String category,
	         String dpd,
	         String configValue);
    
}