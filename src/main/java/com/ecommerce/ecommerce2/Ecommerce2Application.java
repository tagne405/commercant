package com.ecommerce.ecommerce2;

import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ecommerce.ecommerce2.Service.CustomerService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.management.ServiceNotFoundException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.random.RandomGenerator;

@SpringBootApplication
public class Ecommerce2Application {

	public static void main(String[] args) throws IOException {


		SpringApplication.run(Ecommerce2Application.class, args);


	}


	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

	// @Bean
	// CommandLineRunner createAdmin(CustomerService customerService){
	// 	return args->{
	// 		customerService.addRole("ADMIN");

	// 		customerService.addNewUser("admin", "admin");

	// 		customerService.addRoleToUser("admin","ADMIN");
	// 		customerService.addRoleToUser("admin","USER");
	// 	};
	// }
}
	