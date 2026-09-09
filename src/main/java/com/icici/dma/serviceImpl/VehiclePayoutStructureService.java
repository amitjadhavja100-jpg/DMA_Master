package com.icici.dma.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.VehicleCvDetailDTO;
import com.icici.dma.dto.VehicleCvPayoutStructureRequest;
import com.icici.dma.dto.VehiclePayoutDetailDTO;
import com.icici.dma.dto.VehiclePayoutStructureRequest;
import com.icici.dma.repository.VehicleCvPayoutStructureRepository;
import com.icici.dma.repository.VehicleTwPayoutStructureRepository;
import com.icici.dma.slabEntity.VehicleCvPayoutStructure;
import com.icici.dma.slabEntity.VehicleTwPayoutStructure;

@Service
public class VehiclePayoutStructureService {

	@Autowired
	private VehicleTwPayoutStructureRepository twRepository;

	@Autowired
	private VehicleCvPayoutStructureRepository cvRepository;

	@Transactional
	public void saveStructure(VehiclePayoutStructureRequest request, String userId) {
		try {
			LocalDate cycleFromDate = null;
			LocalDate cycleToDate = null;

			if (request.getCycleFrom() != null && !request.getCycleFrom().isEmpty() && request.getCycleTo() != null
					&& !request.getCycleTo().isEmpty()) {
				cycleFromDate = LocalDate.parse(request.getCycleFrom());
				cycleToDate = LocalDate.parse(request.getCycleTo());

			}

			saveTwStructure(request, userId, cycleFromDate, cycleToDate);

		} catch (Exception e) {
			throw new RuntimeException("Invalid date format, expected yyyy-MM-dd", e);
		}
	}

	private void saveTwStructure(VehiclePayoutStructureRequest request, String userId, LocalDate cycleFromDate,
			LocalDate cycleToDate) {

		List<VehicleTwPayoutStructure> toSave = new ArrayList<>();

		for (VehiclePayoutDetailDTO detail : request.getDetails()) {
			VehicleTwPayoutStructure entity = new VehicleTwPayoutStructure();
			entity.setChannelPartnerName(detail.getChannelPartnerName());
			entity.setProductType(request.getProductType());
			entity.setStructureType(request.getStructureType());
			entity.setRrTillDec22(toBigDecimal(detail.getRrTillDec22()));
			entity.setRrFromJan23(toBigDecimal(detail.getRrFromJan23()));
			entity.setRrFromAllAug23(toBigDecimal(detail.getRrFromAllAug23()));
			entity.setFromDate(cycleFromDate);
			entity.setToDate(cycleToDate);
			entity.setCreatedBy(userId);
			entity.setCreatedDate(LocalDate.now());
			entity.setStatus("PENDING");
			toSave.add(entity);
		}
		twRepository.saveAll(toSave);
	}

	private BigDecimal toBigDecimal(String value) {
		if (value == null || value.trim().isEmpty())
			return BigDecimal.ZERO;
		return new BigDecimal(value.trim());
	}

	public List<VehicleTwPayoutStructure> getChekcerDataFortwVehicle() {
		// TODO Auto-generated method stub

		String status = "PENDING";
		return twRepository.findByStatus(status);
	}

	@Transactional
	public void updateStatus(List<Long> ids, String structureType, String status, String checkedBy) {

		if ("tw st".equalsIgnoreCase(structureType)) {
			List<VehicleTwPayoutStructure> rows = twRepository.findByIdIn(ids);
			if (rows.isEmpty())
				throw new RuntimeException("No rows found for given ids");
			for (VehicleTwPayoutStructure row : rows) {
				row.setStatus(status);
				row.setChecked_by(checkedBy);
				row.setChecked_date(LocalDate.now());
			}
			twRepository.saveAll(rows);

		} else {
			throw new IllegalArgumentException("Unknown structure type: " + structureType);
		}
	}

	@Transactional
	public void saveCvStructure(VehicleCvPayoutStructureRequest request, String userId) {
		try {
			LocalDate cycleFromDate = null;
			LocalDate cycleToDate = null;

			if (request.getCycleFrom() != null && !request.getCycleFrom().isEmpty() && request.getCycleTo() != null
					&& !request.getCycleTo().isEmpty()) {
				cycleFromDate = LocalDate.parse(request.getCycleFrom());
				cycleToDate = LocalDate.parse(request.getCycleTo());

			}

			List<VehicleCvPayoutStructure> toSave = new ArrayList<>();

			for (VehicleCvDetailDTO detail : request.getDetails()) {
				VehicleCvPayoutStructure entity = new VehicleCvPayoutStructure();

				entity.setProductType(request.getProductType());
				entity.setStructureType(request.getStructureType());
				entity.setRrFromApril22(toBigDecimal(detail.getRrFromApril22()));
				entity.setRrBeforeApril22(toBigDecimal(detail.getRrBeforeApril22()));
				entity.setFromDate(cycleFromDate);
				entity.setToDate(cycleToDate);
				entity.setCreatedBy(userId);
				entity.setCreatedDate(LocalDate.now());
				entity.setStatus("PENDING");
				toSave.add(entity);
			}
			cvRepository.saveAll(toSave);

		} catch (Exception e) {
			throw new RuntimeException("Invalid date format, expected yyyy-MM-dd", e);
		}
	}

	public List<VehicleCvPayoutStructure> getChekcerDataForcvVehicle() {

		String status = "PENDING";
		return cvRepository.findByStatus(status);
	}

	public void updateStatusCv(List<Long> ids, String structureType, String status, String checkedBy) {
		// TODO Auto-generated method stub

		List<VehicleCvPayoutStructure> rows = cvRepository.findByIdIn(ids);
		if (rows.isEmpty())
			throw new RuntimeException("No rows found for given ids");
		for (VehicleCvPayoutStructure row : rows) {
			row.setStatus(status);
			row.setChecked_by(checkedBy);
			row.setChecked_date(LocalDate.now());
		}
		cvRepository.saveAll(rows);

	}

	public void approve(List<Long> ids) {

		for (Long id : ids) {

			VehicleTwPayoutStructure obj = twRepository.findById(id).get();

			obj.setStatus("APPROVED");
			obj.setChecked_by("CHECKER");
			obj.setChecked_date(LocalDate.now());

			twRepository.save(obj);
		}
	}

	public void reject(List<Long> ids) {

		for (Long id : ids) {

			VehicleTwPayoutStructure obj = twRepository.findById(id).get();

			obj.setStatus("REJECTED");
			obj.setChecked_by("CHECKER");
			obj.setChecked_date(LocalDate.now());

			twRepository.save(obj);
		}
	}

	public List<VehicleTwPayoutStructure> getByIds(List<Long> ids) {

		return twRepository.findAllById(ids);

	}

	public void approveCV(List<Long> ids) {

		for (Long id : ids) {

			VehicleCvPayoutStructure obj = cvRepository.findById(id).get();

			obj.setStatus("APPROVED");
			obj.setChecked_by("CHECKER");
			obj.setChecked_date(LocalDate.now());

			cvRepository.save(obj);
		}
	}

	public void rejectCV(List<Long> ids) {

		for (Long id : ids) {

			VehicleCvPayoutStructure obj = cvRepository.findById(id).get();

			obj.setStatus("REJECTED");
			obj.setChecked_by("CHECKER");
			obj.setChecked_date(LocalDate.now());

			cvRepository.save(obj);
		}
	}
}
