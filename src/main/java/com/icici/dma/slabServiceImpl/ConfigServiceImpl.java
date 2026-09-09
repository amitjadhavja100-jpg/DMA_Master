package com.icici.dma.slabServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.configMasterRepository.ConfigHistoryRepository;
import com.icici.dma.configMasterRepository.ConfigMasterRepository;
import com.icici.dma.configMasterRepository.ConfigTempRepository;
import com.icici.dma.slabEntity.CategoryCityMaster;
import com.icici.dma.slabEntity.CeRangeMaster;
import com.icici.dma.slabEntity.CollectionSlabMaster;
import com.icici.dma.slabEntity.ConfigConstants;
import com.icici.dma.slabEntity.ConfigDTO;
import com.icici.dma.slabEntity.ConfigHistory;
import com.icici.dma.slabEntity.ConfigMaster;
import com.icici.dma.slabEntity.ConfigTemp;
import com.icici.dma.slabEntity.PayoutDetail;
import com.icici.dma.slabEntity.PayoutMaster;
import com.icici.dma.slabRepository.CategoryCityRepo;
import com.icici.dma.slabRepository.CeRangeRepo;
import com.icici.dma.slabRepository.PayoutMasterRepo;
import com.icici.dma.slabRepository.SlabRepo;
import com.icici.dma.slabService.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	private ConfigMasterRepository masterRepo;

	@Autowired
	private ConfigTempRepository tempRepo;

	@Autowired
	private ConfigHistoryRepository historyRepo;

	@Autowired
	private SlabRepo slabRepo;

	@Autowired
	private CeRangeRepo ceRangeRepo;

	@Autowired
	private CategoryCityRepo categoryRepo;
	
	@Autowired
	private PayoutMasterRepo payoutMasterRepo;

	@Override
	public List<?> loadConfigs(
	        String status,
	        String type,
	        String category,
	        String city,
	        String dpd,
	        String collection,
	        String ceRange) {

		//if (type != null && !type.isBlank()) {
		if (type != null && !type.trim().isEmpty()) {
			switch (type) {

			case "CATEGORY":

				List<CategoryCityMaster> list;

				if ("ACTIVE".equals(status)) {
					list = categoryRepo.findByStatusOrderByCategoryAsc("Y");
				} else if ("INACTIVE".equals(status)) {
					list = categoryRepo.findByStatusOrderByCategoryAsc("N");
				} else {
					list = categoryRepo.findAll();
				}

				return orderCategories(list);

			case "CITY":
				if ("ACTIVE".equals(status)) {
					return categoryRepo.findByStatusOrderByDisplayOrderAscIdAsc("Y");
				}

				if ("INACTIVE".equals(status)) {
					return categoryRepo.findByStatusOrderByDisplayOrderAscIdAsc("N");
				}

//				return categoryRepo.findAllByOrderByDisplayOrderAscIdAsc();
				List<CategoryCityMaster> list1 = categoryRepo.findAllByOrderByDisplayOrderAscIdAsc();

				//if(category != null && !category.isBlank()) {
				if (category != null && !category.trim().isEmpty()) {
					list1 = list1.stream()
				            .filter(x -> category.equals(x.getCategory()))
				            .collect(Collectors.toList());
				}

				return list1;

			case "DPD":
				if ("ACTIVE".equals(status)) {
					return slabRepo.findByStatusOrderByCategoryAscDpdAscOrderNoAsc("Y");
				}

				if ("INACTIVE".equals(status)) {
					return slabRepo.findByStatusOrderByCategoryAscDpdAscOrderNoAsc("N");
				}

				//return slabRepo.findAllByOrderByCategoryAscDpdAscOrderNoAsc();
				List<CollectionSlabMaster> list2 =
				        slabRepo.findAllByOrderByCategoryAscDpdAscOrderNoAsc();

				//if(category != null && !category.isBlank()) {
				if (category != null && !category.trim().isEmpty()) {
				    list2 = list2.stream()
				            .filter(x -> category.equals(x.getCategory()))
				            .collect(Collectors.toList());
				}

				return list2;
				
			case "COLLECTION":
				if ("ACTIVE".equals(status)) {
					return slabRepo.findByStatusOrderByCategoryAscDpdAscOrderNoAsc("Y");
				}

				if ("INACTIVE".equals(status)) {
					return slabRepo.findByStatusOrderByCategoryAscDpdAscOrderNoAsc("N");
				}

				//return slabRepo.findAllByOrderByCategoryAscDpdAscOrderNoAsc();
				List<CollectionSlabMaster> list3 =
				        slabRepo.findAllByOrderByCategoryAscDpdAscOrderNoAsc();

				//if(category != null && !category.isBlank()) {
				if (category != null && !category.trim().isEmpty()) {
				    list3 = list3.stream()
				            .filter(x -> category.equals(x.getCategory()))
				            .collect(Collectors.toList());
				}

				//if(dpd != null && !dpd.isBlank()) {
				if(dpd != null && !dpd.trim().isEmpty()) {
				    list3 = list3.stream()
				            .filter(x -> dpd.equals(x.getDpd()))
				            .collect(Collectors.toList());
				}

				return list3;

			case "CE_RANGE":
				if ("ACTIVE".equals(status)) {
					return ceRangeRepo.findByStatusOrderByCategoryAscDpdAscOrderNoAsc("Y");
				}

				if ("INACTIVE".equals(status)) {
					return ceRangeRepo.findByStatusOrderByCategoryAscDpdAscOrderNoAsc("N");
				}

				//return ceRangeRepo.findAllByOrderByCategoryAscDpdAscOrderNoAsc();
				List<CeRangeMaster> list4 =
				        ceRangeRepo.findAllByOrderByCategoryAscDpdAscOrderNoAsc();

				//if(category != null && !category.isBlank()) {
				if (category != null && !category.trim().isEmpty()) {
				    list4 = list4.stream()
				            .filter(x -> category.equals(x.getCategory()))
				            .collect(Collectors.toList());
				}

				//if(dpd != null && !dpd.isBlank()) {
				if(dpd != null && !dpd.trim().isEmpty()) {
				    list4 = list4.stream()
				            .filter(x -> dpd.equals(x.getDpd()))
				            .collect(Collectors.toList());
				}

				return list4;

			default:
			    // For UI Config Types
			    if ("FIELD".equals(type) ||
			        "BUTTON".equals(type) ||
			        "TITLE".equals(type) ||
			        "SCREEN".equals(type)) {
			        if ("ACTIVE".equals(status)) {
			            return masterRepo.findByConfigTypeAndActiveFlagOrderByDisplayOrderAsc(type, "Y");
			        }
			        if ("INACTIVE".equals(status)) {
			            return masterRepo.findByConfigTypeAndActiveFlagOrderByDisplayOrderAsc(type, "N");
			        }
			        return masterRepo.findByConfigTypeOrderByDisplayOrderAsc(type);
			    }
			    // ===== Existing Code =====
			    if ("ACTIVE".equals(status)) {
			        return masterRepo.findByActiveFlagOrderByDisplayOrder("Y");
			    }
			    if ("INACTIVE".equals(status)) {
			        return masterRepo.findByActiveFlagOrderByDisplayOrder("N");
			    }
			    return masterRepo.findAllByOrderByDisplayOrderAsc();
			    //////
			}
		}

		if ("ACTIVE".equals(status)) {
			return masterRepo.findByActiveFlagOrderByDisplayOrder("Y");
		}

		if ("INACTIVE".equals(status)) {
			return masterRepo.findByActiveFlagOrderByDisplayOrder("N");
		}

		return masterRepo.findAllByOrderByDisplayOrderAsc();
	}
	
	@Override
	public List<ConfigTemp> getPending(String user){
	 
	    return tempRepo
	            .findByStatusAndMakerIdNotOrderByMakerDateDesc(
	                    ConfigConstants.PENDING,
	                    user);
	 
	}

	@Override
	public void save(ConfigDTO dto, String user) {
		
		if ("CAPPING".equals(dto.getConfigType())) {
		    dto.setConfigKey("CAPPING");
		    dto.setParentKey("SYSTEM");
		    dto.setDisplayOrder(1);
		}

		if ("FIELD".equals(dto.getConfigType()) || "BUTTON".equals(dto.getConfigType())
				|| "SCREEN".equals(dto.getConfigType()) || "TITLE".equals(dto.getConfigType())) {

			Optional<ConfigMaster> existing = masterRepo.findByConfigTypeAndConfigKey(dto.getConfigType(),
					dto.getConfigKey());

			if (existing.isPresent()) {
				throw new RuntimeException(dto.getConfigKey() + " already exists.");
			}
		}

		ConfigTemp temp = new ConfigTemp();

		temp.setModuleName(dto.getModuleName());
		temp.setScreenName("CONFIG_PAYOUT_STRUCTURE");

		temp.setConfigType(dto.getConfigType());

		temp.setCategory(dto.getCategory());

		temp.setCity(dto.getCity());

		temp.setDpd(dto.getDpd());

		temp.setParentKey(dto.getParentKey());

		temp.setConfigKey(dto.getConfigKey());
		
		temp.setConfigKey(dto.getConfigKey());
		temp.setOldValue(dto.getOldValue());
		temp.setNewValue(dto.getConfigValue());
		temp.setStatus(ConfigConstants.PENDING);
		temp.setActionType(ConfigConstants.ADD);

		Integer displayOrder = dto.getDisplayOrder();

		if (displayOrder == null) {

			Integer maxOrder = masterRepo.findMaxDisplayOrder();

			displayOrder =

					(maxOrder == null)

							? 1

							: maxOrder + 1;
		}

		temp.setDisplayOrder(displayOrder);

		temp.setActiveFlag("Y");

		temp.setActionType(ConfigConstants.ADD);

		temp.setStatus(ConfigConstants.PENDING);

		temp.setMakerId(user);

		temp.setMakerDate(new Date());

		// CATEGORY Validation
		if ("CATEGORY".equals(dto.getConfigType())) {

			if (categoryRepo.findByCategoryAndCity(dto.getCategory(), "NA").isPresent()) {
				throw new RuntimeException("Category '" + dto.getCategory() + "' already exists.");
			}

			Optional<CategoryCityMaster> order = categoryRepo.findByDisplayOrder(dto.getDisplayOrder());

			if (order.isPresent()) {
				throw new RuntimeException("Display Order " + dto.getDisplayOrder()
						+ " is already assigned to Category '" + order.get().getCategory() + "'.");
			}
		}

		if ("CITY".equals(dto.getConfigType())) {

			if (categoryRepo.findByCategoryAndCity(dto.getCategory(), dto.getCity()).isPresent()) {

				throw new RuntimeException(
						"City '" + dto.getCity() + "' already exists under Category '" + dto.getCategory() + "'.");
			}
			Optional<CategoryCityMaster> order = categoryRepo.findCityByDisplayOrder(dto.getDisplayOrder());

			if (order.isPresent()) {

				throw new RuntimeException("Display Order " + dto.getDisplayOrder() + " is already assigned to City '"
						+ order.get().getCity() + "' under Category '" + order.get().getCategory()
						+ "'. Please choose another Display Order.");
			}
		}

		// DPD Validation
		if ("DPD".equals(dto.getConfigType())) {

			if (slabRepo.findByCategoryAndDpdAndOrderNo(dto.getCategory(), dto.getDpd(), dto.getDisplayOrder())
					.isPresent()) {

				throw new RuntimeException("Display Order " + dto.getDisplayOrder() + " already exists for Category '"
						+ dto.getCategory() + "'.");
			}

			if (slabRepo.findFirstByCategoryAndDpd(dto.getCategory(), dto.getDpd()).isPresent()) {

				throw new RuntimeException(
						"DPD '" + dto.getDpd() + "' already exists under Category '" + dto.getCategory() + "'.");
			}
		}

		if ("COLLECTION".equals(dto.getConfigType())) {
			validateCollectionFormat(dto.getConfigValue());
			// Duplicate Collection
			if (slabRepo.findByCategoryAndDpdAndSlabValue(dto.getCategory(), dto.getDpd(), dto.getConfigValue())
					.isPresent()) {

				throw new RuntimeException("Collection '" + dto.getConfigValue() + "' already exists.");
			}

			// Display Order Validation
			Optional<CollectionSlabMaster> order = slabRepo.findByCategoryAndDpdAndOrderNo(dto.getCategory(),
					dto.getDpd(), dto.getDisplayOrder());

			if (order.isPresent()) {

				// Ignore placeholder row (SLAB_VALUE = NULL)
				if (order.get().getSlabValue() != null) {

					throw new RuntimeException("Display Order " + dto.getDisplayOrder() + " already exists.");

				}
			}
		}

		// CE RANGE Validation
		if ("CE_RANGE".equals(dto.getConfigType())) {

			// Validate format first
			validateCeRangeFormat(dto.getConfigValue());

			if (ceRangeRepo.findByCategoryAndDpdAndRangeValue(dto.getCategory(), dto.getDpd(), dto.getConfigValue())
					.isPresent()) {

				throw new RuntimeException("CE Range '" + dto.getConfigValue() + "' already exists.");
			}
			Optional<CeRangeMaster> order = ceRangeRepo.findByCategoryAndDpdAndOrderNo(dto.getCategory(), dto.getDpd(),
					dto.getDisplayOrder());

			if (order.isPresent()) {

				// Ignore placeholder row (RANGE_VALUE = NULL)
				if (order.get().getRangeValue() != null) {

					throw new RuntimeException("Display Order " + dto.getDisplayOrder() + " already exists.");

				}
			}
		}

		if ("CAPPING".equals(dto.getConfigType())) {
			if (dto.getConfigValue() == null || dto.getConfigValue().trim().isEmpty()) {
				throw new RuntimeException("New Value is required.");
			}
		}

		tempRepo.save(temp);
	}

	@Override
	public void update(ConfigDTO dto, String user) {

		if ("CATEGORY".equals(dto.getConfigType())) {
			CategoryCityMaster entity = categoryRepo.findById(dto.getConfigId())
				    .orElseThrow(() -> new RuntimeException("Category Config Master not found"));
			ConfigTemp temp = new ConfigTemp();
			
//			ConfigMaster master =
//					masterRepo.findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
//					        "CATEGORY",
//					        entity.getCategory())
//					.orElseThrow(() ->
//						new RuntimeException("Category Config Master not found"));
//			
//					//temp.setConfigId(master.getConfigId());
//			temp.setConfigId(entity.getId());
			
			Optional<ConfigMaster> master =
			        masterRepo.findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
			                "CATEGORY",
			                entity.getCategory());

			if (master.isPresent()) {
			    temp.setConfigId(master.get().getConfigId());
			} else {
			    temp.setConfigId(entity.getId());
			}
			
			temp.setConfigType("CATEGORY");
			temp.setCategory(entity.getCategory());
			temp.setConfigKey(entity.getCategory());
			temp.setOldValue(entity.getCategory());
			temp.setNewValue(dto.getCategory());
			temp.setDisplayOrder(dto.getDisplayOrder());
			temp.setActionType(ConfigConstants.UPDATE);
			temp.setStatus(ConfigConstants.PENDING);
			temp.setMakerId(user);
			temp.setMakerDate(new Date());
			tempRepo.save(temp);
			return;
		}

		if ("CITY".equals(dto.getConfigType())) {
			CategoryCityMaster entity = categoryRepo.findById(dto.getConfigId())
					.orElseThrow(() ->
	                new RuntimeException("City not found"));
			ConfigTemp temp = new ConfigTemp();
			//temp.setConfigId(entity.getId());
//			ConfigMaster master =
//					masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
//					        "CITY",
//					        entity.getCity())
//					.orElseThrow(() ->
//	                new RuntimeException("City Config Master not found"));
//
//					//temp.setConfigId(master.getConfigId());
//			temp.setConfigId(entity.getId());
			
			Optional<ConfigMaster> master =
			        masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
			                "CITY",
			                entity.getCity());

			if (master.isPresent()) {
			    temp.setConfigId(master.get().getConfigId());
			} else {
			    temp.setConfigId(entity.getId());
			}
			
			temp.setConfigType("CITY");
			temp.setCategory(entity.getCategory());
			temp.setCity(entity.getCity());
			temp.setConfigKey(entity.getCity());
			temp.setOldValue(entity.getCity());
			temp.setNewValue(dto.getCity());
			temp.setDisplayOrder(dto.getDisplayOrder());
			temp.setActionType(ConfigConstants.UPDATE);
			temp.setStatus(ConfigConstants.PENDING);
			temp.setMakerId(user);
			temp.setMakerDate(new Date());
			tempRepo.save(temp);
			return;
		}

		if ("DPD".equals(dto.getConfigType())) {
			CollectionSlabMaster entity = slabRepo.findById(dto.getConfigId()).orElseThrow(() ->
			                new RuntimeException("DPD not found"));
			ConfigTemp temp = new ConfigTemp();
			//temp.setConfigId(entity.getId());
//			ConfigMaster master =
//					masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
//					        "DPD",
//					        entity.getDpd()).orElseThrow(() ->
//			                new RuntimeException("DPD Config Master not found"));
//
//					//temp.setConfigId(master.getConfigId());
//			temp.setConfigId(entity.getId());
			
			Optional<ConfigMaster> master =
			        masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
			                "DPD",
			                entity.getDpd());

			if (master.isPresent()) {
			    temp.setConfigId(master.get().getConfigId());
			} else {
			    temp.setConfigId(entity.getId());
			}
			
			temp.setConfigType("DPD");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getDpd());
			temp.setOldValue(entity.getDpd());
			temp.setNewValue(dto.getDpd());
			temp.setDisplayOrder(dto.getDisplayOrder());
			temp.setActionType(ConfigConstants.UPDATE);
			temp.setStatus(ConfigConstants.PENDING);
			temp.setMakerId(user);
			temp.setMakerDate(new Date());
			tempRepo.save(temp);
			return;
		}

		if ("COLLECTION".equals(dto.getConfigType())) {
			CollectionSlabMaster entity = slabRepo.findById(dto.getConfigId()).orElseThrow(() ->
			                new RuntimeException("Collection not found"));
			
			ConfigTemp temp = new ConfigTemp();
			
//			ConfigMaster master =
//				    masterRepo.findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
//				            "COLLECTION",
//				            entity.getSlabValue())
//				    .orElseThrow(() ->
//				            new RuntimeException("Collection Config Master not found"));
//			
//					//temp.setConfigId(master.getConfigId());
//			temp.setConfigId(entity.getId());
			
			Optional<ConfigMaster> master =
			        masterRepo.findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
			                "COLLECTION",
			                entity.getSlabValue());

			if (master.isPresent()) {
			    temp.setConfigId(master.get().getConfigId());
			} else {
			    temp.setConfigId(entity.getId());
			}
			
			temp.setConfigType("COLLECTION");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getSlabValue());
			temp.setOldValue(entity.getSlabValue());
			temp.setNewValue(dto.getConfigValue());
			temp.setDisplayOrder(dto.getDisplayOrder());
			temp.setActionType(ConfigConstants.UPDATE);
			temp.setStatus(ConfigConstants.PENDING);
			temp.setMakerId(user);
			temp.setMakerDate(new Date());
			tempRepo.save(temp);
			return;
		}
		
		if ("CE_RANGE".equals(dto.getConfigType())) {

		    CeRangeMaster entity = ceRangeRepo.findById(dto.getConfigId())
		            .orElseThrow(() ->
		                    new RuntimeException("CE Range not found"));

		    ConfigTemp temp = new ConfigTemp();

		    Optional<ConfigMaster> master =
		            masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
		                    "CE_RANGE",
		                    entity.getRangeValue());

		    if (master.isPresent()) {
		        temp.setConfigId(master.get().getConfigId());
		    } else {
		        temp.setConfigId(entity.getId());
		    }

		    temp.setConfigType("CE_RANGE");
		    temp.setCategory(entity.getCategory());
		    temp.setDpd(entity.getDpd());
		    temp.setConfigKey(entity.getRangeValue());
		    temp.setOldValue(entity.getRangeValue());
		    temp.setNewValue(dto.getConfigValue());
		    temp.setDisplayOrder(dto.getDisplayOrder());
		    temp.setActionType(ConfigConstants.UPDATE);
		    temp.setStatus(ConfigConstants.PENDING);
		    temp.setMakerId(user);
		    temp.setMakerDate(new Date());

		    tempRepo.save(temp);
		    return;
		}

		ConfigMaster master = masterRepo.findByConfigId(dto.getConfigId())
				.orElseThrow(() ->
                new RuntimeException("Collection not found"));

		ConfigTemp temp = new ConfigTemp();

		temp.setConfigId(master.getConfigId());

		temp.setModuleName(master.getModuleName());

		temp.setScreenName(master.getScreenName());

		temp.setConfigType(master.getConfigType());

		temp.setCategory(dto.getCategory());

		temp.setCity(dto.getCity());

		temp.setDpd(dto.getDpd());

		temp.setParentKey(dto.getParentKey());

		if ("CATEGORY".equals(dto.getConfigType())) {

			temp.setConfigKey(dto.getCategory());
			temp.setOldValue(master.getConfigValue());
			temp.setNewValue(dto.getCategory());

		} else if ("CITY".equals(dto.getConfigType())) {

			temp.setConfigKey(dto.getCity());
			temp.setOldValue(master.getConfigValue());
			temp.setNewValue(dto.getCity());

		} else if ("DPD".equals(dto.getConfigType())) {

			temp.setConfigKey(dto.getDpd());
			temp.setOldValue(master.getConfigValue());
			temp.setNewValue(dto.getDpd());

		} else {

			temp.setConfigKey(master.getConfigKey());
			temp.setOldValue(master.getConfigValue());
			temp.setNewValue(dto.getConfigValue());

		}

		temp.setDisplayOrder(dto.getDisplayOrder());

		temp.setActiveFlag(master.getActiveFlag());

		temp.setActionType(ConfigConstants.UPDATE);

		temp.setStatus(ConfigConstants.PENDING);

		temp.setMakerId(user);

		temp.setMakerDate(new Date());

		tempRepo.save(temp);
	}

	@Override
	public void activate(String type, Long id, String user) {

		ConfigTemp temp = new ConfigTemp();

		temp.setActionType(ConfigConstants.ACTIVATE);
		temp.setStatus(ConfigConstants.PENDING);
		temp.setMakerId(user);
		temp.setMakerDate(new Date());

		switch (type) {

		case "CATEGORY": {

			CategoryCityMaster entity = categoryRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("Category not found"));

			temp.setConfigId(entity.getId());
			temp.setConfigType("CATEGORY");
			temp.setCategory(entity.getCategory());
			temp.setCity(entity.getCity());
			temp.setConfigKey(entity.getCategory());
			temp.setOldValue(entity.getCategory());
			temp.setNewValue(entity.getCategory());
			temp.setDisplayOrder(entity.getDisplayOrder());

			break;
		}

		case "CITY": {
			CategoryCityMaster entity = categoryRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("City not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("CITY");
			temp.setCategory(entity.getCategory());
			temp.setCity(entity.getCity());
			temp.setConfigKey(entity.getCity());
			temp.setOldValue(entity.getCity());
			temp.setNewValue(entity.getCity());
			temp.setDisplayOrder(entity.getDisplayOrder());
			break;
		}

		case "DPD": {
			CollectionSlabMaster entity = slabRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("DPD not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("DPD");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getDpd());
			temp.setOldValue(entity.getDpd());
			temp.setNewValue(entity.getDpd());
			temp.setDisplayOrder(entity.getOrderNo());
			break;
		}

		case "COLLECTION": {
			CollectionSlabMaster entity = slabRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("Collection not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("COLLECTION");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getSlabValue());
			temp.setOldValue(entity.getSlabValue());
			temp.setNewValue(entity.getSlabValue());
			temp.setDisplayOrder(entity.getOrderNo());
			break;
		}

		case "CE_RANGE": {
			CeRangeMaster entity = ceRangeRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("CE Range not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("CE_RANGE");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getRangeValue());
			temp.setOldValue(entity.getRangeValue());
			temp.setNewValue(entity.getRangeValue());
			temp.setDisplayOrder(entity.getOrderNo());
			break;
		}

		default: {

			ConfigMaster master = masterRepo.findByConfigId(id)
					.orElseThrow(() ->
	                new RuntimeException("Configuration not found"));
			temp.setConfigId(master.getConfigId());
			temp.setModuleName(master.getModuleName());
			temp.setScreenName(master.getScreenName());
			temp.setConfigType(master.getConfigType());
			temp.setCategory(master.getCategory());
			temp.setCity(master.getCity());
			temp.setDpd(master.getDpd());
			temp.setParentKey(master.getParentKey());
			temp.setDisplayOrder(master.getDisplayOrder());
			temp.setConfigKey(master.getConfigKey());
			temp.setOldValue(master.getConfigValue());
			temp.setNewValue(master.getConfigValue());

			break;
		}

		}

		tempRepo.save(temp);
	}

	@Override
	public void deactivate(String type, Long id, String user) {

		ConfigTemp temp = new ConfigTemp();

		temp.setActionType(ConfigConstants.DEACTIVATE);
		temp.setStatus(ConfigConstants.PENDING);
		temp.setMakerId(user);
		temp.setMakerDate(new Date());

		switch (type) {

		case "CATEGORY": {

			CategoryCityMaster entity = categoryRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("Category not found"));

			temp.setConfigId(entity.getId());
			temp.setConfigType("CATEGORY");
			temp.setCategory(entity.getCategory());
			temp.setCity(entity.getCity());
			temp.setConfigKey(entity.getCategory());
			temp.setOldValue(entity.getCategory());
			temp.setNewValue(entity.getCategory());
			temp.setDisplayOrder(entity.getDisplayOrder());

			break;
		}

		case "CITY": {
			CategoryCityMaster entity = categoryRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("City not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("CITY");
			temp.setCategory(entity.getCategory());
			temp.setCity(entity.getCity());
			temp.setConfigKey(entity.getCity());
			temp.setOldValue(entity.getCity());
			temp.setNewValue(entity.getCity());
			temp.setDisplayOrder(entity.getDisplayOrder());
			break;
		}

		case "DPD": {
			CollectionSlabMaster entity = slabRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("DPD not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("DPD");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getDpd());
			temp.setOldValue(entity.getDpd());
			temp.setNewValue(entity.getDpd());
			temp.setDisplayOrder(entity.getOrderNo());
			break;
		}

		case "COLLECTION": {
			CollectionSlabMaster entity = slabRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("Collection not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("COLLECTION");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getSlabValue());
			temp.setOldValue(entity.getSlabValue());
			temp.setNewValue(entity.getSlabValue());
			temp.setDisplayOrder(entity.getOrderNo());
			break;
		}

		case "CE_RANGE": {
			CeRangeMaster entity = ceRangeRepo.findById(id)
					.orElseThrow(() ->
	                new RuntimeException("CE Range not found"));
			temp.setConfigId(entity.getId());
			temp.setConfigType("CE_RANGE");
			temp.setCategory(entity.getCategory());
			temp.setDpd(entity.getDpd());
			temp.setConfigKey(entity.getRangeValue());
			temp.setOldValue(entity.getRangeValue());
			temp.setNewValue(entity.getRangeValue());
			temp.setDisplayOrder(entity.getOrderNo());
			break;
		}

		default: {

			ConfigMaster master = masterRepo.findByConfigId(id)
					.orElseThrow(() ->
	                new RuntimeException("Configuration not found"));

			temp.setConfigId(master.getConfigId());
			temp.setModuleName(master.getModuleName());
			temp.setScreenName(master.getScreenName());
			temp.setConfigType(master.getConfigType());
			temp.setCategory(master.getCategory());
			temp.setCity(master.getCity());
			temp.setDpd(master.getDpd());
			temp.setParentKey(master.getParentKey());
			temp.setDisplayOrder(master.getDisplayOrder());
			temp.setConfigKey(master.getConfigKey());
			temp.setOldValue(master.getConfigValue());
			temp.setNewValue(master.getConfigValue());

			break;
		}

		}
		tempRepo.save(temp);
	}

	@Override
	public void approve(Long tempId, String user) {

		ConfigTemp temp = tempRepo.findByTempId(tempId)
				.orElseThrow(() ->
                new RuntimeException("Pending request not found"));

		if (user.equals(temp.getMakerId())) {

			throw new RuntimeException("Maker and Checker cannot be same");
		}

		// 1. Save / Update CONFIG_MASTER
		ConfigMaster master = saveMaster(temp);

		// 2. Sync Business Tables
		switch (temp.getConfigType()) {

		case "CATEGORY":
			processCategory(temp);
			break;

		case "CITY":
			processCity(temp);
			break;

		case "DPD":
			processDpd(temp);
			break;

		case "COLLECTION":
			processCollection(temp);
			break;

		case "CE_RANGE":
			processCeRange(temp);
			break;
			
		case "CAPPING":
			break;

		case "FIELD":
		case "BUTTON":
		case "TITLE":
		case "SCREEN":
			// UI Configuration Only
			break;

		default:
			throw new RuntimeException("Invalid Config Type : " + temp.getConfigType());
		}

		// 3. History
		createHistory(master, temp, user);
		syncPayoutStructure(temp);	
		// 4. Remove pending request
		tempRepo.delete(temp);

	}

	@Override
	public void reject(Long tempId, String user) {

		ConfigTemp temp = tempRepo.findByTempId(tempId)
				.orElseThrow(() ->
                new RuntimeException("Pending request not found"));

		if (user.equals(temp.getMakerId())) {

			throw new RuntimeException("Maker and Checker cannot be same");
		}

		temp.setStatus(ConfigConstants.REJECTED);

		tempRepo.save(temp);
	}

	@Override
	public ConfigTemp compare(Long tempId) {

		return tempRepo.findByTempId(tempId)
				.orElseThrow(() ->
                new RuntimeException("Pending request not found"));
	}

	private void createHistory(ConfigMaster master, ConfigTemp temp, String user) {

		ConfigHistory history = new ConfigHistory();

		history.setConfigId(master.getConfigId());

		history.setModuleName(master.getModuleName());

		history.setScreenName(master.getScreenName());

		history.setConfigType(master.getConfigType());

		history.setCategory(master.getCategory());

		history.setCity(master.getCity());

		history.setDpd(master.getDpd());

		history.setConfigKey(master.getConfigKey());

		history.setActionType(temp.getActionType());

		history.setOldValue(temp.getOldValue());

		history.setNewValue(temp.getNewValue());

		history.setStatus(ConfigConstants.APPROVED);

		history.setCheckerId(user);

		history.setCheckerDate(new Date());

		history.setRemarks("Approved Successfully");

		historyRepo.save(history);
	}

	private ConfigMaster saveMaster(ConfigTemp temp) {

		ConfigMaster master;

		if ("CATEGORY".equals(temp.getConfigType())) {

//		    master = masterRepo
//		            .findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
//		                    "CATEGORY",
//		                    temp.getOldValue())
//		            .orElse(new ConfigMaster());
			Optional<ConfigMaster> existing =
			        masterRepo.findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
			                "CATEGORY",
			                temp.getOldValue());

			if (existing.isPresent()) {
			    master = existing.get();
			} else {
			    master = new ConfigMaster();
			    master.setConfigType("CATEGORY");
			    master.setCategory(temp.getCategory());
			    master.setConfigKey(temp.getConfigKey());
			    master.setModuleName(temp.getModuleName());
			    master.setScreenName(temp.getScreenName());
			    master.setParentKey(temp.getParentKey());
			}

		}
		else if ("CITY".equals(temp.getConfigType())) {

//		    master = masterRepo
//		            .findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
//		                    "CITY",
//		                    temp.getOldValue())
//		            .orElse(new ConfigMaster());

			Optional<ConfigMaster> existing =
			        masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
			                "CITY",
			                temp.getOldValue());

			if (existing.isPresent()) {
			    master = existing.get();
			} else {
			    master = new ConfigMaster();
			    master.setConfigType("CITY");
			    master.setCategory(temp.getCategory());
			    master.setCity(temp.getCity());
			    master.setConfigKey(temp.getConfigKey());
			    master.setModuleName(temp.getModuleName());
			    master.setScreenName(temp.getScreenName());
			    master.setParentKey(temp.getParentKey());
			}
		}
		
		else if ("DPD".equals(temp.getConfigType())) {

//		    master = masterRepo
//		            .findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
//		                    "DPD",
//		                    temp.getOldValue())
//		            .orElse(new ConfigMaster());
			
			Optional<ConfigMaster> existing =
			        masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
			                "DPD",
			                temp.getOldValue());

			if (existing.isPresent()) {
			    master = existing.get();
			} else {
			    master = new ConfigMaster();
			    master.setConfigType("DPD");
			    master.setCategory(temp.getCategory());
			    master.setDpd(temp.getDpd());
			    master.setConfigKey(temp.getConfigKey());
			    master.setModuleName(temp.getModuleName());
			    master.setScreenName(temp.getScreenName());
			    master.setParentKey(temp.getParentKey());
			}

		}
		else if ("COLLECTION".equals(temp.getConfigType())) {

//		    master = masterRepo
//		            .findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
//		                    "COLLECTION",
//		                    temp.getOldValue())
//		            .orElse(new ConfigMaster());

			Optional<ConfigMaster> existing =
			        masterRepo.findTopByConfigTypeAndConfigValueOrderByConfigIdDesc(
			                "COLLECTION",
			                temp.getOldValue());

			if (existing.isPresent()) {
			    master = existing.get();
			} else {
			    master = new ConfigMaster();
			    master.setConfigType("COLLECTION");
			    master.setCategory(temp.getCategory());
			    master.setDpd(temp.getDpd());
			    master.setConfigKey(temp.getConfigKey());
			    master.setModuleName(temp.getModuleName());
			    master.setScreenName(temp.getScreenName());
			    master.setParentKey(temp.getParentKey());
			}
		}
		
		else if ("CE_RANGE".equals(temp.getConfigType())) {

		    Optional<ConfigMaster> existing =
		            masterRepo.findTopByConfigTypeAndConfigKeyOrderByConfigIdDesc(
		                    "CE_RANGE",
		                    temp.getOldValue());

		    if (existing.isPresent()) {
		        master = existing.get();
		    } else {
		        master = new ConfigMaster();

		        master.setConfigType("CE_RANGE");
		        master.setCategory(temp.getCategory());
		        master.setDpd(temp.getDpd());
		        master.setConfigKey(temp.getConfigKey());
		        master.setModuleName(temp.getModuleName());
		        master.setScreenName(temp.getScreenName());
		        master.setParentKey(temp.getParentKey());
		    }
		}
		
		else if (temp.getConfigId() != null) {

		    master = masterRepo
		            .findByConfigId(temp.getConfigId())
		            .orElse(new ConfigMaster());

		}
		else {

		    master = masterRepo
		            .findByConfigTypeAndConfigKey(
		                    temp.getConfigType(),
		                    temp.getConfigKey())
		            .orElse(new ConfigMaster());

		}
		
		// Common Fields

		master.setModuleName(temp.getModuleName());

		master.setScreenName(temp.getScreenName());

		master.setConfigType(temp.getConfigType());

		master.setCategory(temp.getCategory());

		master.setCity(temp.getCity());

		master.setDpd(temp.getDpd());

		master.setParentKey(temp.getParentKey());

		master.setDisplayOrder(temp.getDisplayOrder());

		master.setConfigKey(temp.getConfigKey());

		if("CAPPING".equals(temp.getConfigType())){
		    // Existing approved value becomes OLD_VALUE
		    if(master.getConfigValue() != null){
		        master.setOldValue(master.getConfigValue());
		    }else{
		        master.setOldValue(null);
		    }
		    // Save new approved value
		    master.setConfigValue(temp.getNewValue());
		}else{
		    master.setOldValue(temp.getOldValue());
		    master.setConfigValue(temp.getNewValue());
		}

		master.setStatus(ConfigConstants.APPROVED);

		switch (temp.getActionType()) {

		case ConfigConstants.ADD:

			master.setConfigValue(temp.getNewValue());

			master.setActiveFlag("Y");

			break;

		case ConfigConstants.UPDATE:

			master.setConfigValue(temp.getNewValue());

			break;

		case ConfigConstants.ACTIVATE:

			master.setActiveFlag("Y");

			break;

		case ConfigConstants.DEACTIVATE:

			master.setActiveFlag("N");

			break;
		}

		return masterRepo.save(master);
	}

	@Override
	public Map<String, String> getUiConfig() {

		Map<String, String> map = new HashMap<>();

		List<ConfigMaster> list = masterRepo.findAll();

		for (ConfigMaster c : list) {

			if ("Y".equalsIgnoreCase(c.getActiveFlag()) && "APPROVED".equalsIgnoreCase(c.getStatus())) {

				map.put(c.getConfigKey(), c.getConfigValue());
			}
		}

		return map;
	}

	@Override
	public Map<String, String> getLabels() {

		/* List<ConfigMaster> list = masterRepo.findAll(); */
		List<ConfigMaster> list = masterRepo.findByConfigTypeInAndActiveFlagOrderByDisplayOrder(
				Arrays.asList("FIELD", "BUTTON", "SCREEN", "TITLE"), "Y");

		System.out.println("========== CONFIG MASTER ==========");

		for (ConfigMaster c : list) {

			System.out.println(c.getConfigId() + " | " + c.getConfigKey() + " | " + c.getConfigValue() + " | "
					+ c.getActiveFlag());

		}

		Map<String, String> map = new LinkedHashMap<>();

		for (ConfigMaster c : list) {

			if ("Y".equals(c.getActiveFlag())) {

				map.put(c.getConfigKey(), c.getConfigValue());

			}

		}

		System.out.println("FINAL MAP : " + map);

		return map;

	}

	private void processCategory(ConfigTemp temp) {
		if (ConfigConstants.ADD.equals(temp.getActionType())) {
			CategoryCityMaster entity = new CategoryCityMaster();
			entity.setCategory(temp.getNewValue());
			// entity.setCity("NA");
			entity.setCity(null);
			entity.setStatus("Y");
			entity.setDisplayOrder(temp.getDisplayOrder());
			categoryRepo.save(entity);
			// Update Collection Master
			List<CollectionSlabMaster> slabs =
			        slabRepo.findByCategory(temp.getOldValue());

			for (CollectionSlabMaster slab : slabs) {
			    slab.setCategory(temp.getNewValue());
			}

			slabRepo.saveAll(slabs);

			// Update CE Range Master
			List<CeRangeMaster> ranges =
			        ceRangeRepo.findByCategory(temp.getOldValue());

			for (CeRangeMaster range : ranges) {
			    range.setCategory(temp.getNewValue());
			}

			ceRangeRepo.saveAll(ranges);
			return;
		}
		
		CategoryCityMaster entity =
		        categoryRepo.findByCategoryAndCityIsNull(temp.getOldValue())
		        .orElse(null);

		// Fallback if category row with CITY = NULL doesn't exist
		if (entity == null) {
		    entity = categoryRepo.findFirstByCategory(temp.getOldValue())
		            .orElseThrow(() ->
		                    new RuntimeException("Category not found : " + temp.getOldValue()));
		}
		
		switch (temp.getActionType()) {
//		case ConfigConstants.UPDATE:
//			entity.setCategory(temp.getNewValue());
//			entity.setDisplayOrder(temp.getDisplayOrder());
//			break;
		case ConfigConstants.UPDATE:

		    entity.setCategory(temp.getNewValue());
		    entity.setDisplayOrder(temp.getDisplayOrder());
		    categoryRepo.save(entity);

		    // Update Collection Master
		    List<CollectionSlabMaster> slabs =
		            slabRepo.findByCategory(temp.getCategory());

		    for (CollectionSlabMaster slab : slabs) {
		        slab.setCategory(temp.getNewValue());
		    }
		    slabRepo.saveAll(slabs);

		    // Update CE Range Master
		    List<CeRangeMaster> ranges =
		            ceRangeRepo.findByCategory(temp.getCategory());

		    for (CeRangeMaster range : ranges) {
		        range.setCategory(temp.getNewValue());
		    }
		    ceRangeRepo.saveAll(ranges);

		    break;
		case ConfigConstants.ACTIVATE:
			entity.setStatus("Y");
			break;
		case ConfigConstants.DEACTIVATE:
			entity.setStatus("N");
			break;
		}
		categoryRepo.save(entity);
	}

	private void processCity(ConfigTemp temp) {
		if (ConfigConstants.ADD.equals(temp.getActionType())) {
			Optional<CategoryCityMaster> parent = categoryRepo.findByCategoryAndCityIsNull(temp.getCategory());
			if (parent.isPresent()) {
				CategoryCityMaster entity = parent.get();
				entity.setCity(temp.getNewValue());
				entity.setDisplayOrder(temp.getDisplayOrder());
				entity.setStatus("Y");
				categoryRepo.save(entity);
			} else {
				CategoryCityMaster entity = new CategoryCityMaster();
				entity.setCategory(temp.getCategory());
				entity.setCity(temp.getNewValue());
				entity.setDisplayOrder(temp.getDisplayOrder());
				entity.setStatus("Y");
				categoryRepo.save(entity);
			}
			return;
		}
		
		/*
		 * CategoryCityMaster entity = categoryRepo.findByCategoryAndCity(
		 * temp.getCategory(), temp.getOldValue()) .orElseThrow(() -> new
		 * RuntimeException("City not found"));
		 */
		CategoryCityMaster entity =
		        categoryRepo.findByCategoryAndCity(
		                temp.getCategory(),
		                temp.getOldValue())
		        .orElse(null);

		if (entity == null) {
		    entity = categoryRepo.findFirstByCategory(temp.getCategory())
		            .orElseThrow(() ->
		                    new RuntimeException("City not found : " + temp.getOldValue()));
		}
		
		switch (temp.getActionType()) {
		case ConfigConstants.UPDATE:
			entity.setCity(temp.getNewValue());
			entity.setDisplayOrder(temp.getDisplayOrder());
			break;
		case ConfigConstants.ACTIVATE:
			entity.setStatus("Y");
			break;
		case ConfigConstants.DEACTIVATE:
			entity.setStatus("N");
			break;
		}
		categoryRepo.save(entity);
	}

	private void processDpd(ConfigTemp temp) {
		String category = temp.getCategory();
		String dpd = temp.getNewValue();

		if (category == null || category.trim().isEmpty() || dpd == null || dpd.trim().isEmpty()) {
			return;
		}

		//Optional<CollectionSlabMaster> existing = slabRepo.findFirstByCategoryAndDpd(category, dpd);
		Optional<CollectionSlabMaster> existing =
		        slabRepo.findFirstByCategoryAndDpd(category, dpd);

		if (!existing.isPresent()) {
		    existing = slabRepo.findFirstByCategory(category);
		}
		
		switch (temp.getActionType()) {

		case ConfigConstants.ADD:

			if (!existing.isPresent()) {

				CollectionSlabMaster slab = new CollectionSlabMaster();

				slab.setCategory(category);
				slab.setDpd(dpd);

				// Placeholder row
				/* slab.setSlabValue("NA"); */
				slab.setSlabValue(null);

				/* slab.setOrderNo(1); */
				slab.setOrderNo(temp.getDisplayOrder());

				slab.setStatus("Y");

				slabRepo.save(slab);
			}
			break;
			
		case ConfigConstants.UPDATE:
		    List<CollectionSlabMaster> list =
		            slabRepo.findByCategoryAndDpd(
		                    temp.getCategory(),
		                    temp.getOldValue());
		    if(list.isEmpty()){
		        throw new RuntimeException("DPD not found");
		    }
		    for(CollectionSlabMaster slab : list){
		        slab.setDpd(temp.getNewValue());
		    }
		    slabRepo.saveAll(list);
		    List<CeRangeMaster> ranges =
		            ceRangeRepo.findByCategoryAndDpd(
		                    temp.getCategory(),
		                    temp.getOldValue());

		    for (CeRangeMaster range : ranges) {
		        range.setDpd(temp.getNewValue());
		    }

		    ceRangeRepo.saveAll(ranges);
		    break;	

		case ConfigConstants.ACTIVATE:

			if (existing.isPresent()) {

				CollectionSlabMaster slab = existing.get();

				slab.setStatus("Y");

				slabRepo.save(slab);
			}
			break;

		case ConfigConstants.DEACTIVATE:

			if (existing.isPresent()) {

				CollectionSlabMaster slab = existing.get();

				slab.setStatus("N");

				slabRepo.save(slab);
			}
			break;
		}

	}

	private void processCollection(ConfigTemp temp) {
		System.out.println("========== PROCESS COLLECTION ==========");
		System.out.println("Category   = [" + temp.getCategory() + "]");
		System.out.println("DPD        = [" + temp.getDpd() + "]");
		System.out.println("New Value  = [" + temp.getNewValue() + "]");
		System.out.println("Order      = [" + temp.getDisplayOrder() + "]");

		if (ConfigConstants.ADD.equals(temp.getActionType())) {
			Optional<CollectionSlabMaster> parent = slabRepo.findByCategoryAndDpdAndSlabValueIsNull(temp.getCategory(),
					temp.getDpd());

			System.out.println("Parent Found = " + parent.isPresent());

			if (parent.isPresent()) {
				CollectionSlabMaster entity = parent.get();
				entity.setSlabValue(temp.getNewValue());
				entity.setOrderNo(temp.getDisplayOrder());
				entity.setStatus("Y");
				slabRepo.save(entity);
			} else {
				CollectionSlabMaster entity = new CollectionSlabMaster();
				entity.setCategory(temp.getCategory());
				entity.setDpd(temp.getDpd());
				entity.setSlabValue(temp.getNewValue());
				entity.setOrderNo(temp.getDisplayOrder());
				entity.setStatus("Y");
				slabRepo.save(entity);
			}
			return;
		}
		
		CollectionSlabMaster entity =
		        slabRepo.findByCategoryAndDpdAndSlabValue(
		                temp.getCategory(),
		                temp.getDpd(),
		                temp.getOldValue())
		        .orElse(null);

		if (entity == null) {
		    entity = slabRepo.findFirstByCategoryAndDpd(
		            temp.getCategory(),
		            temp.getDpd())
		            .orElseThrow(() ->
		                    new RuntimeException("Collection not found : " + temp.getOldValue()));
		}

		switch (temp.getActionType()) {

		case ConfigConstants.UPDATE:

			entity.setSlabValue(temp.getNewValue());
			entity.setOrderNo(temp.getDisplayOrder());

			break;

		case ConfigConstants.ACTIVATE:

			entity.setStatus("Y");

			break;

		case ConfigConstants.DEACTIVATE:

			entity.setStatus("N");

			break;
		}

		slabRepo.save(entity);
	}

	private void processCeRange(ConfigTemp temp) {

		if (ConfigConstants.ADD.equals(temp.getActionType())) {

			Optional<CeRangeMaster> parent = ceRangeRepo.findByCategoryAndDpdAndRangeValueIsNull(temp.getCategory(),
					temp.getDpd());

			if (parent.isPresent()) {

				CeRangeMaster entity = parent.get();

				entity.setRangeValue(temp.getNewValue());
				entity.setOrderNo(temp.getDisplayOrder());

				entity.setStatus("Y");

				ceRangeRepo.save(entity);

			} else {

				CeRangeMaster entity = new CeRangeMaster();

				entity.setCategory(temp.getCategory());

				entity.setDpd(temp.getDpd());

				entity.setRangeValue(temp.getNewValue());
				entity.setOrderNo(temp.getDisplayOrder());

				entity.setStatus("Y");

				ceRangeRepo.save(entity);
			}

			return;
		}

	
		CeRangeMaster entity =
		        ceRangeRepo.findByCategoryAndDpdAndRangeValue(
		                temp.getCategory(),
		                temp.getDpd(),
		                temp.getOldValue())
		        .orElse(null);

		if (entity == null) {
		    entity = ceRangeRepo.findFirstByCategoryAndDpd(
		            temp.getCategory(),
		            temp.getDpd())
		            .orElseThrow(() ->
		                    new RuntimeException("CE Range not found : " + temp.getOldValue()));
		}
		
		switch (temp.getActionType()) {

		case ConfigConstants.UPDATE:

			entity.setRangeValue(temp.getNewValue());
			entity.setOrderNo(temp.getDisplayOrder());

			break;

		case ConfigConstants.ACTIVATE:

			entity.setStatus("Y");

			break;

		case ConfigConstants.DEACTIVATE:

			entity.setStatus("N");

			break;
		}

		ceRangeRepo.save(entity);
	}

	private List<CategoryCityMaster> orderCategories(List<CategoryCityMaster> list) {

		List<String> order = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "360 + CAT A",
				"360 + CAT B", "NTC", "TCC", "CCA", "Settlement Incentive/Penalty");

		list.sort((o1, o2) -> {

			int i1 = order.indexOf(o1.getCategory());
			int i2 = order.indexOf(o2.getCategory());

			if (i1 == -1)
				i1 = Integer.MAX_VALUE;
			if (i2 == -1)
				i2 = Integer.MAX_VALUE;

			if (i1 != i2) {
				return Integer.compare(i1, i2);
			}

			return o1.getCategory().compareToIgnoreCase(o2.getCategory());
		});

		return list;
	}


	private void validateCollectionFormat(String value) {

	    value = value.trim();

	    boolean valid =

	            // <8Lac / <400000 / <1Cr
	            value.matches("^<\\d+(\\.\\d+)?\\s*(Lac|Cr)?$")

	            ||

	            // Upto20Lac / Upto 20Lac
	            value.matches("^Upto\\s*\\d+(\\.\\d+)?\\s*(Lac|Cr)?$")

	            ||

	            // 400000-600000 / 600000.1-800000 / 10-15
	            value.matches("^\\d+(\\.\\d+)?-\\d+(\\.\\d+)?$")

	            ||

	            // >3Lac<5Lac
	            value.matches("^>\\d+(\\.\\d+)?\\s*(Lac|Cr)?<\\d+(\\.\\d+)?\\s*(Lac|Cr)?$")

	            ||

	            // >3Lac<=5Lac
	            value.matches("^>\\d+(\\.\\d+)?\\s*(Lac|Cr)?<=\\d+(\\.\\d+)?\\s*(Lac|Cr)?$")

	            ||

	            // >20 Lac to 40 Lac
	            value.matches("^>\\d+(\\.\\d+)?\\s*(Lac|Cr)?\\s*to\\s*\\d+(\\.\\d+)?\\s*(Lac|Cr)?$")

	            ||

	            // >1Cr+
	            value.matches("^>\\d+(\\.\\d+)?\\s*(Lac|Cr)?\\+$")

	            ||

	            // >1000000
	            value.matches("^>\\d+(\\.\\d+)?$")

	            ||

	            // 70-MAX
	            value.matches("^\\d+(\\.\\d+)?-MAX$");

	    if (!valid) {

	        throw new RuntimeException(
	                "Invalid Collection format.\n\n" +
	                "Examples:\n" +
	                "<8Lac\n" +
	                ">8Lac<=11Lac\n" +
	                ">3Lac<5Lac\n" +
	                "400000-600000\n" +
	                "600000.1-800000\n" +
	                "Upto 20Lac\n" +
	                ">20 Lac to 40 Lac\n" +
	                ">1 Cr+\n" +
	                "70-MAX");
	    }
	}
	
	private void validateCeRangeFormat(String value) {
		value = value.trim().replace(" ", "");
		boolean valid =
				// <=2.2%
				value.matches("^<=\\d+(\\.\\d+)?%$")
						// <0.5%
						|| value.matches("^<\\d+(\\.\\d+)?%$")
						// >2.2%
						|| value.matches("^>\\d+(\\.\\d+)?%$")
						// >2.2%<=2.9%
						|| value.matches("^>\\d+(\\.\\d+)?%<=\\d+(\\.\\d+)?%$")
						// 0.5%-1%
						|| value.matches("^\\d+(\\.\\d+)?%-\\d+(\\.\\d+)?%$")
						// 70-MAX
						|| value.matches("^\\d+-MAX$")
						// 181-360
						|| value.matches("^\\d+-\\d+$")
						// 3600+
						|| value.matches("^\\d+\\+$")
						// <3Lac
						|| value.matches("^<\\d+(\\.\\d+)?(Lac|lac|Cr|cr)$")
						// >3Lac
						|| value.matches("^>\\d+(\\.\\d+)?(Lac|lac|Cr|cr)$")
						// >3Lac<5Lac
						|| value.matches("^>\\d+(\\.\\d+)?(Lac|lac|Cr|cr)<\\d+(\\.\\d+)?(Lac|lac|Cr|cr)$")
						// >3Lac<=5Lac
						|| value.matches("^>\\d+(\\.\\d+)?(Lac|lac|Cr|cr)<=\\d+(\\.\\d+)?(Lac|lac|Cr|cr)$")
						// Payout %
						|| value.equalsIgnoreCase("Payout%") || value.equalsIgnoreCase("Payout%".replace(" ", ""))
						|| value.equalsIgnoreCase("Payout");
		if (!valid) {
			throw new RuntimeException("Invalid CE Range format.\n\n" + "Supported examples:\n" + "<=2.2%\n" + "<0.5%\n"
					+ ">5%\n" + ">2.2%<=2.9%\n" + "0.5%-1%\n" + "181-360\n" + "3600+\n" + "70-MAX\n" + "<3Lac\n"
					+ ">3Lac<5Lac\n" + ">12Lac\n" + "Payout %");
		}
	}
	
	private void syncPayoutStructure(ConfigTemp temp) {

	    switch (temp.getConfigType()) {

	    case "CATEGORY":

	        List<PayoutMaster> categoryMasters =
	                payoutMasterRepo.findByCategory(temp.getOldValue());

	        for (PayoutMaster master : categoryMasters) {
	            master.setCategory(temp.getNewValue());
	        }

	        payoutMasterRepo.saveAll(categoryMasters);

	        break;

	    case "CITY":

	        List<PayoutMaster> cityMasters =
	                payoutMasterRepo.findByCity(temp.getOldValue());

	        for (PayoutMaster master : cityMasters) {
	            master.setCity(temp.getNewValue());
	        }

	        payoutMasterRepo.saveAll(cityMasters);

	        break;

	    case "DPD":

	        List<PayoutMaster> dpdMasters =
	                payoutMasterRepo.findByDpd(temp.getOldValue());

	        for (PayoutMaster master : dpdMasters) {
	            master.setDpd(temp.getNewValue());
	        }

	        payoutMasterRepo.saveAll(dpdMasters);

	        break;

	    case "COLLECTION":

	        List<PayoutMaster> collectionMasters = payoutMasterRepo.findAll();

	        for (PayoutMaster master : collectionMasters) {

	            if (master.getDetails() == null) {
	                continue;
	            }

	            for (PayoutDetail detail : master.getDetails()) {

	                if (temp.getOldValue().equals(detail.getCollectionSlab())) {

	                    detail.setCollectionSlab(temp.getNewValue());
	                    detail.setCollectionSlabFrom(getCollectionFrom(temp.getNewValue()));
	                    detail.setCollectionSlabTo(getCollectionTo(temp.getNewValue()));

	                }
	            }
	        }

	        payoutMasterRepo.saveAll(collectionMasters);

	        break;

	    case "CE_RANGE":

	        List<PayoutMaster> ceMasters = payoutMasterRepo.findAll();

	        for (PayoutMaster master : ceMasters) {

	            if (master.getDetails() == null) {
	                continue;
	            }

	            for (PayoutDetail detail : master.getDetails()) {

	                if (temp.getOldValue().equals(detail.getCeRange())) {

	                    detail.setCeRange(temp.getNewValue());
	                    detail.setCeRangeFrom(getCeFrom(temp.getNewValue()));
	                    detail.setCeRangeTo(getCeTo(temp.getNewValue()));
	                    
	                }
	            }
	        }

	        payoutMasterRepo.saveAll(ceMasters);

	        break;

	    default:
	        break;
	    }
	}

	private BigDecimal getCollectionFrom(String slab) {
		slab = slab.replace(" ", "");
		// 70-MAX
		if (slab.toUpperCase().contains("MAX")) {
		    String value = slab.split("-")[0];
		    return new BigDecimal(value)
		            .add(BigDecimal.ONE);
		}
		// <500000 OR <8Lac
		if (slab.startsWith("<")) {
			return BigDecimal.ZERO;
		}
		// Upto20Lac
		if (slab.toLowerCase().startsWith("upto")) {
			return BigDecimal.ZERO;
		}
		// 500000-1000000
		if (slab.matches("\\d+(\\.\\d+)?-\\d+(\\.\\d+)?")) {
			String value = slab.split("-")[0];
			BigDecimal from =
			        new BigDecimal(value);
			if(value.contains(".")){
			    return from;
			}
			return from.add(BigDecimal.ONE);
		}
		// >3Lac<7Lac
		if (slab.startsWith(">") && slab.contains("<") && !slab.contains("<=")) {
			String value = slab.substring(1).split("<")[0];
			return convertAmount(value).add(BigDecimal.ONE);
		}
		// >8Lac<=11Lac
		if (slab.contains("<=")) {
			String value = slab.substring(1).split("<=")[0];
			return convertAmount(value).add(BigDecimal.ONE);
		}
		// >20Lacto40Lac
		if (slab.toLowerCase().contains("to")) {
			String value = slab.substring(1).toLowerCase().split("to")[0];
			return convertAmount(value).add(BigDecimal.ONE);
		}
		// >32Lac OR >1Cr+
		if (slab.startsWith(">")) {
			String value = slab.substring(1).replace("+", "");
			return convertAmount(value).add(BigDecimal.ONE);
		}
		return BigDecimal.ZERO;
	}

	
	private BigDecimal getCollectionTo(String slab) {
		slab = slab.replace(" ", "");
		if (slab.toUpperCase().contains("MAX")) {
			return new BigDecimal("99999999");
		}
		
		// <500000
		/*
		 * if (slab.startsWith("<")) { return convertAmount(slab.substring(1)); }
		 */
		
		// <10Lac<=50Lac OR <500000
		if (slab.startsWith("<")) {

		    String value = slab.substring(1);

		    if(value.contains("<=")){
		        value = value.split("<=")[1];
		    }

		    return convertAmount(value);
		}
		
		// Upto20Lac
		if (slab.toLowerCase().startsWith("upto")) {
			return convertAmount(slab.substring(4));
		}
		// 500000-1000000
		if (slab.matches("\\d+(\\.\\d+)?-\\d+(\\.\\d+)?")) {
			return new BigDecimal(slab.split("-")[1]);
		}
		// >3Lac<7Lac
		if (slab.startsWith(">") && slab.contains("<") && !slab.contains("<=")) {
			String value = slab.substring(slab.indexOf("<") + 1);
			return convertAmount(value);
		}
		// >8Lac<=11Lac
		if (slab.contains("<=")) {
			String value = slab.substring(slab.indexOf("<=") + 2);
			return convertAmount(value);
		}
		// >20Lacto40Lac
		if (slab.toLowerCase().contains("to")) {
			String value = slab.toLowerCase().split("to")[1];
			return convertAmount(value);
		}
		// >1Cr+ OR >3000000
		if (slab.startsWith(">")) {
			return new BigDecimal("99999999");
		}
		return new BigDecimal("99999999");
	}

	
	
	private BigDecimal getCeFrom(String range) {
		range = range.replace("%", "").replace(" ", "");
		if (range.startsWith("<")) {
			return BigDecimal.ZERO;
		}

		// <=2.2
		if (range.startsWith("<=")) {
			return BigDecimal.ZERO;
		}
		// 0-10
		if (range.contains("-") && !range.toUpperCase().contains("MAX")) {
			String value = range.split("-")[0];
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		if(range.endsWith("+")) {
		    String value =
		            range.replace("+","");
		    return new BigDecimal(value)
		            .add(new BigDecimal("0.01"));
		}
		// >2.2<=2.9
		if (range.contains("<=")) {
			String value = range.substring(1).split("<=")[0].replace("%", "").replace("=", "").trim();
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		// >5
		if (range.startsWith(">")) {
			String value = range.substring(1);
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		// 70-MAX
		if(range.contains("-")
		        && !range.toUpperCase().contains("MAX")) {
		    String value =
		            range.split("-")[0]
		                 .replace("%","");
		    return new BigDecimal(value)
		            .add(new BigDecimal("0.01"));
		}
		// 0.5-1
		if (range.contains("-") && !range.contains("<=") && !range.toUpperCase().contains("MAX")) {
			String value = range.split("-")[0];
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		if (range.endsWith("+")) {
			String value = range.replace("+", "");
			return new BigDecimal(value).add(new BigDecimal("0.01"));
		}
		if(range.contains("<=")){
		    String value =
		            range.substring(1)
		                 .split("<=")[0]
		                 .replace("%", "")
		                 .replace("=", "")
		                 .trim();
		    return new BigDecimal(value)
		            .add(new BigDecimal("0.01"));
		}
		if(range.startsWith("<")
		        && !range.startsWith("<=")) {
		    return BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}
	
	
	private BigDecimal getCeTo(String range) {
		System.out.println("CE TO INPUT = [" + range + "]");
	    range = range.replace("%", "")
	                 .replace(" ", "");
	    // <2.2
	    if (range.startsWith("<")
	            && !range.startsWith("<=")) {
	        return new BigDecimal(
	                range.replace("<", "")
	                     .trim());
	    }
	    // <=2.2
	    if (range.startsWith("<=")) {
	        return new BigDecimal(
	                range.replace("<=", "")
	                     .trim());
	    }
	    // >2.2<=2.9 OR >5<=6
	    if (range.contains("<=")) {
	        String value =
	                range.split("<=")[1]
	                     .replace("%", "")
	                     .replace("=", "")
	                     .trim();
	        return new BigDecimal(value);
	    }
	    // 0-10
	    if (range.contains("-")
	            && !range.toUpperCase().contains("MAX")) {
	    	return new BigDecimal(
	    	        range.split("-")[1]
	    	             .replace("%",""));
	    }
	    // 70-MAX
	    if (range.toUpperCase().contains("MAX")) {

	        return new BigDecimal("99999999");
	    }
	    // 3600+
	    if (range.endsWith("+")) {

	        return new BigDecimal("99999999");
	    }
	    if(range.startsWith("<")
	            && !range.startsWith("<=")) {

	        return new BigDecimal(
	                range.substring(1));
	    }
	    // >5
	    return new BigDecimal("99999999");
	}

	

	private BigDecimal convertAmount(String value) {
		String original = value;
		value = value.trim();
		BigDecimal num = new BigDecimal(
				value.replace("Lac", "").replace("lac", "").replace("Cr", "").replace("cr", "").trim());
		if (original.toLowerCase().contains("cr")) {
			return num.multiply(new BigDecimal("10000000"));
		}
		if (original.toLowerCase().contains("lac")) {
			return num.multiply(new BigDecimal("100000"));
		}
		return num;
	}

}