package com.springboot.smartcontactmanager.helper;

public class Message {
	private String content;
	private String className;
	
	public Message() {
		super();
	}
	public Message(String content, String className) {
		super();
		this.content = content;
		this.className = className;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Override
	public String toString() {
		return "Message [content=" + content + ", className=" + className + "]";
	}
	
}
