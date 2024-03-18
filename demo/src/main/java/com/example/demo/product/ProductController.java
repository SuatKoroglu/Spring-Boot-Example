package com.example.demo.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    //sadece id'si 1 olanı getir örneğin


    @PostMapping
    public void registerNewProduct(@RequestBody Product product){
        productService.addNewProduct(product);
    }

    @DeleteMapping(path= "{id}")
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);

    }

    @PutMapping(path= "{id}")
    public void updateProduct(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double price){
        productService.updateProduct(id, name, brand, price);
    }
}
