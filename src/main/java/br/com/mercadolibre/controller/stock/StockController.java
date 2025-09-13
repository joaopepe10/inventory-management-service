package br.com.mercadolibre.controller.stock;

import br.com.mercadolibre.api.controller.StocksApi;
import br.com.mercadolibre.application.stock.StockApplicationService;
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
    
    private final StockApplicationService stockService;
    
    @Override
    public ResponseEntity<List<StockResponse>> getStocks() {
        List<StockResponse> stocks = stockService.getStocks();
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
