package com.onemount.crm.controller;

import com.onemount.crm.exception.CRMException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.onemount.crm.model.*;
import com.onemount.crm.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/api/customers")
@Slf4j
public class CustomerController {
  @Autowired
  private CustomerService customerService;
  
  @GetMapping
  public ResponseEntity<List<Customer>> getCustomers(){
    return ResponseEntity
      .ok()
      .header("onemount", "specialvalue")
      .body(customerService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable("id") long id){
    return ResponseEntity
      .ok()
      .body(customerService.findById(id));
  }

  @PostMapping()
  public ResponseEntity<Customer> addCustomer(@RequestBody CustomerPOJO customer) {
    Customer newCustomer = customerService.save(customer);
    try {
      return ResponseEntity.created(new URI("/api/customers" + newCustomer.getId())).body(newCustomer);
    } catch (URISyntaxException e) {
      //log.error(e.getMessage());
      return ResponseEntity.ok().body(newCustomer);      
    }    
  }

  @PutMapping("/{id}")
  public ResponseEntity<Customer> updateCustomer(@RequestBody CustomerPOJO book, @PathVariable long id) {
    Customer updatedBook = customerService.update(id, book);
    return ResponseEntity.ok().body(updatedBook);
  }

  @PatchMapping("{id}")
  public ResponseEntity<String> updateBookEmail(@RequestBody String email, @PathVariable long id) {
    customerService.updateEmail(id, email);
    return ResponseEntity.ok().body(email);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Long> deleteBookById(@PathVariable long id) {
    customerService.deleteById(id);
    return ResponseEntity.ok(id);
  }

  @GetMapping(value = "/slow")
  public ResponseEntity<List<Customer>> findSlowCustomers(HttpServletRequest request){
    Random random = new Random();
    int ranValue = random.nextInt(6);

    if (ranValue > 4) {
      List<Customer> customers = customerService.getAll();
      return ResponseEntity.ok(customers);
    }

    try {
      Thread.sleep(1000);
      log.debug("After sleep");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new CRMException("Sleep is interrupted", e, HttpStatus.REQUEST_TIMEOUT);
    }

    log.error("Timeout service");
    throw new CRMException("Timeout service", HttpStatus.REQUEST_TIMEOUT);
  }
}
