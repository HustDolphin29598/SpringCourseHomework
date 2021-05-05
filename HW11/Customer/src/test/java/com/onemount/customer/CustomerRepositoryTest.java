package com.onemount.customer;

import com.onemount.customer.model.Customer;
import com.onemount.customer.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql({"/customer.sql"})
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void getCustomerCount(){
        assertThat(customerRepository.count()).isEqualTo(10);
    }

    @Test
    public void get5thCustomer() {
        Optional<Customer> customer = customerRepository.findById(5L);
        if (customer.isPresent()) {
            assertThat(customer.get()).extracting("firstName").isEqualTo("Ogdon");
        }
    }

    @Test
    public void addNewCustomer(){
        long count = customerRepository.count();
        Customer customer = new Customer();
        customerRepository.save(customer);
        assertThat(customerRepository.count()).isEqualTo(count+1);
    }

    @Test
    public void findCustomerByEmail(){
        List<Customer> customer = customerRepository.findCustomerByEmail("vberriball0@mlb.com");
        if (!customer.isEmpty()) {
            assertThat(customer.get(0)).extracting("firstName").isEqualTo("Veda");
        }
    }

    @Test
    public void findCustomerByJob(){
        List<Customer> customer = customerRepository.findCustomerByJob("Tax Accountant");
        if (!customer.isEmpty()) {
            assertThat(customer.get(0)).extracting("firstName").isEqualTo("Marlon");
        }
    }

    @Test
    public void deleteCustomer(){
        long count = customerRepository.count();
        customerRepository.deleteById(4L);
        assertThat(customerRepository.count()).isEqualTo(count-1);
    }

    @Test
    public void addNewCustomerEntityManager() {
        Customer customer = new Customer();
        Long id = (Long) testEntityManager.persistAndGetId(customer);
        assertThat(testEntityManager.find(Customer.class,  id)).isEqualTo(customer);
    }

    @Test
    public void getEntityManagerFromTestingEntityManager() {
        EntityManager entityManager = testEntityManager.getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM customer WHERE id = 1", Customer.class);
        Customer customer = (Customer) query.getSingleResult();
        assertThat(customer.getEmail()).isEqualTo("vberriball0@mlb.com");
    }
}
