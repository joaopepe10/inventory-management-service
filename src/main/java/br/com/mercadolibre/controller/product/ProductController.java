package br.com.mercadolibre.controller.product;

import br.com.mercadolibre.api.controller.ProductsApi;
import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.application.product.ProductApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {
    
    private final ProductApplicationService productService;
    
    @Override
    public ResponseEntity<ProductPage> getProducts(Integer page, Integer size) {
        ProductPage products = productService.getProducts(page, size);
        return ResponseEntity.ok(products);
    }
    
}
