package br.com.mercadolibre.domain.stock;

import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.StockResponse;
import br.com.mercadolibre.application.stock.model.StockDTO;
import br.com.mercadolibre.domain.stock.mapper.StockMapper;
import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import br.com.mercadolibre.infra.sql.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public List<StockResponse> findAll() {
        var entities = stockRepository.findAll();
        return stockMapper.toResponse(entities);
    }

    public StockDTO update(PurchaseRequest request) {
        var entity = findByProductIdAndStoreIdWithLock(request.getProductId(), request.getStoreId());

        if (entity.getAvailableQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                    format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                            entity.getAvailableQuantity(), request.getQuantity())
            );
        }
        entity.setQuantity(request.getQuantity());

        var updatedEntity =  stockRepository.save(entity);
        return stockMapper.toDto(updatedEntity);
    }

    private StockEntity findByProductIdAndStoreIdWithLock(UUID productId, UUID storeId) {
        return stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado"));
    }
}
