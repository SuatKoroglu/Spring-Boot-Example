package com.example.demo.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final IProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(path= "{id}")
    public ProductDto getProduct(@PathVariable("id") Long id){
        return productService.getProduct(id);
    }
    //sadece id'si 1 olanı getir örneğin


    @PostMapping
    public ResponseEntity<ProductDto> registerNewProduct(@RequestBody ProductDto productDto){
        return  ResponseEntity.ok(productService.addNewProduct(productDto));
    }

    @DeleteMapping(path= "{id}")
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
    }

    @PutMapping(path= "{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDto productDto){
        return  ResponseEntity.ok(productService.updateProduct(id,productDto));
    }
}
