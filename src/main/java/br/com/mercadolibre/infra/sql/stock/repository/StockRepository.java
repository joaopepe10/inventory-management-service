package br.com.mercadolibre.infra.sql.stock.repository;

import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.LockModeType.OPTIMISTIC;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {

    @Lock(OPTIMISTIC)
    @Query("SELECT s FROM StockEntity s WHERE s.product.id = :productId AND s.store.id = :storeId")
    Optional<StockEntity> findByProductIdAndStoreIdWithLock(@Param("productId") UUID productId, @Param("storeId") UUID storeId);
}
