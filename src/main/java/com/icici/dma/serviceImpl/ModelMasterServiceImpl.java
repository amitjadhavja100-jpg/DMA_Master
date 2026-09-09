package com.icici.dma.serviceImpl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.ModelMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.ModelMaster;
import com.icici.dma.model.ModelMasterTemp;
import com.icici.dma.repository.ModelMasterRepository;
import com.icici.dma.repository.ModelMasterTempRepository;
import com.icici.dma.service.ModelMasterService;

@Service
public class ModelMasterServiceImpl implements ModelMasterService {

	private static final Logger logger = LogManager.getLogger(ModelMasterServiceImpl.class);

	@Autowired
	public ModelMasterRepository modelMasMainRepo;

	@Autowired
	public ModelMasterTempRepository modelMasTempRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ModelMasterDto> getModelMasMaker() {

		logger.info("Fetching All Model Master record for maker");

		try {
			List<ModelMaster> modelMasterList = modelMasMainRepo.findAll();

			if (modelMasterList == null || modelMasterList.size() < 0 || modelMasterList.isEmpty()) {
				logger.warn("No pending record found for maker");
				throw new ResourceNotFoundException("No record found");
			}

			List<ModelMasterDto> ModelMasterDtoList = new ArrayList<>();

			for (ModelMaster modelMaster : modelMasterList) {

				ModelMasterDto modelMasterDto = new ModelMasterDto();

				modelMasterDto.setTempId(modelMaster.getTempId());
				modelMasterDto.setManufacturerId(modelMaster.getManufacturerId());
				modelMasterDto.setManufacturerDesc(modelMaster.getManufacturerDesc());
				modelMasterDto.setModelId(modelMaster.getModelId());
				modelMasterDto.setModelDesc(modelMaster.getModelDesc());
				modelMasterDto.setAssetCategory(modelMaster.getAssetCategory());
				modelMasterDto.setBand(modelMaster.getBand());
//				modelMasterDto.setCreatedBy(modelMaster.getCreatedBy());
//				modelMasterDto.setCreateDate(modelMaster.getCreateDate());
				modelMasterDto.setModifiedBy(modelMaster.getModifiedBy());
				modelMasterDto.setModifiedDate(modelMaster.getModifiedDate());
//				modelMasterDto.setRemarks(modelMaster.getRemarks());
				modelMasterDto.setStatus(modelMaster.getStatus());

				ModelMasterDtoList.add(modelMasterDto);
			}

			logger.info("Returning {} pending record to checker", ModelMasterDtoList.size());
			return ModelMasterDtoList;

		} catch (DataAccessException e) {

			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Override
	public void updateModelMasByMaker(ModelMasterDto modelMasterDto, String username) {

		try {
			String maker = username;

			logger.info("Maker {} Request Edit for approval record of manufacturere id {}", maker,
					modelMasterDto.getManufacturerId());

			ModelMaster modelMasterMain = modelMasMainRepo.findById(modelMasterDto.getTempId()).orElseThrow(() -> {

				logger.warn("Approved record not found for manufacturere id: {}", modelMasterDto.getManufacturerId());

				return new ResourceNotFoundException(
						"Approved record not found for manufacture id :" + modelMasterDto.getManufacturerId());
			});

			ModelMasterTemp modelMasterTemp1 = modelMasTempRepo.findById(modelMasterDto.getTempId()).get();

			if (modelMasterTemp1.getStatus() == StatusConstant.PENDING
					|| modelMasterTemp1.getStatus().equalsIgnoreCase(StatusConstant.PENDING)) {

				logger.warn("manufacturer id {} already Pending for Approval", modelMasterDto.getManufacturerId());

				throw new IllegalArgumentException(
						"Manufacturer id " + modelMasterDto.getManufacturerId() + " alredy pending for Approval ");
			}

			// update status in main
			modelMasterMain.setStatus(StatusConstant.PENDING);

			modelMasMainRepo.save(modelMasterMain);

			logger.info("Model Master Main Record status updated to PENDING for manufacturere id : {}",
					modelMasterDto.getManufacturerId());

			ModelMasterTemp modelMasterTemp = new ModelMasterTemp();

			modelMasterTemp.setTempId(modelMasterTemp1.getTempId());

			modelMasterTemp.setManufacturerId(modelMasterDto.getManufacturerId());
			modelMasterTemp.setManufacturerDesc(modelMasterDto.getManufacturerDesc());
			modelMasterTemp.setModelId(modelMasterDto.getModelId());
			modelMasterTemp.setModelDesc(modelMasterDto.getModelDesc());
			modelMasterTemp.setAssetCategory(modelMasterDto.getAssetCategory());
			modelMasterTemp.setBand(modelMasterDto.getBand());

			modelMasterTemp.setRemarks(modelMasterTemp1.getRemarks());
			modelMasterTemp.setStatus(StatusConstant.PENDING);

			modelMasterTemp.setCreatedBy(modelMasterMain.getCreatedBy());
			modelMasterTemp.setCreateDate(modelMasterMain.getCreateDate());

			modelMasterTemp.setModifiedBy(maker);
			modelMasterTemp.setModifiedDate(new Date());

			modelMasterTemp.setActionType(ActionConstant.UPDATE);

			modelMasterTemp.setActionUser(maker);
			modelMasterTemp.setActionDate(new Date());

			modelMasTempRepo.save(modelMasterTemp);

			logger.info("Manufacturing Id {} successfully sent for Approval", modelMasterDto.getManufacturerId());

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Model Master Data", e);

			throw e;

		}
	}

	@Override
	public void createModelMas(ModelMasterDto modelMasterDto, String username) {
		try {

			logger.info("Processing create model for Manufacturer ID : {} ", modelMasterDto.getManufacturerId());

			String maker = username;

			if (modelMasMainRepo.existsByManufacturerIdAndModelId(modelMasterDto.getManufacturerId(),
					modelMasterDto.getModelId())) {
				logger.warn("Record already approved for Manufacturer ID : {} ", modelMasterDto.getManufacturerId());
				throw new IllegalArgumentException("Record Already Approved");
			}

			ModelMasterTemp modelMasterTemp1 = modelMasTempRepo
					.findByManufacturerIdAndModelId(modelMasterDto.getManufacturerId(), modelMasterDto.getModelId());

			if (modelMasterTemp1 != null) {
				if (modelMasterTemp1.getStatus() == StatusConstant.PENDING
						|| modelMasterTemp1.getStatus().equalsIgnoreCase(StatusConstant.PENDING)) {

					logger.warn("manufacturere id {} already Pending for Approval", modelMasterDto.getManufacturerId());

					throw new IllegalArgumentException(
							"manufacturere id " + modelMasterDto.getManufacturerId() + " alredy pending for Approval ");
				}

			}

			ModelMasterTemp modelMasterTemp = new ModelMasterTemp();

			modelMasterTemp.setManufacturerId(modelMasterDto.getManufacturerId());
			modelMasterTemp.setManufacturerDesc(modelMasterDto.getManufacturerDesc());

			modelMasterTemp.setModelId(modelMasterDto.getModelId());
			modelMasterTemp.setModelDesc(modelMasterDto.getModelDesc());

			modelMasterTemp.setAssetCategory(modelMasterDto.getAssetCategory());
			modelMasterTemp.setBand(modelMasterDto.getBand());

			modelMasterTemp.setCreatedBy(maker);
			modelMasterTemp.setCreateDate(new Date());

			modelMasterTemp.setStatus(StatusConstant.PENDING);
			modelMasterTemp.setActionType(ActionConstant.INSERT);
			modelMasterTemp.setActionDate(new Date());
			modelMasterTemp.setActionUser(maker);

			modelMasTempRepo.save(modelMasterTemp);

			logger.info("Record saved in Temp Table for Manufacturer ID : {} ", modelMasterDto.getManufacturerId());

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Model Master Data", e);

			throw e;

		}

	}

	@Override
	public List<ModelMasterDto> getAllModelMasterChecker(String user) {

		logger.info("Fetching All model Master record for checker");

		try {

			List<ModelMasterTemp> modelMastTempList = modelMasTempRepo
					.findAllByStatusAndCreatedByNot(StatusConstant.PENDING, user);

			if (modelMastTempList == null || modelMastTempList.size() < 0 || modelMastTempList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<ModelMasterDto> modelmasterDtoList = new ArrayList<>();

			for (ModelMasterTemp modelMasterTemp : modelMastTempList) {

				ModelMasterDto modelMasterDto = new ModelMasterDto();

				modelMasterDto.setTempId(modelMasterTemp.getTempId());
				modelMasterDto.setManufacturerId(modelMasterTemp.getManufacturerId());
				modelMasterDto.setManufacturerDesc(modelMasterTemp.getManufacturerDesc());
				modelMasterDto.setModelId(modelMasterTemp.getModelId());
				modelMasterDto.setModelDesc(modelMasterTemp.getModelDesc());
				modelMasterDto.setAssetCategory(modelMasterTemp.getAssetCategory());
				modelMasterDto.setBand(modelMasterTemp.getBand());

				modelMasterDto.setRemarks(modelMasterTemp.getRemarks());
				modelMasterDto.setStatus(modelMasterTemp.getStatus());

				modelmasterDtoList.add(modelMasterDto);

			}

			logger.info("Returning {} pending record to checker", modelmasterDtoList.size());
			return modelmasterDtoList;

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Model Master Data", e);

			throw e;

		}
	}

	@Override
	public void updateModelMasByChecker(CheckerDecisionReq requestPayload, String username) {

		logger.info(" processing checker action");

		try {
			// Convert decision to uppercase
			String decision = requestPayload.getDecision().toUpperCase();
			String remark = requestPayload.getRemark();
			String checker = username;

			// Validate decision type
			if (!StatusConstant.APPROVE.equals(decision) && !StatusConstant.REJECTE.equals(decision)) {

				logger.error("Invalid decision received: {}", decision);
				throw new IllegalArgumentException("Invalid decision type. Use A for APPROVE and R for REJECT");
			}

			// Process each APS Code
			for (String primaryId : requestPayload.getPrimaryIds()) {

				Integer tempId = Integer.valueOf(primaryId.trim());
				logger.info("Processing model temp Id: {}", tempId);

				// Fetch only PENDING record from TEMP
				ModelMasterTemp modelMasterTemp = modelMasTempRepo.findByTempIdAndStatus(tempId, StatusConstant.PENDING)
						.orElseThrow(() -> {
							logger.error("Pending record not found for model temp Id: {}", tempId);
							return new ResourceNotFoundException(
									"Pending record not found for model temp ID: " + tempId);
						});

				// Update status based on decision
				if (StatusConstant.APPROVE.equals(decision)) {

					modelMasterTemp.setStatus(StatusConstant.APPROVE);
//					modelMasterTemp.setActionUser(checker);
//					modelMasterTemp.setActionDate(new Date());
					modelMasterTemp.setRemarks(remark);

					logger.info(" model temp ID  {} approved successfully ", tempId);

				} else {

					modelMasterTemp.setStatus(StatusConstant.REJECTE);
//					modelMasterTemp.setActionUser(checker);
//					modelMasterTemp.setActionDate(new Date());
					modelMasterTemp.setRemarks(remark);
					logger.info(" model temp ID {} rejected successfully", tempId);

				}

				// Save updated TEMP record
				modelMasTempRepo.save(modelMasterTemp);

			}

			logger.info("Checker status update completed successfully.");

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Model Master Data", e);

			throw e;

		}

	}

	@Override
	public List<?> getModelMasterByStatus(String statusType) {
		logger.info("Fetching  Model Master record for maker");

		List<ModelMasterDto> ModeMasterDtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching Model Master Approve record ");

//				modelMasMainRepo.findAll().forEach(modelMaster -> {

				modelMasMainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(modelMaster -> {

					ModelMasterDto modelMasterDto = new ModelMasterDto();

					modelMasterDto.setTempId(modelMaster.getTempId());
					modelMasterDto.setManufacturerId(modelMaster.getManufacturerId());
					modelMasterDto.setManufacturerDesc(modelMaster.getManufacturerDesc());
					modelMasterDto.setModelId(modelMaster.getModelId());
					modelMasterDto.setModelDesc(modelMaster.getModelDesc());
					modelMasterDto.setAssetCategory(modelMaster.getAssetCategory());
					modelMasterDto.setBand(modelMaster.getBand());
//					modelMasterDto.setCreatedBy(modelMaster.getCreatedBy());
//					modelMasterDto.setCreateDate(modelMaster.getCreateDate());
					modelMasterDto.setModifiedBy(modelMaster.getModifiedBy());
					modelMasterDto.setModifiedDate(modelMaster.getModifiedDate());
//					modelMasterDto.setRemarks(modelMaster.getRemarks());
					modelMasterDto.setStatus(modelMaster.getStatus());
					ModeMasterDtoList.add(modelMasterDto);

				});
				logger.info("Approved Model Master record Count : {}", ModeMasterDtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching Model Master Pending record ");

				modelMasTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(modelMasterTemp -> {

					ModelMasterDto modelMasterDto = new ModelMasterDto();

					modelMasterDto.setTempId(modelMasterTemp.getTempId());
					modelMasterDto.setManufacturerId(modelMasterTemp.getManufacturerId());
					modelMasterDto.setManufacturerDesc(modelMasterTemp.getManufacturerDesc());
					modelMasterDto.setModelId(modelMasterTemp.getModelId());
					modelMasterDto.setModelDesc(modelMasterTemp.getModelDesc());
					modelMasterDto.setAssetCategory(modelMasterTemp.getAssetCategory());
					modelMasterDto.setBand(modelMasterTemp.getBand());
//					modelMasterDto.setCreatedBy(modelMasterTemp.getCreatedBy());
//					modelMasterDto.setCreateDate(modelMasterTemp.getCreateDate());
					modelMasterDto.setModifiedBy(modelMasterTemp.getModifiedBy());
					modelMasterDto.setModifiedDate(modelMasterTemp.getModifiedDate());
//					modelMasterDto.setRemarks(modelMasterTemp.getRemarks());
					modelMasterDto.setStatus(modelMasterTemp.getStatus());

					ModeMasterDtoList.add(modelMasterDto);

				});
				logger.info("Peinding Model record Count : {}", ModeMasterDtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching Model Master Reject record ");
				modelMasTempRepo.findAllByStatus(StatusConstant.REJECTE).forEach(modelMasterTemp -> {

					ModelMasterDto modelMasterDto = new ModelMasterDto();

					modelMasterDto.setTempId(modelMasterTemp.getTempId());
					modelMasterDto.setManufacturerId(modelMasterTemp.getManufacturerId());
					modelMasterDto.setManufacturerDesc(modelMasterTemp.getManufacturerDesc());
					modelMasterDto.setModelId(modelMasterTemp.getModelId());
					modelMasterDto.setModelDesc(modelMasterTemp.getModelDesc());
					modelMasterDto.setAssetCategory(modelMasterTemp.getAssetCategory());
					modelMasterDto.setBand(modelMasterTemp.getBand());
//					modelMasterDto.setCreatedBy(modelMasterTemp.getCreatedBy());
//					modelMasterDto.setCreateDate(modelMasterTemp.getCreateDate());
					modelMasterDto.setModifiedBy(modelMasterTemp.getModifiedBy());
					modelMasterDto.setModifiedDate(modelMasterTemp.getModifiedDate());
//					modelMasterDto.setRemarks(modelMasterTemp.getRemarks());
					modelMasterDto.setStatus(modelMasterTemp.getStatus());

					ModeMasterDtoList.add(modelMasterDto);

				});
				logger.info("Reject Model record Count : {}", ModeMasterDtoList.size());
				break;

			case "All":

				logger.info("Fetching All Model Master pending and approve record ");
//				

				String sql = "SELECT * "
						+ "  FROM ( SELECT TEMP_ID,MANUFACTURER_ID,MANUFACTURER_DESC,MODEL_ID,MODEL_DESC, "
						+ "                ASSET_CATEGORY,BAND, STATUS,  "
						+ "                GREATEST(NVL(modified_date, DATE '1900-01-01'), "
						+ "                        NVL(created_date, DATE '1900-01-01')) AS sort_date "
						+ "         FROM tm_vhl_model_mst " + "         WHERE status = 'A' " + "          "
						+ "         UNION ALL " + "         "
						+ "        SELECT TEMP_ID,MANUFACTURER_ID,MANUFACTURER_DESC,MODEL_ID,MODEL_DESC, "
						+ "                ASSET_CATEGORY,BAND, STATUS, " + "               GREATEST( "
						+ "                        NVL(action_date, DATE '1900-01-01'), "
						+ "                        NVL(created_date, DATE '1900-01-01'), "
						+ "                        NVL(modified_date, DATE '1900-01-01') "
						+ "                        ) AS sort_date " + "          FROM tm_vhl_model_mst_temp "
						+ "          WHERE status = 'P' " + "          ) " + "ORDER BY sort_date DESC";

				List<ModelMasterDto> list = jdbcTemplate.query(connection -> {

					PreparedStatement ps = connection.prepareStatement(sql);
//			        ps.setString(1, status);   
					return ps;

				}, (rs, rowNum) -> {

					ModelMasterDto modelMasterDto = new ModelMasterDto();

					modelMasterDto.setTempId(rs.getInt("TEMP_ID"));
					modelMasterDto.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
					modelMasterDto.setManufacturerDesc(rs.getString("MANUFACTURER_DESC"));
					modelMasterDto.setModelId(rs.getInt("MODEL_ID"));
					modelMasterDto.setModelDesc(rs.getString("MODEL_DESC"));
					modelMasterDto.setAssetCategory(rs.getString("ASSET_CATEGORY"));
					modelMasterDto.setBand(rs.getString("BAND"));
					modelMasterDto.setStatus(rs.getString("STATUS"));
					//

					return modelMasterDto;
				});

				ModeMasterDtoList.addAll(list);

				logger.info("All Model record Count : {}", ModeMasterDtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (ModeMasterDtoList == null || ModeMasterDtoList.size() < 0 || ModeMasterDtoList.isEmpty()) {
				logger.warn("No Model Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All Model Master record to maker", ModeMasterDtoList.size());
			return ModeMasterDtoList;

		} catch (DataAccessException e) {

			logger.error("Database error while fetching the Model Master Data", e);
			throw new RuntimeException("Unable to fetch Model Master Data");

		}
	}

}
