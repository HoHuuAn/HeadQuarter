package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.dto.ProductDTO;
import com.JavaTech.HeadQuarter.model.Product;

import java.util.List;

public interface ProductService {

    Product saveOrUpdate(Product product);

    List<Product> listAll();

    void deleteById(String id);

    void deleteByProduct(Product product);

    Product findById(String id);

    Product findProductByBarCode(String Barcode);

    List<ProductDTO> getTopThreeProductsByTotalSales();

    List<ProductDTO> listDTO( List<Product> list);
}
