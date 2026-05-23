package com.springboot.smartcontactmanager.entities;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "User name cannot be left blank !")
	@Length(min = 3, max = 20, message = "Invalid username - length should be between 3 to 20 !")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "Email cannot be left blank !")
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email id !")
	private String email ;
	
	@Length(min = 8, message = "Invalid password - length should be of minimum 8 characters !")
	private String password ;
	private String role ;
	private boolean enabled;
	private String imageUrl ;
	
	@Column(length = 500)
	@NotBlank(message = "About cannot be left blank !")
	@Length(min = 10, max = 500, message = "Invalid About description - length should be between 10 to 500 !")
	private String about ;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();
	
	public User(int id, String name, String email, String password, String role, boolean enabled, String imageUrl,
			String about, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
		this.contacts = contacts;
	}
	public User(String name, String email, String password, String role, boolean enabled, String imageUrl,
			String about, List<Contact> contacts) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
		this.contacts = contacts;
	}
	public User() {
		super();
	}
	
	//UserBuilder constructor
	public User(UserBuilder userBuilder) {
		super();
		this.name = userBuilder.name;
		this.email = userBuilder.email;
		this.password = userBuilder.password;
		this.role = userBuilder.role;
		this.enabled = userBuilder.enabled;
		this.imageUrl = userBuilder.imageUrl;
		this.about = userBuilder.about;
		this.contacts = userBuilder.contacts;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", Contact["+ contacts+"] ]";
	}
	
	
	//For testing purpose - it is implemented
	//UserBuilder Functionality
	public static class UserBuilder{
		private int id;
		private String name;
		private String email ;		
		private String password ;
		private String role ;
		private boolean enabled;
		private String imageUrl ;
		private String about ;
		private List<Contact> contacts = new ArrayList<>();
		
		public UserBuilder() {
			super();
		}

		//setters methods
		public UserBuilder setId(int id) {
			this.id = id;
			return this;
		}
		public UserBuilder setName(String name) {
			this.name = name;
			return this;
		}
		public UserBuilder setEmail(String email) {
			this.email = email;
			return this;
		}
		public UserBuilder setPassword(String password) {
			this.password = password;
			return this;
		}
		public UserBuilder setRole(String role) {
			this.role = role;
			return this;
		}
		public UserBuilder setEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}
		public UserBuilder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}
		public UserBuilder setAbout(String about) {
			this.about = about;
			return this;
		}
		public UserBuilder setContacts(List<Contact> contacts) {
			this.contacts = contacts;
			return this;
		}
		
		//build method
		public User build() {
			return new User(this);
		}
		
	}
	
	
}
