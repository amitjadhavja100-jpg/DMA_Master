package com.icici.dma.serviceImpl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.BranchMasterDto;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.BranchMaster;
import com.icici.dma.model.BranchMasterTemp;
import com.icici.dma.repository.BranchMasterRepository;
import com.icici.dma.repository.BranchMasterTempRepository;
import com.icici.dma.service.BranchMasterService;

@Service
public class BranchMasterServiceImpl implements BranchMasterService {

	private static final Logger logger = LogManager.getLogger(BranchMasterServiceImpl.class);
	@Autowired
	private BranchMasterRepository branchMasRepo;

	@Autowired
	private BranchMasterTempRepository branchMasTempRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<BranchMasterDto> getAllBranchMastermaker() {

		try {

			List<BranchMaster> branchMasterList = branchMasRepo.findAll();

			if (branchMasterList == null || branchMasterList.size() < 0 || branchMasterList.isEmpty()) {
				logger.info("No record Found");
				throw new ResourceNotFoundException("No Record Found");
			}

//			List<BranchMasterDto> branchMasterDtoList = branchMasterList.stream().map(this::convertbranchMasterToDto)
//					.collect(Collectors.toList());

			List<BranchMasterDto> branchMasterDto = new ArrayList<>();

			for (BranchMaster branchMaster : branchMasterList) {

				BranchMasterDto d = new BranchMasterDto();

				d.setBranchCode(branchMaster.getBranchCode());
				d.setBranchName(branchMaster.getBranchName());
				d.setHub(branchMaster.getHub());
				d.setaLState(branchMaster.getaLState());

				d.setZone(branchMaster.getZone());
				d.setrBH(branchMaster.getrBH());
				d.seteDState(branchMaster.geteDState());
				d.seteDZone(branchMaster.geteDZone());

				d.setzHName(branchMaster.getzHName());
				d.setStateHead(branchMaster.getStateHead());
				d.setmISState(branchMaster.getmISState());
				d.setrCState(branchMaster.getrCState());
				d.setLocation(branchMaster.getLocation());

				d.setStatus(branchMaster.getStatus());
//				d.setRemark(branchMaster.getRemark());

//				d.setCreatedBy(branchMaster.getCreatedBy());
//				d.setCreatedDate(branchMaster.getCreatedDate());
//				d.setModifiedBy(branchMaster.getModifiedBy());
//				d.setModifiedDate(branchMaster.getModifiedDate());

				branchMasterDto.add(d);
			}

			logger.info("Returning {} pending record to maker", branchMasterDto.size());
			return branchMasterDto;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;
		}
	}

	@Override
	public void createBranchMasterbyMaker(BranchMasterDto branchMasterDto, String username) {

		logger.info("Processing create branch master for branch Code : {} ", branchMasterDto.getBranchCode());

		String maker = username;

		try {

			if (branchMasRepo.existsByBranchCode(branchMasterDto.getBranchCode().trim())) {
				logger.warn("Record already approved for APS Code : {} ", branchMasterDto.getBranchCode());
				throw new IllegalArgumentException("Record already approved");
			}

			if (branchMasTempRepo.existsByBranchCodeAndStatus(branchMasterDto.getBranchCode(),
					StatusConstant.PENDING)) {
				logger.warn("Record already pending for APS Code : {} ", branchMasterDto.getBranchCode());
				throw new IllegalArgumentException("Record already aending for approval");
			}

			BranchMasterTemp branchMasterTemp = new BranchMasterTemp();

			branchMasterTemp.setBranchCode(branchMasterDto.getBranchCode());
			branchMasterTemp.setBranchName(branchMasterDto.getBranchName());
			branchMasterTemp.setHub(branchMasterDto.getHub());
			branchMasterTemp.setaLState(branchMasterDto.getaLState());

			branchMasterTemp.setZone(branchMasterDto.getZone());
			branchMasterTemp.setrBH(branchMasterDto.getrBH());
			branchMasterTemp.seteDState(branchMasterDto.geteDState());
			branchMasterTemp.seteDZone(branchMasterDto.geteDZone());

			branchMasterTemp.setzHName(branchMasterDto.getzHName());
			branchMasterTemp.setStateHead(branchMasterDto.getStateHead());
			branchMasterTemp.setmISState(branchMasterDto.getmISState());
			branchMasterTemp.setrCState(branchMasterDto.getrCState());
			branchMasterTemp.setLocation(branchMasterDto.getLocation());

			branchMasterTemp.setRemark(branchMasterDto.getRemark());

//			branchMasterTemp.setCreatedBy(branchMasterDto.getCreatedBy());
//			branchMasterTemp.setCreatedDate(branchMasterDto.getCreatedDate());
//			branchMasterTemp.setModifiedBy(branchMasterDto.getModifiedBy());
//			branchMasterTemp.setModifiedDate(branchMasterDto.getModifiedDate());

			branchMasterTemp.setStatus(StatusConstant.PENDING);
			branchMasterTemp.setCreatedBy(maker);
			branchMasterTemp.setCreatedDate(new Date());

			branchMasterTemp.setActionType(ActionConstant.INSERT);
			branchMasterTemp.setActionDate(new Date());
			branchMasterTemp.setActionUser(maker);

			branchMasTempRepo.save(branchMasterTemp);
			logger.info("Record saved in Temp Table for APS Code : {} ", branchMasterDto.getBranchCode());

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Branch Master Data", e);
			throw e;

		}

	}

	@Override
	public void updateBranchMasterbyMaker(BranchMasterDto branchMasterDto, String username) {

		String maker = username;

		logger.info("Maker {} Request Edit for approval record of BranchCode {}", maker,
				branchMasterDto.getBranchCode());

		try {

			BranchMaster branchMasterMain = branchMasRepo.findById(branchMasterDto.getBranchCode()).orElseThrow(() -> {

				logger.warn("Approved record not found for BranchCode: {}", branchMasterDto.getBranchCode());

				return new ResourceNotFoundException(
						"Approved record not found for BranchCode :" + branchMasterDto.getBranchCode());
			});

			Optional<BranchMasterTemp> branchCodeTempOptional = branchMasTempRepo
					.findByBranchCodeAndStatus(branchMasterDto.getBranchCode(), StatusConstant.PENDING);

			if (branchCodeTempOptional.isPresent()) {

				logger.warn("BranchCode {} already Pending for Approval");

				throw new IllegalArgumentException(
						"BranchCode " + branchMasterDto.getBranchCode() + " alredy Pending for Approval ");
			}

			// update status in main
			branchMasterMain.setStatus(StatusConstant.PENDING);

			branchMasRepo.save(branchMasterMain);

			logger.info("Branch Master Main Record status updated to PENDING for branchCode : {}",
					branchMasterDto.getBranchCode());

			// update or mapping Branch Temp record

			BranchMasterTemp branchMasterTemp = new BranchMasterTemp();

			branchMasterTemp.setBranchCode(branchMasterMain.getBranchCode());
			branchMasterTemp.setBranchName(branchMasterDto.getBranchName());

			branchMasterTemp.setHub(branchMasterDto.getHub());
			branchMasterTemp.setaLState(branchMasterDto.getaLState());

			branchMasterTemp.setZone(branchMasterDto.getZone());
			branchMasterTemp.setrBH(branchMasterDto.getrBH());
			branchMasterTemp.seteDState(branchMasterDto.geteDState());
			branchMasterTemp.seteDZone(branchMasterDto.geteDZone());

			branchMasterTemp.setzHName(branchMasterDto.getzHName());
			branchMasterTemp.setStateHead(branchMasterDto.getStateHead());
			branchMasterTemp.setmISState(branchMasterDto.getmISState());
			branchMasterTemp.setrCState(branchMasterDto.getrCState());
			branchMasterTemp.setLocation(branchMasterDto.getLocation());

			branchMasterTemp.setRemark(branchMasterDto.getRemark());
			branchMasterTemp.setStatus(StatusConstant.PENDING);

			branchMasterTemp.setCreatedBy(branchMasterMain.getCreatedBy());
			branchMasterTemp.setCreatedDate(branchMasterMain.getCreatedDate());

			branchMasterTemp.setModifiedBy(maker);
			branchMasterTemp.setModifiedDate(new Date());

			branchMasterTemp.setActionType(ActionConstant.UPDATE);
			branchMasterTemp.setActionDate(new Date());
			branchMasterTemp.setActionUser(maker);

			branchMasTempRepo.save(branchMasterTemp);

			logger.info("BranchCode {} successfully sent for Approval", branchMasterDto.getBranchCode());

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Branch Master Data", e);
			throw e;

		}

	}

	@Override
	public List<BranchMasterDto> getAllcheckerBranchMaster(String user) {

		logger.info("Fetching All Branch Master record for checker");

		try {
//			List<BranchMasterTemp> branchMasterTempPenList = branchMasTempRepo.findAllByStatus(StatusConstant.PENDING);
			List<BranchMasterTemp> branchMasterTempPenList = branchMasTempRepo
					.findAllByStatusAndCreatedByNot(StatusConstant.PENDING, user);

			if (branchMasterTempPenList == null || branchMasterTempPenList.size() < 0
					|| branchMasterTempPenList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<BranchMasterDto> branchMasterDtoList = new ArrayList<>();

			for (BranchMasterTemp BranchMasterTemp : branchMasterTempPenList) {
				BranchMasterDto d = new BranchMasterDto();

				d.setBranchCode(BranchMasterTemp.getBranchCode());
				d.setBranchName(BranchMasterTemp.getBranchName());
				d.setHub(BranchMasterTemp.getHub());
				d.setaLState(BranchMasterTemp.getaLState());

				d.setZone(BranchMasterTemp.getZone());
				d.setrBH(BranchMasterTemp.getrBH());
				d.seteDState(BranchMasterTemp.geteDState());
				d.seteDZone(BranchMasterTemp.geteDZone());

				d.setzHName(BranchMasterTemp.getzHName());
				d.setStateHead(BranchMasterTemp.getStateHead());
				d.setmISState(BranchMasterTemp.getmISState());
				d.setrCState(BranchMasterTemp.getrCState());
				d.setLocation(BranchMasterTemp.getLocation());

				d.setStatus(BranchMasterTemp.getStatus());
				d.setRemark(BranchMasterTemp.getRemark());

//				d.setProductName(BranchMasterTemp.getProductName());
//				d.setVhlProdId(BranchMasterTemp.getVhlProdId());

//				d.setCreatedBy(branchMaster.getCreatedBy());
//				d.setCreatedDate(branchMaster.getCreatedDate());
//				d.setModifiedBy(branchMaster.getModifiedBy());
//				d.setModifiedDate(branchMaster.getModifiedDate());

				branchMasterDtoList.add(d);

			}

			logger.info("Returning {} pending record to checker", branchMasterDtoList.size());
			return branchMasterDtoList;

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Branch Master Data", e);
			throw e;

		}
	}

	@Override
	public void updatebranchMasterByChecker(CheckerDecisionReq requestPayload, String username) {

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
			for (String branchMaster : requestPayload.getPrimaryIds()) {

				logger.info("Processing APS Code: {}", branchMaster);

				// Fetch only PENDING record from TEMP
				BranchMasterTemp branchMasterTemp = branchMasTempRepo
						.findByBranchCodeAndStatus(branchMaster.toString(), StatusConstant.PENDING).orElseThrow(() -> {
							logger.error("Pending record not found for branch Code : {}", branchMaster);
							return new ResourceNotFoundException(
									"Pending record not found for BranchCode : " + branchMaster);
						});

				// Update status based on decision
				if (StatusConstant.APPROVE.equals(decision)) {

					branchMasterTemp.setStatus(StatusConstant.APPROVE);
//					branchMasterTemp.setActionUser(checker);
//					branchMasterTemp.setActionDate(new Date());
					branchMasterTemp.setRemark(remark);

					logger.info("BranchCode {} approved successfully ", branchMaster);

				} else {

					branchMasterTemp.setStatus(StatusConstant.REJECTE);
//					branchMasterTemp.setActionUser(checker);
//					branchMasterTemp.setActionDate(new Date());
					branchMasterTemp.setRemark(remark);
					logger.info("BranchCode {} rejected successfully", branchMaster);

				}

				// Save updated TEMP record
				branchMasTempRepo.save(branchMasterTemp);

			}

			logger.info("Checker update status successfully.");
		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Branch Master Data", e);
			throw e;

		}

	}

	@Override
	public List<BranchMasterDto> getBranchMasterByStatus(String statusType) {

		logger.info("Fetching  Branch Master record for maker by status");

		List<BranchMasterDto> branchMasterDtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching Branch Master Approve record ");

//				branchMasRepo.findAll().forEach(branchMaster -> {
				branchMasRepo.findAllByStatus(StatusConstant.APPROVE).forEach(branchMaster -> {

					BranchMasterDto d = new BranchMasterDto();

					d.setBranchCode(branchMaster.getBranchCode());
					d.setBranchName(branchMaster.getBranchName());
					d.setHub(branchMaster.getHub());
					d.setaLState(branchMaster.getaLState());

					d.setZone(branchMaster.getZone());
					d.setrBH(branchMaster.getrBH());
					d.seteDState(branchMaster.geteDState());
					d.seteDZone(branchMaster.geteDZone());

					d.setzHName(branchMaster.getzHName());
					d.setStateHead(branchMaster.getStateHead());
					d.setmISState(branchMaster.getmISState());
					d.setrCState(branchMaster.getrCState());
					d.setLocation(branchMaster.getLocation());

					d.setStatus(branchMaster.getStatus());

					branchMasterDtoList.add(d);

				});
				logger.info("Approved Branch record Count : {}", branchMasterDtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching Branch Master Pending record ");

				branchMasTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(branchMasterTemp -> {

					BranchMasterDto d = new BranchMasterDto();

					d.setBranchCode(branchMasterTemp.getBranchCode());
					d.setBranchName(branchMasterTemp.getBranchName());
					d.setHub(branchMasterTemp.getHub());
					d.setaLState(branchMasterTemp.getaLState());

					d.setZone(branchMasterTemp.getZone());
					d.setrBH(branchMasterTemp.getrBH());
					d.seteDState(branchMasterTemp.geteDState());
					d.seteDZone(branchMasterTemp.geteDZone());

					d.setzHName(branchMasterTemp.getzHName());
					d.setStateHead(branchMasterTemp.getStateHead());
					d.setmISState(branchMasterTemp.getmISState());
					d.setrCState(branchMasterTemp.getrCState());
					d.setLocation(branchMasterTemp.getLocation());

					d.setStatus(branchMasterTemp.getStatus());

					branchMasterDtoList.add(d);

				});
				logger.info("Peinding Branch record Count : {}", branchMasterDtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching Branch Master Reject record ");

				branchMasTempRepo.findAllByStatus(StatusConstant.REJECTE).forEach(branchMasterTemp -> {

					BranchMasterDto d = new BranchMasterDto();

					d.setBranchCode(branchMasterTemp.getBranchCode());
					d.setBranchName(branchMasterTemp.getBranchName());
					d.setHub(branchMasterTemp.getHub());
					d.setaLState(branchMasterTemp.getaLState());

					d.setZone(branchMasterTemp.getZone());
					d.setrBH(branchMasterTemp.getrBH());
					d.seteDState(branchMasterTemp.geteDState());
					d.seteDZone(branchMasterTemp.geteDZone());

					d.setzHName(branchMasterTemp.getzHName());
					d.setStateHead(branchMasterTemp.getStateHead());
					d.setmISState(branchMasterTemp.getmISState());
					d.setrCState(branchMasterTemp.getrCState());
					d.setLocation(branchMasterTemp.getLocation());

					d.setStatus(branchMasterTemp.getStatus());

					branchMasterDtoList.add(d);

				});
				logger.info("Reject Branch record Count : {}", branchMasterDtoList.size());
				break;

			case "All":

				logger.info("Fetching Branch Master All PENDING AND Approved record ");

				String sql = "SELECT * "
						+ "  FROM ( SELECT BRANCH_CODE,BRANCH_NAME,HUB,AL_STATE,ZONE,RBH,ED_STATE,ED_ZONE,ZH_NAME, "
						+ "               STATE_HEAD,MIS_STATE,RC_STATE,LOCATION, STATUS, "
						+ "               GREATEST(NVL(created_date, DATE '1900-01-01'), "
						+ "                        NVL(modified_date, DATE '1900-01-01')) AS sort_date "
						+ "         FROM tm_vhl_branch_mst " + "         WHERE status = 'A' "

						+ "        	UNION ALL "

						+ "        	SELECT BRANCH_CODE,BRANCH_NAME,HUB,AL_STATE,ZONE,RBH,ED_STATE,ED_ZONE,ZH_NAME, "
						+ "               STATE_HEAD,MIS_STATE,RC_STATE,LOCATION,STATUS, " + "               GREATEST( "
						+ "                        NVL(action_date, DATE '1900-01-01'), "
						+ "                        NVL(created_date, DATE '1900-01-01'), "
						+ "                        NVL(modified_date, DATE '1900-01-01') "
						+ "                        ) AS sort_date " + "          FROM tm_vhl_branch_mst_temp "
						+ "          WHERE status = 'P' " + "        ) " + "ORDER BY sort_date DESC";

				List<BranchMasterDto> list = jdbcTemplate.query(connection -> {

					PreparedStatement ps = connection.prepareStatement(sql);
//			        ps.setString(1, status);   
					return ps;

				}, (rs, rowNum) -> {

					BranchMasterDto d = new BranchMasterDto();

					d.setBranchCode(rs.getString("BRANCH_CODE"));
					d.setBranchName(rs.getString("BRANCH_NAME"));
					d.setHub(rs.getString("HUB"));
					d.setaLState(rs.getString("AL_STATE"));

					d.setZone(rs.getString("ZONE"));
					d.setrBH(rs.getString("RBH"));
					d.seteDState(rs.getString("ED_STATE"));
					d.seteDZone(rs.getString("ED_ZONE"));

					d.setzHName(rs.getString("ZH_NAME"));
					d.setStateHead(rs.getString("STATE_HEAD"));
					d.setmISState(rs.getString("MIS_STATE"));
					d.setrCState(rs.getString("RC_STATE"));
					d.setLocation(rs.getString("LOCATION"));

					d.setStatus(rs.getString("STATUS"));

					return d;
				});

				branchMasterDtoList.addAll(list);
				logger.info("All Branch record Count : {}", branchMasterDtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (branchMasterDtoList == null || branchMasterDtoList.size() < 0 || branchMasterDtoList.isEmpty()) {
				logger.warn("No Branch Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All Branch Master record to maker", branchMasterDtoList.size());
			return branchMasterDtoList;

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Branch Master Data", e);
			throw new RuntimeException("Unable to fetch Model Master Data");

		}

	}

}
