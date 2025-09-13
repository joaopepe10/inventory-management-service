package br.com.mercadolibre.application.store;

import br.com.mercadolibre.api.model.StoreResponse;
import br.com.mercadolibre.infra.sql.store.model.StoreEntity;
import br.com.mercadolibre.infra.sql.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreApplicationService {
    
    private final StoreRepository storeRepository;
    
    public List<StoreResponse> getStores() {
        return storeRepository.findAll()
                .stream()
                .map(this::toStoreResponse)
                .toList();
    }
    
    public StoreEntity findById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada: " + storeId));
    }

    private StoreResponse toStoreResponse(StoreEntity entity) {
        return StoreResponse.builder()
                .id(entity.getId().toString())
                .storeCode(entity.getStoreCode())
                .name(entity.getName())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .build();
    }
}
