# Sistema de Gestão de Estoque - Inventory Management Service

## 📋 Descrição

Sistema básico de gestão de estoque distribuído desenvolvido em Java com Spring Boot, projetado para gerenciar inventário de uma rede de lojas de varejo.

## 🏗️ Arquitetura

### Entidades Principais

- **ProductEntity**: Representa os produtos do catálogo
- **StoreEntity**: Representa as lojas da rede
- **StockEntity**: Controla o estoque de cada produto em cada loja
- **StockMovementEntity**: Registra todas as movimentações de estoque

### Stack Tecnológica

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **H2 Database** (desenvolvimento)
- **Lombok** (redução de boilerplate)
- **MapStruct** (mapeamento de objetos)
- **OpenApiSwagger** (documentação e geração de end-points))
- **Maven** (gerenciamento de dependências)

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven 3.6+

### Executando a aplicação

```bash
# Execute a aplicação
mvn spring-boot:run
```

### Acessando a aplicação

- **Aplicação**: http://localhost:8080
- **Console H2**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testDb`
  - Username: `sa`
  - Password: (deixe em branco)

## 📊 Modelo de Dados

### Relacionamentos

```
Store (1) -----> (N) Stock (N) <----- (1) Product
                     |
                     v
               StockMovement
```

### Campos Principais

#### Product
- `id` (UUID)
- `sku` (String, único)
- `name` (String)
- `description` (String)

#### Store
- `id` (UUID)
- `storeCode` (String, único)
- `name` (String)
- `address`, `city`, `state`, `zipCode`
- `active` (Boolean)

#### Stock
- `id` (UUID)
- `productId` (UUID)
- `storeId` (UUID)
- `quantity` (Integer)
- `reservedQuantity` (Integer)
- `availableQuantity` (Integer)
- `minimumStock` (Integer)
- `version` (Long) - para controle de concorrência

#### StockMovement
- `id` (UUID)
- `productId`, `storeId` (UUID)
- `movementType` (Enum): INBOUND, OUTBOUND, TRANSFER_IN, TRANSFER_OUT, ADJUSTMENT, RESERVATION, RELEASE
- `quantity`, `previousQuantity`, `newQuantity` (Integer)
- `reason`, `referenceId` (String)

## 🔧 Funcionalidades Implementadas

### Controle de Estoque
- ✅ Gestão de produtos, lojas e estoque
- ✅ Histórico de movimentações
- ✅ Controle de estoque disponível vs reservado
- ✅ Validação de estoque mínimo
- ✅ Controle de concorrência com versionamento otimista

### Operações de Domínio
- ✅ Reserva e liberação de estoque
- ✅ Entrada e saída de mercadorias
- ✅ Validações de negócio

## 📁 Estrutura do Projeto

```
src/main/java/br/com/mercadolibre/
├── domain/                 # Modelos de domínio
│   ├── Product.java
│   ├── Stock.java
│   ├── Store.java
│   └── StockMovement.java
├── infra/sql/             # Camada de persistência
│   ├── product/
│   │   ├── ProductEntity.java
│   │   └── ProductRepository.java
│   ├── stock/
│   │   ├── StockEntity.java
│   │   ├── StockRepository.java
│   │   ├── java
│   │   └── StockMovementRepository.java
│   ├── store/
│   │   ├── StoreEntity.java
│   │   └── StoreRepository.java
│   └── mapper/            # Mappers entre domain e entity
└── App.java               # Classe principal
```

## 🎯 Próximos Passos

### Para completar o sistema básico:
1. **Services** - Implementar a camada de serviços
2. **Controllers** - Criar endpoints REST
3. **DTOs** - Request/Response objects
4. **Exception Handling** - Tratamento de erros
5. **Validation** - Validações de entrada
6. **Tests** - Testes unitários e de integração

### Para otimização distribuída:
1. **Caching** - Redis para cache distribuído
2. **Message Queue** - Para sincronização assíncrona
3. **Event Sourcing** - Para auditoria completa
4. **CQRS** - Separação de leitura e escrita
5. **Circuit Breaker** - Tolerância a falhas
6. **Distributed Locks** - Para operações críticas

## 📈 Observabilidade

- Logs estruturados (configurados)
- Health checks via Spring Actuator
- Métricas básicas disponíveis

## 🔒 Considerações de Segurança

- Controle de concorrência implementado
- Validações de negócio nas entidades de domínio
- Auditoria de movimentações
