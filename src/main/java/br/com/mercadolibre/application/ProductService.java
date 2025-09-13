package br.com.mercadolibre.application;

import br.com.mercadolibre.infra.sql.product.model.ProductEntity;
import br.com.mercadolibre.infra.sql.product.repository.ProductRepository;
import br.com.mercadolibre.api.model.CreateProductRequest;
import br.com.mercadolibre.api.model.PageableResponse;
import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.api.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductPage getProducts(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        var productPage = productRepository.findAll(pageable);

        var products = productPage.getContent()
                .stream()
                .map(this::toProductResponse)
                .toList();

        var pageableResponse = PageableResponse.builder()
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements((int) productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();

        return ProductPage.builder()
                .content(products)
                .pageable(pageableResponse)
                .build();
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU já existe: " + request.getSku());
        }

        var product = ProductEntity.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        var savedProduct = productRepository.save(product);
        return toProductResponse(savedProduct);
    }

    public ProductEntity findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + productId));
    }

    public ProductEntity findBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + sku));
    }

    private ProductResponse toProductResponse(ProductEntity entity) {
        return ProductResponse.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .category("")
                .price(0.0)
                .quantity(0)
                .build();
    }
}
