package br.com.mercadolibre.domain.stock;

import br.com.mercadolibre.api.model.PurchaseRequest;
import br.com.mercadolibre.application.stock.model.StockDTO;
import br.com.mercadolibre.core.exception.InsufficientStockException;
import br.com.mercadolibre.core.exception.StockNotFoundException;
import br.com.mercadolibre.domain.stock.mapper.StockMapper;
import br.com.mercadolibre.infra.message.model.ChangeType;
import br.com.mercadolibre.infra.message.model.EventType;
import br.com.mercadolibre.infra.message.model.Payload;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import br.com.mercadolibre.infra.sql.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockMapper stockMapper;
    @InjectMocks
    private StockService stockService;

    private UUID productId;
    private UUID storeId;
    private StockEntity stockEntity;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        stockEntity = new StockEntity();
        stockEntity.setQuantity(10);
    }

    @Test
    @DisplayName("Deve diminuir o estoque com sucesso quando há quantidade suficiente")
    void decrease_decreaseStock_success() {
        var request = PurchaseRequest.builder()
                .productId(productId)
                .storeId(storeId)
                .quantity(5)
                .build();

        var expectedDto = StockDTO.builder()
                .quantity(5)
                .build();

        when(stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId))
                .thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(any(StockEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(stockMapper.toDto(any(StockEntity.class)))
                .thenReturn(expectedDto);

        
        StockDTO result = stockService.decrease(request);

        assertThat(result).isNotNull();
        assertThat(stockEntity.getQuantity()).isEqualTo(5);
        verify(stockRepository).save(stockEntity);
        verify(stockMapper).toDto(stockEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar diminuir estoque insuficiente")
    void decrease_decreaseStock_insufficientStock() {
        var request = PurchaseRequest.builder()
                .productId(productId)
                .storeId(storeId)
                .quantity(15)
                .build();

        when(stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId))
                .thenReturn(Optional.of(stockEntity));

        assertThatThrownBy(() -> stockService.decrease(request))
                .isInstanceOf(InsufficientStockException.class);

        assertThat(stockEntity.getQuantity()).isEqualTo(10);
        verify(stockRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar estoque de produto inexistente")
    void decrease_decreaseStock_stockNotFound() {
        var request = PurchaseRequest.builder()
                .productId(productId)
                .storeId(storeId)
                .quantity(1)
                .build();

        when(stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockService.decrease(request))
                .isInstanceOf(StockNotFoundException.class);
    }

    @Test
    @DisplayName("Deve aumentar o estoque via mensagem")
    void increase_updateStock_viaMessage() {
        var payload = Payload.builder()
                .productId(productId.toString())
                .storeId(storeId.toString())
                .quantity(7)
                .build();

        var message = UpdateInventoryMessage.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EventType.UPDATED)
                .changeType(ChangeType.INCREASE)
                .aggregateId(UUID.randomUUID().toString())
                .source("test")
                .createdAt(LocalDateTime.now())
                .payload(payload)
                .build();

        when(stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId))
                .thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(any(StockEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        
        stockService.update(message);

        assertThat(stockEntity.getQuantity()).isEqualTo(17);
        verify(stockRepository).save(stockEntity);
    }

    @Test
    @DisplayName("Deve diminuir o estoque via mensagem")
    void update_decreaseStock_viaMessage() {
        var payload = Payload.builder()
                .productId(productId.toString())
                .storeId(storeId.toString())
                .quantity(3)
                .build();

        var message = UpdateInventoryMessage.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EventType.UPDATED)
                .changeType(ChangeType.DECREASE)
                .aggregateId(UUID.randomUUID().toString())
                .source("test")
                .createdAt(LocalDateTime.now())
                .payload(payload)
                .build();

        when(stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId))
                .thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(any(StockEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        stockService.update(message);

        assertThat(stockEntity.getQuantity()).isEqualTo(7);
        verify(stockRepository).save(stockEntity);
    }

    @Test
    @DisplayName("Não deve salvar estoque se produto não encontrado via mensagem")
    void update_viaMessage_stockNotFound() {
        var payload = Payload.builder()
                .productId(productId.toString())
                .storeId(storeId.toString())
                .quantity(1)
                .build();

        var message = UpdateInventoryMessage.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EventType.UPDATED)
                .changeType(ChangeType.INCREASE)
                .aggregateId(UUID.randomUUID().toString())
                .source("test")
                .createdAt(LocalDateTime.now())
                .payload(payload)
                .build();

        when(stockRepository.findByProductIdAndStoreIdWithLock(productId, storeId))
                .thenReturn(Optional.empty());

        stockService.update(message);

        verify(stockRepository, never()).save(any());
    }
}
