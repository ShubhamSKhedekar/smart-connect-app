package com.springboot.smartcontactmanager.controller;

import static org.mockito.Mockito.when;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import com.springboot.smartcontactmanager.dao.ContactRepository;
import com.springboot.smartcontactmanager.dao.UserRepository;
import com.springboot.smartcontactmanager.entities.Contact;
import com.springboot.smartcontactmanager.entities.User;


//Unit Testing with Mockito (Configuration of Mockito framework is already their)
//-Junit testing and Mockito testing are differebt ways of testing
//-In Junit - we dont have to mock functionality / methods instead we use directly actual
//project methods to do testing and further check values with Assertions class

//-In Mockito - we mock/create fake functions - here we do testing in isolation - it doesnt
//have direct relation with actual methods implemented in project unlike Junit
//-If in Junit we would have use below code:
//@BeforeEach
//private void setUp() {
//	searchController = new SearchController();
//}
//-Here we actually use SearchController object - but in mockito we have all definations of 
//SearchController class - but we dont actually use it - we make fake copy in Mockito
//-Mockito - is like reel world testing and Junit is real world testing

//Below is Mockito testing 

public class SearchControllerTest {
	@InjectMocks
	private SearchController searchController;
	
	@Mock
	private ContactRepository contactRepository;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private Principal principal;
	
	@BeforeEach
	private void setUp() {
		//searchController = new SearchController();
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Test
	public void searchContactsTest() {
		
		//model
		String query = "bh";
		User user = new User.UserBuilder()
				.setId(1)
				.setName("shubham")
				.setEmail("shubham.khedekar@gmail.com")
				.setPassword("abcdefh")
				.setRole("Admin")
				.setEnabled(false)
				.setImageUrl("image.png")
				.setAbout("Test About!")
				.setContacts(null)
				.build();
		
		List<Contact> contacts = new ArrayList<>();
		contacts.add(new Contact(10, "Disha", "Maldikar", "BOA", "dishmaldikar@gmail.com", "abc", "default1.png",
			"test1", user));
		contacts.add(new Contact(11, "Aditi", "pendurkar", "XYZ", "test2@gmail.com", "abc", "default2.png",
				"test2", user));
		contacts.add(new Contact(12, "shweta", "Maldikar", "PQR", "test3@gmail.com", "abc", "default3.png",
				"test3", user));
		
		//set contacts
		user.setContacts(contacts);
		
		//Make mock calls - config
		when(principal.getName()).thenReturn("shubham");
		when(userRepository.getUserByUsername(principal.getName()))
							.thenReturn(user);
		when(contactRepository.findByNameContainingAndUser(query, user))
							.thenReturn(contacts);
		
		
		//call actual method
		List<Contact> receivedContacts = this.searchController.searchContacts(principal, query);
		
		//Checking 
		Assertions.assertEquals(user.getContacts(), receivedContacts);
		Assertions.assertEquals(user.getContacts().size(), receivedContacts.size());
		Assertions.assertEquals(user.getName(), receivedContacts.get(0).getUser().getName());
		
		return;
	}
}
