package com.ecommerce.ecommerce2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    
    public List<Category> findAlList(){
        return categoryRepository.findAll();
    }

    public Category save(Category category){
        try{
            Category categorySave= new Category(category.getNom());
            return categoryRepository.save(categorySave);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        
    }

    public Category findId(Long id){
        Optional<Category> category=categoryRepository.findById(id);
        // return categoryRepository.findById(id).get();
        return category.orElse(null);
    }

    public Category update(Category category){
        Category categoryUpdate= categoryRepository.findById(category.getId()).get();
        categoryUpdate.setNom(category.getNom());
        categoryUpdate.set_activated(category.is_activated());
        categoryUpdate.set_delected(category.is_delected());

        return categoryRepository.save(categoryUpdate) ;
    }

    public void deleteById(Long id){
        Category category = categoryRepository.findById(id).get();
        category.set_activated(false);
        category.set_delected(true);
        categoryRepository.save(category);
    }

    public void EnableId(Long id){
        Category category = categoryRepository.findById(id).get();
        category.set_activated(true);
        category.set_delected(false);
        categoryRepository.save(category);

    }

    public List<Category> findAllByActivated(){
        return categoryRepository.findByActivated();
    }
}
