package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.dto.ProductDTO;
import com.JavaTech.HeadQuarter.model.Product;
import com.JavaTech.HeadQuarter.model.QuantityProduct;
import com.JavaTech.HeadQuarter.model.User;
import com.JavaTech.HeadQuarter.repository.ProductRepository;
import com.JavaTech.HeadQuarter.service.ProductService;
import com.JavaTech.HeadQuarter.service.QuantityProductService;
import com.JavaTech.HeadQuarter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private QuantityProductService quantityProductService;

    @Override
    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }
    @Override
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteByProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public Product findProductByBarCode(String barcode) {
        Optional<Product> product = productRepository.findProductByBarCode(barcode);
        return product.orElse(null);
    }

    @Override
    public List<ProductDTO> getTopThreeProductsByTotalSales() {
        // Retrieve all products from the repository
        List<Product> products = productRepository.findAll();

        // Sort the products by their total sales in descending order
        List<Product> list = products.stream()
                .sorted(Comparator.comparingInt(Product::getTotalSales).reversed())
                .limit(3)
                .collect(Collectors.toList());

        return listDTO(list);
    }

    @Override
    public List<ProductDTO> listDTO( List<Product> list) {
        return list.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    private int sumQuantity(Product product){
        return quantityProductService.findAllByProduct(product)
                .stream()
                .mapToInt(QuantityProduct::getQuantity)
                .sum();
    }

}
