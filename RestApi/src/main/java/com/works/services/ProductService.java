package com.works.services;

import com.works.entities.Product;
import com.works.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    final ProductRepository repository;
    final CacheManager cacheManager;
    final RestTemplate restTemplate;

    public Product save( Product product ) {
        repository.save(product);
        cacheManager.getCache("product").clear();
        String url = "http://localhost:8090/product/list";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic emVocmFAbWFpbC5jb206MTIzNDU=");
        HttpEntity httpEntity = new HttpEntity("", headers);
        restTemplate.postForEntity(url, httpEntity, String.class);
        return product;
    }

    @Cacheable("product")
    public List<Product> list() {
        return repository.findAll();
    }

}
