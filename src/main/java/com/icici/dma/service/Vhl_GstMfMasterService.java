package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstMfMasterDto;
import com.icici.dma.model.GstMfMaster;
import com.icici.dma.model.GstMfMasterTemp;

public interface Vhl_GstMfMasterService {

	public Map<String, Object> uploadGstMfMaster(MultipartFile file, String loginUser);

	public ByteArrayInputStream exportErrorExcel(String user) throws Exception;

	public void createGstMfMasterByMaker(GstMfMasterDto dto, String username);

	public void updateGstMfMasterByMaker(GstMfMasterDto dto, String username);

	public List<GstMfMasterDto> getAllGstMfMasterByChecker(String user);

	public void updateGstMfMasterByChecker(CheckerDecisionReq requestPayload, String username);

	public List<GstMfMasterDto> getGstMfMasterByStatus(String statusType);

	/**
	 * @param d
	 * @return
	 */
	public default GstMfMaster convertDtoToMain(GstMfMasterDto d) {

		GstMfMaster m = new GstMfMaster();

		m.setApsCode(d.getApsCode());
		m.setName(d.getName());
		m.setState(d.getState());
//		m.setLocation(d.getLocation());
//		m.setAddress(d.getAddress());
//		m.setDd(d.getDd());
//		m.setLocationMf(d.getLocationMf());

		return m;
	}

	public default GstMfMasterTemp convertDtoToTemp(GstMfMasterDto d) {

		GstMfMasterTemp m = new GstMfMasterTemp();

		m.setApsCode(d.getApsCode());
		m.setName(d.getName());
		m.setState(d.getState());
//		m.setLocation(d.getLocation());
//		m.setAddress(d.getAddress());
//		m.setDd(d.getDd());
//		m.setLocationMf(d.getLocationMf());

		return m;
	}

	public default GstMfMasterDto convertTempToDto(GstMfMasterTemp d) {

		GstMfMasterDto m = new GstMfMasterDto();

		m.setApsCode(d.getApsCode());
		m.setName(d.getName());
		m.setState(d.getState());
//		m.setLocation(d.getLocation());
//		m.setAddress(d.getAddress());
//		m.setDd(d.getDd());
//		m.setLocationMf(d.getLocationMf());

		m.setStatus(d.getStatus());
		return m;
	}

	public default GstMfMasterDto convertMainToDto(GstMfMaster d) {

		GstMfMasterDto m = new GstMfMasterDto();

		m.setApsCode(d.getApsCode());
		m.setName(d.getName());
		m.setState(d.getState());
//		m.setLocation(d.getLocation());
//		m.setAddress(d.getAddress());
//		m.setDd(d.getDd());
//		m.setLocationMf(d.getLocationMf());

		m.setStatus(d.getStatus());

		return m;
	}

	public default GstMfMaster convertTempToMain(GstMfMasterTemp d) {

		GstMfMaster m = new GstMfMaster();

		m.setApsCode(d.getApsCode());
		m.setName(d.getName());
		m.setState(d.getState());
//		m.setLocation(d.getLocation());
//		m.setAddress(d.getAddress());
//		m.setDd(d.getDd());
//		m.setLocationMf(d.getLocationMf());

		return m;
	}

}
