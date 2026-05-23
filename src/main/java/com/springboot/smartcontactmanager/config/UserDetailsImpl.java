package com.springboot.smartcontactmanager.config;

import java.util.Collection;
import java.util.List;
import org.hibernate.bytecode.internal.bytebuddy.PrivateAccessorException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.springboot.smartcontactmanager.entities.User;

public class UserDetailsImpl implements UserDetails{
	
	private User user;

	public UserDetailsImpl(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//Understanding - Need to work more on SGA  
		System.out.println("get user roles");
		SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole());
		return List.of(sga);
	}

	@Override
	public String getPassword() {
		System.out.println("get user password");
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		System.out.println("get user username");
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
