package com.springboot.smartcontactmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping("/profile")
	public String openUserProfile(Model m) {
		System.out.println("in AdminController");
		m.addAttribute("title","Admin Profile - Smart Contact Manager");
		return "admin/adminprofile";
	}
}
