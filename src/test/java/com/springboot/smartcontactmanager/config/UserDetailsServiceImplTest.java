package com.springboot.smartcontactmanager.config;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.smartcontactmanager.dao.UserRepository;
import com.springboot.smartcontactmanager.entities.Contact;
import com.springboot.smartcontactmanager.entities.User;


public class UserDetailsServiceImplTest {
	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Mock
	private UserRepository userRepository;
	
//	@Mock
//	private UserDetailsImpl userDetailsImpl;
	
	public UserDetailsServiceImplTest() {
		//to capture all @Mock annotated Reference variables 
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void loadUserByUsernameTest() {
		
		//model
		String username = "shubham@gmail.com";
		User user = new User(1, "shubham", "shubham@gmail.com" , "abcdefgh", 
					"Admin", true, "test1Image.png", "test1", null);
		
		//Or you can make user using build method
//		User user = new User.UserBuilder()
//				.setId(1)
//				.setName("shubham")
//				.setEmail("shubham.khedekar@gmail.com")
//				.setPassword("abcdefh")
//				.setRole("Admin")
//				.setEnabled(false)
//				.setImageUrl("image.png")
//				.setAbout("Test About!")
//				.setContacts(null)
//				.build();
		
		//mock calls - config
		when(userRepository.getUserByUsername(username)).thenReturn(user);
		
		//make actual call
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) this.userDetailsServiceImpl.loadUserByUsername(username);
		
		//checking and validations
		Assertions.assertEquals(user.getEmail(), userDetailsImpl.getUsername());
		
	}
}
