
package com.icici.dma.slabService;

import java.util.List;
import java.util.Map;

import com.icici.dma.slabEntity.PayoutMaster;
import com.icici.dma.slabEntity.PayoutRequest;

public interface PayoutService {

	
	    PayoutMaster fetch(  String category, String city,String dpd,String fromDate,String toDate);

	    PayoutMaster save(PayoutRequest req, String user);

	    void approve(Long id, String user);

	    void reject(Long id, String user, String remark);

	    List<PayoutMaster> history(String c, String city, String dpd);
	    
	    List<PayoutMaster> pending(String user);
	    
	    List<Map<String,Object>> compare(Long id);
	    
	    Map<String,Object> makerCompare(Long id);

	    Map<String,Object> checkerCompare(Long id);
	    
	    Long latestApprovedId(String category,String city,String dpd);
	    
	    Map<String,Object> existingStructure(
	    		String category,
	    		String city,
	    		String dpd);

	    List<String> getDpds(String category);  
	    
	}
    
