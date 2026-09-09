package com.icici.dma.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.BranchMasterDto;
import com.icici.dma.dto.ChannelMasterDto;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GSTStateMasterDto;
import com.icici.dma.dto.GstMasterDto;
import com.icici.dma.dto.GstMfMasterDto;
import com.icici.dma.dto.GstToMasterDto;
import com.icici.dma.dto.ILensChannelMasterDto;
import com.icici.dma.dto.ModelMasterDto;
import com.icici.dma.dto.OutsourceMasterDto;
import com.icici.dma.service.AlddDumpService;
import com.icici.dma.service.BranchMasterService;
import com.icici.dma.service.ChannelMasterService;
import com.icici.dma.service.FinnoneDumpService;
import com.icici.dma.service.GstMasterService;
import com.icici.dma.service.IlensDumpService;
import com.icici.dma.service.ModelMasterService;
import com.icici.dma.service.ProductMasterService;
import com.icici.dma.service.RcasService;
import com.icici.dma.service.Vhl_GSTStateMasterService;
import com.icici.dma.service.Vhl_GstMfMasterService;
import com.icici.dma.service.Vhl_GstToMasterService;
import com.icici.dma.service.Vhl_OutsourceMasterService;
import com.icici.dma.service.Vhl_iLensChannelMasterService;
import com.icici.dma.serviceImpl.BranchMasterUploadService;
import com.icici.dma.serviceImpl.ChannelMastUploadService;
import com.icici.dma.serviceImpl.GstMasterUploadService;
import com.icici.dma.serviceImpl.ModelMasterUploadService;

@CrossOrigin("*")
@RestController
@RequestMapping("/DMAPayoutWeb3")
public class MasterController {

	private static final Logger logger = LogManager.getLogger(MasterController.class);

	@Autowired
	private BranchMasterService branchMasterService;

	@Autowired
	private ProductMasterService productMasterservice;

	@Autowired
	private ChannelMasterService channelMasterService;

	@Autowired
	private GstMasterService gstMasterService;

	@Autowired
	private ModelMasterService modelMasterService;

	@Autowired
	private GstMasterUploadService gstUploadservice;

	@Autowired
	private ChannelMastUploadService channelUploadservice;

	@Autowired
	private ModelMasterUploadService modelUploadservice;

	@Autowired
	private BranchMasterUploadService branchUploadservice;

	@Autowired
	private FinnoneDumpService finnoneDumpService;

	@Autowired
	private AlddDumpService alddDumpService;

	@Autowired
	private RcasService rcasService;

	@Autowired
	private IlensDumpService ilensDumpService;

	@Autowired
	private Vhl_iLensChannelMasterService vhlIlensChannelService;

	@Autowired
	private Vhl_OutsourceMasterService vhlOutsourceMasterService;

	@Autowired
	private Vhl_GstMfMasterService vhlGstMfMasterService;

	@Autowired
	private Vhl_GstToMasterService vhlGstToMasterService;

	@Autowired
	private Vhl_GSTStateMasterService vhlGSTStateMasterService;

	// ======================================================================================================
	@GetMapping("/getProductMaster")
	public ResponseEntity<?> getAllProductMatser() {
		logger.info("get Product Master");
		List<String> allProductMatser = null;
		try {

			allProductMatser = productMasterservice.getAllProductMatser();
			logger.info(allProductMatser.toString());
			return ResponseEntity.ok(allProductMatser);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}
	}

	// ------------------- Branch Master -------------------------------

	@GetMapping("/allBranchMasMaker")
	@ResponseBody
	public ResponseEntity<?> getAllBranchMaster() {

		logger.info("API called : /allGSTMasMaker  :: method call : getAllGSTMasterMaker");

		List<BranchMasterDto> allBranchMasterList = branchMasterService.getAllBranchMastermaker();

		logger.info("API success : returning {} records ", allBranchMasterList.size());

		return ResponseEntity.ok(allBranchMasterList);

	}

	@PostMapping("/createBranchMasMaker")
	public ResponseEntity<String> createBranchMasterMaker(@RequestBody BranchMasterDto branchMasterDto,
			@RequestHeader("user") String user) {

		logger.warn("create GST request Recived");

		if (branchMasterDto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (branchMasterDto.getBranchCode() == null || branchMasterDto.getBranchCode().trim() == null) {
			logger.warn("ApsCode is missing");
			return ResponseEntity.badRequest().body("BranchCode is mandatory field");

		}

		branchMasterService.createBranchMasterbyMaker(branchMasterDto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateBranchMasMaker")
	public ResponseEntity<String> updateBranchMasterMaker(@RequestBody(required = true) BranchMasterDto branchMasterDto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of ApsCode {}", branchMasterDto.getBranchCode());

		if (branchMasterDto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (branchMasterDto.getBranchCode() == null || branchMasterDto.getBranchCode().trim().isEmpty()) {
			logger.warn("ApsCode is null");
			return ResponseEntity.badRequest().body("Update Failed : ApsCode is mandatory");
		}

		branchMasterService.updateBranchMasterbyMaker(branchMasterDto, user);

		logger.info("Update request submitted succesfully for ApsCode : {}", branchMasterDto.getBranchCode());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@PostMapping("/updateBranchMasChecker")
	public ResponseEntity<?> updateBranchMasterCheker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		// ===== NULL REQUEST CHECK =====
		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}

		// ===== DECISION CHECK =====
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		// ===== APS CODE LIST CHECK =====
		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("APS Code list is empty");

			return ResponseEntity.badRequest().body("At least one APS Code must be selected");
		}

		// ===== CALL SERVICE =====
		branchMasterService.updatebranchMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());
		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");

	}

	@GetMapping("/allBranchMasCheker")
	public ResponseEntity<?> getAllBranchMasterCheker(@RequestHeader String user) {

		logger.info("API called: fetch all GST master record for checker");

		List<BranchMasterDto> allcheckerBranchMaster = branchMasterService.getAllcheckerBranchMaster(user);

		return ResponseEntity.ok(allcheckerBranchMaster);

	}

	// ---------------------Channel Master-----------------------------------

	@GetMapping("/allChannelMasMaker")
	public ResponseEntity<?> getAllChannelMasterMaker() {

		logger.info("API called : /channel MasMaker  :: method call : allChannelMasterMaker");

		List<ChannelMasterDto> ChannelMstaerMakerList = channelMasterService.getAllChannelMstaerMaker();

		logger.info("API success : returning {} records ", ChannelMstaerMakerList.size());

		return ResponseEntity.ok(ChannelMstaerMakerList);

	}

	@PostMapping("/createChannelMasMaker")
	public ResponseEntity<String> createChannelMasterMaker(@RequestBody ChannelMasterDto channelMasterDto,
			@RequestHeader("user") String user) {

		logger.warn("create Channel request Recived");

		if (channelMasterDto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (channelMasterDto.getApsCode() == null) {
			logger.warn("ApsCode is missing");
			return ResponseEntity.badRequest().body("ApsCode is mandatory field");

		}

		channelMasterService.createChannelMaster(channelMasterDto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateChannelMasMaker")
	public ResponseEntity<String> updateChannelMasterMaker(@RequestBody ChannelMasterDto channelMasterDto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of ApsCode {}", channelMasterDto.getApsCode());

		if (channelMasterDto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (channelMasterDto.getApsCode() == null) {
			logger.warn("ApsCode is null");
			return ResponseEntity.badRequest().body("Update Failed : ApsCode is mandatory");
		}

		channelMasterService.updateChannelMasterByMaker(channelMasterDto, user);

		logger.info("Update request submitted succesfully for ApsCode : {}", channelMasterDto.getApsCode());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");

	}

	@PostMapping("/updateChannelMasChecker")
	public ResponseEntity<String> updateChannelMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		// ===== NULL REQUEST CHECK =====
		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}

		// ===== DECISION CHECK =====
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		// ===== APS CODE LIST CHECK =====
		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("APS Code list is empty");

			return ResponseEntity.badRequest().body("At least one APS Code must be selected");
		}

		// ===== CALL SERVICE =====
		channelMasterService.updateChannelMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());

		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");
	}

	@GetMapping("/allChannelMasChecker")
	public ResponseEntity<?> getAllChannelMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all channel master record for checker");

		List<ChannelMasterDto> allChannelMasterChecker = channelMasterService.getAllChannelMasterChecker(user);

		return ResponseEntity.ok(allChannelMasterChecker);
	}

	// -----------------------GST Master --------------------------------

	@GetMapping("/allGSTMasMaker")
	public ResponseEntity<?> getAllGSTMasterMaker() {

		logger.info("API called : /allGSTMasMaker  :: method call : getAllGSTMasterMaker");

		List<GstMasterDto> allgstMasterList = gstMasterService.getGSTMasterMaker();

		logger.info("API success : returning {} records ", allgstMasterList.size());

		return ResponseEntity.ok(allgstMasterList);

	}

	@PostMapping("/createGSTMasMaker")
	public ResponseEntity<?> createGstMasterMaker(@RequestBody GstMasterDto gstMasterDto,
			@RequestHeader("user") String user) {

		logger.warn("create GST request Recived");

		if (gstMasterDto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (gstMasterDto.getApsCode() == null) {
			logger.warn("ApsCode is missing");
			return ResponseEntity.badRequest().body("Aps Code is mandatory field");

		}

		gstMasterService.createGSTMaster(gstMasterDto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateGSTMasMaker")
	public ResponseEntity<String> updateGSTMasterMaker(@RequestBody(required = true) GstMasterDto gstMasterDto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of ApsCode {}", gstMasterDto.getApsCode());

		if (gstMasterDto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (gstMasterDto.getApsCode() == null) {
			logger.warn("ApsCode is null");
			return ResponseEntity.badRequest().body("Update Failed : ApsCode is mandatory");
		}

		gstMasterService.updateGSTMasterByMaker(gstMasterDto, user);

		logger.info("Update request submitted succesfully for ApsCode : {}", gstMasterDto.getApsCode());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@PostMapping("/updateGstMasChecker")
	public ResponseEntity<?> updateGstMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		// ===== NULL REQUEST CHECK =====
		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}

		// ===== DECISION CHECK =====
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		// ===== APS CODE LIST CHECK =====
		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("APS Code list is empty");

			return ResponseEntity.badRequest().body("At least one APS Code must be selected");
		}

		// ===== CALL SERVICE =====
		gstMasterService.updateGSTMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());
		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";
		return ResponseEntity.ok(action + " action completed successfully");

	}

	@GetMapping("/allGstMasChecker")
	public ResponseEntity<?> getAllGSTMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all GST master record for checker");

		// List<ChannelMasterDto> allChannelMasterChecker =
		// channelMasterService.getAllChannelMasterChecker();
		List<GstMasterDto> allGSTMasterChecker = gstMasterService.getAllGSTMasterChecker(user);

		return ResponseEntity.ok(allGSTMasterChecker);
	}

	// -------------------- Model Master --------------------------------

	@GetMapping("/allModelMasMaker")
	public ResponseEntity<?> getAllModelMasterMaker() {

		logger.info("API called : /allGSTMasMaker  :: method call : getAllGSTMasterMaker");

		List<ModelMasterDto> allmodelMaslist = modelMasterService.getModelMasMaker();

		logger.info("API success : returning {} records ", allmodelMaslist.size());

		return ResponseEntity.ok(allmodelMaslist);

	}

	@PostMapping("/createModelMasMaker")
	public ResponseEntity<?> createModelMasterMaker(@RequestBody ModelMasterDto modelMasterDto,
			@RequestHeader("user") String user) {

		logger.warn("create GST request Recived");

		if (modelMasterDto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (modelMasterDto.getManufacturerId() == null) {
			logger.warn("ApsCode is missing");
			return ResponseEntity.badRequest().body("Aps Code is mandatory field");

		}

		modelMasterService.createModelMas(modelMasterDto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateModelMasMaker")
	public ResponseEntity<String> updateModelMasterMaker(@RequestBody(required = true) ModelMasterDto modelMasterDto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of Manufacturer id {}",
				modelMasterDto.getManufacturerId());

		if (modelMasterDto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (modelMasterDto.getManufacturerId() == null) {
			logger.warn("ApsCode is null");
			return ResponseEntity.badRequest().body("Update Failed : Manufacturer is mandatory");
		}

		modelMasterService.updateModelMasByMaker(modelMasterDto, user);

		logger.info("Update request submitted succesfully for Manufacturer Id : {}",
				modelMasterDto.getManufacturerId());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@GetMapping("/allModelMasChecker")
	public ResponseEntity<?> getAllModelMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all GST master record for checker");

		List<ModelMasterDto> allModelMasterChecker = modelMasterService.getAllModelMasterChecker(user);

		return ResponseEntity.ok(allModelMasterChecker);
	}

	@PostMapping("/updateModelMasChecker")
	public ResponseEntity<?> updateModelMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		// ===== NULL REQUEST CHECK =====
		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}

		// ===== DECISION CHECK =====
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		// ===== APS CODE LIST CHECK =====
		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("APS Code list is empty");

			return ResponseEntity.badRequest().body("At least one APS Code must be selected");
		}

		// ===== CALL SERVICE =====
		modelMasterService.updateModelMasByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());
		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";
		return ResponseEntity.ok(action + " action completed successfully");

	}

// -------------------- ILens Channel Master ---------------------------------

	@PostMapping("/createIlensChannelMasMaker")
	public ResponseEntity<?> createIlensChannelMasterMaker(@RequestBody ILensChannelMasterDto dto,
			@RequestHeader("user") String user) {

		logger.warn("create ILens Channel request Recived");

		if (dto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (dto.getUserID() == null) {
			logger.warn("userID is missing");
			return ResponseEntity.badRequest().body("UserId is mandatory field");

		}

		vhlIlensChannelService.createILensChannelMasterByMaker(dto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateIlensChannelMasMaker")
	public ResponseEntity<String> updateIlensChannelMasterMaker(@RequestBody(required = true) ILensChannelMasterDto dto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of User Id {}", dto.getUserID());

		if (dto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (dto.getUserID() == null) {
			logger.warn("User Id is null");
			return ResponseEntity.badRequest().body("Update Failed : User Id is mandatory");
		}

		vhlIlensChannelService.updateILensChannelMasterByMaker(dto, user);

		logger.info("Update request submitted succesfully for user Id : {}", dto.getUserID());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@GetMapping("/allIlensChannelMasChecker")
	public ResponseEntity<?> getAllIlensChannelMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all ILens Channel master record for checker");

		List<ILensChannelMasterDto> pendingDtoList = vhlIlensChannelService.getAlliLensChannelMasterByChecker(user);

		return ResponseEntity.ok(pendingDtoList);
	}

	@PostMapping("/updateIlensChannelMasChecker")
	public ResponseEntity<?> updateIlensChannelMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("User Ids list is empty");

			return ResponseEntity.badRequest().body("At least one User Ids must be selected");
		}

		vhlIlensChannelService.updateiLensChannelMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());

		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");

	}

// -------------------- Gst Mf Master ---------------------------------

	@PostMapping("/createGstMfMasMaker")
	public ResponseEntity<?> createGstMfMasterMaker(@RequestBody GstMfMasterDto dto,
			@RequestHeader("user") String user) {

		logger.warn("create ILens Channel request Recived");

		if (dto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (dto.getApsCode() == null) {
			logger.warn("userID is missing");
			return ResponseEntity.badRequest().body("aps Code is mandatory field");

		}

		vhlGstMfMasterService.createGstMfMasterByMaker(dto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateGstMfMasMaker")
	public ResponseEntity<String> updateGstMfMasterMaker(@RequestBody(required = true) GstMfMasterDto dto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of ApsCode {}", dto.getApsCode());

		if (dto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (dto.getApsCode() == null) {
			logger.warn("User Id is null");
			return ResponseEntity.badRequest().body("Update Failed : ApsCode is mandatory");
		}
		vhlGstMfMasterService.updateGstMfMasterByMaker(dto, user);

		logger.info("Update request submitted succesfully for ApsCode : {}", dto.getApsCode());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@GetMapping("/allGstMfMasChecker")
	public ResponseEntity<?> getAllGstMfMasterChecker(@RequestHeader String user) {
		logger.info("API called: fetch all ILens Channel master record for checker");

		List<GstMfMasterDto> pendingDtoList = vhlGstMfMasterService.getAllGstMfMasterByChecker(user);

		return ResponseEntity.ok(pendingDtoList);
	}

	@PostMapping("/updateGstMfMasChecker")
	public ResponseEntity<?> updateGstMfMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("ApsCodes list is empty");

			return ResponseEntity.badRequest().body("At least one User Ids must be selected");
		}
		vhlGstMfMasterService.updateGstMfMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());

		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");

	}

// -------------------- Gst State Master ---------------------------------

	@PostMapping("/createGstStateMasMaker")
	public ResponseEntity<?> createGstStateMasterMaker(@RequestBody GSTStateMasterDto dto,
			@RequestHeader("user") String user) {

		logger.warn("create Gst State request Recived");

		if (dto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (dto.getPartnerId() == null) {
			logger.warn("partner Id is missing");
			return ResponseEntity.badRequest().body("Partner Id is mandatory field");

		}

		vhlGSTStateMasterService.createGstStateMasterByMaker(dto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateGstStateMasMaker")
	public ResponseEntity<String> updateGstStateMasterMaker(@RequestBody(required = true) GSTStateMasterDto dto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of partner Id {}", dto.getPartnerId());

		if (dto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (dto.getPartnerId() == null) {
			logger.warn("partner Id is null");
			return ResponseEntity.badRequest().body("Update Failed : partner Id is mandatory");
		}
		vhlGSTStateMasterService.updateGstStateMasterByMaker(dto, user);

		logger.info("Update request submitted succesfully for partner Id : {}", dto.getPartnerId());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@GetMapping("/allGstStateMasChecker")
	public ResponseEntity<?> getAllGstStateMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all GST State record for checker");

		List<GSTStateMasterDto> pendingDtoList = vhlGSTStateMasterService.getAllGstStateMasterByChecker(user);

		return ResponseEntity.ok(pendingDtoList);
	}

	@PostMapping("/updateGstStateMasChecker")
	public ResponseEntity<?> updateGstStateMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("Partner Id list is empty");

			return ResponseEntity.badRequest().body("At least one partner Ids must be selected");
		}

		vhlGSTStateMasterService.updateGstStateMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());

		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");

	}

// -------------------- Gst To Master ---------------------------------

	@PostMapping("/createGstToMasMaker")
	public ResponseEntity<?> createGstToMasterMaker(@RequestBody GstToMasterDto dto,
			@RequestHeader("user") String user) {

		logger.warn("create ILens Channel request Recived");

		if (dto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (dto.getProcessShop() == null) {
			logger.warn("ProcessShop is missing");
			return ResponseEntity.badRequest().body("ProcessShop is mandatory field");

		}

		vhlGstToMasterService.createGstToMasterByMaker(dto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateGstToMasMaker")
	public ResponseEntity<String> updateGstToMasterMaker(@RequestBody(required = true) GstToMasterDto dto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of processShop {}", dto.getProcessShop());

		if (dto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (dto.getProcessShop() == null) {
			logger.warn("Process shop is null");
			return ResponseEntity.badRequest().body("Update Failed : Process Shop is mandatory");
		}

		vhlGstToMasterService.updateGstToMasterByMaker(dto, user);

		logger.info("Update request submitted succesfully for Process Shop : {}", dto.getProcessShop());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@GetMapping("/allGstToMasChecker")
	public ResponseEntity<?> getAllGstTolMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all Gst To master record for checker");

		List<GstToMasterDto> pendingDtoList = vhlGstToMasterService.getAllGstToMasterByChecker(user);

		return ResponseEntity.ok(pendingDtoList);
	}

	@PostMapping("/updateGstToMasChecker")
	public ResponseEntity<?> updateGstToMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("Process Shop list is empty");

			return ResponseEntity.badRequest().body("At least one process shop must be selected");
		}

		vhlGstToMasterService.updateGstStateMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());

		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");

	}

// -------------------- Outsource Master ---------------------------------

	@PostMapping("/createOutsourceMasMaker")
	public ResponseEntity<?> createOutsourceMasterMaker(@RequestBody OutsourceMasterDto dto,
			@RequestHeader("user") String user) {

		logger.warn("create ILens Channel request Recived");

		if (dto == null) {
			logger.warn("requestBody is null");
			return ResponseEntity.badRequest().body("Please Fill All mandatory field");

		}
		if (dto.getEmpCode() == null) {
			logger.warn("Emp code is missing");
			return ResponseEntity.badRequest().body("Emp code is mandatory field");

		}
		vhlOutsourceMasterService.createOutsourceMasterByMaker(dto, user);

		return ResponseEntity.ok("Record submited successfully And sent for Approval");

	}

	@PostMapping("/updateOutsourceMasMaker")
	public ResponseEntity<String> updateOutsourceMasterMaker(@RequestBody(required = true) OutsourceMasterDto dto,
			@RequestHeader("user") String user) {

		logger.info("Maker edit request recived for appoval record of ApsCode {}", dto.getEmpCode());

		if (dto == null) {
			logger.warn("Request body is Null");
			return ResponseEntity.badRequest().body("Request Body can not be null");
		}
		if (dto.getEmpCode() == null) {
			logger.warn("Emp Code is null");
			return ResponseEntity.badRequest().body("Update Failed : Emp Code is mandatory");
		}

		vhlOutsourceMasterService.updateOutsourceMasterByMaker(dto, user);

		logger.info("Update request submitted succesfully for Emp Code : {}", dto.getEmpCode());
		return ResponseEntity.ok("Record Updated successfully and sent for approval");
	}

	@GetMapping("/allOutsourceMasChecker")
	public ResponseEntity<?> getAllGstMflMasterChecker(@RequestHeader String user) {

		logger.info("API called: fetch all Outsource master record for checker");

		List<OutsourceMasterDto> pendingDtoList = vhlOutsourceMasterService.getAllOutsourceMasterByChecker(user);

		return ResponseEntity.ok(pendingDtoList);
	}

	@PostMapping("/updateOutsourceMasChecker")
	public ResponseEntity<?> updateOutsourceMasterChecker(@RequestBody CheckerDecisionReq reqPayload,
			@RequestHeader("user") String user) {

		logger.info("Checker  Action recived");

		if (reqPayload == null) {

			logger.error("Request body is NULL");

			return ResponseEntity.badRequest().body("Action request can not be null");
		}
		if (reqPayload.getDecision() == null || reqPayload.getDecision().trim().isEmpty()) {

			logger.error("Decision type is missing");

			return ResponseEntity.badRequest().body("Decision type is required");
		}

		if (reqPayload.getPrimaryIds() == null || reqPayload.getPrimaryIds().isEmpty()) {

			logger.error("Emp Code list is empty");

			return ResponseEntity.badRequest().body("At least one Emp code must be selected");
		}
		vhlOutsourceMasterService.updateOutsourceMasterByChecker(reqPayload, user);

		logger.info(" {} action completed successfully.", reqPayload.getDecision());

		String action = "A".equalsIgnoreCase(reqPayload.getDecision()) ? "APPROVE" : "REJECT";

		return ResponseEntity.ok(action + " action completed successfully");

	}

	// ----------------------- Upload -------------------------------------------

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("product") String product, @RequestParam("master") String masterType,
			@RequestHeader String user) {

		logger.info("File upload controller with user : {}.", user.toString());
		logger.info(" File upload controller with master : {}.", masterType.toString());
		logger.info(" File upload controller with user : {}.", file.getOriginalFilename());

		if (file == null && file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is empty");
		}

		logger.info("File upload controller with user : {}", user.toString());
		logger.info(" File upload controller with master : {}", masterType.toString());
		logger.info(" File upload controller with fileName : {}", file.getOriginalFilename());

		if ("GST Master".equalsIgnoreCase(masterType)) {

			return ResponseEntity.ok(gstUploadservice.upload(file, user));

		} else if ("Channel Master".equalsIgnoreCase(masterType)) {

			return ResponseEntity.ok(channelUploadservice.upload(file, user));

		} else if ("Branch Master".equalsIgnoreCase(masterType)) {

			return ResponseEntity.ok(branchUploadservice.upload(file, user));

		} else if ("Model Master".equalsIgnoreCase(masterType)) {

			return ResponseEntity.ok(modelUploadservice.upload(file, user));

		} else if ("Ilens Channel master".equalsIgnoreCase(masterType)) {

			Map<String, Object> response = vhlIlensChannelService.uploadILensChannelMaster(file, user);

			return ResponseEntity.ok(response);

		} else if ("Outsource Master".equalsIgnoreCase(masterType)) {

			Map<String, Object> resp = vhlOutsourceMasterService.uploadOutsourceMaster(file, user);

			return ResponseEntity.ok(resp);

		} else if ("GST Mf Master".equalsIgnoreCase(masterType)) {

			Map<String, Object> res = vhlGstMfMasterService.uploadGstMfMaster(file, user);

			return ResponseEntity.ok(res);

		} else if ("GST State Master".equalsIgnoreCase(masterType)) {

			Map<String, Object> res = vhlGSTStateMasterService.uploadGSTStateMaster(file, user);
			return ResponseEntity.ok(res);

		} else if ("GST TO MASTER".equalsIgnoreCase(masterType) || "GST To Master".equalsIgnoreCase(masterType)) {

			Map<String, Object> res = vhlGstToMasterService.uploadGstToMaster(file, user);
			return ResponseEntity.ok(res);

		} else {

			return ResponseEntity.badRequest().body("Invalid master type");
		}

	}

	// =====================================================================================================
	@GetMapping("/error/download")
	public ResponseEntity<?> downloadErrorFile(@RequestParam(name = "master", required = false) String masterType,
			@RequestParam(name = "dumpType", required = false) String dumpType, @RequestHeader("user") String user) {

		try {

//			HttpSession session = request.getSession();
//			String user = (String) session.getAttribute("USER_ID");
			HttpHeaders headers = new HttpHeaders();

			ByteArrayInputStream file = null;

			if (masterType != null && !masterType.trim().isEmpty()) {

				switch (masterType.trim().toUpperCase()) {

				case "GST MASTER":
					file = gstUploadservice.exportErrorExcel(user);
					break;

				case "CHANNEL MASTER":
					file = channelUploadservice.exportErrorExcel(user);
					break;

				case "BRANCH MASTER":
					file = branchUploadservice.exportErrorExcel(user);
					break;

				case "MODEL MASTER":
					file = modelUploadservice.exportErrorExcel(user);
					break;

				case "ILENS CHANNEL MASTER":
					file = vhlIlensChannelService.exportErrorExcel(user);
					break;

				case "OUTSOURCE MASTER":

					file = vhlOutsourceMasterService.exportErrorExcel(user);
					break;

				case "GST MF MASTER":

					file = vhlGstMfMasterService.exportErrorExcel(user);
					break;

				case "GST STATE MASTER":

					file = vhlGSTStateMasterService.exportErrorExcel(user);
					break;

				case "GST TO MASTER":

					file = vhlGstToMasterService.exportErrorExcel(user);
					break;

				default:
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
							.body("Invalid master type");
				}

				// 🚨 If no records found
				if (file == null || file.available() == 0) {

					return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
							.body("No record found");
				}

				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + masterType + "_ERROR.xlsx");

				return ResponseEntity.ok().headers(headers).contentLength(file.available())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(file));

			} else if (dumpType != null && !dumpType.trim().isEmpty()) {

				if ("ALDD-Transaction Report".equalsIgnoreCase(dumpType)) {

					file = alddDumpService.exportErrorExcel(user);

				} else if ("RCAS".equalsIgnoreCase(dumpType)) {

					file = rcasService.exportRcasErrorExcel(user);

				} else if ("ilens dump".equalsIgnoreCase(dumpType)) {

					file = ilensDumpService.exportErrorExcel(user);

				} else if ("Finnone Dump".equalsIgnoreCase(dumpType)) {

					file = finnoneDumpService.exportFinnoneErrorExcel(user);

//				} else if ("Tagging File".equalsIgnoreCase(dumpType)) {

				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
							.body("Invalid dump type");
				}
				logger.info("file::: " + file);
				// 🚨 If no records found
				if (file == null || file.available() == 0) {

					return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
							.body("No record found");
				}

				headers.add(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + dumpType + "_ERROR_" + new Date() + ".xlsx");

				return ResponseEntity.ok().headers(headers).contentLength(file.available())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(file));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
						.body("Invalid type");
			}

		} catch (RuntimeException e) {
			logger.info(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());

		} catch (Exception ex) {
			logger.info(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Download failed : " + ex.getMessage());
		}

	}
	// =====================================================================================================

	@GetMapping("/vhlMaster")
	@ResponseBody
	public ResponseEntity<?> getAllMasterRecordMaker(HttpServletRequest request,
			@RequestParam("master") String masterType, @RequestParam("status") String statusType) {

		logger.info("API called : /vhlMaster  ::");

		if (masterType == null) {
			return ResponseEntity.badRequest().body("Master is null");
		}

		if (statusType == null) {
			return ResponseEntity.badRequest().body("statusType is null");
		}

		logger.info("API called : /vhlMaster  :: For VHL MASTER : {}", masterType);
		logger.info("API called : /vhlMaster  :: For VHL MASTER Status: {}", statusType);

		List<?> masterList = new ArrayList<>();

		switch (masterType.trim().toUpperCase()) {

		case "GST MASTER":
			masterList = gstMasterService.getGSTMasterByStatus(statusType);
			break;

		case "CHANNEL MASTER":
			masterList = channelMasterService.getChannelMasterByStatus(statusType);
			break;

		case "BRANCH MASTER":
			masterList = branchMasterService.getBranchMasterByStatus(statusType);
			break;

		case "MODEL MASTER":
			masterList = modelMasterService.getModelMasterByStatus(statusType);
			break;

		case "ILENS CHANNEL MASTER":
			masterList = vhlIlensChannelService.getiLensChannelMasterByStatus(statusType);
			break;

		case "OUTSOURCE MASTER":
			masterList = vhlOutsourceMasterService.getOutsourceMasterByStatus(statusType);
			break;

		case "GST STATE MASTER":
			masterList = vhlGSTStateMasterService.getGstStateMasterByStatus(statusType);
			break;

		case "GST TO MASTER":

			masterList = vhlGstToMasterService.getGstToMasterByStatus(statusType);
			break;

		case "GST MF MASTER":
			masterList = vhlGstMfMasterService.getGstMfMasterByStatus(statusType);
			break;

		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
					.body("Invalid master type");
		}

		logger.info("API success : returning '{}' records of '{}' with status type '{}'", masterList.size(), masterType,
				statusType);

		return ResponseEntity.ok(masterList);

	}
}
