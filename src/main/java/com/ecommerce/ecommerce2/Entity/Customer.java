package com.ecommerce.ecommerce2.Entity;

import java.util.Collection;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Clients", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "photo", "phone_number"}))
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Size(min = 3, max = 15, message = "le nom est compris entre 3 et 15 caractere")
    private String firstName;
    @Size(min = 3, max = 15, message = "le prenom est compris entre 3 et 15 caractere")
    private String lastName;
    private String username;
    @Column(name = "phone_number")
    private String telephone;
    private String address;
    
    private String password;
    @Lob
    @Column(name = "photo", columnDefinition = "MEDIUMBLOB")
    private String photo;

//    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    private String city;

    @OneToOne(mappedBy = "customer" )
    private ShoppingCart shoppingcart;
    @OneToMany(mappedBy = "customer")
    private List<Order> order;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // @JoinTable(name = "customer_id",
    //            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName ="customer_id"),
    //             inverseJoinColumns = @JoinColumn(name= "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

}
