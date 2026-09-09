/*package com.icici.dma.serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.service.CreditCardBillingService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class CreditCardBillingServiceImpl implements CreditCardBillingService {

	private static final Logger logger = LogManager.getLogger(CreditCardBillingServiceImpl.class);

	@Value("${spring.datasource.url}")
	private String DB_URL;

	@Value("${spring.datasource.username}")
	private String DB_USERNAME;

	@Value("${spring.datasource.password}")
	private String DB_PASS;

	@Value("${creditcard.files.basepath}")
	private String BASE_PATH;

	@Override
	public void executeSqlLoader(String dataFilePath) throws Exception {

		logger.info("Starting SQLLoader execution for file : {}", dataFilePath);

		String ctlPath = BASE_PATH + "ctl/billing.ctl";

		String logPath = BASE_PATH + "log/loader.log";

		String badPath = BASE_PATH + "bad/loader.bad";

		logger.info("DB URL : {}, DB URL : {}, DB URL : {}", DB_URL, DB_USERNAME, DB_PASS);

		String DB_NAME = DB_URL.substring(DB_URL.lastIndexOf(":") + 1);

		String command = "sqlldr " + DB_USERNAME + "/" + DB_PASS + "@" + DB_NAME + "control=" + ctlPath + " data="
				+ dataFilePath + " log=" + logPath + " bad=" + badPath + " direct=true";

		logger.info("SQLLoader command : {}", command);

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);

		Process process = pb.start();

		int exitCode = process.waitFor();

		if (exitCode == 0) {

			logger.info("SQLLoader completed successfully");

		} else {

			logger.error("SQLLoader failed with exit code {}", exitCode);

			throw new RuntimeException("SQLLoader execution failed");

		}
	}

	@Override
	public void validateHeader(File file, List<String> expectedHeaders, String seprator) throws Exception {

		logger.info("Starting header validation");

		BufferedReader reader = new BufferedReader(new FileReader(file));

		// Read first line (header)
		String headerLine = reader.readLine();

		if (headerLine == null || headerLine.trim().isEmpty()) {
			throw new RuntimeException("Uploaded file is empty. Header row missing.");
		}

		logger.info("Header line found : {}", headerLine);

		// Split header by whitespace or tab
//	    String[] actualHeaders = headerLine.trim().split("\\s+");
		String[] headers = headerLine.trim().split(seprator);

		logger.info("Actual header count : {}", headers.length);
		logger.info("Expected header count : {}", expectedHeaders.size());

		// Check header count
		if (headers.length != expectedHeaders.size()) {
			throw new RuntimeException(
					"Header count mismatch. Expected " + expectedHeaders.size() + " but found " + headers.length);
		}

		Set<String> actualHeaders = new HashSet<>();
		Set<String> duplicateHeaders = new HashSet<>();
		Set<String> extraHeaders = new HashSet<>();

		// normalize the expected header
		Set<String> normalizedExpected = expectedHeaders.stream().map(this::normalizeHeader)
				.collect(Collectors.toSet());

		// process actual header
		for (String header : headers) {

			// normalize the file header
			String normalized = normalizeHeader(header);

			// cheack duplicate header
			if (!actualHeaders.add(normalized)) {
				duplicateHeaders.add(normalized);
			}

			// cheack Extra Header
			if (!normalizedExpected.contains(normalized)) {
				extraHeaders.add(normalized);
			}
		}

		// cheack missing header
		Set<String> missingHeaders = new HashSet<>(normalizedExpected);
		missingHeaders.removeAll(actualHeaders);

		if (!duplicateHeaders.isEmpty() || !extraHeaders.isEmpty() || !missingHeaders.isEmpty()) {

			StringBuilder error = new StringBuilder("\nHeader validation failed\n");

			if (!duplicateHeaders.isEmpty()) {
				error.append("Duplicate Headers : ").append(duplicateHeaders).append("\n");
			}

			if (!extraHeaders.isEmpty()) {
				error.append("Unexpected Headers : ").append(extraHeaders).append("\n");
			}

			if (!missingHeaders.isEmpty()) {
				error.append("Missing Headers : ").append(missingHeaders).append("\n");
			}

			throw new RuntimeException(error.toString());
		}
		reader.close();
	}

	@Override
	public String normalizeHeader(String header) {
		return header == null ? null
				: header.trim().replace("-", "_").replace("&", "_").replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "_")
						.replaceAll("_+", "_").replaceAll("^_|_$", "_").toUpperCase();
	}

	@Override
	public File saveFileOnServer(MultipartFile file, String dir) throws Exception {

		File savedFile = null;

//		String uploadDir = "D:/app/dma/CreditCard/upload/";
		String uploadDir = dir;

		File directory = new File(uploadDir);

		// Create directory if it does not exist
		if (!directory.exists()) {
			directory.mkdirs();

			logger.info("Upload directory created at location: {}", uploadDir);
		}

		// ---------------------------------------------------------
		// Prepare file path where uploaded file will be saved
		// ---------------------------------------------------------
		savedFile = new File(directory, file.getOriginalFilename());

		logger.info("Starting file upload process. File Name: {}", file.getOriginalFilename());

		// ---------------------------------------------------------
		// Transfer file from request to server filesystem
		// ---------------------------------------------------------
		file.transferTo(savedFile);

		logger.info("File successfully uploaded to server location: {}", savedFile.getAbsolutePath());
		return savedFile;
	}

	public void fileTransferMainServerToDBServer(File file) {

		String host = null;
		int port = 12;
		String user = DB_USERNAME;
		String password = DB_PASS;

		Session session = null;
		ChannelSftp channel = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setPassword(password);

			String remoteDir = "/data/creditCard/upload";

			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			logger.info("Connected to DB Server");

			Channel channelObj = session.openChannel("sftp");
			channelObj.connect();

			channel = (ChannelSftp) channelObj;

			// Upload file
			channel.put(file.getAbsolutePath(), remoteDir + file.getName());

			logger.info("File transferred to DB server: {}", remoteDir + file.getName());

		} catch (Exception e) {
			logger.error("SFTP failed", e);
			throw new RuntimeException("File transfer failed");
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}
	}

	public void createCTLFile() {
	}
}
*/