package com.ecommerce.ecommerce2.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.ecommerce2.Entity.City;
import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Service.CityService;
import com.ecommerce.ecommerce2.Service.CustomerService;

@Controller
public class AccountController {
//    injecte par le contructeur et non par les annotations
    CustomerService customerService;

    public AccountController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/account")
    public String accountHome(Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        model.addAttribute("customer", customer);

        return "monCompte";
    }

    @RequestMapping(value = "/update-infor", method = {RequestMethod.POST, RequestMethod.GET})
    public String updateInfo(@ModelAttribute("customer")Customer customer,
                            Model model, 
                            RedirectAttributes redirectAttributes,
                            Principal principal){

        if(principal == null){
            return "redirect:/login";
        }
        Customer customerSave = customerService.saveInfo(customer);

        redirectAttributes.addFlashAttribute("customerSave", customerSave);
        //model.addAttribute("sucess",customerSave);
        return "redirect:/monCompte";
    }
}
