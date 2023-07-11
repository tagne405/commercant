package com.ecommerce.ecommerce2.Security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Service.CustomerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CustomerDetails implements UserDetailsService{

     private CustomerService customerService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.findByUsername(username);
        if(customer == null) throw new UsernameNotFoundException(String.format("user %S not found", username));

        List<SimpleGrantedAuthority> authorities = customer.getRoles()
                                                           .stream()
                                                           .map(r-> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
       
        UserDetails userDetails = User
                                    .withUsername(customer.getUsername())
                                    .password(customer.getPassword())
                                    .authorities(authorities).build();

        return userDetails;
    }
    
}
