package com.springboot.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ReplaceOverride;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.smartcontactmanager.dao.ContactRepository;
import com.springboot.smartcontactmanager.dao.DonateOrderRepository;
import com.springboot.smartcontactmanager.dao.UserRepository;
import com.springboot.smartcontactmanager.entities.Contact;
import com.springboot.smartcontactmanager.entities.DonationOrder;
import com.springboot.smartcontactmanager.entities.User;
import com.springboot.smartcontactmanager.helper.Message;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import com.razorpay.*;

@MultipartConfig
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	DonateOrderRepository donateOrderRepository;
	
	private User userInfo;
	
	//@ModelAttribute : will cause this method to run everytime before any request handler is being run in this class
	//this is used to fetch user information which is required in almost every request handler in this class
	@ModelAttribute
	public void getUserInfo(Principal principal) {
		System.out.println("fetch user details");
		String username = principal.getName();
		this.userInfo = userRepository.getUserByUsername(username);
		System.out.println("userinfo: "+userInfo);
	}
	
	//show user profile
	@RequestMapping("/profile")
	public String openUserProfile(Model m, Principal principal) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		
		//Principal class
		//Class comes spring security - which helps to extract data from db with username
		//pricipal.getName() method - helps us tp provide username from login form (which we provide while login)
		//With username, we can fetch user details from db - userRepository class method like below
		String username = principal.getName();
		System.out.println("Username: "+username);
		User user = userRepository.getUserByUsername(username);
		System.out.println("User: "+ user);
		m.addAttribute("user", user);
		//output
		//Username: nehahaldankar@gmail.com
		//User: User [id=452, name=Neha Hal, email=nehahaldankar@gmail.com, password=$2a$10$Y5Bk9bjl.xwZc3fPMPelaeRnDX8t84gH/iE/Ao41XtYnflHla.BRK, role=ROLE_USER, enabled=true, imageUrl=default.png, about=hellooooooooooooooooo, Contact[[]] ]
		
		//in video "user_dashboard" is use instead "userprofile"
		m.addAttribute("title","User Profile - Smart Contact Manager");
		return "normal/userprofile";
	}
	
	//show user home page
	@RequestMapping("/home")
	public String showHome(Model m) {
		m.addAttribute("user", this.userInfo);
		m.addAttribute("title", "User Home - Smart Contact Manager");
		return "normal/home";
	}
	
	
	@RequestMapping("/addcontact")
	public String getAddContactForm(Model m, Principal principal) {
		m.addAttribute("title", "Add Contacts - Smart Contact Manager");
		
		//fetch user - with username
		String username = principal.getName();
		User user = userRepository.getUserByUsername(username);		
		m.addAttribute("user", user);
		//System.out.println("Username: "+username);
		//System.out.println("User: "+ user);
		
		m.addAttribute("title","Add Contacts - Smart Contact Manager");
		m.addAttribute("contact", new Contact());
		return "normal/addcontact";
	}
	
	@PostMapping("/processaddcontactform")
	public String processAddContactForm(@ModelAttribute("contact") Contact contact, @RequestParam("profileimage") MultipartFile multipartFile , Principal principal, 
			Model m, HttpSession httpSession) {
		
		try {
		//upload profile image in desired folder in project
		contact.setImage(multipartFile.getOriginalFilename());
		File file = new ClassPathResource("static/images").getFile();
		Path actualPath = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
		//for read and write of Multipartfile
		Files.copy(multipartFile.getInputStream(), actualPath, StandardCopyOption.REPLACE_EXISTING);
		//System.out.println("file:"+ file +" actualpath:"+actualPath);
		
		//set all form submitted values into Contact object - using @ModelAttribute("contact") Contact contact
		//System.out.println("Contact: "+ contact);
		
		//fetch user - with username
		String username = principal.getName();
		User user = userRepository.getUserByUsername(username);
		m.addAttribute("user", user);
		//System.out.println("Username: "+username);
		//System.out.println("User: "+ user);
		
		//as relation between Contact and User class is bidirectional
		//we are saving new contact into user's contacts list
		//and also saving user's details in contacts table - with user id
		user.getContacts().add(contact);
		contact.setUser(user);
		
		//send success message
		httpSession.setAttribute("message", new Message("New Contact added successfully!", "alert-success"));
		
		//save user in db
		this.userRepository.save(user);
		
		System.out.println("Contact added successfully!!");
		} catch (Exception e) {
			String username = principal.getName();
			User user = userRepository.getUserByUsername(username);
			m.addAttribute("user", user);
			
			e.printStackTrace();
			//send failure message
			httpSession.setAttribute("message", new Message("Something went wrong, try again!", "alert-danger"));
		}
		return "normal/addcontact";
	}
	
	//view contacts for single user
	//Pagination: 2 important things currentpage, no of elements per page(size) is very important
	//Page is interface which is nothing but sublist of list of objects - in this case Page is storing list of contacts
	//as per size mentioned in PageRequest.of(<currentpage>, <size>)
	//Pageable is also interface - which is necessary to pass in dao/repository method as args, where we 
	//actual fetch list of objects from db
	@RequestMapping("/viewcontacts/{currentPage}")
	public String getContacts(@PathVariable("currentPage") Integer currentPage, Model m) {
		m.addAttribute("title", "View Contacts - Smart Contact Manager");
		m.addAttribute("user", userInfo);
		Pageable pageable = PageRequest.of(currentPage, 3);
		Page<Contact> contactsList = this.contactRepository.findContactsByUserId(this.userInfo.getId(), pageable);
		System.out.println("contactlist: "+contactsList);
		
		//this data will help to actually make work bootstrap pagination html code
		//to make it dynamic
		m.addAttribute("contacts", contactsList);
		m.addAttribute("currentPage", currentPage);
		m.addAttribute("totalPages", contactsList.getTotalPages());
		return "normal/viewcontacts";
	}
	
	
	//view specific contact details
	@RequestMapping("/{cid}/viewcontactprofile")
	public String viewContactProfile(@PathVariable("cid") Integer cid, Model m) {
		
		m.addAttribute("title", "Contact Details - Smart Contact Manager");
		m.addAttribute("user", userInfo);
		
		//fetch specific contact details with cid - contact id
		Optional<Contact> contactDetail = this.contactRepository.findById(cid);
		Contact contact = contactDetail.get();
		
		//Security logic
		//incase a user can only see his/her contacts only - and not contacts which belong to other users
		//System.out.println(this.userInfo.getId());
		//System.out.println(contact.getUser().getId());
		if(this.userInfo.getId() == contact.getUser().getId()) {
			//when current User.id login and contact of user having User.id is same
			m.addAttribute("contact", contact);
			m.addAttribute("userAccess", true);
		}
		else {
			//User trying to see someone else contacts - display error
			m.addAttribute("userAccess", false);
		}
		
		return "normal/viewcontactprofile";
	}
	
	
	//update contact details
	@PostMapping("/updatecontact/{cId}")
	public String updateContactDetails(@PathVariable("cId") int cId ,Model m) {
		m.addAttribute("title", "Update Contact Details - Smart Contact Manager");
		m.addAttribute("user", userInfo);
		
		//fetch contact details
		Optional<Contact> contacts = this.contactRepository.findById(cId);
		Contact contact = contacts.get();
		m.addAttribute("contact", contact);
		
		return "normal/updatecontactform";
	}
	
	//process update contact form
	@PostMapping("/processupdatecontactform")
	public String processUpdateContactForm(@ModelAttribute("contact") Contact contact, 
			@RequestParam("profileimage") MultipartFile multipartFile, 
			Model m, HttpSession session) 
	{
		try {
			Contact oldContactDetails = this.contactRepository.findById(contact.getcId()).get();
			String oldProfileImg = oldContactDetails.getImage();
			String newProfileImage = multipartFile.getOriginalFilename();
			
			System.out.println(contact.getcId());
			
			if(multipartFile.isEmpty()) {
				//save updated details
				contact.setUser(userInfo);
				contact.setImage(oldProfileImg);
				this.contactRepository.save(contact);

				//give message
				session.setAttribute("message", new Message("Contact Updated successfully!", "alert-success"));
			}
			else {
				//save profile image
				contact.setUser(userInfo);
				contact.setImage(newProfileImage);
				File file = new ClassPathResource("static/images").getFile();
				Path actualPath = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());   
				Files.copy(multipartFile.getInputStream(), actualPath , StandardCopyOption.REPLACE_EXISTING);
				
				//delete old profile image - only if it is not default.png
				if(!oldProfileImg.equals("default.png")) {
					File deleteFile = new File(file, oldProfileImg); 
					deleteFile.delete();
				}
			
				//save updated details
				this.contactRepository.save(contact);
				
				//give message
				session.setAttribute("message", new Message("Contact Updated successfully!", "alert-success"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//send failure message
			session.setAttribute("message", new Message("Something went wrong, try again!", "alert-danger"));
		}
		
		return "redirect:/user/"+contact.getcId()+"/viewcontactprofile";
	}
	
	
	//delete contact method
	@GetMapping("/deletecontact/{cId}")
	public String deleteContact(@PathVariable("cId") int cId, HttpSession session) {
		try {
			List<Contact> userContacts = this.userInfo.getContacts();
			//System.out.println("userContacts: "+userContacts);
			Contact deleteContact = this.contactRepository.findById(cId).get();
			//System.out.println("deleteContact: "+deleteContact.getName());
			
			if(this.userInfo.getId() == deleteContact.getUser().getId()) {
				userContacts.remove(deleteContact);
				this.contactRepository.delete(deleteContact);
				
				//give message
				session.setAttribute("message", new Message("Contact Deleted successfully!", "alert-success"));
			}
			else {
				throw new Exception("Invalid delete!");
			}
		
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong while deleting contact, try again!", "alert-danger"));
			e.printStackTrace();
		}
		
		return "redirect:/user/viewcontacts/0";
	}
	
	
	//change user password method
	@GetMapping("/settings")
	public String getSettings(Model m) {
		m.addAttribute("title", "Settings - Smart Contact Manager");
		m.addAttribute("user", userInfo);
		
		return "normal/settings";
	}
	
	
	@PostMapping("/changepassword")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, 
			@RequestParam("newPassword") String newPassword, HttpSession session) 
	{
		try {
			System.out.println("oldPassword: "+ oldPassword);
			System.out.println("newPassword: "+ newPassword);
			
			//if oldpassword matches with user registered password from db - then change password
			if(bCryptPasswordEncoder.matches(oldPassword, this.userInfo.getPassword())) {
				this.userInfo.setPassword(bCryptPasswordEncoder.encode(newPassword));
				this.userRepository.save(userInfo);
				System.out.println("new password updated");
				session.setAttribute("message", new Message("Password updated succesfully!", "alert-success"));
			}
			else {
				//oldpassword != user actual db password
				throw new Exception();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Incorrect old password!!", "alert-danger"));
		}
		
		return "redirect:/user/settings";
	}
	
	
	//donate us page
	@GetMapping("/donateus")
	public String getDonateUs(Model m) {
		m.addAttribute("title", "Donate Us - Smart Contact Manager");
		m.addAttribute("user", userInfo);
		return "normal/donateus";
	}
	
	
	//create donate order request with Razorpay
	@PostMapping("/createorder")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> amountDetails, HttpSession session) {
		try {
		//System.out.println(amountDetails);	
		
		RazorpayClient razorpay = new RazorpayClient("rzp_test_jabxAwxeNFGlPC", "1laXWGFqTlTHyk10TQZfVGbd");

		JSONObject orderRequest = new JSONObject();
		int amount = Integer.parseInt(amountDetails.get("amount").toString());
		orderRequest.put("amount",amount*100);
		orderRequest.put("currency","INR");
		
		//Generate Random receipt number
		Random random = new Random();
		int rNo = random.nextInt(99999);
		orderRequest.put("receipt", "SCMDOrder"+rNo);
		
		JSONObject notes = new JSONObject();
		notes.put("notes_key_1","Donation For Better World");
		orderRequest.put("notes",notes);

		Order order = razorpay.orders.create(orderRequest);
		//System.out.println("Order: "+order);
		//Order: {"amount":44500,"amount_paid":0,"notes":{"notes_key_1":"Donation For Better World"},"created_at":1718460359,"amount_due":44500,"currency":"INR","receipt":"SCMDOrder36286","id":"order_ON3zX0NYgm5JyZ","entity":"order","offer_id":null,"attempts":0,"status":"created"}
		
		//save newly created order of payment
		DonationOrder donationOrder = new DonationOrder();
		donationOrder.setAmount(amount*100);
		donationOrder.setOrderId(order.get("id"));
		donationOrder.setReceiptId(order.get("receipt"));
		donationOrder.setStatus(order.get("status"));
		donationOrder.setUser(userInfo);
		this.donateOrderRepository.save(donationOrder);
		
		return order.toString();
		
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: Transaction Failed! Try again.";
		}
	}
	
	
	@PostMapping("/updateorder")
	@ResponseBody
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object>order) {
		try {
			DonationOrder donationOrder = this.donateOrderRepository.findByOrderId(order.get("orderId").toString());
			if(donationOrder!=null) {
				donationOrder.setStatus("Paid");
				donationOrder.setPaymentId(order.get("paymentId").toString());
				this.donateOrderRepository.save(donationOrder);
				
				return ResponseEntity.status(HttpStatus.OK).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
}
