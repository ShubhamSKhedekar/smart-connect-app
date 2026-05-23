package com.springboot.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.smartcontactmanager.dao.UserRepository;
import com.springboot.smartcontactmanager.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	public UserDetailsServiceImpl() {
		super();
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//username = user email
		//method is used to return user object from db - by its email/username
		User user = this.userRepository.getUserByUsername(username);
		//System.out.println("UserDetailsServiceImpl 1: "+user);
		if(user==null) {
			//System.out.println("UserDetailsServiceImpl 2: "+user);
			throw new UsernameNotFoundException("User not found !!");
		}
		return new UserDetailsImpl(user);
	}

}
