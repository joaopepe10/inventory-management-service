package br.com.mercadolibre.infra.sql.store.repository;

import br.com.mercadolibre.infra.sql.store.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {

}
