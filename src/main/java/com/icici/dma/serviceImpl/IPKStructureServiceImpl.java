package com.icici.dma.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.ApsRowRequest;
import com.icici.dma.dto.BrokerRowRequest;
import com.icici.dma.dto.CategorySlabRequest;
import com.icici.dma.dto.IPKPendingSummary;
import com.icici.dma.dto.IPKStructureSaveRequest;
import com.icici.dma.dto.IncentiveStructureDto;
import com.icici.dma.dto.MPKCommonPendingSummary;
import com.icici.dma.dto.RetainerPayloadDto;
import com.icici.dma.dto.SlabRowRequest;
import com.icici.dma.dto.SourcingRowRequest;
import com.icici.dma.dto.UsedCarStructureRegionSaveRequest;
import com.icici.dma.dto.UsedCarStructureSaveRequest;
import com.icici.dma.repository.AutoIprocessSpecialCasesEntityRepository;
import com.icici.dma.repository.AutoManipalChennaiUsedStructureRepository;
import com.icici.dma.repository.AutoManipalRetainerMasterRepository;
import com.icici.dma.repository.AutoManipalSpecialCasesEntityRepository;
import com.icici.dma.repository.AutoProcessApsUsedMasterIpkRepository;
import com.icici.dma.repository.AutoProcessApsUsedMasterRepository;
import com.icici.dma.repository.AutoProcessBrokerMasterRepository;
import com.icici.dma.repository.AutoProcessChennaiUsedMasterRepository;
import com.icici.dma.repository.AutoProcessCommonSpecialCasesRepository;
import com.icici.dma.repository.AutoProcessIncentiveStructureMasterDummyRepository;
import com.icici.dma.repository.AutoProcessIncentiveStructureMasterManipalRepository;
import com.icici.dma.repository.IncentiveStructureRepository;
import com.icici.dma.service.IPKStructureService;
import com.icici.dma.slabEntity.AutoIprocessSpecialCasesEntity;
import com.icici.dma.slabEntity.AutoManipalChennaiUsedStructure;
import com.icici.dma.slabEntity.AutoManipalRetainerMaster;
import com.icici.dma.slabEntity.AutoManipalSpecialCasesEntity;
import com.icici.dma.slabEntity.AutoProcessApsUsedMaster;
import com.icici.dma.slabEntity.AutoProcessApsUsedMasterIpk;
import com.icici.dma.slabEntity.AutoProcessBrokerMaster;
import com.icici.dma.slabEntity.AutoProcessChennaiUsedMaster;
import com.icici.dma.slabEntity.AutoProcessCommonSpecialCases;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterDummy;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterManipal;
import com.icici.dma.slabEntity.IncentiveStructure;

@Service
public class IPKStructureServiceImpl implements IPKStructureService {
	@Autowired
	private AutoProcessIncentiveStructureMasterDummyRepository repository;
	@Autowired
	private AutoProcessIncentiveStructureMasterManipalRepository manipalRepository;

	@Autowired
	private AutoProcessApsUsedMasterRepository apsRepository;

	@Autowired
	private AutoProcessBrokerMasterRepository brokerRepository;

	@Autowired
	private AutoProcessChennaiUsedMasterRepository sourcingRepository;

	@Autowired
	private AutoProcessApsUsedMasterIpkRepository apsUsedMasterIpkRepository;

	@Autowired
	private AutoProcessCommonSpecialCasesRepository commonSpecialCasesRepository;

	@Autowired
	private AutoManipalChennaiUsedStructureRepository chennaiUsedStructureRepository;

	@Autowired
	private AutoIprocessSpecialCasesEntityRepository iprocessSpecialRepository;
	@Autowired
	private AutoManipalSpecialCasesEntityRepository manipalSpecialRepository;
	
	 @Autowired
	    private AutoManipalRetainerMasterRepository retainerRepository;
	 
	 @Autowired
	 private  IncentiveStructureRepository incentiveStructureRepository;

	@Transactional
	public void saveIPKStructure(IPKStructureSaveRequest request) {

		Long maxSequence = repository.findMaxSequence();
		long nextSequence = (maxSequence == null) ? 1L : maxSequence + 1;


		List<AutoProcessIncentiveStructureMasterDummy> entityList = new ArrayList<AutoProcessIncentiveStructureMasterDummy>();

		for (CategorySlabRequest categoryReq : request.getCategories()) {
			for (SlabRowRequest rowReq : categoryReq.getRows()) {

				AutoProcessIncentiveStructureMasterDummy entity = new AutoProcessIncentiveStructureMasterDummy();
				entity.setState(request.getState());
				entity.setCategory(categoryReq.getCategory());
				entity.setSlabFrom(rowReq.getSlabFrom());
				entity.setSlabTo(rowReq.getSlabTo());
				entity.setIncentiveAmount(rowReq.getIncentiveAmount());
				entity.setRemarks(categoryReq.getRemarks());
				entity.setCycleFrom(request.getCycleFrom());
				entity.setCycleTo(request.getCycleTo());
				entity.setStatus("N");
				entity.setSequence(nextSequence);
				entity.setCreatedBy("SYSTEM");
				entity.setCreatedDate(LocalDate.now());

				entityList.add(entity);
			}
		}

		repository.saveAll(entityList);
	}

	@Transactional
	public void saveMPKStructure(IPKStructureSaveRequest request) {

		Long maxSequence = manipalRepository.findMaxSequence();
		long nextSequence = (maxSequence == null) ? 1L : maxSequence + 1;

		LocalDate cycleFrom = LocalDate.parse(request.getCycleFrom());
		LocalDate cycleTo = LocalDate.parse(request.getCycleTo());

		List<AutoProcessIncentiveStructureMasterManipal> entityList = new ArrayList<AutoProcessIncentiveStructureMasterManipal>();

		for (CategorySlabRequest categoryReq : request.getCategories()) {
			for (SlabRowRequest rowReq : categoryReq.getRows()) {

				AutoProcessIncentiveStructureMasterManipal entity = new AutoProcessIncentiveStructureMasterManipal();
				entity.setState(request.getState());
				entity.setCategory(categoryReq.getCategory());
				entity.setSlabFrom(rowReq.getSlabFrom());
				entity.setSlabTo(rowReq.getSlabTo());
				entity.setIncentiveAmount(rowReq.getIncentiveAmount());
				entity.setRemarks(categoryReq.getRemarks());
				entity.setCycleFrom(request.getCycleFrom());
				entity.setCycleTo(request.getCycleTo());
				entity.setStatus("N");
				entity.setSequence(nextSequence);
				entity.setCreatedBy("SYSTEM");
				entity.setCreatedDate(LocalDate.now());

				entityList.add(entity);
			}
		}

		manipalRepository.saveAll(entityList);
	}

	@Override
	public List<IPKPendingSummary> getPendingList() {
		return manipalRepository.findPendingSummary();
	}

	public List<AutoProcessIncentiveStructureMasterDummy> getBySequence(Long sequence) {
		return repository.findBySequence(sequence);
	}

	@Transactional
	public void approve(Long sequence) {
		List<AutoProcessIncentiveStructureMasterDummy> list = repository.findBySequence(sequence);
		for (AutoProcessIncentiveStructureMasterDummy obj : list) {
			obj.setStatus("Y");
			obj.setCheckedBy("CHECKER");
			obj.setCheckedDate(LocalDate.now());
			repository.save(obj);
		}
	}

	@Override
	public void approveCommonIP(Integer sequence) {
		List<AutoProcessApsUsedMasterIpk> autoProcessApsUsedMasterIpkList = apsUsedMasterIpkRepository
				.findBySequence(sequence);
		for (AutoProcessApsUsedMasterIpk data : autoProcessApsUsedMasterIpkList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			apsUsedMasterIpkRepository.save(data);
		}
		List<AutoProcessCommonSpecialCases> autoProcessCommonSpecialCasesList = commonSpecialCasesRepository
				.findBySequence(sequence);
		for (AutoProcessCommonSpecialCases data : autoProcessCommonSpecialCasesList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			commonSpecialCasesRepository.save(data);
		}
		List<AutoManipalChennaiUsedStructure> autoManipalChennaiUsedStructureList = chennaiUsedStructureRepository
				.findBySequence(sequence);

		for (AutoManipalChennaiUsedStructure data : autoManipalChennaiUsedStructureList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			chennaiUsedStructureRepository.save(data);
		}

	}

	@Override
	public void rejectCommonIP(Integer sequence) {
		List<AutoProcessApsUsedMasterIpk> autoProcessApsUsedMasterIpkList = apsUsedMasterIpkRepository
				.findBySequence(sequence);
		for (AutoProcessApsUsedMasterIpk data : autoProcessApsUsedMasterIpkList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			apsUsedMasterIpkRepository.save(data);
		}
		List<AutoProcessCommonSpecialCases> autoProcessCommonSpecialCasesList = commonSpecialCasesRepository
				.findBySequence(sequence);
		for (AutoProcessCommonSpecialCases data : autoProcessCommonSpecialCasesList) {
			data.setCheckedBy("CHEKCER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			commonSpecialCasesRepository.save(data);
		}
		List<AutoManipalChennaiUsedStructure> autoManipalChennaiUsedStructureList = chennaiUsedStructureRepository
				.findBySequence(sequence);

		for (AutoManipalChennaiUsedStructure data : autoManipalChennaiUsedStructureList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			chennaiUsedStructureRepository.save(data);
		}

	}

	@Transactional
	public void reject(Long sequence) {
		List<AutoProcessIncentiveStructureMasterDummy> list = repository.findBySequence(sequence);
		for (AutoProcessIncentiveStructureMasterDummy obj : list) {
			obj.setStatus("R");
			obj.setCheckedBy("CHECKER");
			obj.setCheckedDate(LocalDate.now());
			repository.save(obj);
		}
	}

	@Override
	public List<MPKCommonPendingSummary> getPendingMPList() {
		// TODO Auto-generated method stub
		return commonSpecialCasesRepository.findPendingSummaryCommon();
	}

	@Override
	public List<AutoProcessIncentiveStructureMasterManipal> getBySequenceMP(Long sequence) {
		// TODO Auto-generated method stub
		return manipalRepository.findBySequence(sequence);
	}

	@Override
	public void approveMP(Long sequence) {
		List<AutoProcessIncentiveStructureMasterManipal> list = manipalRepository.findBySequence(sequence);
		for (AutoProcessIncentiveStructureMasterManipal obj : list) {
			obj.setStatus("Y");
			obj.setCheckedBy("CHECKER");
			obj.setCheckedDate(LocalDate.now());
			manipalRepository.save(obj);

		}
	}

	@Override
	public void rejectMP(Long sequence) {
		List<AutoProcessIncentiveStructureMasterManipal> list = manipalRepository.findBySequence(sequence);
		for (AutoProcessIncentiveStructureMasterManipal obj : list) {
			obj.setStatus("R");
			obj.setCheckedBy("CHECKER");
			obj.setCheckedDate(LocalDate.now());
			manipalRepository.save(obj);

		}
	}

	@Transactional
	@Override
	public void saveStructure(UsedCarStructureSaveRequest request) {

		Integer nextSequence = generateNextSequence();

		List<AutoProcessApsUsedMaster> apsList = new ArrayList<AutoProcessApsUsedMaster>();
		for (ApsRowRequest row : request.getApsRows()) {
			AutoProcessApsUsedMaster entity = new AutoProcessApsUsedMaster();
			entity.setState(request.getState());
			entity.setCategory("APS_CODE");
			entity.setApsCode(row.getApsCode());
			entity.setChannelName(row.getChannelName());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			apsList.add(entity);
		}
		apsRepository.saveAll(apsList);

		List<AutoProcessBrokerMaster> brokerList = new ArrayList<AutoProcessBrokerMaster>();
		for (BrokerRowRequest row : request.getBrokerRows()) {
			AutoProcessBrokerMaster entity = new AutoProcessBrokerMaster();
			entity.setState(request.getState());
			entity.setBrokerId(row.getBrokerId());
			entity.setBrokerName(row.getBrokerName());
			entity.setNewCar(row.getNewCar());
			entity.setUsedCar(row.getUsedCar());
			entity.setAttachmentIncentive(row.getAttachmentIncentive());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setSequence(nextSequence);
			brokerList.add(entity);
		}
		brokerRepository.saveAll(brokerList);

		List<AutoProcessChennaiUsedMaster> sourcingList = new ArrayList<AutoProcessChennaiUsedMaster>();
		for (SourcingRowRequest row : request.getSourcingRows()) {
			AutoProcessChennaiUsedMaster entity = new AutoProcessChennaiUsedMaster();
			entity.setState(request.getState());
			entity.setCategory(row.getCategory());
			entity.setSlabFrom(row.getSlabFrom());
			entity.setSlabTo(row.getSlabTo());

			Double pct = row.getPercentage();
			entity.setPercentage(pct == null ? 0.0 : pct);

			entity.setDesignation(row.getDesignation());
			entity.setRemarks(row.getRemarks());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			sourcingList.add(entity);
		}
		sourcingRepository.saveAll(sourcingList);
	}

	@Override
	public List<MPKCommonPendingSummary> getPendingIPCommonList() {
		// TODO Auto-generated method stub
		return commonSpecialCasesRepository.findPendingSummaryCommon();
		// return null;
	}

	private Integer generateNextSequence() {
		Integer maxAps = apsRepository.findMaxSequence();
		Integer maxBroker = brokerRepository.findMaxSequence();
		Integer maxSourcing = sourcingRepository.findMaxSequence();

		Integer max = 0;
		if (maxAps != null && maxAps > max)
			max = maxAps;
		if (maxBroker != null && maxBroker > max)
			max = maxBroker;
		if (maxSourcing != null && maxSourcing > max)
			max = maxSourcing;

		return max + 1;
	}

	@Override
	public List<MPKCommonPendingSummary> getPendingMPCommonList() {
		// TODO Auto-generated method stub
		return brokerRepository.findPendingSummaryCommon();
		// return null;
	}

	@Override
	public void approveCommonMP(Integer sequence) {
		// TODO Auto-generated method stub
		List<AutoProcessApsUsedMaster> autoProcessApsUsedMasterList = apsRepository.findBySequence(sequence);
		for (AutoProcessApsUsedMaster data : autoProcessApsUsedMasterList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			apsRepository.save(data);
		}
		List<AutoProcessBrokerMaster> autoProcessBrokerMasterList = brokerRepository.findBySequence(sequence);
		for (AutoProcessBrokerMaster data : autoProcessBrokerMasterList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			brokerRepository.save(data);
		}
		List<AutoProcessChennaiUsedMaster> autoProcessChennaiUsedMasterList = sourcingRepository
				.findBySequence(sequence);

		for (AutoProcessChennaiUsedMaster data : autoProcessChennaiUsedMasterList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			sourcingRepository.save(data);
		}

	}

	@Override
	public void rejectCommonMP(Integer sequence) {
		// TODO Auto-generated method stub

		List<AutoProcessApsUsedMaster> autoProcessApsUsedMasterList = apsRepository.findBySequence(sequence);
		for (AutoProcessApsUsedMaster data : autoProcessApsUsedMasterList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			apsRepository.save(data);
		}
		List<AutoProcessBrokerMaster> autoProcessBrokerMasterList = brokerRepository.findBySequence(sequence);
		for (AutoProcessBrokerMaster data : autoProcessBrokerMasterList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			brokerRepository.save(data);
		}
		List<AutoProcessChennaiUsedMaster> autoProcessChennaiUsedMasterList = sourcingRepository
				.findBySequence(sequence);

		for (AutoProcessChennaiUsedMaster data : autoProcessChennaiUsedMasterList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			sourcingRepository.save(data);
		}

	}

	@Transactional
	@Override
	public void saveIPCStructure(UsedCarStructureSaveRequest request) {
		Integer nextSequence = generateNextSequence();

		List<AutoProcessApsUsedMasterIpk> apsList = new ArrayList<AutoProcessApsUsedMasterIpk>();
		for (ApsRowRequest row : request.getApsRows()) {
			AutoProcessApsUsedMasterIpk entity = new AutoProcessApsUsedMasterIpk();
			entity.setState(request.getState());
			entity.setCategory("APS_CODE");
			entity.setApsCode(row.getApsCode());
			entity.setChannelName(row.getChannelName());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			apsList.add(entity);
		}
		apsUsedMasterIpkRepository.saveAll(apsList);

		List<AutoProcessCommonSpecialCases> brokerList = new ArrayList<AutoProcessCommonSpecialCases>();
		for (BrokerRowRequest row : request.getBrokerRows()) {
			AutoProcessCommonSpecialCases entity = new AutoProcessCommonSpecialCases();
			entity.setState(request.getState());
			entity.setBrokerId(row.getBrokerId());
			entity.setBrokerName(row.getBrokerName());
			entity.setNewCar(row.getNewCar());
			entity.setUsedCar(row.getUsedCar());
			entity.setAttachmentIncentive(row.getAttachmentIncentive());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setSequence(nextSequence);
			brokerList.add(entity);
		}
		commonSpecialCasesRepository.saveAll(brokerList);

		List<AutoManipalChennaiUsedStructure> sourcingList = new ArrayList<AutoManipalChennaiUsedStructure>();
		for (SourcingRowRequest row : request.getSourcingRows()) {
			AutoManipalChennaiUsedStructure entity = new AutoManipalChennaiUsedStructure();
			entity.setState(request.getState());
			entity.setCategory(row.getCategory());
			entity.setSlabFrom(row.getSlabFrom());
			entity.setSlabTo(row.getSlabTo());

			Double pct = row.getPercentage();
			entity.setPercentage(pct == null ? 0.0 : pct);

			entity.setDesignation(row.getDesignation());
			entity.setRemarks(row.getRemarks());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			sourcingList.add(entity);
		}
		chennaiUsedStructureRepository.saveAll(sourcingList);

	}

	@Override
	public void saveIPPNStructure(UsedCarStructureRegionSaveRequest request) {

		Integer nextSequence = generateNextSequence();

		List<AutoIprocessSpecialCasesEntity> apsList = new ArrayList<AutoIprocessSpecialCasesEntity>();
		for (ApsRowRequest row : request.getApsPNRows()) {
			AutoIprocessSpecialCasesEntity entity = new AutoIprocessSpecialCasesEntity();
			entity.setState(request.getState());
			entity.setVehicleType("USED");
			entity.setName(row.getChannelName());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			apsList.add(entity);
		}
		for (ApsRowRequest row : request.getApsKLRows()) {
			AutoIprocessSpecialCasesEntity entity = new AutoIprocessSpecialCasesEntity();
			entity.setState(request.getState());
			entity.setVehicleType("USED");
			entity.setName(row.getChannelName());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			apsList.add(entity);
		}
		iprocessSpecialRepository.saveAll(apsList);

	}

	@Override
	public List<MPKCommonPendingSummary> getPendingIPOtherList() {
		// TODO Auto-generated method stub
		return iprocessSpecialRepository.findPendingSummaryOther();
	}

	@Override
	public void approveOtherIP(Integer sequence) {
		// TODO Auto-generated method stub

		List<AutoIprocessSpecialCasesEntity> autoIprocessSpecialCasesEntityList = iprocessSpecialRepository
				.findBySequence(sequence);
		for (AutoIprocessSpecialCasesEntity data : autoIprocessSpecialCasesEntityList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			iprocessSpecialRepository.save(data);
		}

	}

	@Override
	public void rejectOtherIP(Integer sequence) {
		List<AutoIprocessSpecialCasesEntity> autoIprocessSpecialCasesEntityList = iprocessSpecialRepository
				.findBySequence(sequence);
		for (AutoIprocessSpecialCasesEntity data : autoIprocessSpecialCasesEntityList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("N");
			iprocessSpecialRepository.save(data);

		}

	}

	@Override
	public void saveMPPNStructure(UsedCarStructureRegionSaveRequest request) {
		// TODO Auto-generated method stub

		Integer nextSequence = generateNextSequence();

		List<AutoManipalSpecialCasesEntity> apsList = new ArrayList<AutoManipalSpecialCasesEntity>();
		for (ApsRowRequest row : request.getApsPNRows()) {
			AutoManipalSpecialCasesEntity entity = new AutoManipalSpecialCasesEntity();
			entity.setState(request.getState());
			entity.setVehicleType("USED");
			entity.setName(row.getChannelName());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			apsList.add(entity);
		}
		for (ApsRowRequest row : request.getApsKLRows()) {
			AutoManipalSpecialCasesEntity entity = new AutoManipalSpecialCasesEntity();
			entity.setState(request.getState());
			entity.setVehicleType("USED");
			entity.setName(row.getChannelName());
			entity.setStatus("N");
			entity.setCreatedBy("SYSTEM");
			entity.setCreatedDate(LocalDate.now());
			entity.setCycleFromDate(request.getCycleFromDate());
			entity.setCycleToDate(request.getCycleToDate());
			entity.setSequence(nextSequence);
			apsList.add(entity);
		}
		manipalSpecialRepository.saveAll(apsList);

	}

	@Override
	public List<MPKCommonPendingSummary> getPendingMPOtherList() {
		// TODO Auto-generated method stub
		return manipalSpecialRepository.findPendingSummaryOther();
	}

	@Override
	public void approveOtherMP(Integer sequence) {
		// TODO Auto-generated method stub
		List<AutoManipalSpecialCasesEntity> autoManipalSpecialCasesEntityList = manipalSpecialRepository
				.findBySequence(sequence);
		for (AutoManipalSpecialCasesEntity data : autoManipalSpecialCasesEntityList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("Y");
			manipalSpecialRepository.save(data);
		}
	}

	@Override
	public void rejectOtherMP(Integer sequence) {
		// TODO Auto-generated method stub
		List<AutoManipalSpecialCasesEntity> autoManipalSpecialCasesEntityList = manipalSpecialRepository
				.findBySequence(sequence);
		for (AutoManipalSpecialCasesEntity data : autoManipalSpecialCasesEntityList) {
			data.setCheckedBy("CHECKER");
			data.setCheckedDate(LocalDate.now());
			data.setStatus("R");
			manipalSpecialRepository.save(data);
		}
	}

	@Override
	public List<IPKPendingSummary> getIPPendingList() {
		// TODO Auto-generated method stub
		return repository.findPendingSummary();
	}

	@Override
	 @Transactional
	    public List<AutoManipalRetainerMaster> saveRetainerMaster(RetainerPayloadDto payload) {
	        List<AutoManipalRetainerMaster> entitiesToSave = new ArrayList<>();
	 
	        String cycleFromDate = payload.getCycleFromDate();
	        String cycleToDate = payload.getCycleToDate();
	        // Get current user (fallback to "SYSTEM")
	        String currentUser = (payload.getUser() != null) ? payload.getUser() : "SYSTEM";
	 
	        // Handle sequence generation (Long type safely handling null max)
	        Long latestSequence = retainerRepository.findMaxSequnce();
	        Long nextSeq = (latestSequence == null) ? 1L : latestSequence + 1L;
	 
	        if (payload.getTableData() != null) {
	            for (RetainerPayloadDto.SlabRowDto row : payload.getTableData()) {
	 
	                // 1. Map NEW_CAR / INBOUND record
	                if (row.getNewCarCasesFrom() != null) {
	                    AutoManipalRetainerMaster newCarRecord = new AutoManipalRetainerMaster();
	                    newCarRecord.setSlab(row.getSLAB());
	                    newCarRecord.setCaseType("INBOUND_OUTBOUND");
	                    newCarRecord.setCasesFrom(row.getNewCarCasesFrom());
	                    newCarRecord.setCasesTo(row.getNewCarCasesTo());
	                    newCarRecord.setCurrentFixedSalaryPerc(row.getCurrentFixedSalaryPerc());
	                    newCarRecord.setPerCaseAmount(row.getPerCaseAmount());
	                    newCarRecord.setRemarks(row.getRemark());
	                    newCarRecord.setCreatedBy(currentUser);
	                    newCarRecord.setCreatedDate(LocalDateTime.now());
	                    newCarRecord.setSequence(nextSeq);
	                    newCarRecord.setCurrentIncentivePerc(row.getCurrentIncentivePerc());
	                    newCarRecord.setStatus("N");
	                    
	                    newCarRecord.setCycleFromDate(cycleFromDate);
	                    newCarRecord.setCycleToDate(cycleToDate);
	                    
	                    entitiesToSave.add(newCarRecord);
	                }
	 
	                // 2. Map USED_CAR record
	                if (row.getUsedCarCasesFrom() != null) {
	                    AutoManipalRetainerMaster usedCarRecord = new AutoManipalRetainerMaster();
	                    usedCarRecord.setSlab(row.getSLAB());
	                    usedCarRecord.setCaseType("USED");
	                    usedCarRecord.setCasesFrom(row.getUsedCarCasesFrom());
	                    usedCarRecord.setCasesTo(row.getUsedCarCasesTo());
	                    usedCarRecord.setCurrentFixedSalaryPerc(row.getCurrentFixedSalaryPerc());
	                    usedCarRecord.setPerCaseAmount(row.getPerCaseAmount());
	                    usedCarRecord.setRemarks(row.getRemark());
	                    usedCarRecord.setCreatedBy(currentUser);
	                    usedCarRecord.setCreatedDate(LocalDateTime.now());
	                    usedCarRecord.setSequence(nextSeq);
	                    usedCarRecord.setStatus("P");
	                    usedCarRecord.setCurrentIncentivePerc(row.getCurrentIncentivePerc());

	 
	                    usedCarRecord.setCycleFromDate(cycleFromDate);
	                    usedCarRecord.setCycleToDate(cycleToDate);
	                    entitiesToSave.add(usedCarRecord);
	                }
	            }
	        }
	 
	        // Batch save all constructed entities & return list
	        return retainerRepository.saveAll(entitiesToSave);
	    }

	@Override
	public void saveIncentiveStructuresAll(IncentiveStructureDto requestDto) {
		// TODO Auto-generated method stub
		// Transform incoming rows and inject the parent header dates directly into each
		// entity row
		List<IncentiveStructure> structureEntities = requestDto.getDtos().stream().map(dto -> {
			IncentiveStructure entity = new IncentiveStructure();
//		            

			// Map header timeline dates directly into the individual record row fields
			entity.setCycleFromDate(requestDto.getCycleFromDate());
			entity.setCycleToDate(requestDto.getCycleToDate());

			// Get current user (fallback to "SYSTEM")
			String currentUser = (requestDto.getCreatedBy() != null) ? requestDto.getCreatedBy() : "SYSTEM";
			// Handle sequence generation (Long type safely handling null max)
			Integer latestSequence = incentiveStructureRepository.findMaxSequence();
			Integer nextSeq = (latestSequence == null) ? 1 : latestSequence + 1;

			// Map individual row values from your payload data grid arrays
			// entity.setSeqNo(dto.getSeqNo());
			entity.setSeqNo(nextSeq);
			// System.out.println(nextSeq);
			entity.setSrNo(dto.getSrNo());
			entity.setType(dto.getType());
			entity.setState(dto.getState());
			entity.setFromSlab(dto.getFromSlab());
			entity.setToSlab(dto.getToSlab());
			entity.setPercentage(dto.getPercentage());
			entity.setFixedAmount(dto.getFixedAmount());
			entity.setMaximumIncentive(dto.getMaxIncentive());

			entity.setCapping(dto.getCapping());
			entity.setMaximumSalaryCap(dto.getMaxSalaryCap());
			entity.setRemark(dto.getRemark());
			entity.setCreatedDate(LocalDate.now());
			entity.setStatus("N");
			entity.setCreatedBy(currentUser);

			return entity;
		}).collect(Collectors.toList());

		// Perform batch storage operations using only your single entity repository
		// layer
		incentiveStructureRepository.saveAll(structureEntities);
	}

}
