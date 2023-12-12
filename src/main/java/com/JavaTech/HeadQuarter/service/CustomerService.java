package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.model.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer saveOrUpdate(Customer customer);

    Customer findByName(String name);

    Customer findByPhone(String phone);
}
