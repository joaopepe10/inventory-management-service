package br.com.mercadolibre.application.product;

import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductApplicationService {

    private final ProductService productService;

    public ProductPage getProducts(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
       return productService.findAll(pageable);
    }

}
