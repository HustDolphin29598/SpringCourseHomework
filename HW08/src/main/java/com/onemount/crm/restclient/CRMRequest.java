package com.onemount.crm.restclient;

import com.onemount.crm.model.Customer;
import com.onemount.crm.model.CustomerPOJO;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface CRMRequest extends BaseRequest<Customer, CustomerPOJO>{
    @Headers("Content-Type: application/json")
    @RequestLine("PATCH /{id}/")
    @Body("{title}")
    String patch(@Param("id") long id, @Param("title") String title);


    @RequestLine("GET /slow")
    List<Customer> listSlow();
}
