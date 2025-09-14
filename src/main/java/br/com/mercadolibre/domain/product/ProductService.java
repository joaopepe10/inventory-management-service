package br.com.mercadolibre.domain.product;

import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.api.model.ProductResponse;
import br.com.mercadolibre.domain.product.mapper.ProductMapper;
import br.com.mercadolibre.infra.sql.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductPage findAll(PageRequest pageRequest) {
        var entities = productRepository.findAllAvailability(pageRequest);
        return productMapper.toProductPage(entities);
    }

    public ProductPage findAllByCategory(String category, PageRequest pageable) {
        var entities = productRepository.findAllByCategory(category, pageable);
        return productMapper.toProductPage(entities);
    }

    public ProductResponse findById(UUID id) {
        var entity = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponse(entity);
    }
}
