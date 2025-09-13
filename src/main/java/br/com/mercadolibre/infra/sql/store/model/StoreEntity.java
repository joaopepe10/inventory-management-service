package br.com.mercadolibre.infra.sql.store.model;

import br.com.mercadolibre.infra.sql.stock.model.StockEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_stores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class StoreEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "store_code", nullable = false, unique = true)
    private String storeCode;

    @Column(name = "name", nullable = false,  length = 50)
    private String name;

    @Column(name = "address", nullable = false,  length = 50)
    private String address;

    @Column(name = "city", nullable = false,  length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private BrazilianState state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<StockEntity> stocks = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
