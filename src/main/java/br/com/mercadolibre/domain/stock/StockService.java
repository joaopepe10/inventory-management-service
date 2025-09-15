package br.com.mercadolibre.domain.stock;

import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.api.model.StockResponse;
import br.com.mercadolibre.application.stock.model.StockDTO;
import br.com.mercadolibre.core.exception.InsufficientStockException;
import br.com.mercadolibre.core.exception.StockNotFoundException;
import br.com.mercadolibre.domain.stock.mapper.StockMapper;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import br.com.mercadolibre.infra.sql.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static br.com.mercadolibre.infra.message.model.ChangeType.INCREASE;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public List<StockResponse> findAll() {
        var entities = stockRepository.findAll();
        return stockMapper.toResponse(entities);
    }

    public StockResponse findById(UUID id) {
        var entity = stockRepository.findById(id).orElseThrow(StockNotFoundException::new);
        return stockMapper.toResponse(entity);
    }

    @Transactional
    public StockDTO decrease(PurchaseRequest request) {
        var entity = findByProductIdAndStoreIdWithLock(request.getProductId(), request.getStoreId());

        if (entity.getQuantity() < request.getQuantity()) {
            throw new InsufficientStockException();
        }
        entity.decreaseQuantity(request.getQuantity());

        var updatedEntity =  stockRepository.save(entity);
        var quantityPurchased = request.getQuantity();
        return stockMapper.toDto(updatedEntity, quantityPurchased);
    }

    @Transactional
    public void update(UpdateInventoryMessage message) {
        try {
            var productId = UUID.fromString(message.payload().productId());
            var storeId = UUID.fromString(message.payload().storeId());
            var entity = findByProductIdAndStoreIdWithLock(productId, storeId);

            if (message.changeType() == INCREASE) {
                entity.increaseQuantity(message.payload().quantity());
            } else {
                entity.decreaseQuantity(message.payload().quantity());
            }
            stockRepository.save(entity);
            log.info("Estoque atualizado com sucesso: productId={}, storeId={}, changeType={}, quantity={}",
                    productId, storeId, message.changeType(), message.payload().quantity());
        } catch (Exception e) {
            log.error("Erro ao atualizar estoque: eventId={}, erro={}", message.eventId(), e.getMessage(), e);
        }
    }

    private StockEntity findByProductIdAndStoreIdWithLock(UUID productId, UUID storeId) {
        return stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId)
                .orElseThrow(StockNotFoundException::new);
    }
}
