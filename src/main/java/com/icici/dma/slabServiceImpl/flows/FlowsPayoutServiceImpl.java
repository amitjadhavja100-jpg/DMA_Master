package com.icici.dma.slabServiceImpl.flows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsPayoutDetailDTO;
import com.icici.dma.dto.flows.FlowsPayoutFooterDetailDTO;
import com.icici.dma.dto.flows.FlowsPayoutRequest;
import com.icici.dma.dto.flows.FlowsPayoutResponse;
import com.icici.dma.dto.flows.FlowsPerformanceDTO;
import com.icici.dma.dto.flows.FlowsResolutionDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;
import com.icici.dma.slabEntity.flows.FlowsCityMaster;
import com.icici.dma.slabEntity.flows.FlowsPayoutDetail;
import com.icici.dma.slabEntity.flows.FlowsPayoutFooterDetail;
import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;
import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;
import com.icici.dma.slabEntity.flows.FlowsSlabMaster;
import com.icici.dma.slabEntity.flows.FooterRule;
import com.icici.dma.slabRepository.flows.FlowsBucketRepository;
import com.icici.dma.slabRepository.flows.FlowsCategoryRepository;
import com.icici.dma.slabRepository.flows.FlowsCityRepository;
import com.icici.dma.slabRepository.flows.FlowsPerformanceRepository;
import com.icici.dma.slabRepository.flows.FlowsResolutionRepository;
import com.icici.dma.slabRepository.flows.FlowsSlabRepository;
import com.icici.dma.slabService.flows.FlowsFooterRuleService;
import com.icici.dma.slabService.flows.FlowsPayoutService;

@Service
@Transactional
public class FlowsPayoutServiceImpl implements FlowsPayoutService {

	@Autowired
	private FlowsBucketRepository bucketRepo;

	@Autowired
	private FlowsCategoryRepository categoryRepo;

	@Autowired
	private FlowsResolutionRepository resolutionRepo;

	@Autowired
	private FlowsPerformanceRepository performanceRepo;

	@Autowired
	private FlowsSlabRepository slabRepo;

	@Autowired
	private FlowsCityRepository cityRepo;
	
	@Autowired
	private FlowsFooterRuleService footerRuleService;
	
	
	@Override
	@Transactional
	public String save(FlowsPayoutRequest request, String userId) {

	    validatePending(request);
	    FlowsSlabMaster master = buildMaster(request,userId);

		buildDetails(master, request.getPayoutList(), userId);
		
		buildFooters(master, request.getFooterList(), userId);

	    slabRepo.save(master);
	    return "Saved Successfully";

	}
	
	//snz25-6
//	@Override
//	public List<FlowsBucketMaster> getBuckets(Long cityId) {
//
//	    return bucketRepo.findByCityIdAndStatusOrderById(
//	            cityId,
//	            "APPROVED"
//	    );
//	}
	
	@Override
	public List<FlowsBucketMaster> getBuckets(
	        Long categoryId,
	        Long cityId) {

	    if (cityId == null) {

	        return bucketRepo.findByCategoryIdAndCityIdIsNullAndStatusOrderById(
	                categoryId,
	                "APPROVED"
	        );
	    }

	    return bucketRepo.findByCityIdAndStatusOrderById(
	            cityId,
	            "APPROVED"
	    );
	}
	
	@Override
	public List<FlowsCategoryMaster> getCategories() {

	    return categoryRepo.findByStatusOrderByCategoryName("APPROVED");

	}
	
	@Override
	public List<FlowsCityMaster> getCities(Long categoryId) {

	    return cityRepo.findByCategoryIdAndStatusOrderByCityName(
	            categoryId,
	            "APPROVED"
	    );
	}
	
	@Override
	public List<FlowsResolutionMaster> getResolution(Long bucketId) {

	    return resolutionRepo.findByBucketIdAndStatusOrderByOrderId(
	            bucketId,
	            "APPROVED");

	}	
	
	@Override
	public List<FlowsPerformanceMaster> getPerformance(Long bucketId) {

	    return performanceRepo.findByBucketIdAndStatusOrderByOrderId(
	            bucketId,
	            "APPROVED");

	}
		
	@Override
	public FlowsSlabMaster fetch(
	        String product,
	        String subProduct,
	        Long categoryId,
	        Long cityId,
	        Long bucketId,
	        String fromDate,
	        String toDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

		LocalDate from = LocalDate.parse(fromDate, formatter);

		LocalDate to = LocalDate.parse(toDate, formatter);

	    List<FlowsSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    product,
	                    subProduct,
	                    categoryId,
	                    cityId,
	                    bucketId,
	                    from,
	                    to);

		if (approved.isEmpty()) {
			return null;
		}
		return approved.get(0);
	}

	
	@Override
	public List<FlowsSlabMaster> getMakerList() {

		return slabRepo.findByStatusOrderByCreatedDateDesc("PENDING");
	}

	@Override
	public List<FlowsSlabMaster> getCheckerList(String user) {

	    List<FlowsSlabMaster> list = slabRepo.findByStatusAndCreatedByNotOrderByCreatedDateDesc("PENDING", user);

	    for (FlowsSlabMaster master : list) {
	    	
				if (master.getCategoryId() != null) {

					FlowsCategoryMaster category = categoryRepo.findById(master.getCategoryId()).orElse(null);

					if (category != null) {
						master.setCategoryName(category.getCategoryName());
					}
				}

				if (master.getCityId() != null) {

					FlowsCityMaster city = cityRepo.findById(master.getCityId()).orElse(null);

					if (city != null) {
						master.setCityName(city.getCityName());
					}
				}

				if (master.getBucketId() != null) {

					FlowsBucketMaster bucket = bucketRepo.findById(master.getBucketId()).orElse(null);

					if (bucket != null) {
						master.setBucketName(bucket.getBucketName());
					}
				}
	    }
	    return list;
	}
	
	@Override
	@Transactional
	public String approve(Long id, String checkerId) {

		FlowsSlabMaster pending = slabRepo.findById(id)
		        .orElseThrow(() -> new RuntimeException("Record not found"));

		if (checkerId.equals(pending.getCreatedBy())) {

			throw new RuntimeException("Maker cannot approve own record");

		}
		inactivatePrevious(pending);

		pending.setStatus("APPROVED");
		pending.setApprovedBy(checkerId);
		pending.setApprovedDate(LocalDateTime.now());
		slabRepo.save(pending);

		return "Approved Successfully";

	}

	@Override
	@Transactional
	public String reject(
			Long id,
			String checker,
			String remarks) {

		FlowsSlabMaster pending = slabRepo.findById(id)
		        .orElseThrow(() -> new RuntimeException("Record not found"));

		if (checker.equals(pending.getCreatedBy())) {
			throw new RuntimeException("Maker cannot reject own record");

		}
		pending.setStatus("REJECTED");
		pending.setApprovedBy(checker);
		pending.setApprovedDate(LocalDateTime.now());
		pending.setRemarks(remarks);

		slabRepo.save(pending);

		return "Rejected";

	}
	
	@Override
	public Map<String, Object> compare(Long id, String mode) {

		Map<String, Object> map = new HashMap<>();
	    FlowsSlabMaster record =
	            slabRepo.findById(id)
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Record not found"));


	    if ("checker".equalsIgnoreCase(mode)) {

	        List<FlowsSlabMaster> approved =
	                slabRepo.findLatestApproved(
	                        record.getProduct(),
	                        record.getSubProduct(),
	                        record.getCategoryId(),
	                        record.getCityId(),
	                        record.getBucketId(),
	                        record.getFromDate(),
	                        record.getToDate());

			map.put("top", buildCompareData(record));
	        map.put(
	                "bottom",
	                approved.isEmpty()
	                        ? null
	                        : buildCompareData(
	                                approved.get(0))
	        );
	        return map;
	    }

	    if ("maker".equalsIgnoreCase(mode)) {
			if (!"APPROVED".equalsIgnoreCase(record.getStatus())) {
				throw new RuntimeException("Selected record is not an approved record");
	        }

	        List<FlowsSlabMaster> previousList =
	                slabRepo.findPreviousApproved(
	                        record.getProduct(),
	                        record.getSubProduct(),
	                        record.getCategoryId(),
	                        record.getCityId(),
	                        record.getBucketId(),
	                        record.getFromDate(),
	                        record.getToDate(),
	                        record.getVersionNo());

			map.put("top", buildCompareData(record));

	        if (previousList.isEmpty()) {
	            map.put("bottom", null);
	        } else {
				FlowsSlabMaster previous = previousList.get(0);
				map.put("bottom", buildCompareData(previous));
	        }
	        return map;
	    }
		throw new RuntimeException("Invalid compare mode: " + mode);
	}
	
	@Override
	public Long getLatestApprovedId(
	        String product,
	        String subProduct,
	        Long categoryId,
	        Long cityId,
	        Long bucketId,
	        String fromDate,
	        String toDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		LocalDate from = LocalDate.parse(fromDate, formatter);
		LocalDate to = LocalDate.parse(toDate, formatter);

	    List<FlowsSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    product,
	                    subProduct,
	                    categoryId,
	                    cityId,
	                    bucketId,
	                    from,
	                    to);

	    if (approved.isEmpty()) {
	        return null;
	    }

	    return approved.get(0).getId();
	}
	
	private Map<String, Object> buildCompareData(FlowsSlabMaster master) {

		Map<String, Object> data = new HashMap<>();

		data.put("id", master.getId());
		data.put("product", master.getProduct());
		data.put("subProduct", master.getSubProduct());
		data.put("categoryId", master.getCategoryId());
		
		if (master.getCategoryId() != null) {
			FlowsCategoryMaster category = categoryRepo.findById(master.getCategoryId()).orElse(null);
			data.put("categoryName", category != null ? category.getCategoryName() : "");
		} else {
			data.put("categoryName", "");
		}
		
		// CITY
		data.put("cityId", master.getCityId());
		if (master.getCityId() != null) {
			FlowsCityMaster city = cityRepo.findById(master.getCityId()).orElse(null);
			data.put("cityName", city != null ? city.getCityName() : "");
		} else {
			data.put("cityName", "");
		}
		
		data.put("bucketId", master.getBucketId());
		if (master.getBucketId() != null) {
			FlowsBucketMaster bucket = bucketRepo.findById(master.getBucketId()).orElse(null);
			data.put("bucketName", bucket != null ? bucket.getBucketName() : "");
		} else {
			data.put("bucketName", "");
		}
		
		data.put("tableType", master.getTableType());
		data.put("fromDate", master.getFromDate());
		data.put("toDate", master.getToDate());
		data.put("versionNo", master.getVersionNo());
		data.put("status", master.getStatus());
		data.put("createdBy", master.getCreatedBy());
		data.put("createdDate", master.getCreatedDate());
		data.put("remarks", master.getRemarks());

		FlowsPayoutResponse response = buildResponse(master);
		data.put("resolutions", response.getResolutions());
		data.put("performances", response.getPerformances());
		data.put("payoutList", response.getPayoutList());
		data.put("footers", response.getFooters());

		return data;
	}
	
	private void validatePending(FlowsPayoutRequest request) {

	    List<FlowsSlabMaster> pending =
	            slabRepo.findPending(
	                    request.getProduct(),
	                    request.getSubProduct(),
	                    request.getCategoryId(),
	                    request.getCityId(),
	                    request.getBucketId(),
	                    request.getFromDate(),
	                    request.getToDate());

	    if (!pending.isEmpty()) {
	        throw new RuntimeException(
	                "Pending request already exists.");
	    }
	}
	
	private FlowsSlabMaster buildMaster(
	        FlowsPayoutRequest request,
	        String userId) {

		FlowsSlabMaster master = new FlowsSlabMaster();

	    master.setProduct(request.getProduct());
	    master.setSubProduct(request.getSubProduct());
	    master.setCategoryId(request.getCategoryId());
		master.setCityId(request.getCityId());
	    master.setBucketId(request.getBucketId());
	    master.setTableType(request.getTableType());
	    master.setFromDate(request.getFromDate());
	    master.setToDate(request.getToDate());
	    master.setRemarks(request.getRemarks());
	    master.setStatus("PENDING");
	    master.setCreatedBy(userId);
	    master.setCreatedDate(LocalDateTime.now());

	    setVersion(master,request);

	    return master;

	}
	
	private void setVersion(
	        FlowsSlabMaster master,
	        FlowsPayoutRequest request) {

		List<FlowsSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    request.getProduct(),
	                    request.getSubProduct(),
	                    request.getCategoryId(),
	                    request.getCityId(),
	                    request.getBucketId(),
	                    request.getFromDate(),
	                    request.getToDate());

	    if(approved.isEmpty()) {
	        master.setVersionNo(1);
	        return;

	    }
		FlowsSlabMaster latest = approved.get(0);

	    master.setParentId(
	            latest.getParentId()==null
	                    ? latest.getId()
	                    : latest.getParentId());

		master.setVersionNo(latest.getVersionNo() + 1);

	}
	
	private void buildDetails(
	        FlowsSlabMaster master,
	        List<FlowsPayoutDetailDTO> list,
	        String userId) {

		for (FlowsPayoutDetailDTO dto : list) {

			FlowsPayoutDetail detail = new FlowsPayoutDetail();

			detail.setMaster(master);
			detail.setResolutionId(dto.getResolutionId());
			detail.setPerformanceId(dto.getPerformanceId());
			detail.setPayoutPercent(dto.getPayoutPercent());
			detail.setCreatedBy(userId);
			detail.setCreatedDate(LocalDateTime.now());

			master.getDetails().add(detail);

	    }

	}
	
	private void buildFooters(FlowsSlabMaster master, List<FlowsPayoutFooterDetailDTO> list, String userId) {

		//Get bucket
		FlowsBucketMaster bucket = bucketRepo.findById(master.getBucketId())
				.orElseThrow(() -> new RuntimeException("Bucket Not Found"));

		//Get footer rule for this bucket
		FooterRule footerRule = footerRuleService.getFooterRule(bucket);

		//Bucket does NOT require footer
		if (!footerRule.isFooterRequired()) {

			if (list != null && !list.isEmpty()) {
				throw new RuntimeException("Footer is not allowed for this bucket.");
			}
			return;
		}

		//Bucket requires footer
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("Footer is required for this bucket.");
		}

		//Build footer details
		for (FlowsPayoutFooterDetailDTO dto : list) {

			if (dto.getFooterType() == null || dto.getFooterType().trim().isEmpty()) {
				throw new RuntimeException("Footer Type is required.");
			}
			if (dto.getFooterText() == null || dto.getFooterText().trim().isEmpty()) {
				throw new RuntimeException("Footer Text is required.");
			}

			// Performance footer
			if ("INCENTIVE".equalsIgnoreCase(dto.getFooterType())) {

				if (!footerRule.isPerformanceFooterRequired()) {
					throw new RuntimeException("Performance footer is not allowed for this bucket.");
				}

				if (dto.getPerformanceId() == null) {
					throw new RuntimeException("Performance is required.");
				}

				if (dto.getFooterValue() == null) {
					throw new RuntimeException("Footer Value is required.");
				}
			}

			//Non-performance footer
			else {
				if (footerRule.isPerformanceFooterRequired()) {
					throw new RuntimeException("Performance footer is required for this bucket.");
				}
			}

			//Create payout footer detail
			FlowsPayoutFooterDetail footer = new FlowsPayoutFooterDetail();

			footer.setMaster(master);
			footer.setSourceFooterId(dto.getSourceFooterId());
			footer.setBucketOrderId(dto.getBucketOrderId());
			footer.setFooterType(dto.getFooterType().trim());
			footer.setFooterText(dto.getFooterText().trim());
			footer.setPerformanceId(dto.getPerformanceId());
			footer.setFooterValue(dto.getFooterValue());
			footer.setCreatedBy(userId);
			footer.setCreatedDate(LocalDateTime.now());
			master.getFooters().add(footer);
		}
	}
	
	private FlowsPayoutResponse buildResponse(
	        FlowsSlabMaster master){

		FlowsPayoutResponse response = new FlowsPayoutResponse();

		response.setResolutions(
		        convertResolution(
		                resolutionRepo
		                        .findByBucketIdAndStatusOrderByOrderId(
		                                master.getBucketId(),
		                                "APPROVED")));

		response.setPerformances(
		        convertPerformance(
		                performanceRepo
		                        .findByBucketIdAndStatusOrderByOrderId(
		                                master.getBucketId(),
		                                "APPROVED")));

		List<FlowsPayoutDetailDTO> dto = new ArrayList<>();

		for (FlowsPayoutDetail d : master.getDetails()) {

			FlowsPayoutDetailDTO cell = new FlowsPayoutDetailDTO();

			cell.setResolutionId(d.getResolutionId());
			cell.setPerformanceId(d.getPerformanceId());
			cell.setPayoutPercent(d.getPayoutPercent());
			dto.add(cell);
		}
		response.setPayoutList(dto);
		
		 // FOOTER DETAILS
	    response.setFooters(
	            convertFooterDetails(
	                    master.getFooters()
	            )
	    );

		return response;

	}
	
	private void inactivatePrevious(FlowsSlabMaster pending) {

		List<FlowsSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    pending.getProduct(),
	                    pending.getSubProduct(),
	                    pending.getCategoryId(),
	                    pending.getCityId(),
	                    pending.getBucketId(),
	                    pending.getFromDate(),
	                    pending.getToDate());

		if (approved.isEmpty()) {
			return;
		}

		FlowsSlabMaster old = approved.get(0);
		old.setStatus("INACTIVE");
		
		slabRepo.save(old);

	}
	
	private List<FlowsResolutionDTO> convertResolution(
	        List<FlowsResolutionMaster> list) {

	    List<FlowsResolutionDTO> dtoList = new ArrayList<>();

	    for (FlowsResolutionMaster r : list) {

	        FlowsResolutionDTO dto = new FlowsResolutionDTO();

	        dto.setId(r.getId());
	        dto.setOrderId(r.getOrderId());
	        dto.setDisplayText(r.getDisplayText());
	        dto.setFromValue(r.getFromValue());
	        dto.setToValue(r.getToValue());

	        dtoList.add(dto);
	    }

	    return dtoList;
	}
	
	private List<FlowsPerformanceDTO> convertPerformance(
	        List<FlowsPerformanceMaster> list) {

	    List<FlowsPerformanceDTO> dtoList = new ArrayList<>();

	    for (FlowsPerformanceMaster p : list) {

	        FlowsPerformanceDTO dto = new FlowsPerformanceDTO();

	        dto.setId(p.getId());
	        dto.setOrderId(p.getOrderId());
	        dto.setDisplayText(p.getDisplayText());
	        dto.setFromValue(p.getFromValue());
	        dto.setToValue(p.getToValue());

	        dtoList.add(dto);
	    }

	    return dtoList;
	}
	
	private List<FlowsPayoutFooterDetailDTO> convertFooterDetails(
	        List<FlowsPayoutFooterDetail> list) {

		List<FlowsPayoutFooterDetailDTO> dtoList = new ArrayList<>();

	    if (list == null || list.isEmpty()) {
	        return dtoList;
	    }

		for (FlowsPayoutFooterDetail f : list) {

			FlowsPayoutFooterDetailDTO dto = new FlowsPayoutFooterDetailDTO();

			dto.setId(f.getId());
			dto.setSourceFooterId(f.getSourceFooterId()); 
			dto.setBucketOrderId(f.getBucketOrderId());
			dto.setFooterType(f.getFooterType());
			dto.setFooterText(f.getFooterText());
			dto.setPerformanceId(f.getPerformanceId());
			dto.setFooterValue(f.getFooterValue());

			dtoList.add(dto);
		}

		return dtoList;
	}
	
}
