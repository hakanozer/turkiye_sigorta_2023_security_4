package com.works.services;

import com.works.entities.Product;
import com.works.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    final ProductRepository repository;

    public Product save( Product product ) {
        repository.save(product);
        return product;
    }

    public List<Product> list() {
        return repository.findAll();
    }

}
