package com.ecommerce.ecommerce2.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Repository.CustomerRepository;
import com.ecommerce.ecommerce2.Service.CustomerService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    CustomerService customerService;


    @GetMapping("/admin/user")
    public String findCustomer(Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        List<Customer> customer = customerService.findAll();
        model.addAttribute("CustomerList", customer);

        return "user";
    }
    
    @GetMapping("/admin/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success","ce bon");
         customerService.deleteCustomer(id);
         return "redirect:/admin/user";
    }
}
