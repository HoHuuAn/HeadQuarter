package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.model.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> listAll();
    Brand addOrSave(Brand brand);
    Brand findByName(String name);
    void deleteById(Long id);
    Brand findById(Long id);
}
