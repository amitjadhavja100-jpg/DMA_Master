package com.icici.dma.slabServiceImpl.osp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.OspPayoutRequest;
import com.icici.dma.dto.OspRowData;
import com.icici.dma.dto.osp.OspCompareResponse;
import com.icici.dma.dto.osp.OspPayoutCompareDto;
import com.icici.dma.dto.osp.OspPayoutDetailDto;
import com.icici.dma.dto.osp.OspPendingResponse;
import com.icici.dma.helper.OspConstants;
import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;
import com.icici.dma.slabEntity.osp.OspPayoutDetail;
import com.icici.dma.slabEntity.osp.OspPayoutMaster;
import com.icici.dma.slabRepository.osp.OspConfigDpdMasterRepository;
import com.icici.dma.slabRepository.osp.OspPayoutDetailRepo;
import com.icici.dma.slabRepository.osp.OspPayoutMasterRepo;
import com.icici.dma.slabService.osp.OspPayoutService;

@Service
public class OspPayoutServiceImpl
        implements OspPayoutService{
	
	@Autowired
	private OspPayoutMasterRepo masterRepo;

	@Autowired
	private OspPayoutDetailRepo detailRepo;
	
	@Autowired
	private OspConfigDpdMasterRepository dpdRepository;
	
	@Override
	public OspPayoutMaster fetch(String product, String subProduct, Long dpdId, String fromDate, String toDate) {

		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);

		  OspConfigDpdMaster dpd =
		            dpdRepository.findById(dpdId)
		                    .orElseThrow(() ->
		                        new RuntimeException("DPD not found"));

		
		return masterRepo.findTopByProductAndSubProductAndDpdAndFromDateAndToDateAndStatusOrderByVersionDesc(product,
				subProduct, dpd, from, to, "APPROVED").orElse(null);

	}

	@Override
	@Transactional
	public OspPayoutMaster save(OspPayoutRequest request, String user) {

		OspPayoutMaster master = new OspPayoutMaster();

		master.setProduct(request.getProduct());
		master.setSubProduct(request.getSubProduct());

		OspConfigDpdMaster dpd = dpdRepository
				.findById(request.getDpdId())
		        .orElseThrow(() ->
		                new RuntimeException("DPD not found"));

		master.setDpd(dpd);
		//master.setDpdId(request.getDpdId());
		master.setStatus("PENDING");
		master.setCreatedBy(user);
		master.setCreatedDate(new Date());
		
		System.out.println("Created Date = " + master.getCreatedDate());

		LocalDate from = LocalDate.parse(request.getFromDate());
		LocalDate to = LocalDate.parse(request.getToDate());

		master.setFromDate(from);
		master.setToDate(to);
		Optional<OspPayoutMaster> pendingRecord = masterRepo
				.findByProductAndSubProductAndDpdAndFromDateAndToDateAndStatus(
						request.getProduct(),
						request.getSubProduct(),
						dpd,
						from,
						to,
						"PENDING");
		if (pendingRecord.isPresent()) {
			throw new RuntimeException(
					"A Pending Record Already Exists For Selected Product, Sub Product, DPD And Date Range.");
		}

		Optional<OspPayoutMaster> latest = masterRepo
				.findTopByProductAndSubProductAndDpdAndFromDateAndToDateAndStatusOrderByVersionDesc(
						request.getProduct(), 
						request.getSubProduct(), 
						dpd, 
						from, 
						to, 
						"APPROVED");

		if (latest.isPresent()) {

			master.setParentMasterId(latest.get().getId());
			master.setVersion(latest.get().getVersion() + 1);

		} else {

			master.setParentMasterId(null);
			master.setVersion(1);

		}
		
		master = masterRepo.save(master);

		int order = 1;

		for (OspRowData row : request.getRows()) {

			OspPayoutDetail detail = new OspPayoutDetail();

			detail.setMaster(master);
			detail.setDpd(master.getDpd());
//			detail.setDpdId(master.getDpdId());
			detail.setFromAmount(row.getFromAmount());
			detail.setToAmount(row.getToAmount());
			detail.setIncentivePercent(row.getIncentive());
			detail.setOrderNo(order++);
			
			detailRepo.save(detail);
		}
		return master;

	}

	@Override
	@Transactional
	public void approve(Long id, String user) {

		OspPayoutMaster pending = masterRepo.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));

		if (user.equalsIgnoreCase(pending.getCreatedBy())) {
			throw new RuntimeException("Maker and Checker cannot be same");
		}

		if (pending.getParentMasterId() != null) {
			OspPayoutMaster old = masterRepo.findById(pending.getParentMasterId()).orElse(null);

			if (old != null) {
				old.setStatus("INACTIVE");
				masterRepo.save(old);
			}
		}
		pending.setStatus("APPROVED");
		pending.setApprovedBy(user);
		pending.setApprovedDate(LocalDateTime.now());

		masterRepo.save(pending);

	}

	@Override
	@Transactional
	public void reject(Long id, String user, String remarks) {

		OspPayoutMaster pending = masterRepo.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));

		if (user.equalsIgnoreCase(pending.getCreatedBy())) {
			throw new RuntimeException("Maker and Checker cannot be same");
		}
		pending.setStatus("REJECTED");
		pending.setApprovedBy(user);
		pending.setApprovedDate(LocalDateTime.now());
		pending.setRemarks(remarks);

		masterRepo.save(pending);

	}

	@Override
	public List<OspPendingResponse> pending(String user) {

	    List<OspPayoutMaster> masters =
	            masterRepo.findByStatusAndCreatedByNotOrderByCreatedDateDesc(
	                    "PENDING",
	                    user);

	    List<OspPendingResponse> response = new ArrayList<>();

	    for (OspPayoutMaster master : masters) {

	        OspPendingResponse dto = new OspPendingResponse();

	        dto.setId(master.getId());
	        dto.setProduct(master.getProduct());
	        dto.setSubProduct(master.getSubProduct());

	        if (master.getDpd() != null) {

	            dto.setDpdId(master.getDpd().getId());
	            dto.setDpdName(master.getDpd().getDpdName());

	        }
	        dto.setFromDate(master.getFromDate());
	        dto.setToDate(master.getToDate());

	        dto.setStatus(master.getStatus());
	        dto.setCreatedBy(master.getCreatedBy());

	        response.add(dto);
	    }

	    return response;
	}

	@Override
	public List<OspPayoutMaster> history() {

		return masterRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

	}

	@Override
	public List<Map<String, Object>> compare(Long id) {

		OspPayoutMaster pending = masterRepo.findById(id).orElse(null);
		List<Map<String, Object>> list = new ArrayList<>();

		if (pending == null) {
			return list;
		}
		OspPayoutMaster approved = null;

		if (pending.getParentMasterId() != null) {
			approved = masterRepo.findById(pending.getParentMasterId()) 
					.orElse(null);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("top", pending);
		map.put("bottom", approved);
		list.add(map);

		return list;

	}

	@Override
	public OspCompareResponse makerCompare(Long id) {

	    OspPayoutMaster current =
	            masterRepo.findById(id).orElse(null);

	    OspPayoutMaster previous = null;

	    if (current != null && current.getParentMasterId() != null) {
	        previous = masterRepo.findById(current.getParentMasterId())
	                .orElse(null);
	    }

	    OspCompareResponse response = new OspCompareResponse();

	    response.setTop(convertToDto(current));
	    response.setBottom(convertToDto(previous));

	    return response;
	}
	
	
	@Override
	public OspCompareResponse checkerCompare(Long id){

	    return makerCompare(id);

	}
	
	@Override
	public Long latestApprovedId(
	        String product,
	        String subProduct,
	        Long dpdId,
	        String fromDate,
	        String toDate) {

	    LocalDate from = LocalDate.parse(fromDate);
	    LocalDate to = LocalDate.parse(toDate);
	    
	    OspConfigDpdMaster dpd =
	            dpdRepository.findById(dpdId)
	                .orElseThrow(() -> new RuntimeException("DPD not found"));

	    return masterRepo
	            .findTopByProductAndSubProductAndDpdAndFromDateAndToDateAndStatusOrderByVersionDesc(
	                    product,
	                    subProduct,
	                    dpd,
	                    from,
	                    to,
	                    "APPROVED")
	            .map(OspPayoutMaster::getId)
	            .orElse(null);
	}
	
	private OspPayoutCompareDto convertToDto(OspPayoutMaster master) {

	    if (master == null) {
	        return null;
	    }

	    OspPayoutCompareDto dto = new OspPayoutCompareDto();

	    dto.setId(master.getId());
	    dto.setParentMasterId(master.getParentMasterId());

	    dto.setProduct(master.getProduct());
	    dto.setSubProduct(master.getSubProduct());

	    if (master.getDpd() != null) {
	        dto.setDpdId(master.getDpd().getId());
	        dto.setDpdName(master.getDpd().getDpdName());
	    }

	    dto.setStatus(master.getStatus());

	    dto.setFromDate(master.getFromDate());
	    dto.setToDate(master.getToDate());

	    dto.setVersion(master.getVersion());

	    dto.setCreatedBy(master.getCreatedBy());
	    dto.setApprovedBy(master.getApprovedBy());

	    dto.setCreatedDate(master.getCreatedDate());
	    dto.setApprovedDate(master.getApprovedDate());

	    dto.setRemarks(master.getRemarks());

	    List<OspPayoutDetailDto> detailDtos = new ArrayList<>();

	    if (master.getDetails() != null) {

	        for (OspPayoutDetail detail : master.getDetails()) {

	            OspPayoutDetailDto detailDto = new OspPayoutDetailDto();

	            detailDto.setId(detail.getId());

	            if (detail.getDpd() != null) {
	                detailDto.setDpdId(detail.getDpd().getId());
	                detailDto.setDpdName(detail.getDpd().getDpdName());
	            }

	            detailDto.setFromAmount(detail.getFromAmount());
	            detailDto.setToAmount(detail.getToAmount());
	            detailDto.setIsMax(
	            	    detail.getToAmount() != null &&
	            	    detail.getToAmount().compareTo(OspConstants.MAX_TO_AMOUNT) == 0
	            	);
	            detailDto.setIncentivePercent(detail.getIncentivePercent());
	            detailDto.setOrderNo(detail.getOrderNo());

	            detailDtos.add(detailDto);
	        }
	    }

	    dto.setDetails(detailDtos);

	    return dto;
	}
	

}
