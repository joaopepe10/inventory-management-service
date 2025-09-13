package br.com.mercadolibre.controller;

import br.com.mercadolibre.application.StoreService;
import br.com.mercadolibre.model.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreController implements StoresApi {
    
    private final StoreService storeService;
    
    @Override
    public ResponseEntity<List<StoreResponse>> getStores() {
        List<StoreResponse> stores = storeService.getStores();
        return ResponseEntity.ok(stores);
    }
}
