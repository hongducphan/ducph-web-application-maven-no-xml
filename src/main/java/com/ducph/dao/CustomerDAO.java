package com.ducph.dao;

import com.ducph.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> getCustomers();
    boolean delete(int id);
}
