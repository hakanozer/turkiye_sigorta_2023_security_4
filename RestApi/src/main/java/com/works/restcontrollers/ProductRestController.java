package com.works.restcontrollers;

import com.works.entities.Product;
import com.works.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductRestController {

    final ProductService service;

    @PostMapping("/save")
    public Product save( @Valid @RequestBody Product product ) {
        return service.save(product);
    }

    @GetMapping("/list")
    public List<Product> list() {
        return service.list();
    }


}
