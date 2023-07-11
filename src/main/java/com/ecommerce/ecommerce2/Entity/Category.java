package com.ecommerce.ecommerce2.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor

@Table( name="categories",uniqueConstraints=@UniqueConstraint(columnNames = "nom"))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "category_id")

    private Long id;
    private String nom;
    private boolean is_delected;
    private boolean is_activated;

    public Category(String nom){
        this.nom=nom;
        this.is_activated=true;
        this.is_delected=false;
    }
}
