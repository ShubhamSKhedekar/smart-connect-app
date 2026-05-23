package com.springboot.smartcontactmanager.controller;

import java.util.Random;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.springboot.smartcontactmanager.dao.UserRepository;
import com.springboot.smartcontactmanager.emailapi.service.EmailService;
import com.springboot.smartcontactmanager.entities.User;
import com.springboot.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MainController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	EmailService emailService;
	
	@GetMapping("/home")
	public String getHome(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/signup")
	public String getSignUpPage(Model m, HttpSession session) {
		m.addAttribute("title", "SignUp - Smart Contact Manager");
		m.addAttribute("user", new User());
		//session.setAttribute("message", "");
		return "signup";
	}
	
	@PostMapping("/registeruser")
	public String handleUserRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(defaultValue = "false", value = "check") boolean check, Model m, HttpSession session) {
		System.out.println(user);
		System.out.println(check);
		
		//check box - validation & server side validation with BindingResult
		try {
			if (!check || result.hasErrors()) {
				if(!check) {
					System.out.println("Terms & conditions not agreed. Please agree & try to submit again.");
					throw new Exception("Terms & conditions not agreed. Please agree & try to submit again.");
				}
				
				//We already have @Valid to show inavlid inputs given on form
				//Here, just to know on console below code of server side validation
				if(result.hasErrors()) {
					System.out.println(result);
					System.out.println("Something went wrong. Invalid feilds present.");
					throw new Exception("Something went wrong. Invalid feilds present.");
				}
				
				//m.addAttribute("user", user);
				//return "signup"; neha12345
			}
			
			//set Role, enabled url and image (Default) feilds
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			//password encryption
			user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
			System.out.println(user);
			//save User in database
			this.userRepository.save(user);
			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("User registered successfully !!", "alert-success"));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e + e.getMessage());
			session.setAttribute("message", new Message("Error: "+e.getMessage(), "alert-danger"));
			m.addAttribute("user", user);
			return "signup";
		}
		return "signup";
	}
	
	
	//Request handler for login page
	@GetMapping(path = "/login")
	public String getLoginPage(Model m) {
		m.addAttribute("title", "Login - Smart Contact Manager");
		//in video - signin instead of login is used
		return "login";
	}
	
	
	//view forgotpasswordsendemail
	@GetMapping(path = "/forgotpasswordsendemail")
	public String forgotpasswordsendemail(Model m) {
		m.addAttribute("title", "Forgot Password - Smart Contact Manager");
		return "/forgotpasswordsendemail";
	}
	
	
	//fetch user email from db(verify) & send otp with email notification
	@PostMapping("/verifyusername&sendotp")
	public String verifyUsernameAndSendOtp(Model m, @RequestParam("username") String username,
			HttpSession session) 
	{
		m.addAttribute("title", "Verify Otp - Smart Contact Manager");
		
		User user =  this.userRepository.getUserByUsername(username);
		if(user==null){
			//user is invalid
			session.setAttribute("message", new Message("Please enter correct Username", "alert-danger"));
			return "redirect:/forgotpasswordsendemail";
		}
		else {
			//sent otp with email service
			Random random = new Random();
			Integer integer = random.nextInt(10000);
			String emailSubject = "<div class='container' style='border: 2px solid black;'>"
					+ "<h1> OTP: "
					+ integer
					+ "</h1>"
					+ "</div>"; 
			Boolean flag = this.emailService.sendEmailService(username,"Verify OTP Received", emailSubject);
			
			if(flag==true) {
				session.setAttribute("otp", integer);
				session.setAttribute("user", user);
				session.setAttribute("message", new Message("OTP sent successfully! Please verify your OTP.", "alert-success"));
			}
			else {
				session.setAttribute("message", new Message("Something went worng! Resend OTP to your username.", "alert-danger"));
				return "redirect:/forgotpasswordsendemail";
			}
		}
		
		return "/verifyotp";
	}
	
	
	//verify sent OTP by user
	@PostMapping("/verifysentotp")
	public String verifysentotp(@RequestParam("userOtp") String userOtp, Model m, HttpSession session) {
		String actualOtp = Integer.toString((Integer)session.getAttribute("otp")) ;
		System.out.println("actual otp: "+actualOtp);
		System.out.println("user otp: "+userOtp);
		if(actualOtp.equals(userOtp)) {
			//allow user password change
			System.out.println("valid otp");
			session.setAttribute("message", new Message("Thank You! Please change your password.", "alert-success"));
			return "/passwordchange";
		}
		else {
			System.out.println("invalid otp");
			session.setAttribute("message", new Message("Enter valid OTP!", "alert-danger"));
			return "/verifyotp";
		}
	}
	
	
	//Forgot password - change user password
	@PostMapping("/changepassword")
	public String passwordChange(@RequestParam("newPassword") String newPassword, 
			HttpSession session, Model m) 
	{
		try {
			System.out.println(newPassword);
			
			User user = (User) session.getAttribute("user");
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);
			
			session.setAttribute("message", new Message("Password change successfully, redirected to login page. Please login with new password.", "alert-success"));
			return "/login";
		}
		catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went worng, Please try again!!", "alert-danger"));
			return "/forgotpasswordsendemail";
		}		
	}
	
	
	
}
