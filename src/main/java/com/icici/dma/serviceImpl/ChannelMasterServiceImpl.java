package com.icici.dma.serviceImpl;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.ChannelMasterDto;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.ChannelMaster;
import com.icici.dma.model.ChannelMasterTemp;
import com.icici.dma.repository.ChannelMasterRepository;
import com.icici.dma.repository.ChannnelMasterTempRepository;
import com.icici.dma.service.ChannelMasterService;

@Service
public class ChannelMasterServiceImpl implements ChannelMasterService {

	private static final Logger logger = LogManager.getLogger(ChannelMasterServiceImpl.class);

	@Autowired
	private ChannelMasterRepository channelMasMainRepo;

	@Autowired
	private ChannnelMasterTempRepository channelMasTempRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ChannelMasterDto> getAllChannelMstaerMaker() {

		logger.info("Fetching All channel Master record for maker");

		try {
			List<ChannelMaster> channelMasterList = channelMasMainRepo.findAll();

			if (channelMasterList == null || channelMasterList.size() < 0 || channelMasterList.isEmpty()) {
				logger.warn("No pending record found for maker");
				throw new ResourceNotFoundException("No record found");
			}

			List<ChannelMasterDto> ChannelMasterDtoList = new ArrayList<>();

			for (ChannelMaster e : channelMasterList) {

				ChannelMasterDto d = new ChannelMasterDto();

				d.setApsCode(e.getApsCode());
				d.setiBoxId(e.getiBoxId());
				d.setSupplierId(e.getSupplierId());
				d.setDates(e.getDates());
				d.setSuspended(e.getSuspended());

				d.setNameOfChannel(e.getNameOfChannel());
				d.setTypeOfDsa(e.getTypeOfDsa());
				d.setPanNo(e.getPanNo());
				d.setYy(e.getYy());

				d.setSupplierId1(e.getSupplierId1());
				d.setSupplierId2(e.getSupplierId2());
				d.setSupplierId3(e.getSupplierId3());
				d.setSupplierId4(e.getSupplierId4());
				d.setSupplierId5(e.getSupplierId5());
				d.setSupplierId6(e.getSupplierId6());
				d.setSupplierId7(e.getSupplierId7());
				d.setSupplierId8(e.getSupplierId8());
				d.setSupplierId9(e.getSupplierId9());
				d.setSupplierId10(e.getSupplierId10());
				d.setSupplierId11(e.getSupplierId11());
				d.setSupplierId12(e.getSupplierId12());
//				d.setSupplierId13(e.getSupplierId13());

				d.setRemark(e.getRemark());
				d.setLocation(e.getLocation());
				d.setMisState(e.getMisState());
				d.setcState(e.getcState());
				d.setEdState(e.getEdState());
				d.setEdZone(e.getEdZone());

				d.setSourcing(e.getSourcing());
				d.setSourcing1(e.getSourcing1());

				d.setManufactuName(e.getManufactuName());
				d.setNewManufactuName(e.getNewManufactuName());
				d.setOldIBoxId(e.getOldIBoxId());

				d.setRcLimit(e.getRcLimit());
				d.setSapCode(e.getSapCode());

				d.setAccountNo(e.getAccountNo());
				d.setIfscCode(e.getIfscCode());
				d.setBankName(e.getBankName());
				d.setiBankYesNonIBankNo(e.getiBankYesNonIBankNo());

//				d.setRemarksBk(e.getRemarksBk());
				d.setCreatedBy(e.getCreatedBy());
				d.setCreatedDate(e.getCreatedDate());
				d.setModifiedBy(e.getModifiedBy());
				d.setModifiedDate(e.getModifiedDate());
//				d.setRemarksBkk(e.getRemarksBkk());

				d.setStatus(e.getStatus());

				ChannelMasterDtoList.add(d);
			}

			logger.info("Returning {} pending record to maker", ChannelMasterDtoList.size());
			return ChannelMasterDtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Override
	public void updateChannelMasterByMaker(ChannelMasterDto channelMasterDto, String username) {

		String maker = username;
		try {
			logger.info("Maker {} Request Edit for approval record of ApsCode {}", maker,
					channelMasterDto.getApsCode());

			ChannelMaster channelMasterMain = channelMasMainRepo.findById(channelMasterDto.getApsCode())
					.orElseThrow(() -> {

						logger.warn("Approved record not found for APS Code: {}", channelMasterDto.getApsCode());

						return new ResourceNotFoundException(
								"Approved record not found for APS Code :" + channelMasterDto.getApsCode());
					});

			Optional<ChannelMasterTemp> optChannelMaster = channelMasTempRepo
					.findByApsCodeAndStatus(channelMasterDto.getApsCode(), StatusConstant.PENDING);
//			gstMasTempRepo.findById(null)

			if (optChannelMaster.isPresent()) {

				logger.warn("AspCode {} already Pending for Approval");

				throw new IllegalArgumentException(
						"AspCode " + channelMasterDto.getApsCode() + " alredy pending for Approval ");
			}

			// update status in main
			channelMasterMain.setStatus(StatusConstant.PENDING);

			channelMasMainRepo.save(channelMasterMain);

			logger.info("channel Master Main Record status updated to PENDING for APSCode : {}",
					channelMasterDto.getApsCode());

//			GSTMasterTemp gstMasterTemp = gstMasTempRepo.findById(GSTMasterDto.getApsCode()).get();

			// update or mapping channel Temp record
			ChannelMasterTemp updatedChannelMasTemp = this.convertDtoToChannelMasTemp(channelMasterDto);

			updatedChannelMasTemp.setApsCode(channelMasterMain.getApsCode());

			updatedChannelMasTemp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- >
																		// Pending

			updatedChannelMasTemp.setCreatedBy(channelMasterMain.getCreatedBy()); // 1st main record createdBy
			updatedChannelMasTemp.setCreatedDate(channelMasterMain.getCreatedDate()); // 1st main record createdDate

			updatedChannelMasTemp.setModifiedBy(maker); // modified by whom
			updatedChannelMasTemp.setModifiedDate(new Date()); // current modified date

			updatedChannelMasTemp.setActionType(ActionConstant.UPDATE); // now Action taken
//			updatedGstMasTemp.setActionUser(maker); // Action taken by whom
//			updatedGstMasTemp.setActionDate(gstMasterMain.getActionDate()); // Action taken Date

			updatedChannelMasTemp.setActionUser(maker); // Action taken by whom
			updatedChannelMasTemp.setActionDate(new Date()); // Action taken Date

			channelMasTempRepo.save(updatedChannelMasTemp);

			logger.info("AspCode {} successfully sent for Approval", channelMasterDto.getApsCode());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}

	}

	@Override
	public void createChannelMaster(ChannelMasterDto channelMasterDto, String username) {

		try {
			logger.info("Processing create channel for APS Code : {} ", channelMasterDto.getApsCode());

			String maker = username;

			if (channelMasMainRepo.existsByApsCode(channelMasterDto.getApsCode())) {
				logger.warn("Record already approved for APS Code : {} ", channelMasterDto.getApsCode());
				throw new IllegalArgumentException("Record Already Approved");
			}

			if (channelMasTempRepo.existsByApsCodeAndStatus(channelMasterDto.getApsCode(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for APS Code : {} ", channelMasterDto.getApsCode());
				throw new IllegalArgumentException("Approval Already Pending");
			}

			ChannelMasterTemp channelMasTemp = this.convertDtoToChannelMasTemp(channelMasterDto);

			channelMasTemp.setStatus(StatusConstant.PENDING);
			channelMasTemp.setCreatedBy(maker);
			channelMasTemp.setCreatedDate(new Date());

			channelMasTemp.setActionType(ActionConstant.INSERT);
			channelMasTemp.setActionDate(new Date());
			channelMasTemp.setActionUser(maker);

			channelMasTempRepo.save(channelMasTemp);
			logger.info("Record saved in Temp Table for APS Code : {} ", channelMasterDto.getApsCode());
		} catch (DataAccessException e) {

			logger.error("Database error while saving the channel master Record in Temp Table", e);
			throw e;
		}
	}

	@Override
	public List<ChannelMasterDto> getAllChannelMasterChecker(String username) {

		logger.info("Fetching All Channel Master record for checker");

		try {
			List<ChannelMasterTemp> channelMastertemp = channelMasTempRepo
					.findAllByStatusAndCreatedByNot(StatusConstant.PENDING, username);

			if (channelMastertemp == null || channelMastertemp.size() < 0 || channelMastertemp.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<ChannelMasterDto> chnannelMasterTempPendingList = channelMastertemp.stream()
					.map(this::convertChannelMasterTempToDto).collect(Collectors.toList());

			logger.info("Returning {} pending record to checker", chnannelMasterTempPendingList.size());
			return chnannelMasterTempPendingList;

		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Override
	public void updateChannelMasterByChecker(CheckerDecisionReq requestPayload, String username) {

		// Convert decision to uppercase

		logger.info(" processing checker action");

		try {
			// Convert decision to uppercase
			String decision = requestPayload.getDecision().toUpperCase();
			String remark = requestPayload.getRemark();

			String checker = username;
			// Validate decision type
			if (!StatusConstant.APPROVE.equals(decision) && !StatusConstant.REJECTE.equals(decision)) {

				logger.error("Invalid decision received: {}", decision);
				throw new IllegalArgumentException("Invalid decision type. Use U for APPROVE and R for REJECT");
			}

			// Process each APS Code
			for (String primarykeys : requestPayload.getPrimaryIds()) {

				Integer apsCode = Integer.valueOf(primarykeys.trim());

				logger.info("Processing APS Code: {}", apsCode);

				// Fetch only PENDING record from TEMP
				ChannelMasterTemp channelMasterTemp = channelMasTempRepo
						.findByApsCodeAndStatus(apsCode, StatusConstant.PENDING).orElseThrow(() -> {
							logger.error("Pending record not found for APS: {}", apsCode);
							return new ResourceNotFoundException("Pending record not found for APS: " + apsCode);
						});

				// Update status based on decision
				if (StatusConstant.APPROVE.equals(decision)) {

					channelMasterTemp.setStatus(StatusConstant.APPROVE);
//					channelMasterTemp.setActionUser(checker);
//					channelMasterTemp.setActionDate(new Date());
					channelMasterTemp.setRemarkByChk(remark);

					logger.info("APS {} approved successfully ", apsCode);

				} else {

					channelMasterTemp.setStatus(StatusConstant.REJECTE);
//					channelMasterTemp.setActionUser(checker);
//					channelMasterTemp.setActionDate(new Date());
					channelMasterTemp.setRemarkByChk(remark);
					logger.info("APS {} rejected successfully", apsCode);

				}

				// Save updated TEMP record
				channelMasTempRepo.save(channelMasterTemp);

			}

			logger.info("Checker update status successfully.");
		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Override
	public List<ChannelMasterDto> getChannelMasterByStatus(String statusType) {

		logger.info("Fetching  Channel Master record for maker");

		List<ChannelMasterDto> channelMasterDtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching Channel Master Approve record ");

//				channelMasMainRepo.findAll().forEach(channelMaster -> {
				channelMasMainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(channelMaster -> {

					ChannelMasterDto d = new ChannelMasterDto();

					d.setApsCode(channelMaster.getApsCode());
					d.setiBoxId(channelMaster.getiBoxId());
					d.setSupplierId(channelMaster.getSupplierId());
					d.setDates(channelMaster.getDates());
//					System.out.println(channelMaster.getDates().toString());
					d.setSuspended(channelMaster.getSuspended());

					d.setNameOfChannel(channelMaster.getNameOfChannel());
					d.setTypeOfDsa(channelMaster.getTypeOfDsa());
					d.setPanNo(channelMaster.getPanNo());
					d.setYy(channelMaster.getYy());

					d.setSupplierId1(channelMaster.getSupplierId1());
					d.setSupplierId2(channelMaster.getSupplierId2());
					d.setSupplierId3(channelMaster.getSupplierId3());
					d.setSupplierId4(channelMaster.getSupplierId4());
					d.setSupplierId5(channelMaster.getSupplierId5());
					d.setSupplierId6(channelMaster.getSupplierId6());
					d.setSupplierId7(channelMaster.getSupplierId7());
					d.setSupplierId8(channelMaster.getSupplierId8());
					d.setSupplierId9(channelMaster.getSupplierId9());
					d.setSupplierId10(channelMaster.getSupplierId10());
					d.setSupplierId11(channelMaster.getSupplierId11());
					d.setSupplierId12(channelMaster.getSupplierId12());
//					d.setSupplierId13(channelMaster.getSupplierId13());

					d.setRemark(channelMaster.getRemark());
					d.setLocation(channelMaster.getLocation());
					d.setMisState(channelMaster.getMisState());
					d.setcState(channelMaster.getcState());
					d.setEdState(channelMaster.getEdState());
					d.setEdZone(channelMaster.getEdZone());

					d.setSourcing(channelMaster.getSourcing());
					d.setSourcing1(channelMaster.getSourcing1());

					d.setManufactuName(channelMaster.getManufactuName());
					d.setNewManufactuName(channelMaster.getNewManufactuName());
					d.setOldIBoxId(channelMaster.getOldIBoxId());

					d.setRcLimit(channelMaster.getRcLimit());
					d.setSapCode(channelMaster.getSapCode());

					d.setAccountNo(channelMaster.getAccountNo());
					d.setIfscCode(channelMaster.getIfscCode());
					d.setBankName(channelMaster.getBankName());
					d.setiBankYesNonIBankNo(channelMaster.getiBankYesNonIBankNo());

//					d.setRemarksBk(channelMaster.getRemarksBk());
					d.setCreatedBy(channelMaster.getCreatedBy());
					d.setCreatedDate(channelMaster.getCreatedDate());
					d.setModifiedBy(channelMaster.getModifiedBy());
					d.setModifiedDate(channelMaster.getModifiedDate());
//					d.setRemarksBkk(channelMaster.getRemarksBkk());

					d.setStatus(channelMaster.getStatus());

					channelMasterDtoList.add(d);

				});
				logger.info("Approved Channel record Count : {}", channelMasterDtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching Channel Master Pending record ");

				channelMasTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(channelMaster -> {

					ChannelMasterDto d = new ChannelMasterDto();

					d.setApsCode(channelMaster.getApsCode());
					d.setiBoxId(channelMaster.getiBoxId());
					d.setSupplierId(channelMaster.getSupplierId());
					d.setDates(channelMaster.getDates());
					d.setSuspended(channelMaster.getSuspended());

					d.setNameOfChannel(channelMaster.getNameOfChannel());
					d.setTypeOfDsa(channelMaster.getTypeOfDsa());
					d.setPanNo(channelMaster.getPanNo());
					d.setYy(channelMaster.getYy());

					d.setSupplierId1(channelMaster.getSupplierId1());
					d.setSupplierId2(channelMaster.getSupplierId2());
					d.setSupplierId3(channelMaster.getSupplierId3());
					d.setSupplierId4(channelMaster.getSupplierId4());
					d.setSupplierId5(channelMaster.getSupplierId5());
					d.setSupplierId6(channelMaster.getSupplierId6());
					d.setSupplierId7(channelMaster.getSupplierId7());
					d.setSupplierId8(channelMaster.getSupplierId8());
					d.setSupplierId9(channelMaster.getSupplierId9());
					d.setSupplierId10(channelMaster.getSupplierId10());
					d.setSupplierId11(channelMaster.getSupplierId11());
					d.setSupplierId12(channelMaster.getSupplierId12());
//					d.setSupplierId13(channelMaster.getSupplierId13());

					d.setRemark(channelMaster.getRemark());
					d.setLocation(channelMaster.getLocation());
					d.setMisState(channelMaster.getMisState());
					d.setcState(channelMaster.getcState());
					d.setEdState(channelMaster.getEdState());
					d.setEdZone(channelMaster.getEdZone());

					d.setSourcing(channelMaster.getSourcing());
					d.setSourcing1(channelMaster.getSourcing1());

					d.setManufactuName(channelMaster.getManufactuName());
					d.setNewManufactuName(channelMaster.getNewManufactuName());
					d.setOldIBoxId(channelMaster.getOldIBoxId());

					d.setRcLimit(channelMaster.getRcLimit());
					d.setSapCode(channelMaster.getSapCode());

					d.setAccountNo(channelMaster.getAccountNo());
					d.setIfscCode(channelMaster.getIfscCode());
					d.setBankName(channelMaster.getBankName());
					d.setiBankYesNonIBankNo(channelMaster.getiBankYesNonIBankNo());

//					d.setRemarksBk(channelMaster.getRemarksBk());
					d.setCreatedBy(channelMaster.getCreatedBy());
					d.setCreatedDate(channelMaster.getCreatedDate());
					d.setModifiedBy(channelMaster.getModifiedBy());
					d.setModifiedDate(channelMaster.getModifiedDate());
//					d.setRemarksBkk(channelMaster.getRemarksBkk());

					d.setStatus(channelMaster.getStatus());

					channelMasterDtoList.add(d);

				});
				logger.info("Peinding Channel record Count : {}", channelMasterDtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching Channel Master Reject record ");
				channelMasTempRepo.findAllByStatus(StatusConstant.REJECTE).forEach(channelMaster -> {

					ChannelMasterDto d = new ChannelMasterDto();

					d.setApsCode(channelMaster.getApsCode());
					d.setiBoxId(channelMaster.getiBoxId());
					d.setSupplierId(channelMaster.getSupplierId());
					d.setDates(channelMaster.getDates());
					d.setSuspended(channelMaster.getSuspended());

					d.setNameOfChannel(channelMaster.getNameOfChannel());
					d.setTypeOfDsa(channelMaster.getTypeOfDsa());
					d.setPanNo(channelMaster.getPanNo());
					d.setYy(channelMaster.getYy());

					d.setSupplierId1(channelMaster.getSupplierId1());
					d.setSupplierId2(channelMaster.getSupplierId2());
					d.setSupplierId3(channelMaster.getSupplierId3());
					d.setSupplierId4(channelMaster.getSupplierId4());
					d.setSupplierId5(channelMaster.getSupplierId5());
					d.setSupplierId6(channelMaster.getSupplierId6());
					d.setSupplierId7(channelMaster.getSupplierId7());
					d.setSupplierId8(channelMaster.getSupplierId8());
					d.setSupplierId9(channelMaster.getSupplierId9());
					d.setSupplierId10(channelMaster.getSupplierId10());
					d.setSupplierId11(channelMaster.getSupplierId11());
					d.setSupplierId12(channelMaster.getSupplierId12());
//					d.setSupplierId13(channelMaster.getSupplierId13());

					d.setRemark(channelMaster.getRemark());
					d.setLocation(channelMaster.getLocation());
					d.setMisState(channelMaster.getMisState());
					d.setcState(channelMaster.getcState());
					d.setEdState(channelMaster.getEdState());
					d.setEdZone(channelMaster.getEdZone());

					d.setSourcing(channelMaster.getSourcing());
					d.setSourcing1(channelMaster.getSourcing1());

					d.setManufactuName(channelMaster.getManufactuName());
					d.setNewManufactuName(channelMaster.getNewManufactuName());
					d.setOldIBoxId(channelMaster.getOldIBoxId());

					d.setRcLimit(channelMaster.getRcLimit());
					d.setSapCode(channelMaster.getSapCode());

					d.setAccountNo(channelMaster.getAccountNo());
					d.setIfscCode(channelMaster.getIfscCode());
					d.setBankName(channelMaster.getBankName());
					d.setiBankYesNonIBankNo(channelMaster.getiBankYesNonIBankNo());

//					d.setRemarksBk(channelMaster.getRemarksBk());
					d.setCreatedBy(channelMaster.getCreatedBy());
					d.setCreatedDate(channelMaster.getCreatedDate());
					d.setModifiedBy(channelMaster.getModifiedBy());
					d.setModifiedDate(channelMaster.getModifiedDate());
//					d.setRemarksBkk(channelMaster.getRemarksBkk());

					d.setStatus(channelMaster.getStatus());

					channelMasterDtoList.add(d);

				});
				logger.info("Reject Channel record Count : {}", channelMasterDtoList.size());
				break;

			case "All":

				logger.info("Fetching All Channel Master Approve record ");
//				

				String sql = "SELECT *\r\n"
						+ "  FROM (SELECT APS_CODE, I_BOX_ID,SUPPLIER_ID,DATES,SUSPENDED,NAME_OF_CHANNEL,TYPE_OF_DSA,PAN_NO,YY,\r\n"
						+ "                SUPPLIER_ID_1,SUPPLIER_ID_2,SUPPLIER_ID_3,SUPPLIER_ID_4,SUPPLIER_ID_5,SUPPLIER_ID_6,\r\n"
						+ "                SUPPLIER_ID_7,SUPPLIER_ID_8,SUPPLIER_ID_9,SUPPLIER_ID_10,SUPPLIER_ID_11,SUPPLIER_ID_12,\r\n"
						+ "                REMARK,LOCATION,MIS_STATE,C_STATE,ED_STATE,ED_ZONE,SOURCING,SOURCING_1,MANUFACTU_NAME,NEW_MANUFACTU_NAME,\r\n"
						+ "                OLD_I_BOX_ID,RC_LIMIT,SAP_CODE,ACCOUNT_NO,IFSC_CODE,BANK_NAME,I_BANK_YES_NON_I_BANK_NO,\r\n"
						+ "                STATUS,\r\n"
						+ "               GREATEST(NVL(created_date, DATE '1900-01-01'),\r\n"
						+ "                        NVL(modified_date, DATE '1900-01-01')) AS sort_date\r\n"
						+ "          FROM tm_vhl_channel_mst\r\n" + "          WHERE status = 'A'\r\n" + "         \r\n"
						+ "        UNION ALL\r\n" + "        \r\n"
						+ "        SELECT APS_CODE, I_BOX_ID,SUPPLIER_ID,DATES,SUSPENDED,NAME_OF_CHANNEL,TYPE_OF_DSA,PAN_NO,YY,\r\n"
						+ "                SUPPLIER_ID_1,SUPPLIER_ID_2,SUPPLIER_ID_3,SUPPLIER_ID_4,SUPPLIER_ID_5,SUPPLIER_ID_6,\r\n"
						+ "                SUPPLIER_ID_7,SUPPLIER_ID_8,SUPPLIER_ID_9,SUPPLIER_ID_10,SUPPLIER_ID_11,SUPPLIER_ID_12,\r\n"
						+ "                REMARK,LOCATION,MIS_STATE,C_STATE,ED_STATE,ED_ZONE,SOURCING,SOURCING_1,MANUFACTU_NAME,NEW_MANUFACTU_NAME,\r\n"
						+ "                OLD_I_BOX_ID,RC_LIMIT,SAP_CODE,ACCOUNT_NO,IFSC_CODE,BANK_NAME,I_BANK_YES_NON_I_BANK_NO,\r\n"
						+ "                STATUS,\r\n"
						+ "               GREATEST(NVL(created_date, DATE '1900-01-01'),\r\n"
						+ "                        NVL(modified_date, DATE '1900-01-01')) AS sort_date\r\n"
						+ "          FROM tm_vhl_channel_mst_temp\r\n" + "          WHERE status = 'P'\r\n"
						+ "          )\r\n" + "ORDER BY sort_date DESC";

				List<ChannelMasterDto> list = jdbcTemplate.query(connection -> {

					PreparedStatement ps = connection.prepareStatement(sql);
//			        ps.setString(1, status);   
					return ps;

				}, (rs, rowNum) -> {

					ChannelMasterDto d = new ChannelMasterDto();

					d.setApsCode(rs.getInt("APS_CODE"));
					d.setiBoxId(rs.getString("I_BOX_ID"));
					d.setSupplierId(rs.getLong("SUPPLIER_ID"));
					d.setDates(rs.getDate("DATES"));
					d.setSuspended(rs.getString("SUSPENDED"));

					d.setNameOfChannel(rs.getString("NAME_OF_CHANNEL"));
					d.setTypeOfDsa(rs.getString("TYPE_OF_DSA"));
					d.setPanNo(rs.getString("PAN_NO"));
					d.setYy(rs.getString("YY"));

					d.setSupplierId1(rs.getLong("SUPPLIER_ID_1"));
					d.setSupplierId2(rs.getLong("SUPPLIER_ID_2"));
					d.setSupplierId3(rs.getLong("SUPPLIER_ID_3"));
					d.setSupplierId4(rs.getLong("SUPPLIER_ID_4"));
					d.setSupplierId5(rs.getLong("SUPPLIER_ID_5"));
					d.setSupplierId6(rs.getLong("SUPPLIER_ID_6"));
					d.setSupplierId7(rs.getLong("SUPPLIER_ID_7"));
					d.setSupplierId8(rs.getLong("SUPPLIER_ID_8"));
					d.setSupplierId9(rs.getLong("SUPPLIER_ID_9"));
					d.setSupplierId10(rs.getLong("SUPPLIER_ID_10"));
					d.setSupplierId11(rs.getLong("SUPPLIER_ID_11"));
					d.setSupplierId12(rs.getLong("SUPPLIER_ID_12"));

					d.setRemark(rs.getString("REMARK"));
					d.setLocation(rs.getString("LOCATION"));
					d.setMisState(rs.getString("MIS_STATE"));
					d.setcState(rs.getString("C_STATE"));
					d.setEdState(rs.getString("ED_STATE"));
					d.setEdZone(rs.getString("ED_ZONE"));

					d.setSourcing(rs.getString("SOURCING"));
					d.setSourcing1(rs.getString("SOURCING_1"));

					d.setManufactuName(rs.getString("MANUFACTU_NAME"));
					d.setNewManufactuName(rs.getString("NEW_MANUFACTU_NAME"));
					d.setOldIBoxId(rs.getString("OLD_I_BOX_ID"));

					d.setRcLimit(rs.getString("RC_LIMIT"));
					d.setSapCode(rs.getString("SAP_CODE"));

					d.setAccountNo(rs.getLong("ACCOUNT_NO"));
					d.setIfscCode(rs.getString("IFSC_CODE"));
					d.setBankName(rs.getString("BANK_NAME"));
					d.setiBankYesNonIBankNo(rs.getString("I_BANK_YES_NON_I_BANK_NO"));

//			    						d.setCreatedBy(rs.getString());
//			    						d.setCreatedDate(rs.getCreatedDate());
//			    						d.setModifiedBy(rs.getString());
//			    						d.setModifiedDate(rs.getModifiedDate());

					//
					d.setStatus(rs.getString("STATUS"));

					return d;
				});

				channelMasterDtoList.addAll(list);
				logger.info("All Channel record Count : {}", channelMasterDtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (channelMasterDtoList == null || channelMasterDtoList.size() < 0 || channelMasterDtoList.isEmpty()) {
				logger.warn("No Gst Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All Channel Master record to maker", channelMasterDtoList.size());
			return channelMasterDtoList;

		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;

		}

	}

}
