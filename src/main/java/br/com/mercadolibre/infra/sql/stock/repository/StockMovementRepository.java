package br.com.mercadolibre.infra.sql.stock.repository;

import br.com.mercadolibre.infra.sql.stock.model.StockMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovementEntity, UUID> {

}
