package com.icici.dma.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin("*")
@Controller
@RequestMapping("/mainPage")
public class LoadPageController {

	@GetMapping("load")
	public String createBranchMasterMaker(
			@RequestParam(name = "master", required = false, defaultValue = "MAKER") String master, Model model) {

		if (master != null && "MAKER".equalsIgnoreCase(master.trim())) {

			System.out.println(master);
			model.addAttribute("contentPage", "maker.jsp");

		}
		if (master != null && "CHECKER".equalsIgnoreCase(master.trim())) {

			model.addAttribute("contentPage", "checker.jsp");
		}

		if (master != null && "GRIDAPPLY".equalsIgnoreCase(master.trim())) {

			System.out.println(master);
			model.addAttribute("contentPage", "gridapply.jsp");

		}
		if (master != null && "REPORTS".equalsIgnoreCase(master.trim())) {

			model.addAttribute("contentPage", "report.jsp");
		}

		if (master != null && "SLABMAKER".equalsIgnoreCase(master.trim())) {

			model.addAttribute("contentPage", "slab/payout.jsp");
		}
		if (master != null && "SLABCHECKER".equalsIgnoreCase(master.trim())) {

			model.addAttribute("contentPage", "slab/checker.jsp");
		}
		
		if (master != null && "PAYOUTCOMPAREMAKER".equalsIgnoreCase(master.trim())) {

		    model.addAttribute("contentPage", "slab/payoutCompare.jsp");
		}

		if (master != null && "PAYOUTCOMPARECHECKER".equalsIgnoreCase(master.trim())) {

			model.addAttribute("contentPage", "slab/payoutCompare.jsp");
		}

		// OSP Payout Pages
		if (master != null && "OSPPAYOUTHISTORY".equalsIgnoreCase(master.trim())) {
			model.addAttribute("contentPage", "slab/ospHistory.jsp");
		}

		if (master != null && "OSPPAYOUTCOMPAREMAKER".equalsIgnoreCase(master.trim())) {
			model.addAttribute("contentPage", "slab/ospPayoutCompare.jsp");
		}

		if (master != null && "OSPPAYOUTCOMPARECHECKER".equalsIgnoreCase(master.trim())) {
			model.addAttribute("contentPage", "slab/ospPayoutCompare.jsp");
		}
		
		// FLOWS PAYOUT COMPARE
		if (master != null && "FLOWSPAYOUTCOMPAREMAKER".equalsIgnoreCase(master.trim())) {
			model.addAttribute("contentPage", "slab/flowsPayoutCompare.jsp");
		}

		if (master != null && "FLOWSPAYOUTCOMPARECHECKER".equalsIgnoreCase(master.trim())) {
			model.addAttribute("contentPage", "slab/flowsPayoutCompare.jsp");
		}
		
		// VALUATION PAYOUT COMPARE
		if (master != null && "VALUATIONPAYOUTCOMPAREMAKER".equalsIgnoreCase(master.trim())) {
		    model.addAttribute("contentPage", "slab/valuationPayoutCompare.jsp");
		}

		if (master != null && "VALUATIONPAYOUTCOMPARECHECKER".equalsIgnoreCase(master.trim())) {
		    model.addAttribute("contentPage", "slab/valuationPayoutCompare.jsp");
		}
		
		if (master != null
				&& "CONFIGMAKER".equalsIgnoreCase(master.trim())) {
				model.addAttribute("contentPage","slab/configMaster.jsp");
             }
        if (master != null
				&& "CONFIGCHECKER".equalsIgnoreCase(master.trim())) {
				model.addAttribute("contentPage","slab/configMasterChecker.jsp");
				}
		if (master != null
				&& "CONFIGCOMPARE".equalsIgnoreCase(master.trim())) {
			    model.addAttribute("contentPage","slab/configMasterCompare.jsp");
				}

		if (master != null && "CONFIGCOMPAREMAKER".equalsIgnoreCase(master.trim())) {

		    model.addAttribute("contentPage", "slab/configMasterCompare.jsp");
		}

		if (master != null && "CONFIGCOMPARECHECKER".equalsIgnoreCase(master.trim())) {

		    model.addAttribute("contentPage", "slab/configMasterCompare.jsp");
		}
		
		if (master != null && "OSPCONFIGCOMPARE".equalsIgnoreCase(master.trim())) {

		    model.addAttribute("contentPage", "slab/ospConfigMasterCompare.jsp");
		}
		
		return "menu";

		}

		
		
	//added for dump template download and master template download (ban502236)-start
	@Autowired
	private ServletContext servletContext;
	 
	@GetMapping("/downloadTemplate")
	public void downloadTemplate(
	        @RequestParam(value = "masterType", required = false) String masterType,
	        @RequestParam(value = "dumpType", required = false) String dumpType,
	        HttpServletResponse response) throws IOException {
	 
	    String fileName = null;
	 
	    if (masterType != null && !masterType.trim().isEmpty()) {
	 
	        if ("Model Master".equalsIgnoreCase(masterType)) {
	            fileName = "Model Master.xlsx";
	        } else if ("Branch Master".equalsIgnoreCase(masterType)) {
	            fileName = "Branch Master.xlsx";
	        } else if ("Channel Master".equalsIgnoreCase(masterType)) {
	            fileName = "Channel Master.xlsx";
	        } else if ("GST Master".equalsIgnoreCase(masterType)) {
	            fileName = "GST Master.xlsx";
	        }
	        else if ("ILENS CHANNEL MASTER".equalsIgnoreCase(masterType)) {
				fileName = "iLens Channel Master.xlsx";
			} else if ("OUTSOURCE MASTER".equalsIgnoreCase(masterType)) {
				fileName = "Master Outsource.xlsx";
			} else if ("GST MF MASTER".equalsIgnoreCase(masterType)) {
				fileName = "GST MF.xlsx";
			} else if ("GST STATE MASTER".equalsIgnoreCase(masterType)) {
				fileName = "Active I box GST state.xlsx";
			} else if ("GST TO MASTER".equalsIgnoreCase(masterType)) {
				fileName = "GST_To.xlsx";
			}
	        
	        // Credit Card Masters
	        else if ("SAP Master".equalsIgnoreCase(masterType)) {
	            fileName = "SAP_Master.xlsx";
	        }
	        else if ("Recovery City Master".equalsIgnoreCase(masterType)) {
	            fileName = "Recovery_City_Master.xlsx";
	        }
	        else if ("Payment Code Master".equalsIgnoreCase(masterType)) {
	            fileName = "Payment code Master.xlsx";
	        }
	        else if ("Hold Code Master".equalsIgnoreCase(masterType)) {
	            fileName = "Hold Code Master.xlsx";
	        }
	        
	        else if ("Flow City Master".equalsIgnoreCase(masterType)) {
	            fileName = "Flow City Master.xlsx";
	        }
	        
	        else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid master type");
	            return;
	        }
	 
	    } else if (dumpType != null && !dumpType.trim().isEmpty()) {
	 
	        if ("ALDD-Transaction Report".equalsIgnoreCase(dumpType)) {
	            fileName = "ALDD-Transaction Report.xlsx";
	        } else if ("RCAS".equalsIgnoreCase(dumpType)) {
	            fileName = "RCAS.xlsx";
	        } else if ("ilens dump".equalsIgnoreCase(dumpType)) {
	            fileName = "ilens dump.xlsx";
	        } else if ("Finnone Dump".equalsIgnoreCase(dumpType)) {
	            fileName = "Finnone Dump.xlsx";
	        } else if ("Tagging File".equalsIgnoreCase(dumpType)) {
	            fileName = "Tagging File.xlsx";
	        } else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid dump type");
	            return;
	        }
	 
	    } else {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "masterType or dumpType is required");
	        return;
	    }
	 
	    String fullPath = servletContext.getRealPath("/templates/" + fileName);
	 
	    File file = new File(fullPath);
	 
	    if (!file.exists()) {
	        response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found: " + fileName);
	        return;
	    }
	 
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	 
	    FileInputStream fis = new FileInputStream(file);
	    OutputStream os = response.getOutputStream();
	 
	    byte[] buffer = new byte[4096];
	    int bytesRead;
	 
	    while ((bytesRead = fis.read(buffer)) != -1) {
	        os.write(buffer, 0, bytesRead);
	    }
	 
	    fis.close();
	    os.flush();
	    os.close();
	}
	 
	//added for dump template download and master template download (ban502236)-end

	@PostMapping("/setUserSession")
	@ResponseBody
	public void setUserSession(@RequestBody Map<String, String> req, HttpServletRequest request) {

		String user = req.get("user");

		HttpSession session = request.getSession();

		session.setAttribute("USER_ID", user);
	}

}
