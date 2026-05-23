package com.springboot.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.smartcontactmanager.dao.ContactRepository;
import com.springboot.smartcontactmanager.dao.UserRepository;
import com.springboot.smartcontactmanager.entities.Contact;
import com.springboot.smartcontactmanager.entities.User;

@RestController
@RequestMapping("/user")
public class SearchController {
	
	@Autowired
	ContactRepository contactRepository;

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/searchcontacts/{query}")
	public  List<Contact> searchContacts (Principal principal, @PathVariable("query") String query) {
		//get logged in user details
		User currentUser = this.userRepository.getUserByUsername(principal.getName());
	
		//fetch contacts of user according to name/query entered in search bar
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, currentUser);
		System.out.println("seacrh controller:"+contacts);
		return contacts;
	}
}
