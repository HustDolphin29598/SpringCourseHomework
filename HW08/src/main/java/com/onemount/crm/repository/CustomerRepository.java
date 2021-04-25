package com.onemount.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.onemount.crm.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
  
}
