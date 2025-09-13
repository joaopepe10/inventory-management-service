package br.com.mercadolibre.controller;

import br.com.mercadolibre.api.controller.StocksApi;
import br.com.mercadolibre.application.StockService;
import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.PurchaseResponse;
import br.com.mercadolibre.api.model.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
