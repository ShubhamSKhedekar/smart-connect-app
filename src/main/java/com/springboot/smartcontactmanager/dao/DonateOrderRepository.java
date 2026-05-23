package com.springboot.smartcontactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.smartcontactmanager.entities.DonationOrder;
import java.util.List;


public interface DonateOrderRepository extends JpaRepository<DonationOrder, Long> {

	public DonationOrder findByOrderId(String orderId);
	
	public Boolean deleteByStatus(String status);
}
