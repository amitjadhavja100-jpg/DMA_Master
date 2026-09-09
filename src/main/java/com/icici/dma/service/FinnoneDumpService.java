package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public interface FinnoneDumpService {

	public String uploadFinnoneExcel(MultipartFile file, String user,Date cycleFromDate,Date cycleToDate);

	public byte[] exportToExcel(Date cycleFromDate,Date cycleToDate) throws Exception;
	
	/*public byte[] exportToExcel() throws Exception;*/
	public ByteArrayInputStream exportFinnoneErrorExcel(String user) throws Exception;
	
}