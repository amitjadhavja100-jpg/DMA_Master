
package com.icici.dma.slabServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.slabController.PayoutController;
import com.icici.dma.slabEntity.CellData;
import com.icici.dma.slabEntity.PayoutDetail;
import com.icici.dma.slabEntity.PayoutMaster;
import com.icici.dma.slabEntity.PayoutRequest;
import com.icici.dma.slabEntity.RowData;
import com.icici.dma.slabRepository.CeRangeRepo;
import com.icici.dma.slabRepository.PayoutMasterRepo;
import com.icici.dma.slabRepository.SlabRepo;
import com.icici.dma.slabService.PayoutService;

@Service
public class PayoutServiceImpl implements PayoutService {

	@Autowired
	private PayoutMasterRepo repo;

	@Autowired
	private CeRangeRepo cerepo;

	@Autowired
	private SlabRepo slabrepo;

	private static final Logger logger = LogManager.getLogger(PayoutServiceImpl.class);

	@Override
	public PayoutMaster save(PayoutRequest req, String user) {

		// GET CURRENT APPROVED RECORD
		PayoutMaster oldApproved = repo
				.findTopByCategoryAndCityAndDpdAndStatusOrderByVersionDesc(req.category, req.city, req.dpd, "APPROVED")
				.orElse(null);

		// CREATE NEW MASTER
		PayoutMaster m = new PayoutMaster();
		m.setCategory(req.category);
		m.setCity(req.city);
		m.setDpd(req.dpd);
		m.setStatus("PENDING");
		m.setCreatedBy(user);
		m.setCreatedDate(LocalDateTime.now());
		m.setFromDate(LocalDate.parse(req.fromDate));

		m.setToDate(LocalDate.parse(req.toDate));

		// VERSION + PARENT LOGIC
		if (oldApproved != null) {
			m.setParentMasterId(oldApproved.getId());
			m.setVersion(oldApproved.getVersion() + 1);
		} else {
			m.setVersion(1);
		}
		// DETAILS
		List<PayoutDetail> list = new ArrayList<>();

		for (RowData r : req.matrix) {

			for (CellData c : r.values) {

				PayoutDetail d = new PayoutDetail();

				// NORMAL + BAND DPD SUPPORT

				if (c.getCollectionSlab() != null) {

					// BAND DPD SCREEN

					d.setCollectionSlab(c.getCollectionSlab());

					d.setCeRange(c.getCeRange());

				} else {

					// NORMAL SCREEN

					d.setCollectionSlab(r.collection);

					d.setCeRange(c.getCeRange());
				}

//				d.setCollectionSlabFrom(getCollectionFrom(d.getCollectionSlab()));
//
//				d.setCollectionSlabTo(getCollectionTo(d.getCollectionSlab()));
//
//				d.setCeRangeFrom(getCeFrom(d.getCeRange()));
//
//				d.setCeRangeTo(getCeTo(d.getCeRange()));

				try {

					if (c.getPayout() == null || c.getPayout().toString().trim().isEmpty()) {

						d.setPayout(0.0);

					} else {

						d.setPayout(Double.valueOf(c.getPayout().toString().trim()));
					}

				} catch (Exception e) {

					d.setPayout(0.0);
				}

				d.setMaster(m);

				list.add(d);
			}
		}
		m.setDetails(list);

		try {

			PayoutMaster saved = repo.save(m);

			repo.flush();

			return saved;

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("SAVE FAILED : " + e.getMessage());
		}
	}

	@Override
	public void approve(Long id, String user) {
		try {
			System.out.println("================================");
			System.out.println("APPROVE START");
			System.out.println("ID = " + id);
			System.out.println("CHECKER USER = " + user);

			PayoutMaster master = repo.findById(id).orElseThrow(() -> new RuntimeException("No records found"));

			System.out.println("MAKER USER = " + master.getCreatedBy());
			System.out.println("CURRENT STATUS = " + master.getStatus());

			// checker cannot approve own record
			if (user.equals(master.getCreatedBy())) {
				throw new RuntimeException("Maker and Checker cannot be same");
			}

			List<PayoutMaster> list = repo.findByCategoryAndCityAndDpdOrderByVersionDesc(master.getCategory(),
					master.getCity(), master.getDpd());

			for (PayoutMaster m : list) {
				if ("APPROVED".equals(m.getStatus())) {
					m.setStatus("INACTIVE");
					repo.save(m);
				}
			}
			System.out.println("SETTING STATUS TO APPROVED");
			master.setStatus("APPROVED");
			master.setApprovedBy(user);
			master.setRemarks("Approved Successfully");
			repo.save(master);
			System.out.println("APPROVE SUCCESS");
			System.out.println("================================");
		} catch (Exception e) {
			System.out.println("APPROVE ERROR = " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("APPROVE FAILED : " + (e.getMessage() == null ? e.toString() : e.getMessage()));
		}
	}

	// snz
	@Override
	public void reject(Long id, String user, String remark) {
		try {
			System.out.println("================================");
			System.out.println("REJECT START");
			System.out.println("ID = " + id);
			System.out.println("CHECKER USER = " + user);
			PayoutMaster master = repo.findById(id).orElseThrow(() -> new RuntimeException("No records found"));
			if (user.equals(master.getCreatedBy())) {
				throw new RuntimeException("Maker and Checker cannot be same / Own record cannot reject");
			}
			System.out.println("SETTING STATUS TO REJECTED");
			master.setStatus("REJECTED");
			master.setApprovedBy(user);
			master.setRemarks(remark);
			repo.save(master);
			System.out.println("REJECT SUCCESS");
			System.out.println("================================");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("REJECT FAILED : " + (e.getMessage() == null ? e.toString() : e.getMessage()));
		}
	}

	@Override
	public List<PayoutMaster> history(String c, String city, String dpd) {
		return repo.findByCategoryAndCityAndDpdOrderByVersionDesc(c, city, dpd);
	}

	@Override
	public PayoutMaster fetch(String category, String city, String dpd, String fromDate, String toDate) {

		return repo.findTopByCategoryAndCityAndDpdAndFromDateAndToDateAndStatusOrderByVersionDesc(category, city, dpd,
				LocalDate.parse(fromDate), LocalDate.parse(toDate), "APPROVED").orElse(null);

	}

	@Override
	public List<PayoutMaster> pending(String user) {

		System.out.println("================================");
		System.out.println("LOGIN USER = " + user);
		List<PayoutMaster> list = repo.findByStatusOrderByCreatedDateDesc("PENDING");
		System.out.println("TOTAL PENDING = " + list.size());

		list.forEach(x -> System.out.println("ID = " + x.getId() + " CREATED_BY = " + x.getCreatedBy())

		);

		List<PayoutMaster> result = list.stream().filter(x -> !user.equalsIgnoreCase(x.getCreatedBy()))
				.collect(Collectors.toList());

		System.out.println("AFTER FILTER = " + result.size());
		System.out.println("================================");

		return result;
	}

	@Override
	public List<Map<String, Object>> compare(Long id) {

		/* PayoutMaster pending = repo.findById(id).orElseThrow(); */
		PayoutMaster pending = repo.findById(id).orElseThrow(() -> new RuntimeException("No records found"));

		PayoutMaster approved = repo.findTopByCategoryAndCityAndDpdAndStatusOrderByVersionDesc(

				pending.getCategory(),

				pending.getCity(),

				pending.getDpd(),

				"APPROVED"

		).orElse(null);

		Map<String, Double> approvedMap = new HashMap<>();

		if (approved != null) {

			for (PayoutDetail d : approved.getDetails()) {

				approvedMap.put(

						d.getCollectionSlab() + "-" + d.getCeRange(),

						d.getPayout());
			}
		}

		List<Map<String, Object>> result = new ArrayList<>();

		for (PayoutDetail d : pending.getDetails()) {

			Map<String, Object> row = new HashMap<>();

			String key = d.getCollectionSlab() + "-" + d.getCeRange();

			row.put("collection", d.getCollectionSlab());

			row.put("ceRange", d.getCeRange());

			row.put("oldValue", approvedMap.get(key));

			row.put("newValue", d.getPayout());

			result.add(row);
		}

		return result;
	}

	@Override
	public Map<String, Object> makerCompare(Long id) {

		/* PayoutMaster current = repo.findById(id).orElseThrow(); */
		PayoutMaster current = repo.findById(id).orElseThrow(() -> new RuntimeException("No records found"));

		PayoutMaster previous = null;

		// GET PARENT RECORD

		if (current.getParentMasterId() != null) {

			previous = repo.findById(current.getParentMasterId()).orElse(null);
		}

		Map<String, Object> map = new HashMap<>();

		// TOP = SELECTED RECORD

		map.put("top", convert(current));

		// BOTTOM = PARENT RECORD

		map.put("bottom", previous != null ? convert(previous) : null);

		return map;
	}

	@Override
	public Map<String, Object> checkerCompare(Long id) {

		/* PayoutMaster pending = repo.findById(id).orElseThrow(); */
		PayoutMaster pending = repo.findById(id).orElseThrow(() -> new RuntimeException("No records found"));

		PayoutMaster approved = null;

		// GET CURRENT APPROVED

		if (pending.getParentMasterId() != null) {

			approved = repo.findById(pending.getParentMasterId()).orElse(null);
		}

		Map<String, Object> map = new HashMap<>();

		// TOP = PENDING EDITED

		map.put("top", convert(pending));

		// BOTTOM = CURRENT APPROVED

		map.put("bottom", approved != null ? convert(approved) : null);

		return map;
	}

	@Override
	public Long latestApprovedId(String category, String city, String dpd) {

		return repo.findTopByCategoryAndCityAndDpdAndStatusOrderByVersionDesc(

				category, city, dpd, "APPROVED"

		).map(PayoutMaster::getId)

				.orElse(null);
	}

	private Map<String, Object> convert(PayoutMaster master) {

		Map<String, Object> map = new LinkedHashMap<>();

		map.put("id", master.getId());

		map.put("category", master.getCategory());

		map.put("city", master.getCity());

		map.put("dpd", master.getDpd());

		map.put("status", master.getStatus());

		List<Map<String, Object>> detailsList = new ArrayList<>();

		for (PayoutDetail d : master.getDetails()) {

			Map<String, Object> row = new LinkedHashMap<>();

			row.put("collectionSlab", d.getCollectionSlab());

			row.put("ceRange", d.getCeRange());

			row.put("payout", d.getPayout());

			detailsList.add(row);
		}

		map.put("details", detailsList);

		return map;
	}

	@Override
	public Map<String, Object> existingStructure(String category, String city, String dpd) {

		Map<String, Object> map = new HashMap<>();

		String actualDpd = dpd;

		// COMMON STRUCTURE CATEGORIES
		if (category.equals("NTC") || category.equals("CCA") || category.equals("TCC")
				|| category.equals("Settlement Incentive/Penalty")) {

			actualDpd = "ALL";
		}

		PayoutMaster approved = repo
				.findTopByCategoryAndCityAndDpdAndStatusOrderByVersionDesc(category, city, dpd, "APPROVED")
				.orElse(null);

		List<String> rows = new ArrayList<>();
		List<String> cols = new ArrayList<>();

//		if (approved != null) {
//
//			//snz
//			 System.out.println("=========== DETAILS ===========");
//
//			    approved.getDetails().forEach(d -> {
//			        System.out.println(
//			                d.getCollectionSlab()
//			                + " | "
//			                + d.getCeRange()
//			        );
//			    });
//
//			    System.out.println("===============================");
//			    //snz --end
//			
//			rows = approved.getDetails().stream().map(PayoutDetail::getCollectionSlab).distinct()
//					.collect(Collectors.toList());
//
//			cols = approved.getDetails().stream().map(PayoutDetail::getCeRange).distinct().collect(Collectors.toList());
//		
//			//snz
//		    System.out.println("ROWS = " + rows);
//		    System.out.println("COLS = " + cols);
//		}

		if (approved != null) {

		    List<String> activeRows = slabrepo.getCollectionSlabs(category, actualDpd);
		    List<String> activeCols = cerepo.getCeRanges(category, actualDpd);

		    rows = approved.getDetails().stream()
		            .map(PayoutDetail::getCollectionSlab)
		            .filter(activeRows::contains)
		            .distinct()
		            .collect(Collectors.toList());

		    cols = approved.getDetails().stream()
		            .map(PayoutDetail::getCeRange)
		            .filter(activeCols::contains)
		            .distinct()
		            .collect(Collectors.toList());
		}
		
		
		if (rows.isEmpty()) {

			rows = slabrepo.getCollectionSlabs(category, actualDpd);
		}

		if (cols.isEmpty()) {

			cols = cerepo.getCeRanges(category, actualDpd);
		}

		if (category.equals("TCC")) {

			cols = Arrays.asList("");
		} else if (category.equals("Settlement Incentive/Penalty")) {

			cols = cerepo.getCeRanges(category, "ALL");
		}

		map.put("rows", rows);

		map.put("cols", cols);

		return map;
	}

	@Override
	public List<String> getDpds(String category) {
		// NTC / CCA / TCC NO DPD DROPDOWN
		if (category.equals("NTC") || category.equals("CCA") || category.equals("TCC")
				|| category.equals("Settlement Incentive/Penalty")) {
			return Collections.emptyList();
		}
		return slabrepo.findDistinctDpds(category);
	}

	
	
	private BigDecimal getCollectionFrom(String slab) {
		slab = slab.replace(" ", "");
		// 70-MAX
		if (slab.toUpperCase().contains("MAX")) {
		    String value = slab.split("-")[0];
		    return new BigDecimal(value)
		            .add(BigDecimal.ONE);
		}
		// <500000 OR <8Lac
		if (slab.startsWith("<")) {
			return BigDecimal.ZERO;
		}
		// Upto20Lac
		if (slab.toLowerCase().startsWith("upto")) {
			return BigDecimal.ZERO;
		}
		// 500000-1000000
		if (slab.matches("\\d+(\\.\\d+)?-\\d+(\\.\\d+)?")) {
			String value = slab.split("-")[0];
			BigDecimal from =
			        new BigDecimal(value);
			if(value.contains(".")){
			    return from;
			}
			return from.add(BigDecimal.ONE);
		}
		// >3Lac<7Lac
		if (slab.startsWith(">") && slab.contains("<") && !slab.contains("<=")) {
			String value = slab.substring(1).split("<")[0];
			return convertAmount(value).add(BigDecimal.ONE);
		}
		// >8Lac<=11Lac
		if (slab.contains("<=")) {
			String value = slab.substring(1).split("<=")[0];
			return convertAmount(value).add(BigDecimal.ONE);
		}
		// >20Lacto40Lac
		if (slab.toLowerCase().contains("to")) {
			String value = slab.substring(1).toLowerCase().split("to")[0];
			return convertAmount(value).add(BigDecimal.ONE);
		}
		// >32Lac OR >1Cr+
		if (slab.startsWith(">")) {
			String value = slab.substring(1).replace("+", "");
			return convertAmount(value).add(BigDecimal.ONE);
		}
		return BigDecimal.ZERO;
	}

	
	private BigDecimal getCollectionTo(String slab) {
		slab = slab.replace(" ", "");
		if (slab.toUpperCase().contains("MAX")) {
			return new BigDecimal("99999999");
		}
		
		// <500000
		/*
		 * if (slab.startsWith("<")) { return convertAmount(slab.substring(1)); }
		 */
		
		// <10Lac<=50Lac OR <500000
		if (slab.startsWith("<")) {

		    String value = slab.substring(1);

		    if(value.contains("<=")){
		        value = value.split("<=")[1];
		    }

		    return convertAmount(value);
		}
		
		// Upto20Lac
		if (slab.toLowerCase().startsWith("upto")) {
			return convertAmount(slab.substring(4));
		}
		// 500000-1000000
		if (slab.matches("\\d+(\\.\\d+)?-\\d+(\\.\\d+)?")) {
			return new BigDecimal(slab.split("-")[1]);
		}
		// >3Lac<7Lac
		if (slab.startsWith(">") && slab.contains("<") && !slab.contains("<=")) {
			String value = slab.substring(slab.indexOf("<") + 1);
			return convertAmount(value);
		}
		// >8Lac<=11Lac
		if (slab.contains("<=")) {
			String value = slab.substring(slab.indexOf("<=") + 2);
			return convertAmount(value);
		}
		// >20Lacto40Lac
		if (slab.toLowerCase().contains("to")) {
			String value = slab.toLowerCase().split("to")[1];
			return convertAmount(value);
		}
		// >1Cr+ OR >3000000
		if (slab.startsWith(">")) {
			return new BigDecimal("99999999");
		}
		return new BigDecimal("99999999");
	}

	
	
	private BigDecimal getCeFrom(String range) {
		range = range.replace("%", "").replace(" ", "");
		if (range.startsWith("<")) {
			return BigDecimal.ZERO;
		}

		// <=2.2
		if (range.startsWith("<=")) {
			return BigDecimal.ZERO;
		}
		// 0-10
		if (range.contains("-") && !range.toUpperCase().contains("MAX")) {
			String value = range.split("-")[0];
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		if(range.endsWith("+")) {
		    String value =
		            range.replace("+","");
		    return new BigDecimal(value)
		            .add(new BigDecimal("0.01"));
		}
		// >2.2<=2.9
		if (range.contains("<=")) {
			String value = range.substring(1).split("<=")[0].replace("%", "").replace("=", "").trim();
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		// >5
		if (range.startsWith(">")) {
			String value = range.substring(1);
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		// 70-MAX
		if(range.contains("-")
		        && !range.toUpperCase().contains("MAX")) {
		    String value =
		            range.split("-")[0]
		                 .replace("%","");
		    return new BigDecimal(value)
		            .add(new BigDecimal("0.01"));
		}
		// 0.5-1
		if (range.contains("-") && !range.contains("<=") && !range.toUpperCase().contains("MAX")) {
			String value = range.split("-")[0];
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		if (range.endsWith("+")) {
			String value = range.replace("+", "");
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		if(range.contains("<=")){
		    String value =
		            range.substring(1)
		                 .split("<=")[0]
		                 .replace("%", "")
		                 .replace("=", "")
		                 .trim();
		    return new BigDecimal(value)
		            .add(new BigDecimal("0.01"));
		}
		if(range.startsWith("<")
		        && !range.startsWith("<=")) {
		    return BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}
	
	
	private BigDecimal getCeTo(String range) {
		System.out.println("CE TO INPUT = [" + range + "]");
	    range = range.replace("%", "")
	                 .replace(" ", "");
	    // <2.2
	    if (range.startsWith("<")
	            && !range.startsWith("<=")) {
	        return new BigDecimal(
	                range.replace("<", "")
	                     .trim());
	    }
	    // <=2.2
	    if (range.startsWith("<=")) {
	        return new BigDecimal(
	                range.replace("<=", "")
	                     .trim());
	    }
	    // >2.2<=2.9 OR >5<=6
	    if (range.contains("<=")) {
	        String value =
	                range.split("<=")[1]
	                     .replace("%", "")
	                     .replace("=", "")
	                     .trim();
	        return new BigDecimal(value);
	    }
	    // 0-10
	    if (range.contains("-")
	            && !range.toUpperCase().contains("MAX")) {
	    	return new BigDecimal(
	    	        range.split("-")[1]
	    	             .replace("%",""));
	    }
	    // 70-MAX
	    if (range.toUpperCase().contains("MAX")) {

	        return new BigDecimal("99999999");
	    }
	    // 3600+
	    if (range.endsWith("+")) {

	        return new BigDecimal("99999999");
	    }
	    if(range.startsWith("<")
	            && !range.startsWith("<=")) {

	        return new BigDecimal(
	                range.substring(1));
	    }
	    // >5
	    return new BigDecimal("99999999");
	}

	

	private BigDecimal convertAmount(String value) {
		String original = value;
		value = value.trim();
		BigDecimal num = new BigDecimal(
				value.replace("Lac", "").replace("lac", "").replace("Cr", "").replace("cr", "").trim());
		if (original.toLowerCase().contains("cr")) {
			return num.multiply(new BigDecimal("10000000"));
		}
		if (original.toLowerCase().contains("lac")) {
			return num.multiply(new BigDecimal("100000"));
		}
		return num;
	}


}