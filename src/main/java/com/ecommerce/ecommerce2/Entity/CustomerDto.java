package com.ecommerce.ecommerce2.Entity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    
    @Size(min = 3, max = 15, message = "le nom est compris entre 3 et 15 caractere")
    private String firstName;
    @Size(min = 3, max = 15, message = "le prenom est compris entre 3 et 15 caractere")
    private String lastName;

    private String username;

    @Size(min = 5, max = 20, message = "password compris entre 5-20 caractere")
    private String password;

    @Size(min = 5, max = 20, message = "password compris entre 5-20 caractere")
    private String repeatPassword;

}
