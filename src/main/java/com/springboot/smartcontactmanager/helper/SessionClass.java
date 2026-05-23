package com.springboot.smartcontactmanager.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//default component name: sessionClass -- camelcase of classname is followed
@Component
public class SessionClass {

	public void removeMessageAttribute() {
		try {
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
			System.out.println("Meesage Attribute removed");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
