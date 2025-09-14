package br.com.mercadolibre.infra.sql.product.repository;

import br.com.mercadolibre.infra.sql.product.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query(
           value = "SELECT p FROM ProductEntity p WHERE (SELECT SUM(s.quantity) FROM StockEntity s WHERE s.product = p) > 0",
            countQuery = "SELECT COUNT(p) FROM ProductEntity p WHERE (SELECT SUM(s.quantity) FROM StockEntity s WHERE s.product = p) > 0"
    )
    Page<ProductEntity> findAllAvailability(Pageable pageable);

    @Query(
            value = "SELECT p FROM ProductEntity p WHERE p.category = :category AND (SELECT SUM(s.quantity) FROM StockEntity s WHERE s.product = p) > 0",
            countQuery = "SELECT COUNT(p) FROM ProductEntity p WHERE p.category = :category AND (SELECT SUM(s.quantity) FROM StockEntity s WHERE s.product = p) > 0")
    Page<ProductEntity> findAllByCategory(String category, Pageable pageable);
}
