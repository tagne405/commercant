package com.ecommerce.ecommerce2.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Builder
public class Role {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "role_id")
    // private String id;

    private String name;
}
