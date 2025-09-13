package br.com.mercadolibre.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mercadolibre.application.StockService;
import br.com.mercadolibre.model.PurchaseRequest;
import br.com.mercadolibre.model.PurchaseResponse;
import br.com.mercadolibre.model.StockResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StockController implements StocksApi {
    
    private final StockService stockService;
    
    @Override
    public ResponseEntity<List<StockResponse>> getStocks(String storeId, String productId) {
        List<StockResponse> stocks = stockService.getStocks(storeId, productId);
        return ResponseEntity.ok(stocks);
    }
    
    @Override
    public ResponseEntity<PurchaseResponse> simulatePurchase(PurchaseRequest purchaseRequest) {
        try {
            PurchaseResponse response = stockService.simulatePurchase(purchaseRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
