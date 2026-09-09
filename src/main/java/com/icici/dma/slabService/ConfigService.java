package com.icici.dma.slabService;

import java.util.List;
import java.util.Map;

import com.icici.dma.slabEntity.ConfigDTO;
import com.icici.dma.slabEntity.ConfigMaster;
import com.icici.dma.slabEntity.ConfigTemp;

public interface ConfigService {

   // List<ConfigMaster> getAll();

	/* List<ConfigTemp> getPending(); */
	 List<ConfigTemp> getPending(String user);

    void save(ConfigDTO dto,String user);

    void update(ConfigDTO dto,String user);

    void activate(String type, Long id, String user);

    void deactivate(String type, Long id, String user);

    void approve(Long tempId,String user);

    void reject(Long tempId,String user);

    ConfigTemp compare(Long tempId);
    
    Map<String,String> getLabels();
    
    Map<String,String> getUiConfig();

	//List<ConfigMaster> loadConfigs(String status, String type);
	
    List<?> loadConfigs(
            String status,
            String type,
            String category,
            String city,
            String dpd,
            String collection,
            String ceRange);
	
	//List<?> loadRecords(String type);
    
  //  Map<String, String> loadLabels();

}