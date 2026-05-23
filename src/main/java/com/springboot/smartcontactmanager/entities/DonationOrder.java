package com.springboot.smartcontactmanager.entities;

import org.hibernate.bytecode.internal.bytebuddy.PrivateAccessorException;
import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DonationOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long dId;
	private String orderId;
	private String receiptId;
	private String paymentId;
	private int amount;
	private String status;
	@ManyToOne
	private User user;
	
	public DonationOrder(String orderId, String receiptId, 
			int amount, String status, String paymentId, User user) 
	{
		super();
		this.orderId = orderId;
		this.receiptId = receiptId;
		this.amount = amount;
		this.status = status;
		this.paymentId = paymentId;
		this.user = user;
	}
	public DonationOrder() {
		super();
	}
	
	public long getdId() {
		return dId;
	}
	public void setdId(long dId) {
		this.dId = dId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	@Override
	public String toString() {
		return "DonationOrder [dId=" + dId + ", orderId=" + orderId + ", receiptId=" + receiptId + ", paymentId="
				+ paymentId + ", amount=" + amount + ", status=" + status + ", user=" + user + "]";
	}
	
}
