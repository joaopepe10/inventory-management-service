package br.com.mercadolibre.application.product;

import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static br.com.mercadolibre.core.constants.CacheNameConstant.PRODUCTS_BY_CATEGORY;

@Service
@RequiredArgsConstructor
public class ProductApplicationService {

    private final ProductService productService;

    public ProductPage getProducts(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
       return productService.findAll(pageable);
    }

    @Cacheable(value = PRODUCTS_BY_CATEGORY, key = "#category")
    public ProductPage getProductsByCategory(String category, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        return productService.findAllByCategory(category, pageable);
    }
}
