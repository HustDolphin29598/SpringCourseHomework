package com.onemount.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.onemount.crm.model.Customer;
import com.onemount.crm.model.CustomerPOJO;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    CustomerPOJO customerToPOJO(Customer customer);
    Customer pojoToCustomer(CustomerPOJO pojo);
}
