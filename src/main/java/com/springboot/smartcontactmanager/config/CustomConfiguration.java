package com.springboot.smartcontactmanager.config;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.FormLoginBeanDefinitionParser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//Spring Security working & flow:
//When spring application is ran initially at that time all beans declared in CustomConfiguration for spring security
//are loaded - includes bean passwordEncoder, securityfilterchain, userdetailsservice 
//Login form (custom/default) of spring security has two parameters username & password on basis of which it fetches
//information of user from database
//When login form is submitted with username & password parameters - it calls UserDetailsService implementation 
//class and its loadUserbyUsername method
//It is very important to use same (default/custom)login form feild name and  UserDetailsService implementation 
//class and its loadUserbyUsername method - then only login functionality will work properly
//This method fetches user information from database - creates UserDetails implementation class object with user details
//If loadUserbyUsername is unable to fetch user information from db - it throws user not found exception
//User details like username, password, role etc - further fetched with help of UserDetails class's methods

@Configuration
@EnableWebSecurity
public class CustomConfiguration{
	
	@Autowired
	CustomSuccessHandler customSuccessHandler;
	
	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService getUserDetailsServiceImpl() {
		return new UserDetailsServiceImpl();
	}	
	
	@Bean
	public DaoAuthenticationProvider getDaoAuthenticationProvider() {
		//System.out.println("in DaoAuthenticationProvider");
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsServiceImpl());
		daoAuthenticationProvider.setPasswordEncoder(getBCryptPasswordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//System.out.println("in securityFilterChain");
		http
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(authorize->authorize
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll())
		.formLogin(formlogin->formlogin
				.loginPage("/login")
				.loginProcessingUrl("/dologin")
				.successHandler(customSuccessHandler) //.successHandler() incase there are role based login (Admin & Users)
				
				//In case Login fails - it can be handled by using below anyone method
				//.failureForwardUrl(null).failureUrl(null).failureHandler(null)
				);
		
		
		//UserRole - USER
		//currently there is only one user role we are working with "USER" - after successfull authentication
		//it redirects to .defaultSuccessUrl("/user/profile")
		
		//UserRole - ADMIN
		//To cater user role ADMIN - need to do a class implementation + configuration (do not have exact idea) -
		//will learn in future
		
		http.authenticationProvider(getDaoAuthenticationProvider());
		
		return http.build();
	} 
	
	
}
