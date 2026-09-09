package com.icici.dma.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.HoldCodeDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.model.HoldCodeMaster;
import com.icici.dma.model.HoldCodeMasterTemp;
import com.icici.dma.repository.HoldCodeMasterRepository;
import com.icici.dma.repository.HoldCodeMasterTempRepository;
import com.icici.dma.service.HoldCodeService;

@Service
@Transactional
public class HoldCodeServiceImpl implements HoldCodeService {

	@Autowired
	private HoldCodeMasterRepository holdCodeRepo;

	@Autowired
	private HoldCodeMasterTempRepository holdCodeTempRepo;

	@Override
	public List<HoldCodeDto> getHoldCodeMaker() {

		List<HoldCodeDto> dtoList = new ArrayList<>();

		holdCodeRepo.findAllByStatus(StatusConstant.APPROVE).forEach(e -> dtoList.add(convertMasterToDto(e)));

		holdCodeTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(e -> dtoList.add(convertTempToDto(e)));

		if (dtoList.isEmpty()) {
			throw new ResourceNotFoundException("No Hold Code records found");
		}

		dtoList.sort((a, b) -> {

			Date dateA = a.getActionDate() != null ? a.getActionDate()
					: a.getModifiedDate() != null ? a.getModifiedDate() : a.getCreatedDate();

			Date dateB = b.getActionDate() != null ? b.getActionDate()
					: b.getModifiedDate() != null ? b.getModifiedDate() : b.getCreatedDate();

			if (dateA == null && dateB == null)
				return 0;
			if (dateA == null)
				return 1;
			if (dateB == null)
				return -1;

			return dateB.compareTo(dateA);
		});

		return dtoList;
	}

	@Override
	public void createHoldCodeMaker(HoldCodeDto dto, String username) {

		if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
			throw new IllegalArgumentException("Code is mandatory");
		}

		if (dto.getHoldReason() == null || dto.getHoldReason().trim().isEmpty()) {
			throw new IllegalArgumentException("Hold Reason is mandatory");
		}

		if (holdCodeTempRepo.existsById(dto.getCode())) {

			HoldCodeMasterTemp temp = holdCodeTempRepo.findById(dto.getCode()).get();

			if (StatusConstant.PENDING.equals(temp.getStatus())) {
				throw new IllegalArgumentException("Already waiting for approval");
			}

			throw new IllegalArgumentException("Already Rejected");
		}

		if (holdCodeRepo.existsById(dto.getCode())) {

			throw new IllegalArgumentException("Record Already Approved");
		}

		if (holdCodeTempRepo.existsByCodeAndStatus(dto.getCode(), StatusConstant.PENDING)) {
			throw new IllegalArgumentException("Code already pending for approval");
		}

		HoldCodeMasterTemp temp = convertDtoToTemp(dto);

		temp.setCreatedBy(username);
		temp.setCreatedDate(new Date());
		temp.setStatus(StatusConstant.PENDING);
		
		temp.setActionType(ActionConstant.INSERT);
		temp.setActionDate(new Date());
		temp.setActionUser(username);

		holdCodeTempRepo.save(temp);
	}

	@Override
	public void updateHoldCodeMaker(HoldCodeDto dto, String username) {

		Optional<HoldCodeMasterTemp> tempOpt = holdCodeTempRepo.findById(dto.getCode());

		if (tempOpt.isPresent()) {

			HoldCodeMasterTemp existingTemp = tempOpt.get();

			if (StatusConstant.PENDING.equals(existingTemp.getStatus())) {

				throw new IllegalArgumentException("Record already waiting for approval");
			}

			throw new IllegalArgumentException("Rejected records cannot be edited");
		}

		HoldCodeMaster master = holdCodeRepo.findById(dto.getCode())
				.orElseThrow(() -> new ResourceNotFoundException("Approved record not found"));

		HoldCodeMasterTemp temp = convertMasterToTemp(master);

		if (dto.getHoldReason() != null) {
			temp.setHoldReason(dto.getHoldReason());
		}
		temp.setStatus(StatusConstant.PENDING);

		temp.setCreatedBy(master.getCreatedBy());
		temp.setCreatedDate(master.getCreatedDate());

		temp.setActionType(ActionConstant.UPDATE);
		temp.setActionDate(new Date());
		temp.setActionUser(username);

		temp.setModifiedBy(username);
		temp.setModifiedDate(new Date());

		holdCodeTempRepo.save(temp);
	}

	@Override
	public List<HoldCodeDto> getAllHoldCodeChecker(String user) {

		List<HoldCodeDto> dtoList = new ArrayList<>();

		holdCodeTempRepo.findAllByStatusAndActionUserNot(StatusConstant.PENDING, user)
				.forEach(e -> dtoList.add(convertTempToDto(e)));

		return dtoList;
	}

	@Override
	public List<HoldCodeDto> getHoldCodeByStatus(String statusType) {

		List<HoldCodeDto> dtoList = new ArrayList<>();

		if (StatusConstant.ALL.equalsIgnoreCase(statusType)) {

			// Approved records from master table
			holdCodeRepo.findAllByStatus(StatusConstant.APPROVE).forEach(e -> dtoList.add(convertMasterToDto(e)));

			// Pending + Rejected from temp table
			holdCodeTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(e -> dtoList.add(convertTempToDto(e)));

		} else if (StatusConstant.APPROVE.equalsIgnoreCase(statusType)) {
			holdCodeRepo.findAllByStatus(StatusConstant.APPROVE).forEach(e -> dtoList.add(convertMasterToDto(e)));

		} else if (StatusConstant.PENDING.equalsIgnoreCase(statusType)) {
			holdCodeTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(e -> dtoList.add(convertTempToDto(e)));

		} else if (StatusConstant.REJECTE.equalsIgnoreCase(statusType)) {
			holdCodeTempRepo.findAllByStatus(StatusConstant.REJECTE).forEach(e -> dtoList.add(convertTempToDto(e)));

		} else {
			throw new IllegalArgumentException("Invalid status");
		}

		if (dtoList.isEmpty()) {
			throw new ResourceNotFoundException("No records found for status: " + statusType);
		}
		dtoList.sort((a, b) -> {

			Date dateA = a.getActionDate() != null ? a.getActionDate()
					: a.getModifiedDate() != null ? a.getModifiedDate() : a.getCreatedDate();

			Date dateB = b.getActionDate() != null ? b.getActionDate()
					: b.getModifiedDate() != null ? b.getModifiedDate() : b.getCreatedDate();

			if (dateA == null && dateB == null)
				return 0;
			if (dateA == null)
				return 1;
			if (dateB == null)
				return -1;

			return dateB.compareTo(dateA);
		});

		return dtoList;
	}

	@Override
	public void updateHoldCodeChecker(CheckerDecisionReq requestPayload, String username) {

		List<String> codes = requestPayload.getPrimaryIds();

		List<String> validCodes = codes.stream().filter(s -> s != null && !s.trim().isEmpty()).map(String::trim)
				.collect(Collectors.toList());

		List<String> pendingCodes = holdCodeTempRepo.findPendingCodes(validCodes, StatusConstant.PENDING);

		if (pendingCodes.size() != validCodes.size()) {

			List<String> missing = new ArrayList<>(validCodes);

			missing.removeAll(pendingCodes);

			throw new ResourceNotFoundException("Pending records not found: " + missing);
		}

		List<HoldCodeMasterTemp> tempRecords = holdCodeTempRepo.findAllById(pendingCodes);

		if (StatusConstant.APPROVE.equalsIgnoreCase(requestPayload.getDecision())) {

			List<HoldCodeMaster> approved = tempRecords.stream().map(t -> {

				Optional<HoldCodeMaster> existing = holdCodeRepo.findById(t.getCode());

				HoldCodeMaster m;

				if (existing.isPresent()) {

					m = existing.get();

					m.setModifiedBy(username);
					m.setModifiedDate(new Date());

				} else {

					m = new HoldCodeMaster();

					m.setCreatedBy(t.getCreatedBy());
					m.setCreatedDate(t.getCreatedDate());
				}

				m.setCode(t.getCode());

				m.setHoldReason(t.getHoldReason());

				if (!existing.isPresent()) {

					m.setCreatedBy(t.getCreatedBy());
					m.setCreatedDate(t.getCreatedDate());
				}

				m.setModifiedBy(username);
				m.setModifiedDate(new Date());

				m.setStatus(StatusConstant.APPROVE);

				return m;
			}).collect(Collectors.toList());

			holdCodeRepo.saveAll(approved);

			// Delete from TEMP table
			holdCodeTempRepo.deleteApprovedRecords(pendingCodes);

		} else {

			holdCodeTempRepo.bulkUpdateStatus(pendingCodes, StatusConstant.REJECTE, requestPayload.getRemark(),
					username, StatusConstant.PENDING);
		}
	}

	private HoldCodeMasterTemp convertMasterToTemp(HoldCodeMaster master) {

		HoldCodeMasterTemp temp = new HoldCodeMasterTemp();

		temp.setCode(master.getCode());
		temp.setHoldReason(master.getHoldReason());

		temp.setCreatedBy(master.getCreatedBy());
		temp.setCreatedDate(master.getCreatedDate());

		temp.setModifiedBy(master.getModifiedBy());
		temp.setModifiedDate(master.getModifiedDate());

		return temp;
	}
}