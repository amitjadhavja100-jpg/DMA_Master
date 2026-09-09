package com.icici.dma.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.service.AlddDumpService;
import com.icici.dma.service.FinnoneDumpService;
import com.icici.dma.service.IlensDumpService;
import com.icici.dma.service.ProductDumpService;
import com.icici.dma.service.RcaCibilService;
import com.icici.dma.service.RcasService;

@CrossOrigin("*")
@RestController
@RequestMapping("/DMAPayoutWeb2")
public class ReportController {

	private static final Logger logger = LogManager.getLogger(ReportController.class);

	@Autowired
	private RcaCibilService rcaService;

	@Autowired
	private RcasService rcaService1;

	@Autowired
	private ProductDumpService productDumpService;

	@Autowired
	private IlensDumpService ilensDumpService;

	@Autowired
	private AlddDumpService alddDumpService;

	@Autowired
	private FinnoneDumpService finnoneDumpService;

	@GetMapping("/getProductDump")
	public ResponseEntity<?> getAllProductMatser(Model model) {

		List<String> allProductMatser = null;
		try {

			allProductMatser = productDumpService.getAllProductDump();
			System.out.println(allProductMatser);
			return ResponseEntity.ok(allProductMatser);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("product") String product, @RequestParam("dumpType") String dumpType,
			@RequestParam(value = "rcaOption", required = false) String rcaOption,
			@RequestHeader(value = "userId", required = false) String user,
			@RequestParam("cycleFromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date cycleFromDate,
			@RequestParam("cycleToDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date cycleToDate,
			HttpServletRequest request) {

		try {

			HttpSession session = request.getSession();
			String user1 = (String) session.getAttribute("USER_ID");

			logger.info("user from Session is user : {}", user1);

			if (user == null || user.isEmpty() || user.trim().equalsIgnoreCase("")) {
				logger.info("user from header is Null");
				user = user1;
			}

			/* user = "BAN495699"; */

			logger.info("product " + product + " dumpType" + dumpType);
			logger.info("file " + file.getOriginalFilename());
			logger.info("cycleFromDate" + cycleFromDate + "cycleToDate" + cycleToDate);

			if ("Rcas".equalsIgnoreCase(dumpType)) {
//				return ResponseEntity.ok(rcaService.saveCibil(file, user, cycleFromDate, cycleToDate));
				return ResponseEntity.ok(rcaService1.uploadExcelRcas(file, user, cycleFromDate, cycleToDate));

			} else if ("Aldd-transaction Report".equalsIgnoreCase(dumpType)) {

				// alddsService.saveTransactionReport(file);
				return ResponseEntity.ok(alddDumpService.uploadExcel(file, user, cycleFromDate, cycleToDate));

			} else if ("Ilens Dump".equalsIgnoreCase(dumpType)) {

				logger.info("ILENS FILE UPLOADED CONTROLLER LOGGER::");

				return ResponseEntity.ok(ilensDumpService.uploadExcel(file, user, cycleFromDate, cycleToDate));
				// alddsService.saveTransactionReport(file);

			} else if ("Finnone Dump".equalsIgnoreCase(dumpType)) {

				// alddsService.saveTransactionReport(file);
				return ResponseEntity.ok(finnoneDumpService.uploadFinnoneExcel(file, user, cycleFromDate, cycleToDate));

			} else {

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" Select Proper Dump ");

			}

//			return ResponseEntity.ok("File uploaded successfully");

		} catch (Exception e) {

			logger.info("Report Controller uploadFile Exception::" + e);
			System.out.println("Exception e " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}
	}

	@PostMapping("/downloadFinnone")
	public ResponseEntity<?> downloadFinnoneDump(
			@RequestParam("cycleFromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date cycleFromDate,
			@RequestParam("cycleToDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date cycleToDate) throws Exception {
		/* public ResponseEntity<?> downloadFinnoneDump() { */
		try {
			byte[] fileData = finnoneDumpService.exportToExcel(cycleFromDate, cycleToDate);
			/* byte[] fileData = finnoneDumpService.exportToExcel(); */

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Finnone_Dump.xlsx")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileData);

		} catch (RuntimeException e) {
			// 🔴 VALIDATION ERRORS COME HERE
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());

		} /*
			 * catch (Exception e) { // 🔴 UNEXPECTED ERRORS return ResponseEntity
			 * .status(HttpStatus.INTERNAL_SERVER_ERROR) .contentType(MediaType.TEXT_PLAIN)
			 * .body("Download failed : " + e.getMessage()); }
			 */
	}
}
