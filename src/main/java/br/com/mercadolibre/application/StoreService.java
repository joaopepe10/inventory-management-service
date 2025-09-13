package br.com.mercadolibre.application;

import br.com.mercadolibre.infra.sql.store.model.StoreEntity;
import br.com.mercadolibre.infra.sql.store.repository.StoreRepository;
import br.com.mercadolibre.model.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    
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
        return new StoreResponse()
                .id(entity.getId().toString())
                .storeCode(entity.getStoreCode())
                .name(entity.getName())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode());
    }
}
