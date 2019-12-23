package com.ducph.controller;

import com.ducph.entity.Customer;
import com.ducph.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/list")
    public String getCustomers(Model model) {
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("customers", customers);
        return "home";
    }
    
    @GetMapping("/delete")
    public String delete(@RequestParam(name = "customerId") int id, Model model) {
        boolean result = customerService.delete(id);
        if (!result) {
            model.addAttribute("error", "Update fail!");
        }
        return "redirect:/customer/list";
    }
}
