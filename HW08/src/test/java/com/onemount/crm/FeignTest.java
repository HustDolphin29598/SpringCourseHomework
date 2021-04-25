package com.onemount.crm;

import com.github.javafaker.Faker;
import com.onemount.crm.exception.APIException;
import com.onemount.crm.model.Customer;
import com.onemount.crm.model.CustomerPOJO;
import com.onemount.crm.restclient.APIErrorDecoder;
import com.onemount.crm.restclient.CRMRequest;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class FeignTest {
    private CRMRequest customerClient;
    private Faker faker;
    @BeforeAll
    public void buildFeignClient() {
        final Decoder decoder = new GsonDecoder();
        final Encoder encoder = new GsonEncoder();
        customerClient = Feign.builder()
                // .retryer(new Retryer.Default(100, 300, 5))
                .client(new OkHttpClient()).decoder(decoder).encoder(encoder).logLevel(Logger.Level.BASIC)
                .errorDecoder(new APIErrorDecoder(decoder)).target(CRMRequest.class, "http://localhost:8080/api/customers");

        faker	= new Faker();
    }

    @Test
    @DisplayName(" 1. GET /api/customers")
    @Order(1)
    void getAllCustomers() {
        List<Customer> customers = customerClient.list();
        assertThat(customers.size()).isGreaterThan(1);
    }

    @Test
    @DisplayName(" 2. GET /api/customers/{id}")
    @Order(2)
    void getCustomerById() {
        Customer customer = customerClient.get(1L);
        assertThat(customer).isNotNull();
    }

    @Test
    @DisplayName(" 3. POST /api/customers")
    @Order(2)
    void postNewCustomer() {
        String fullname = faker.leagueOfLegends().champion();
        String mobile = "0123456789";
        String email = faker.bothify("????##@mail.com");

        System.out.println(fullname);
        System.out.println(mobile);
        System.out.println(email);
        CustomerPOJO customerPOJO = new CustomerPOJO(fullname, email, mobile);
        Customer createdCustomer = customerClient.post(customerPOJO);
        assertThat(createdCustomer.getId()).isPositive();
    }

    @Test
    @DisplayName(" 4. PUT /api/customers/{id}")
    @Order(4)
    void updateNewCustomer() {
        String fullname = faker.leagueOfLegends().champion();
        String mobile = "0123456789";
        String email = faker.bothify("????##@mail.com");

        CustomerPOJO customerPOJO = new CustomerPOJO(fullname, email, mobile);
        Customer updatedCustomer = customerClient.put(3, customerPOJO);
//        assertThat(updatedCustomer.getId()).isEqualTo(3); //only when sort test case by order asc
        assertThat(updatedCustomer.getFullname()).isEqualTo(fullname);
        assertThat(updatedCustomer.getEmail()).isEqualTo(email);
        assertThat(updatedCustomer.getMobile()).isEqualTo(mobile);

    }

//    @Test
//    @DisplayName(" 5. DELETE /api/customers/{id}")
//    @Order(5)
//    void deleteCustomerById() {
//        long idOfDeletedCustomer = customerClient.delete(4L);
//        assertThat(idOfDeletedCustomer).isEqualTo(4L);
//    }

    @Test
    @DisplayName(" 6. DELETE Not Found Customer")
    @Order(6)
    void deleteNotFoundCustomer() {
        try {
            customerClient.delete(100L);
        } catch (APIException e) {
            assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(e.getMessage()).isEqualTo("CRMException : Cannot delete a customer");
        }

    }

    @Test
    @DisplayName(" 7. Update Not Found Customer")
    @Order(7)
    void UpdateNotFoundCustomer() {
        try {
            String fullname = faker.leagueOfLegends().champion();
            String mobile = "0123456789";
            String email = faker.bothify("????##@mail.com");

            CustomerPOJO customerPOJO = new CustomerPOJO(fullname, email, mobile);
            customerClient.put(100L, customerPOJO);
        } catch (APIException e) {
            assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(e.getMessage()).isEqualTo("CRMException : Cannot update a customer");
        }

    }

    @Test
    @DisplayName(" 8. Create 10 customer")
    @Order(8)
    void post10NewCustomer() {
        List<Customer> customers = customerClient.list();
        long numberOfCustomersBeforeAdd = customers.size();
        for (int i = 0; i < 10; i++) {
            String fullname = faker.leagueOfLegends().champion();
            String mobile = "0123456789";
            String email = faker.bothify("????##@mail.com");

            CustomerPOJO customerPOJO = new CustomerPOJO(fullname, email, mobile);
            customerClient.post(customerPOJO);
        }
        List<Customer> customersAfter = customerClient.list();
        assertThat(customersAfter.size() - numberOfCustomersBeforeAdd).isEqualTo(10);
    }

    @Test
    @DisplayName(" 9. GET /api/customers/slow")
    @Order(9)
    void getSlowCustomers() {
        try {
            callSlowAPI();
        } catch (APIException ex) {
            assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.REQUEST_TIMEOUT);
            assertThat(ex.getMessage()).isEqualTo("CRMException : Timeout service");
        }
    }

    @Test
    @DisplayName(" 10. Spring Retryable")
    @Order(10)
    void getSlowCustomersSpringRetryable() {

        RetryTemplate template = RetryTemplate.builder().maxAttempts(4).fixedBackoff(1000).build();

        try {
            List<Customer> customers = template.execute((RetryCallback<List<Customer>, Exception>) context -> {
                log.info("Retry attempt " + context.getRetryCount());
                try {
                    return customerClient.listSlow();
                } catch (APIException apiEx) {
                    throw new RuntimeException("Failed to execute after " + (context.getRetryCount() + 1));
                }
            });

            assertThat(customers.size()).isGreaterThan(1);
            log.info("Call slow API success with " + customers.size() + " records returned");

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }


    @Retryable(value = RuntimeException.class, maxAttempts = 6, backoff = @Backoff(delay = 1000))
    private void callSlowAPI() {
        customerClient.listSlow();
    }

    @Test
    @DisplayName("10. POST /api/customers invalid customer")
    @Order(10)
    void postInvalidCustomer() {
        try {
            CustomerPOJO customer = new CustomerPOJO("a", "b", "9");
            customerClient.post(customer);
        } catch (APIException e) {
            assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(e.getMessage()).isEqualTo("CRMException : New customer violates constrains");
        }
    }
}
