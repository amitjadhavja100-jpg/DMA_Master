package com.icici.dma.slabController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.configMasterRepository.ConfigMasterRepository;
import com.icici.dma.slabEntity.CategoryCityMaster;
import com.icici.dma.slabEntity.CeRangeMaster;
import com.icici.dma.slabEntity.CollectionSlabMaster;
import com.icici.dma.slabEntity.PayoutMaster;
import com.icici.dma.slabEntity.PayoutRequest;
import com.icici.dma.slabRepository.CategoryCityRepo;
import com.icici.dma.slabRepository.CeRangeRepo;
import com.icici.dma.slabRepository.PayoutMasterRepo;
import com.icici.dma.slabRepository.SlabRepo;
import com.icici.dma.slabService.PayoutService;

@RestController
@RequestMapping("/payout")
@CrossOrigin
public class PayoutController {

	@Autowired
	private PayoutService service;

	@Autowired
	private CategoryCityRepo categoryRepo;

	@Autowired
	private CeRangeRepo ceRepo;

	@Autowired
	private SlabRepo slabRepo;

	@Autowired
	private PayoutMasterRepo repo;
	
	private static final Logger logger = LogManager.getLogger(PayoutController.class);


	/*
	 * @GetMapping("/categories") public List<String> categories() {
	 * 
	 * List<String> dbCategories =
	 * categoryRepo.findAll().stream().map(CategoryCityMaster::getCategory).distinct
	 * () .collect(Collectors.toList());
	 * 
	 * List<String> ordered = new ArrayList<>(); // A TO J String[] normal = { "A",
	 * "B", "C", "D", "E", "F", "G", "H", "I", "J" }; for (String c : normal) {
	 * 
	 * if (dbCategories.contains(c)) { ordered.add(c); } } // 360 if
	 * (dbCategories.contains("360 + CAT A")) { ordered.add("360 + CAT A"); }
	 * 
	 * if (dbCategories.contains("360 + CAT B")) { ordered.add("360 + CAT B"); }
	 * 
	 * // COMMON if (dbCategories.contains("NTC")) { ordered.add("NTC"); }
	 * 
	 * if (dbCategories.contains("TCC")) { ordered.add("TCC"); }
	 * 
	 * if (dbCategories.contains("CCA")) { ordered.add("CCA"); }
	 * 
	 * if (dbCategories.contains("Settlement Incentive/Penalty")) {
	 * ordered.add("Settlement Incentive/Penalty"); }
	 * 
	 * return ordered; }
	 */
	
	@GetMapping("/categories")
	public List<String> categories() {

	    List<String> dbCategories = categoryRepo.findDistinctCategories();

	    List<String> ordered = new ArrayList<>();

	    String[] fixed = {
	            "A","B","C","D","E","F","G","H","I","J",
	            "360 + CAT A",
	            "360 + CAT B",
	            "NTC",
	            "TCC",
	            "CCA",
	            "Settlement Incentive/Penalty"
	    };

	    // Existing categories in fixed order
	    for(String c : fixed){
	        if(dbCategories.contains(c)){
	            ordered.add(c);
	        }
	    }

	    // Add newly created categories
	    for(String c : dbCategories){

	        if(!ordered.contains(c)){
	            ordered.add(c);
	        }
	    }

	    return ordered;
	}
	
	// CITY DROPDOWN
	@GetMapping("/cities")
	public List<String> cities(@RequestParam String category) {

		List<String> collect = categoryRepo.findByCategory(category).stream().map(CategoryCityMaster::getCity)
				.collect(Collectors.toList());

		return collect;
	}

//    // STRUCTURE
	@GetMapping("/existingStructure")
	public Map<String, Object> existingStructure(

			@RequestParam String category,

			@RequestParam String city,

			@RequestParam String dpd) {

		return service.existingStructure(category, city, dpd);
	}

	// FETCH
	@GetMapping("/fetch")
	public PayoutMaster fetch(

			@RequestParam String category,

			@RequestParam String city,

			@RequestParam String dpd,

			@RequestParam String fromDate,

			@RequestParam String toDate) {

		return service.fetch(category, city, dpd, fromDate, toDate);
	}

	// SAVE
	@PostMapping("/save")
	public PayoutMaster save(@RequestBody PayoutRequest req, @RequestParam String user, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user1 = (String) session.getAttribute("USER_ID");
		logger.info("Save Master");
		return service.save(req, user1);
	}

	// APPROVE
//	@PostMapping("/approve/{id}")
//	public String approve(@PathVariable Long id, @RequestParam String user) {
//
//		service.approve(id, user);
//
//		return "APPROVED";
//	}
//
//	// REJECT
//	@PostMapping("/reject/{id}")
//	public String reject(@PathVariable Long id, @RequestParam String user, @RequestParam String remarks) {
//		service.reject(id, user, remarks);
//		return "Rejected";
//	}

	
	
	//snz
//	@PostMapping("/approve/{id}")
//	public String approve(@PathVariable Long id,
//	                      @RequestParam String user) {
//	    try {
//	    	logger.info("In approve controller method :: ID : {} , User Id : {}",id,user);
//	        service.approve(id, user);
//	        return "APPROVED";
//	    } catch (Exception e) {
//	    	logger.info("In approve controller method :: exception {}",e);
//	        e.printStackTrace();
//	        return "ERROR = " + e.getMessage();
//	    }
//	}
//	
//	@PostMapping("/reject/{id}")
//	public String reject(@PathVariable Long id,
//	                     @RequestParam String user,
//	                     @RequestParam String remarks) {
//	    try {
//	        service.reject(id, user, remarks);
//	        return "REJECTED";
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return "ERROR = " + e.getMessage();
//	    }
//	}	
	
	//snz
		@PostMapping("/approve/{id}")
		public ResponseEntity<String> approve(
		        @PathVariable Long id,
		        @RequestParam String user) {

		    try {

		        service.approve(id,user);

		        return ResponseEntity.ok("Approved Successfully");

		    } catch(Exception e){

		        e.printStackTrace();

		        Throwable root = e;

		        while(root.getCause()!=null){
		            root = root.getCause();
		        }

		        System.out.println("ROOT ERROR = " + root);

		        return ResponseEntity
		                .status(500)
		                .body(root.toString());
		    }
		}
		
		//snz
		@PostMapping("/reject/{id}")
		public ResponseEntity<String> reject(
		        @PathVariable Long id,
		        @RequestParam String user,
		        @RequestParam String remarks) {

		    try {

		        service.reject(id,user,remarks);

		        return ResponseEntity.ok("Rejected Successfully");

		    }
		    catch(Exception e) {

		        e.printStackTrace();

		        Throwable root = e;

		        while(root.getCause() != null){
		            root = root.getCause();
		        }

		        System.out.println("ROOT ERROR = " + root.getMessage());

		        return ResponseEntity
		                .badRequest()
		                .body(root.getMessage());
		    }
		}
	
	// HISTORY
	@GetMapping("/history")
	public List<PayoutMaster> history() {

		return repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	@GetMapping("/pending")
	public List<PayoutMaster> pending(@RequestParam String user) {
		return service.pending(user);
	}

	@GetMapping("/compare/{id}")
	public List<Map<String, Object>> compare(@PathVariable Long id) {
		return service.compare(id);
	}

	@GetMapping("/makerCompare/{id}")
	public ResponseEntity<?> makerCompare(@PathVariable Long id) {

		return ResponseEntity.ok(service.makerCompare(id));
	}

	@GetMapping("/checkerCompare/{id}")
	public ResponseEntity<?> checkerCompare(@PathVariable Long id) {

		return ResponseEntity.ok(service.checkerCompare(id));
	}

	@GetMapping("/latestApprovedId")
	public Long latestApprovedId(@RequestParam String category, @RequestParam String city, @RequestParam String dpd) {

		return service.latestApprovedId(category, city, dpd);
	}

//    		// ALL SLABS
//	@GetMapping("/allSlabs")
//	public List<String> allSlabs(
//
//			@RequestParam String category,
//
//			@RequestParam String dpd) {
//
//		List<String> collect = slabRepo.findByCategoryAndDpd(category, dpd)
//
//				.stream()
//
//				.map(CollectionSlabMaster::getSlabValue)
//
//				.collect(Collectors.toList());
//
//		return collect;
//	}
	
	@GetMapping("/allSlabs")
	public List<String> allSlabs(

	        @RequestParam String category,

	        @RequestParam String dpd) {

	    return slabRepo.findByCategoryAndDpd(
	            category,
	            dpd)
	            .stream()
	            .map(CollectionSlabMaster::getSlabValue)
	            .collect(Collectors.toList());
	}

//	@GetMapping("/allCeRanges")
//	public List<String> allCeRanges(
//
//			@RequestParam String category,
//
//			@RequestParam String dpd) {
//
//		List<String> collect = ceRepo.findByCategoryAndDpd(category, dpd)
//
//				.stream()
//
//				.map(CeRangeMaster::getRangeValue).collect(Collectors.toList());
//
//		return collect;
//	}
	
//	@GetMapping("/allCeRanges")
//	public List<String> allCeRanges(
//	        @RequestParam String category,
//	        @RequestParam String dpd) {
//
//	    List<String> data =
//	            ceRepo.getCeRanges(category, dpd);
//
//	    System.out.println("DATA = " + data);
//
//	    return data;
//	}
	
	@GetMapping("/allCeRanges")
	public List<String> allCeRanges(

	        @RequestParam String category,

	        @RequestParam String dpd) {

	    return ceRepo.getCeRanges(
	            category,
	            dpd);
	}
	
	@PostMapping("/addSlab")
	public String addSlab(

			@RequestParam String category,

			@RequestParam String dpd,

			@RequestParam String slab) {

		CollectionSlabMaster s = new CollectionSlabMaster();

		s.setCategory(category);

		s.setDpd(dpd);

		s.setSlabValue(slab);

		slabRepo.save(s);

		return "Saved";
	}

	@PostMapping("/addCeRange")
	public String addCeRange(

			@RequestParam String category,

			@RequestParam String dpd,

			@RequestParam String range) {

		CeRangeMaster c = new CeRangeMaster();

		c.setCategory(category);

		c.setDpd(dpd);

		c.setRangeValue(range);

		ceRepo.save(c);

		return "Saved";
	}

//    		// ALL CE RANGES
//	@PostMapping("/addSlab")
//	public String addSlab(
//	@RequestParam String category,
//	@RequestParam String dpd,
//	@RequestParam String slab) {
//
//	    Integer maxOrder =
//	    slabRepo.getMaxOrderNo(
//	    category,
//	    dpd
//	    );
//
//	    if(maxOrder == null){
//	        maxOrder = 0;
//	    }
//
//	    CollectionSlabMaster s =
//	    new CollectionSlabMaster();
//	    s.setCategory(category);
//	    s.setDpd(dpd);
//	    s.setSlabValue(slab);
//	    s.setOrderNo(maxOrder + 1);
//
//	    slabRepo.save(s);
//	    return "Saved";
//	}
//
//	@PostMapping("/addCeRange")
//	public String addCeRange(
//	@RequestParam String category,
//	@RequestParam String dpd,
//	@RequestParam String range) {
//
//	    Integer maxOrder =
//	    ceRepo.getMaxOrderNo(
//	    category,
//	    dpd
//	    );
//
//	    if(maxOrder == null){
//	        maxOrder = 0;
//	    }
//
//	    CeRangeMaster c =
//	    new CeRangeMaster();
//	    c.setCategory(category);
//	    c.setDpd(dpd);
//	    c.setRangeValue(range);
//	    c.setOrderNo(maxOrder + 1);
//	    ceRepo.save(c);
//	    return "Saved";
//	}

	@GetMapping("/dpds")
	@ResponseBody
	public List<String> getDpds(@RequestParam String category) {

		return service.getDpds(category);
	}

	
}
