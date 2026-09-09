package com.icici.dma.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface TdsCodeRateUploadService {
	 
    Map<String, Object> upload(
            MultipartFile file,
            String user) throws Exception;
 
    byte[] exportErrorExcel(
            String uploadId) throws Exception;
}