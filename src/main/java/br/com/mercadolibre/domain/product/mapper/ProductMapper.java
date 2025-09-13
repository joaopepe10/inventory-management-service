package br.com.mercadolibre.domain.product.mapper;

import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.api.model.ProductResponse;
import br.com.mercadolibre.infra.sql.product.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "totalQuantity", source = "productEntity.totalQuantity")
    ProductResponse toResponse(ProductEntity productEntity);

    ProductPage toProductPage(Page<ProductEntity> page);

}
