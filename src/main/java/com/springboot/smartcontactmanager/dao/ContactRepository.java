package com.springboot.smartcontactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import com.springboot.smartcontactmanager.entities.Contact;
import com.springboot.smartcontactmanager.entities.User;



@Service
public interface ContactRepository extends JpaRepository<Contact, Integer>  {
	
	@Query("select c from Contact c where c.user.id=:uId")
	public Page<Contact> findContactsByUserId(@Param("uId") int uId, Pageable pageable);
	//Pagination: 2 important things currentpage, no of elements per page(size) is very important
	//Page is interface which is nothing but sublist of list of objects - in this case Page is storing 
	//list of contacts
	//as per size mentioned in PageRequest.of(<currentpage>, <size>)
	//Pageable is also interface - which is necessary to pass in dao/repository method as args, where we 
	//actual fetch list of objects from db
	
	
	//Search Contacts Method
	//searches Contacts on basis of name(query having some words of name) which belongs to particular User
	//given in args of method
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
}
