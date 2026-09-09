package com.icici.dma.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	private static final Logger logger = LogManager.getLogger(LoginController.class);
	
	// 1️⃣ Load Login Page
	@GetMapping("/")
	public String showLogin() {
		return "login"; // login.jsp
	}

	// 2️⃣ Handle Login Submit
	@PostMapping("/login")
	public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {

		if (username.equals("maker1") && password.equals("1234")) {
			session.setAttribute("role", "MAKER");
			session.setAttribute("username", username);
			return "redirect:/mainPage/load?master=MAKER";
		}

		if (username.equals("checker1") && password.equals("1234")) {
			session.setAttribute("role", "CHECKER");
			session.setAttribute("username", username);
			return "redirect:/mainPage/load?master=CHECKER";

		}

		return "login"; // back to login if invalid
	}

	// 3️⃣ Logout
	@GetMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		String baseUrl=request.getScheme()+"://"+ request.getServerName();
		logger.info(baseUrl);
		logger.info("User Logout from DMA Master");
		response.sendRedirect(baseUrl+"/DMAPayoutWeb/index2.jsp#!/login");
	}
}