package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.IPKPendingSummary;
import com.icici.dma.dto.IPKStructureSaveRequest;
import com.icici.dma.dto.IncentiveStructureDto;
import com.icici.dma.dto.MPKCommonPendingSummary;
import com.icici.dma.dto.RetainerPayloadDto;
import com.icici.dma.dto.UsedCarStructureRegionSaveRequest;
import com.icici.dma.dto.UsedCarStructureSaveRequest;
import com.icici.dma.slabEntity.AutoManipalRetainerMaster;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterDummy;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterManipal;

public interface IPKStructureService {

	void saveIPKStructure(IPKStructureSaveRequest request);

	List<IPKPendingSummary> getPendingList();

	void reject(Long sequence);

	void approve(Long sequence);

	List<AutoProcessIncentiveStructureMasterDummy> getBySequence(Long sequence);

	void saveMPKStructure(IPKStructureSaveRequest request);

	List<MPKCommonPendingSummary> getPendingMPList();

	List<AutoProcessIncentiveStructureMasterManipal> getBySequenceMP(Long sequence);

	void approveMP(Long sequence);

	void rejectMP(Long sequence);

	void saveStructure(UsedCarStructureSaveRequest request);

	List<MPKCommonPendingSummary> getPendingMPCommonList();

	void approveCommonMP(Integer sequence);

	void rejectCommonMP(Integer sequence);

	void saveIPCStructure(UsedCarStructureSaveRequest request);

	List<MPKCommonPendingSummary> getPendingIPCommonList();

	void approveCommonIP(Integer sequence);

	void rejectCommonIP(Integer sequence);

	void saveIPPNStructure(UsedCarStructureRegionSaveRequest request);

	List<MPKCommonPendingSummary> getPendingIPOtherList();

	void approveOtherIP(Integer sequence);

	void rejectOtherIP(Integer sequence);

	void saveMPPNStructure(UsedCarStructureRegionSaveRequest request);

	List<MPKCommonPendingSummary> getPendingMPOtherList();

	void approveOtherMP(Integer sequence);

	void rejectOtherMP(Integer sequence);

	List<IPKPendingSummary> getIPPendingList();

	List<AutoManipalRetainerMaster> saveRetainerMaster(RetainerPayloadDto payload);

	void saveIncentiveStructuresAll(IncentiveStructureDto requestDto);

}
