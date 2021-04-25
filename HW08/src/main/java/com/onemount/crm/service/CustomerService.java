package com.onemount.crm.service;

import com.onemount.crm.mapper.CustomerMapper;
import com.onemount.crm.model.CustomerPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.onemount.crm.exception.CRMException;
import com.onemount.crm.model.Customer;
import com.onemount.crm.repository.CustomerRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {
  @Autowired
  private CustomerRepository customerRepository;

  static final String CUSTOMER_ID_NOT_EXIST = "Customer id %d doest not exist";
  private Validator validator;

  public CustomerService(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  public List<Customer> getAll(){
    return customerRepository.findAll();
  }

  public Customer findById(long id){
    Optional<Customer> customer = customerRepository.findById(id);
    if (customer.isPresent()) {
      return customer.get();
    } else {
      throw new CRMException("Cannot find customer",
        new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)), 
        HttpStatus.NOT_FOUND);      
    }
    
  }

  private void validateCustomer(CustomerPOJO customerPOJO) {
    Set<ConstraintViolation<CustomerPOJO>> constraintViolations = validator.validate(customerPOJO);
    if (!constraintViolations.isEmpty()) {
      throw new CRMException("New customer violates constrains",
              new Throwable(
                      constraintViolations
                              .stream().map(ConstraintViolation::getMessage)
                              .collect(Collectors.joining(", "))),
              HttpStatus.BAD_REQUEST);
    }
  }
  public Customer save(CustomerPOJO customerPOJO) {
    validateCustomer(customerPOJO);
    Customer newCustomer = CustomerMapper.INSTANCE.pojoToCustomer(customerPOJO);
    return customerRepository.save(newCustomer);
  }

  public Customer update(long id, CustomerPOJO customerPOJO) {
    validateCustomer(customerPOJO);
    Optional<Customer> optionalBook = customerRepository.findById(id);

    if (optionalBook.isPresent()) {
      Customer updatedCustomer = CustomerMapper.INSTANCE.pojoToCustomer(customerPOJO);
      customerRepository.save(updatedCustomer);
      return updatedCustomer;
    } else {
      throw new CRMException("Cannot update a customer",
              new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)),
              HttpStatus.NOT_FOUND);
    }
  }

  public void updateEmail(long id, String email) {
    Optional<Customer> optionalBook = customerRepository.findById(id);
    if (optionalBook.isPresent()) {
      Customer customer = optionalBook.get();
      customer.setEmail(email);
      customerRepository.save(customer);
    } else {
      throw new CRMException("Cannot update email of a customer",
              new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)),
              HttpStatus.NOT_FOUND);
    }
  }

  public void updateMobile(long id, String mobile) {
    Optional<Customer> optionalBook = customerRepository.findById(id);
    if (optionalBook.isPresent()) {
      Customer customer = optionalBook.get();
      customer.setMobile(mobile);
      customerRepository.save(customer);
    } else {
      throw new CRMException("Cannot update mobile of a customer",
              new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)),
              HttpStatus.NOT_FOUND);
    }
  }

  public void deleteById(long id) {
    Optional<Customer> optionalBook = customerRepository.findById(id);
    if (optionalBook.isPresent()) {
      customerRepository.delete(optionalBook.get());
    } else {
      throw new CRMException("Cannot delete a customer",
              new Throwable(String.format(CUSTOMER_ID_NOT_EXIST, id)),
              HttpStatus.NOT_FOUND);
    }
  }
}
