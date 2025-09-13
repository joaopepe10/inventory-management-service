package br.com.mercadolibre.application;

import br.com.mercadolibre.infra.sql.product.model.ProductEntity;
import br.com.mercadolibre.infra.sql.product.repository.ProductRepository;
import br.com.mercadolibre.model.CreateProductRequest;
import br.com.mercadolibre.model.ProductPage;
import br.com.mercadolibre.model.ProductPagePageable;
import br.com.mercadolibre.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductPage getProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        
        List<ProductResponse> products = productPage.getContent()
                .stream()
                .map(this::toProductResponse)
                .toList();
        
        ProductPagePageable pageableResponse = new ProductPagePageable()
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements((int) productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast());
        
        return new ProductPage()
                .content(products)
                .pageable(pageableResponse);
    }
    
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU já existe: " + request.getSku());
        }
        
        ProductEntity product = ProductEntity.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        
        ProductEntity savedProduct = productRepository.save(product);
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
        return new ProductResponse()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .category("")
                .price(0.0)
                .quantity(0);
    }
}
