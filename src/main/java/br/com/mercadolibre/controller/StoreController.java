package br.com.mercadolibre.controller;

import java.util.List;

import br.com.mercadolibre.api.controller.StoresApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.mercadolibre.application.StoreService;
import br.com.mercadolibre.api.model.StoreResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreController implements StoresApi {
    
    private final StoreService storeService;
    
    @Override
    public ResponseEntity<List<StoreResponse>> getStores() {
        List<StoreResponse> stores = storeService.getStores();
        return ResponseEntity.ok(stores);
    }
}
