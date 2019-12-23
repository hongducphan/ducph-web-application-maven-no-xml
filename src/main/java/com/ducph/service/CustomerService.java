package com.ducph.service;

import com.ducph.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomers();
    boolean delete(int id);
}
