package com.icici.dma.serviceImpl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.GSTMaster;
import com.icici.dma.model.GSTMasterTemp;
import com.icici.dma.repository.GSTMasterTempRepository;
import com.icici.dma.repository.GstMasterRepository;
import com.icici.dma.service.GstMasterService;

@Service
public class GstMasterServiceImpl implements GstMasterService {

	private static final Logger logger = LogManager.getLogger(GstMasterServiceImpl.class);

	@Autowired
	public GstMasterRepository gstMasMainRepo;

	@Autowired
	public GSTMasterTempRepository gstMasTempRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

//

	@Override
	public List<GstMasterDto> getGSTMasterMaker() {

		logger.info("Fetching GST Master record for maker");

		try {
			List<GSTMaster> gstMasterMakerList = gstMasMainRepo.findAll();

			if (gstMasterMakerList == null || gstMasterMakerList.size() < 0 || gstMasterMakerList.isEmpty()) {
				logger.warn("No pending record found for maker");
				throw new ResourceNotFoundException("No record found");
			}

			List<GstMasterDto> gstMasterDtoList = new ArrayList<>();

			for (GSTMaster gstMaster : gstMasterMakerList) {

				GstMasterDto GstMasterdto = new GstMasterDto();

				GstMasterdto.setApsCode(gstMaster.getApsCode());
				GstMasterdto.setName(gstMaster.getName());
				GstMasterdto.setState(gstMaster.getState());
				GstMasterdto.setLocation(gstMaster.getLocation());

//				GstMasterdto.setCreatedBy(gstMaster.getCreatedBy());
//				GstMasterdto.setCreatedDate(gstMaster.getCreatedDate());
//				GstMasterdto.setModifiedBy(gstMaster.getModifiedBy());
//				GstMasterdto.setModifiedDate(gstMaster.getModifiedDate());

//				GstMasterdto.setRemarak(gstMaster.getRemarak());
				GstMasterdto.setStatus(gstMaster.getStatus());
//
//				GstMasterdto.setActionDate(gstMaster.getActionDate());
//				GstMasterdto.setActionType(gstMaster.getActionType());
//				GstMasterdto.setActionUser(gstMaster.getActionUser());

//				return d;
				gstMasterDtoList.add(GstMasterdto);
			}

			logger.info("Returning {} pending record to maker", gstMasterDtoList.size());
			return gstMasterDtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Override
	public void createGSTMaster(GstMasterDto GstMasterDto, String username) {

		logger.info("Processing create GST for APS Code : {} ", GstMasterDto.getApsCode());

		String maker = username;
		try {

			if (gstMasMainRepo.existsByApsCode(GstMasterDto.getApsCode())) {
				logger.warn("Record already approved for APS Code : {} ", GstMasterDto.getApsCode());
				throw new IllegalArgumentException("Record Already Approved");
			}

			if (gstMasTempRepo.existsByApsCodeAndStatus(GstMasterDto.getApsCode(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for APS Code : {} ", GstMasterDto.getApsCode());
				throw new IllegalArgumentException("Approval Already Pending");
			}

			GSTMasterTemp gstMasterTemp = this.converDtoToGstMasTemp(GstMasterDto);

			gstMasterTemp.setStatus(StatusConstant.PENDING);
			gstMasterTemp.setCreatedBy(maker);
			gstMasterTemp.setCreatedDate(new Date());

			gstMasterTemp.setActionType(ActionConstant.INSERT);
			gstMasterTemp.setActionDate(new Date());
			gstMasterTemp.setActionUser(maker);

			gstMasTempRepo.save(gstMasterTemp);
			logger.info("Record saved in Temp Table for APS Code : {} ", GstMasterDto.getApsCode());
		} catch (DataAccessException e) {
			logger.error("Database error while saving the GST master Record in Temp Table", e);
			throw e;
		}
	}

	@Override
	public void updateGSTMasterByMaker(GstMasterDto GSTMasterDto, String username) {

		String maker = username;
		try {

			logger.info("Maker {} Request Edit for approval record of ApsCode {}", maker, GSTMasterDto.getApsCode());

			GSTMaster gstMasterMain = gstMasMainRepo.findById(GSTMasterDto.getApsCode()).orElseThrow(() -> {

				logger.warn("Approved record not found for APS Code: {}", GSTMasterDto.getApsCode());

				return new ResourceNotFoundException(
						"Approved record not found for APS Code :" + GSTMasterDto.getApsCode());
			});

			Optional<GSTMasterTemp> optinalOfgstMastemp = gstMasTempRepo
					.findByApsCodeAndStatus(GSTMasterDto.getApsCode(), StatusConstant.PENDING);

			if (optinalOfgstMastemp.isPresent()) {

				logger.warn("AspCode {} already Pending for Approval");

				throw new IllegalArgumentException(
						"AspCode " + GSTMasterDto.getApsCode() + " alredy pending for Approval ");
			}

			// update status in main
			gstMasterMain.setStatus(StatusConstant.PENDING);

			gstMasMainRepo.save(gstMasterMain);

			logger.info("Gst Master Main Record status updated to PENDING for APSCode : {}", GSTMasterDto.getApsCode());

			// update or mapping GST Temp record
			GSTMasterTemp updatedGstMasTemp = this.converDtoToGstMasTemp(GSTMasterDto);

			updatedGstMasTemp.setApsCode(gstMasterMain.getApsCode());

			updatedGstMasTemp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- > Pending

			updatedGstMasTemp.setCreatedBy(gstMasterMain.getCreatedBy()); // 1st main record createdBy
			updatedGstMasTemp.setCreatedDate(gstMasterMain.getCreatedDate()); // 1st main record createdDate

			updatedGstMasTemp.setModifiedBy(maker); // modified by whom
			updatedGstMasTemp.setModifiedDate(new Date()); // current modified date

			updatedGstMasTemp.setActionType(ActionConstant.UPDATE); // now Action taken
			updatedGstMasTemp.setActionUser(maker); // Action taken by whom
			updatedGstMasTemp.setActionDate(new Date()); // Action taken Date

			gstMasTempRepo.save(updatedGstMasTemp);

			logger.info("AspCode {} successfully sent for Approval", GSTMasterDto.getApsCode());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}
	}

	@Override
	public List<GstMasterDto> getAllGSTMasterChecker(String user) {

		try {
			logger.info("Fetching All GST Master record for checker ");
			logger.info("Fetching All GST Master but not created by checker : {}", user.toUpperCase());

			List<GSTMasterTemp> gstMasterList = gstMasTempRepo.findAllByStatusAndCreatedByNot(StatusConstant.PENDING,
					user.toUpperCase());
			if (gstMasterList == null || gstMasterList.size() < 0 || gstMasterList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<GstMasterDto> gstMasterTempPendingList = gstMasterList.stream().map(this::convertGSTMasTempToDto)
					.collect(Collectors.toList());

			logger.info("Returning {} pending record to checker", gstMasterTempPendingList.size());
			return gstMasterTempPendingList;

		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Override
	public void updateGSTMasterByChecker(CheckerDecisionReq requestPayload, String username) {

		logger.info("Processing checker action");

		try {

			// ================= Normal VALIDATION =================
			if (requestPayload == null) {
				throw new IllegalArgumentException("Request payload is null");
			}
			String checker = username;

			String decision = requestPayload.getDecision().toUpperCase();
			String remark = requestPayload.getRemark();

			if (decision == null) {
				throw new IllegalArgumentException("Decision is null");
			}

//	        decision = decision.toUpperCase();

			if (!StatusConstant.APPROVE.equals(decision) && !StatusConstant.REJECTE.equals(decision)) {

				logger.error("Invalid decision received: {}", decision);
				throw new IllegalArgumentException("Invalid decision type. Use A for APPROVE and R for REJECT");
			}

			List<String> ids = requestPayload.getPrimaryIds();

			if (ids == null || ids.isEmpty()) {
				throw new IllegalArgumentException("Primary IDs list is empty");
			}

			// ================= convert Integer and VALIDATE ALL IDS WITH BATCH
			// =================
			List<Integer> intIds = ids.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty())
					.map(Integer::valueOf).collect(Collectors.toList());

			int batchSize = 900;
			List<Integer> pendingIds = new ArrayList<>();

			for (int i = 0; i < intIds.size(); i += batchSize) {

				List<Integer> batch = intIds.subList(i, Math.min(i + batchSize, intIds.size()));

				List<Integer> result = gstMasTempRepo.findPendingIds(batch, StatusConstant.PENDING);

				pendingIds.addAll(result);
			}

//	        List<Integer> pendingIds =
//	                gstMasTempRepo.findPendingIds(intIds, StatusConstant.PENDING);

			if (pendingIds.size() != intIds.size()) {

				List<Integer> missingIds = new ArrayList<>(intIds);
				missingIds.removeAll(pendingIds);

				logger.error("Pending record not found for APS Codes: {}", missingIds);

				throw new ResourceNotFoundException("Pending record not found for APS Codes: " + missingIds);
			}

			// ================= DECIDE STATUS =================
			String status = decision.equals(StatusConstant.APPROVE) ? StatusConstant.APPROVE : StatusConstant.REJECTE;

			int totalUpdated = 0;

			// ================= BATCH OPERATION FOR BULK UPDATE =================
//	        int batchSize = 900;

			for (int i = 0; i < pendingIds.size(); i += batchSize) {

				List<Integer> batch = pendingIds.subList(i, Math.min(i + batchSize, pendingIds.size()));

				int updated = gstMasTempRepo.bulkUpdateStatus(batch, status, remark, StatusConstant.PENDING);

				totalUpdated += updated;
			}

			// ================= =================
			if (StatusConstant.APPROVE.equals(status)) {
				logger.info("All APS Codes approved successfully. Count: {}", totalUpdated);
			} else {
				logger.info("All APS Codes rejected successfully. Count: {}", totalUpdated);
			}

			logger.info("Checker update status successfully");

		} catch (DataAccessException e) {

			logger.error("Database error while updating pending GST records", e);
			throw e;

		}

	}

	@Override
	public List<GstMasterDto> getGSTMasterByStatus(String statusType) {

		logger.info("Fetching  GST Master record for maker");

		List<GstMasterDto> gstMasterDtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching GST Master Approve record ");

//				gstMasMainRepo.findAll().forEach(gstMaster -> {
				gstMasMainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(gstMaster -> {

					GstMasterDto GstMasterdto = new GstMasterDto();

					GstMasterdto.setApsCode(gstMaster.getApsCode());
					GstMasterdto.setName(gstMaster.getName());
					GstMasterdto.setState(gstMaster.getState());
					GstMasterdto.setLocation(gstMaster.getLocation());

					GstMasterdto.setStatus(gstMaster.getStatus());

					gstMasterDtoList.add(GstMasterdto);

				});
				logger.info("Approved GST record Count : {}", gstMasterDtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching GST Master Pending record ");

				gstMasTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(gstMasterTemp -> {

					GstMasterDto GstMasterdto = new GstMasterDto();

					GstMasterdto.setApsCode(gstMasterTemp.getApsCode());
					GstMasterdto.setName(gstMasterTemp.getName());
					GstMasterdto.setState(gstMasterTemp.getState());
					GstMasterdto.setLocation(gstMasterTemp.getLocation());

					GstMasterdto.setStatus(gstMasterTemp.getStatus());

					gstMasterDtoList.add(GstMasterdto);

				});
				logger.info("Peinding GST record Count : {}", gstMasterDtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching GST Master Reject record ");
				gstMasTempRepo.findAllByStatus(StatusConstant.REJECTE).forEach(gstMasterTemp -> {

					GstMasterDto GstMasterdto = new GstMasterDto();

					GstMasterdto.setApsCode(gstMasterTemp.getApsCode());
					GstMasterdto.setName(gstMasterTemp.getName());
					GstMasterdto.setState(gstMasterTemp.getState());
					GstMasterdto.setLocation(gstMasterTemp.getLocation());

					GstMasterdto.setStatus(gstMasterTemp.getStatus());

					gstMasterDtoList.add(GstMasterdto);

				});
				logger.info("Reject GST record Count : {}", gstMasterDtoList.size());
				break;

			case "All":

				logger.info("Fetching All GST Master Approve record ");

				String sql = "SELECT * " + "  FROM (	SELECT APS_CODE, NAME,STATE,LOCATION, " + "			status, "
						+ "			GREATEST(NVL(created_date, DATE '1900-01-01'), "
						+ "					 NVL(modified_date, DATE '1900-01-01')) AS sort_date "
						+ "			FROM tm_vhl_gst_mst " + "			WHERE status = 'A' " + "		UNION ALL "
						+ "			SELECT APS_CODE,NAME, STATE, LOCATION,  " + "      status, "
						+ "			GREATEST( " + "           NVL(action_date, DATE '1900-01-01'), "
						+ "           NVL(created_date, DATE '1900-01-01'), "
						+ "					 NVL(modified_date, DATE '1900-01-01')) AS sort_date "
						+ "			FROM tm_vhl_gst_mst_temp " + "      WHERE status = 'P' " + "      ) "
						+ "ORDER BY sort_date DESC";

				List<GstMasterDto> listAllGSTDTO = jdbcTemplate.query(con -> {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setFetchSize(500);
					return ps;
				}, (rs, rowNum) -> {

					GstMasterDto GstMasterdto = new GstMasterDto();

					GstMasterdto.setApsCode(rs.getInt("APS_CODE"));
					GstMasterdto.setName(rs.getString("NAME"));
					GstMasterdto.setState(rs.getString("STATE"));
					GstMasterdto.setLocation(rs.getString("LOCATION"));

					GstMasterdto.setStatus(rs.getString("STATUS"));
					return GstMasterdto;
				});
				gstMasterDtoList.addAll(listAllGSTDTO);

				logger.info("All GST record Count : {}", gstMasterDtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (gstMasterDtoList == null || gstMasterDtoList.size() < 0 || gstMasterDtoList.isEmpty()) {
				logger.warn("No Gst Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All GST Master record to maker", gstMasterDtoList.size());
			return gstMasterDtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;

		}

	}

}
