package br.com.mercadolibre.domain.product.mapper;

import br.com.mercadolibre.api.model.ProductPage;
import br.com.mercadolibre.api.model.ProductResponse;
import br.com.mercadolibre.api.model.StoreResponse;
import br.com.mercadolibre.infra.sql.product.model.ProductEntity;
import br.com.mercadolibre.infra.sql.store.model.StoreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "totalQuantity", source = "productEntity.totalQuantity")
    ProductResponse toResponse(ProductEntity productEntity);

    @Mapping(target = "state", source = "state.fullName")
    StoreResponse toStoreResponse(StoreEntity storeEntity);

    ProductPage toProductPage(Page<ProductEntity> page);

}
