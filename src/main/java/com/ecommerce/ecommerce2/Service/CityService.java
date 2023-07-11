package com.ecommerce.ecommerce2.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce2.Entity.City;
import com.ecommerce.ecommerce2.Repository.CityRepository;


@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> getAll(){
        

        return cityRepository.findAll();
    }
}
