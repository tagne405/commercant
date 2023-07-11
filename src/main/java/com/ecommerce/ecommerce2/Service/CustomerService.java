package com.ecommerce.ecommerce2.Service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.CustomerDto;
import com.ecommerce.ecommerce2.Entity.Role;
import com.ecommerce.ecommerce2.Repository.CustomerRepository;
import com.ecommerce.ecommerce2.Repository.RoleRepository;

@Service
public class CustomerService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    

    public CustomerDto save(CustomerDto customerDto){

        Customer customer= new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(roleRepository.findByName("USER")));
        

        Customer customerSave= customerRepository.save(customer);

        return mapperDTO(customerSave);
    }


    public Role addRole(String role){
        
        Role role2 = roleRepository.findById(role).orElse(null);
        role2 = Role.builder().name(role).build();

        return roleRepository.save(role2);
    }
    

    public Customer findByUsername(String username){
        return customerRepository.findByUsername(username);
    }

    private CustomerDto mapperDTO (Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setUsername(customer.getUsername());
        
        return customerDto;
    }

    public Customer addNewUser(String username, String password) {
        Customer customer= customerRepository.findByUsername(username);
        if(customer != null)
        throw new RuntimeException("Cet utilisateur exist Deja");
        
        customer = Customer.builder()
            // .userId(UUID.randomUUID().toString())
            .username(username)
            // .email(email)
            .password(passwordEncoder.encode(password))
            .build();

        Customer savedAppUser= customerRepository.save(customer);
        return savedAppUser;
    }

    public void addRoleToUser(String username, String role) {
        Customer customer=customerRepository.findByUsername(username);
        Role appRole=roleRepository.findById(role).get();

        customer.getRoles().add(appRole);
        customerRepository.save(customer);
    }

    public Customer saveInfo(Customer customer){

        Customer customer1 = customerRepository.findByUsername(customer.getUsername());
        customer1.setAddress(customer.getAddress());
        customer1.setCity(customer.getCity());
        customer1.setTelephone(customer.getTelephone());

        return customerRepository.save(customer1);
    }

    //lister les utilisateur
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    //supprimer les utilisateurs
    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }

    //Count Customer
    public Long countUser(){

        return customerRepository.count();
    }
}
