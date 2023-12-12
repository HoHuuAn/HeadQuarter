package com.JavaTech.HeadQuarter.repository;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findProductByBarCode(String barcode);
}
