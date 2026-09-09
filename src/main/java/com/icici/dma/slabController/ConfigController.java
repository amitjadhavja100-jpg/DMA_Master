package com.icici.dma.slabController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.configMasterRepository.ConfigHistoryRepository;
import com.icici.dma.configMasterRepository.ConfigMasterRepository;
import com.icici.dma.configMasterRepository.ConfigTempRepository;
import com.icici.dma.slabEntity.CategoryCityMaster;
import com.icici.dma.slabEntity.CeRangeMaster;
import com.icici.dma.slabEntity.CollectionSlabMaster;
import com.icici.dma.slabEntity.ConfigDTO;
import com.icici.dma.slabEntity.ConfigHistory;
import com.icici.dma.slabEntity.ConfigMaster;
import com.icici.dma.slabEntity.ConfigTemp;
import com.icici.dma.slabRepository.CategoryCityRepo;
import com.icici.dma.slabRepository.CeRangeRepo;
import com.icici.dma.slabRepository.SlabRepo;
import com.icici.dma.slabService.ConfigService;

@RestController
@RequestMapping("/config")
@CrossOrigin
public class ConfigController {

	@Autowired
	private ConfigService service;

	@Autowired
	ConfigHistoryRepository historyRepo;

	@Autowired
	ConfigMasterRepository masterRepo;

	@Autowired
	ConfigTempRepository tempRepo;

	@Autowired
	private CategoryCityRepo categoryRepo;

	@Autowired
	private SlabRepo slabRepo;

	@Autowired
	private CeRangeRepo ceRangeRepo;

//	@GetMapping("/all")
//	@ResponseBody
//	public List<?> loadConfigs(
//
//			@RequestParam(required = false) String status,
//
//			@RequestParam(required = false) String type) {
//
//		return service.loadConfigs(status, type);
//
//	}
	
	@GetMapping("/all")
	@ResponseBody
	public List<?> loadConfigs(

	        @RequestParam(required = false) String status,
	        @RequestParam(required = false) String type,
	        @RequestParam(required = false) String category,
	        @RequestParam(required = false) String city,
	        @RequestParam(required = false) String dpd,
	        @RequestParam(required = false) String collection,
	        @RequestParam(required = false) String ceRange) {

	    return service.loadConfigs(status, type, category, city, dpd, collection, ceRange);

	}

	/*
	 * @GetMapping("/pending") public List<ConfigTemp> pending() { return
	 * service.getPending(); }
	 */
	
	@GetMapping("/pending")
	public List<ConfigTemp> pending(
	        @RequestParam String user){
	    return service.getPending(user);
	 
	}

	@PostMapping("/save")
	public String save(@RequestBody ConfigDTO dto, @RequestParam String user) {
		service.save(dto, user);
		return "SUCCESS";
	}

	@PostMapping("/update")
	public String update(@RequestBody ConfigDTO dto, @RequestParam String user) {
		service.update(dto, user);
		return "SUCCESS";
	}

	@PostMapping("/activate/{type}/{id}")
	public String activate(@PathVariable String type, @PathVariable Long id, @RequestParam String user) {
		service.activate(type, id, user);
		return "SUCCESS";
	}

	@PostMapping("/deactivate/{type}/{id}")
	public String deactivate(@PathVariable String type, @PathVariable Long id, @RequestParam String user) {
		service.deactivate(type, id, user);
		return "SUCCESS";
	}

	@PostMapping("/approve/{id}")
	public String approve(@PathVariable Long id, @RequestParam String user) {
		service.approve(id, user);
		return "APPROVED";
	}

	@PostMapping("/reject/{id}")
	public String reject(@PathVariable Long id, @RequestParam String user) {
		service.reject(id, user);
		return "REJECTED";
	}

	@GetMapping("/compare/{id}")
	public ConfigTemp compare(@PathVariable Long id) {
		return service.compare(id);
	}

	@GetMapping("/history")
	public List<ConfigHistory> history() {

		return historyRepo.findAll();

	}

	@GetMapping("/all/{status}")
	@ResponseBody
	public List<ConfigMaster> getByStatus(@PathVariable String status) {

		if ("ACTIVE".equals(status)) {
			return masterRepo.findByActiveFlagOrderByDisplayOrder("Y");
		}

		if ("INACTIVE".equals(status)) {

			return masterRepo.findByActiveFlagOrderByDisplayOrder("N");

		}

		return masterRepo.findAll();
	}

	@GetMapping("/history/{configId}")
	@ResponseBody
	public List<ConfigHistory> history(@PathVariable Long configId) {

		return historyRepo.findByConfigIdOrderByHistoryIdDesc(configId);

	}

	@GetMapping("/makerCompare/{id}")
	@ResponseBody
	public Map<String,Object> makerCompare(@PathVariable Long id){

	    Map<String,Object> map = new HashMap<>();

	    CategoryCityMaster category =
	            categoryRepo.findById(id).orElse(null);

	    if(category == null){
	        map.put("top", null);
	        map.put("bottom", null);
	        return map;
	    }

	    ConfigMaster current =
	            masterRepo.findTopByConfigKeyOrderByConfigIdDesc(
	                    category.getCategory())
	            .orElse(null);

	    ConfigHistory previous = null;

	    if(current != null){
	        previous =
	            historyRepo.findTopByConfigIdOrderByHistoryIdDesc(
	                    current.getConfigId());
	    }

	    map.put("top", current);
	    map.put("bottom", previous);

	    return map;
	}
	
//	@GetMapping("/checkerCompare/{id}")
//	public Map<String,Object> checkerCompare(
//	@PathVariable Long id){
//
//	    ConfigTemp pending =
//	            tempRepo.findByTempId(id)
//	                    .orElse(null);
//
//	    ConfigMaster approved = null;
//
//	    if(pending != null && pending.getConfigId()!=null){
//	    	 approved = masterRepo.findByConfigId(pending.getConfigId())
//                     .orElse(null);
//
//	    }
//
//	    Map<String,Object> map=new HashMap<>();
//
//	    map.put("top",pending);
//	    map.put("bottom",approved);
//
//	    return map;
//	}
	
	@GetMapping("/checkerCompare/{id}")
	public Map<String, Object> checkerCompare(@PathVariable Long id) {

	    ConfigTemp pending = tempRepo.findByTempId(id).orElse(null);

	    ConfigMaster approved = null;

	    if (pending != null && pending.getConfigId() != null) {

	        // Existing code (DO NOT CHANGE)
	        approved = masterRepo.findByConfigId(pending.getConfigId()).orElse(null);

	        // ADD THIS BLOCK ONLY
//	        if (approved == null) {
//
//	            approved = masterRepo
//	                    .findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
//	                            pending.getConfigType(),
//	                            pending.getOldValue())
//	                    .orElse(null);
//	        }
	        
	        if (approved == null) {

	            switch (pending.getConfigType()) {

	            case "CATEGORY":

	                approved = masterRepo
	                        .findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
	                                "CATEGORY",
	                                pending.getOldValue())
	                        .orElse(null);
	                break;

	            case "CITY":

	                approved = masterRepo
	                        .findTopByConfigTypeAndCategoryAndConfigValueOrderByConfigIdDesc(
	                                "CITY",
	                                pending.getCategory(),
	                                pending.getOldValue())
	                        .orElse(null);
	                break;

	            case "DPD":

	                approved = masterRepo
	                        .findTopByConfigTypeAndCategoryAndConfigValueOrderByConfigIdDesc(
	                                "DPD",
	                                pending.getCategory(),
	                                pending.getOldValue())
	                        .orElse(null);
	                break;

	            case "COLLECTION":

	                approved = masterRepo
	                        .findTopByConfigTypeAndCategoryAndDpdAndConfigValueOrderByConfigIdDesc(
	                                "COLLECTION",
	                                pending.getCategory(),
	                                pending.getDpd(),
	                                pending.getOldValue())
	                        .orElse(null);
	                break;

	            case "CE_RANGE":

	                approved = masterRepo
	                        .findTopByConfigTypeAndCategoryAndDpdAndConfigValueOrderByConfigIdDesc(
	                                "CE_RANGE",
	                                pending.getCategory(),
	                                pending.getDpd(),
	                                pending.getOldValue())
	                        .orElse(null);
	                break;
	            }
	        }
	        
	    }

	    Map<String, Object> map = new HashMap<>();

	    map.put("top", pending);
	    map.put("bottom", approved);

	    return map;
	}

	@GetMapping("/get/{id}")
	public ConfigMaster getById(@PathVariable Long id) {

		return masterRepo.findById(id).orElse(null);
	}

	@GetMapping("/categories")
	public List<String> categories() {

		return categoryRepo.findActiveCategories();

	}

	@GetMapping("/cities")
	public List<String> cities(@RequestParam String category) {

		return categoryRepo.findActiveCities(category);

	}

	@GetMapping("/dpds")
	public List<String> dpds(@RequestParam String category) {

		return slabRepo.findDistinctDpds(category);
	}

	@GetMapping("/collections")
	public List<CollectionSlabMaster> collections(@RequestParam String category, @RequestParam String dpd) {
		return slabRepo.findByCategoryAndDpdAndStatusOrderByOrderNoAsc(category, dpd, "Y");
	}

	@GetMapping("/ceranges")
	public List<CeRangeMaster> ceranges(@RequestParam String category, @RequestParam String dpd) {
		return ceRangeRepo.findByCategoryAndDpdAndStatusOrderByOrderNoAsc(category, dpd, "Y");
	}

	@GetMapping("/labels")
	public Map<String, String> labels() {

		return service.getLabels();
	}

	@GetMapping("/ui")
	public Map<String, String> uiConfig() {

		return service.getUiConfig();

	}

	@GetMapping("/category/{id}")
	public CategoryCityMaster getCategory(@PathVariable Long id) {

		return categoryRepo.findById(id).orElse(null);
	}

	@GetMapping("/city/{id}")
	public CategoryCityMaster getCity(@PathVariable Long id) {

		return categoryRepo.findById(id).orElse(null);

	}

	@GetMapping("/dpd/{id}")
	public CollectionSlabMaster getDpd(@PathVariable Long id) {

		return slabRepo.findById(id).orElse(null);

	}

	@GetMapping("/collection/{id}")
	public CollectionSlabMaster getCollection(@PathVariable Long id) {

		return slabRepo.findById(id).orElse(null);

	}

	@GetMapping("/cerange/{id}")
	public CeRangeMaster getCeRange(@PathVariable Long id) {

		return ceRangeRepo.findById(id).orElse(null);

	}

	@GetMapping("/capping")
	public ConfigMaster getCapping() {

		return masterRepo.findTopByConfigTypeOrderByConfigIdDesc("CAPPING").orElse(null);
	}

}