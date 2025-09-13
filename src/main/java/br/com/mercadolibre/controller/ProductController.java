package br.com.mercadolibre.controller;

import br.com.mercadolibre.application.ProductService;
import br.com.mercadolibre.model.CreateProductRequest;
import br.com.mercadolibre.model.ProductPage;
import br.com.mercadolibre.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController implements ProductsApi {
    
    private final ProductService productService;
    
    @Override
    public ResponseEntity<ProductPage> getProducts(Integer page, Integer size) {
        ProductPage products = productService.getProducts(
            page != null ? page : 0, 
            size != null ? size : 20
        );
        return ResponseEntity.ok(products);
    }
    
    @Override
    public ResponseEntity<ProductResponse> create(CreateProductRequest createProductRequest) {
        try {
            ProductResponse product = productService.createProduct(createProductRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
