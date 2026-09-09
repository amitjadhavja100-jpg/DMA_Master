package com.icici.dma.slabServiceImpl.ccrValuation;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.slabDto.ccrValuation.ValuationMasterDTO;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutDetailDTO;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutRequest;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutResponse;
import com.icici.dma.slabDto.ccrValuation.ValuationResponseDetailDTO;
import com.icici.dma.slabEntity.ccrValuation.ValuationMaster;
import com.icici.dma.slabEntity.ccrValuation.ValuationPayoutDetail;
import com.icici.dma.slabEntity.ccrValuation.ValuationSlabMaster;
import com.icici.dma.slabRepository.ccrValuation.ValuationMasterRepository;
import com.icici.dma.slabRepository.ccrValuation.ValuationSlabMasterRepository;
import com.icici.dma.slabService.ccrValuation.ValuationMasterService;
import com.icici.dma.slabService.ccrValuation.ValuationPayoutService;

@Service
@Transactional
public class ValuationPayoutServiceImpl implements ValuationPayoutService {

    @Autowired
    private ValuationMasterRepository valuationMasterRepo;
    
    @Autowired
    private ValuationMasterService valuationMasterService;

    @Autowired
    private ValuationSlabMasterRepository slabRepo;
    
    @Override
    @Transactional(readOnly = true)
    public List<ValuationMasterDTO> getValuations() {

        return valuationMasterService.getActive();
    }
    
    @Override
    @Transactional
	public String save(ValuationPayoutRequest request, String userId) {

        validateRequest(request);
        validatePending(request);

		ValuationSlabMaster master = buildMaster(request, userId);

        buildDetails(
                master,
                request.getDetails(),
                userId
        );
        slabRepo.save(master);
        return "Saved Successfully";
    }
    
	private void validateRequest(ValuationPayoutRequest request) {

		if (request.getFromDate() == null) {
			throw new RuntimeException("From Date is required");
		}

		if (request.getToDate() == null) {
			throw new RuntimeException("To Date is required");
		}

		if (request.getFromDate().isAfter(request.getToDate())) {
			throw new RuntimeException("From Date cannot be greater than To Date");
		}

		if (request.getDetails() == null || request.getDetails().isEmpty()) {
			throw new RuntimeException("Valuation details are required");
		}
    }
	
	private void validatePending(ValuationPayoutRequest request) {

	    List<ValuationSlabMaster> pending =
	            slabRepo.findPending(
	                    request.getProduct(),
	                    request.getSubProduct(),
	                    request.getFromDate(),
	                    request.getToDate()
	            );

		if (!pending.isEmpty()) {
			throw new RuntimeException("Pending request already exists.");
		}
	}
	
	private ValuationSlabMaster buildMaster(ValuationPayoutRequest request, String userId) {

		ValuationSlabMaster master = new ValuationSlabMaster();

		master.setProduct(request.getProduct());
		master.setSubProduct(request.getSubProduct());
		master.setFromDate(request.getFromDate());
		master.setToDate(request.getToDate());
		master.setRemarks(request.getRemarks());

		master.setStatus("PENDING");
		master.setCreatedBy(userId);
		master.setCreatedDate(LocalDateTime.now());

		setVersion(master, request);

		return master;
	}
	
	private void setVersion(ValuationSlabMaster master, ValuationPayoutRequest request) {

	    List<ValuationSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    request.getProduct(),
	                    request.getSubProduct(),
	                    request.getFromDate(),
	                    request.getToDate()
	            );

	    if (approved.isEmpty()) {
	        master.setVersionNo(1);
	        return;
	    }

		ValuationSlabMaster latest = approved.get(0);
	    master.setParentId(
	            latest.getParentId() == null
	                    ? latest.getId()
	                    : latest.getParentId()
	    );
		master.setVersionNo(latest.getVersionNo() + 1);
	}
    
	
	private void buildDetails(
	        ValuationSlabMaster master,
	        List<ValuationPayoutDetailDTO> list,
	        String userId) {

		for (ValuationPayoutDetailDTO dto : list) {

			if (dto.getRateUnderGst() == null) {
				throw new RuntimeException("Rate under GST is required");
			}

	        ValuationMaster valuationMaster =
	                valuationMasterRepo
	                    .findById(
	                        dto.getValuationMasterId()
	                    )
	                    .orElseThrow(
	                        () -> new RuntimeException(
	                            "Valuation master not found"
	                        )
	                    );

			ValuationPayoutDetail detail = new ValuationPayoutDetail();

			detail.setMaster(master);
			detail.setValuationMaster(valuationMaster);
			detail.setRateUnderGst(dto.getRateUnderGst());
			detail.setCreatedBy(userId);
			detail.setCreatedDate(LocalDateTime.now());

			master.getDetails().add(detail);
		}
	}
	
	@Override
	public ValuationPayoutResponse fetch(
	        String product,
	        String subProduct,
	        LocalDate fromDate,
	        LocalDate toDate) {

	    List<ValuationSlabMaster> list =
	            slabRepo.findLatestApproved(
	                    product,
	                    subProduct,
	                    fromDate,
	                    toDate
	            );

	    if (list.isEmpty()) {
	        return null;
	    }

	    return buildResponse(list.get(0));
	}
	
	private ValuationPayoutResponse buildResponse(ValuationSlabMaster master) {

		ValuationPayoutResponse response = new ValuationPayoutResponse();

		response.setSlabId(master.getId());
		response.setProduct(master.getProduct());
		response.setSubProduct(master.getSubProduct());

		response.setFromDate(master.getFromDate());
		response.setToDate(master.getToDate());

		response.setVersionNo(master.getVersionNo());
		response.setStatus(master.getStatus());

		List<ValuationResponseDetailDTO> details = new ArrayList<>();

		for (ValuationPayoutDetail detail : master.getDetails()) {

			ValuationResponseDetailDTO dto = new ValuationResponseDetailDTO();
			ValuationMaster vm = detail.getValuationMaster();

			dto.setValuationMasterId(vm.getId());
			dto.setIboxId(vm.getIboxId());
			dto.setValuerName(vm.getValuerName());
			dto.setProductSubType(vm.getProductSubType());
			dto.setRateUnderGst(detail.getRateUnderGst());

			details.add(dto);
		}
		response.setDetails(details);
		return response;
	}
    
	
	@Override
	public List<ValuationSlabMaster> getMakerList() {
		return slabRepo.findByStatusOrderByCreatedDateDesc("PENDING");
	}
	
	@Override
	public List<ValuationSlabMaster> getCheckerList(String userId) {
		
		return slabRepo.findByStatusAndCreatedByNotOrderByCreatedDateDesc(
				"PENDING", 
				userId);
	}
	
	@Override
	@Transactional
	public String approve(
	        Long id,
	        String checkerId) {

	    ValuationSlabMaster pending =
	            slabRepo.findById(id)
	                .orElseThrow(
	                    () -> new RuntimeException(
	                        "Record not found"
	                    )
	                );

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
	
	private void inactivatePrevious(ValuationSlabMaster pending) {

	    List<ValuationSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    pending.getProduct(),
	                    pending.getSubProduct(),
	                    pending.getFromDate(),
	                    pending.getToDate()
	            );

	    if (approved.isEmpty()) {
	        return;
	    }

		ValuationSlabMaster old = approved.get(0);
		old.setStatus("INACTIVE");

		slabRepo.save(old);
	}
	
	@Override
	@Transactional
	public String reject(
	        Long id,
	        String checkerId,
	        String remarks) {

	    ValuationSlabMaster pending =
	            slabRepo.findById(id)
	                .orElseThrow(
	                    () -> new RuntimeException(
	                        "Record not found"
	                    )
	                );

		if (checkerId.equals(pending.getCreatedBy())) {
			throw new RuntimeException("Maker cannot reject own record");
		}

		pending.setStatus("REJECTED");
		pending.setApprovedBy(checkerId);
		pending.setApprovedDate(LocalDateTime.now());
		pending.setRemarks(remarks);

		slabRepo.save(pending);

		return "Rejected Successfully";
	}
	
	@Override
	public Map<String, Object> compare(Long id, String mode) {

		Map<String, Object> map = new HashMap<>();

	    ValuationSlabMaster record =
	            slabRepo.findById(id)
	                .orElseThrow(
	                    () -> new RuntimeException(
	                        "Record not found"
	                    )
	                );

	    if ("checker".equalsIgnoreCase(mode)) {

			if (!"PENDING".equalsIgnoreCase(record.getStatus())) {
				throw new RuntimeException("Selected record is not pending");
			}

	        List<ValuationSlabMaster> approved =
	                slabRepo.findLatestApproved(
	                    record.getProduct(),
	                    record.getSubProduct(),
	                    record.getFromDate(),
	                    record.getToDate()
	                );

			map.put("top", buildResponse(record));
			map.put("bottom", approved.isEmpty() ? null : buildResponse(approved.get(0)));

			return map;
		}

		if ("maker".equalsIgnoreCase(mode)) {

			if (!"APPROVED".equalsIgnoreCase(record.getStatus())) {
				throw new RuntimeException("Selected record is not an approved record");
			}

	        List<ValuationSlabMaster> previousList =
	                slabRepo.findPreviousApproved(
	                    record.getProduct(),
	                    record.getSubProduct(),
	                    record.getFromDate(),
	                    record.getToDate(),
	                    record.getVersionNo()
	                );

			// TOP = Current Approved
			map.put("top", buildResponse(record));

			// BOTTOM = Previous Approved
			if (previousList.isEmpty()) {

				map.put("bottom", null);

			} else {

				ValuationSlabMaster previous = previousList.get(0);

				map.put("bottom", buildResponse(previous));
			}

			return map;
		}
		throw new RuntimeException("Invalid compare mode: " + mode);
	}
	
	@Override
	public Long latestApprovedId(
	        String product,
	        String subProduct,
	        LocalDate fromDate,
	        LocalDate toDate) {

	    List<ValuationSlabMaster> approved =
	            slabRepo.findLatestApproved(
	                    product,
	                    subProduct,
	                    fromDate,
	                    toDate
	            );

	    if (approved.isEmpty()) {
	        return null;
	    }

	    return approved.get(0).getId();
	}
}