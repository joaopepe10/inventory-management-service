

-- Inserir lojas exemplo (apenas se não existirem)
INSERT INTO tbl_stores (id, store_code, name, address, city, state, zip_code, phone, email, active, created_at, updated_at)
SELECT * FROM (VALUES 
    (CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 'STORE001', 'Loja Centro SP', 'Rua Augusta, 123', 'São Paulo', 'SP', '01305-000', '(11) 1234-5678', 'centro.sp@mercadolibre.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 'STORE002', 'Loja Zona Sul RJ', 'Av. Copacabana, 456', 'Rio de Janeiro', 'RJ', '22070-011', '(21) 9876-5432', 'zonasul.rj@mercadolibre.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 'STORE003', 'Loja Savassi BH', 'Rua Pernambuco, 789', 'Belo Horizonte', 'MG', '30112-000', '(31) 5555-1234', 'savassi.bh@mercadolibre.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
) AS new_stores(id, store_code, name, address, city, state, zip_code, phone, email, active, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM tbl_stores WHERE tbl_stores.store_code = new_stores.store_code);

-- Inserir produtos exemplo (apenas se não existirem)
INSERT INTO tbl_products (id, sku, name, description, price, created_at, updated_at)
SELECT * FROM (VALUES 
    (CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), 'SMARTPHONE001', 'iPhone 15 Pro', 'Apple iPhone 15 Pro 256GB', 5000 ,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), 'NOTEBOOK001', 'MacBook Air M2', 'Apple MacBook Air 13" M2 8GB 256GB', 6000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), 'HEADPHONE001', 'AirPods Pro', 'Apple AirPods Pro 2ª Geração', 1500, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), 'WATCH001', 'Apple Watch Series 9', 'Apple Watch Series 9 45mm GPS', 2000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
) AS new_products(id, sku, name, description, price, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM tbl_products WHERE tbl_products.sku = new_products.sku);

-- Inserir estoque exemplo (apenas se não existir a combinação produto+loja)
INSERT INTO tbl_stock (id, product_id, store_id, quantity, reserved_quantity, available_quantity, created_at, updated_at, version)
SELECT * FROM (VALUES 
    -- iPhone 15 Pro
    (CAST('770e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 50, 5, 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 30, 2, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 25, 0, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    -- MacBook Air M2
    (CAST('770e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 20, 1, 19,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-446655440005' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 15, 0, 15,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-446655440006' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 12, 2, 10,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    -- AirPods Pro
    (CAST('770e8400-e29b-41d4-a716-446655440007' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 100, 10, 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-446655440008' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 80, 5, 75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-446655440009' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 60, 0, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    -- Apple Watch Series 9
    (CAST('770e8400-e29b-41d4-a716-44665544000a' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 35, 3, 32,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-44665544000b' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 25, 1, 24,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (CAST('770e8400-e29b-41d4-a716-44665544000c' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 18, 0, 18,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
) AS new_stock(id, product_id, store_id, quantity, reserved_quantity, available_quantity, created_at, updated_at, version)
WHERE NOT EXISTS (
    SELECT 1 FROM tbl_stock 
    WHERE tbl_stock.product_id = new_stock.product_id 
    AND tbl_stock.store_id = new_stock.store_id
);

-- Inserir algumas movimentações de estoque exemplo (apenas se não existirem)
INSERT INTO tbl_stock_movements (id, product_id, store_id, movement_type, quantity, previous_quantity, new_quantity, reason, reference_id, created_at)
SELECT * FROM (VALUES 
    (CAST('880e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 'INBOUND', 50, 0, 50, 'Estoque inicial', 'INITIAL_STOCK_001', CURRENT_TIMESTAMP),
    (CAST('880e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 'INBOUND', 30, 0, 30, 'Estoque inicial', 'INITIAL_STOCK_002', CURRENT_TIMESTAMP),
    (CAST('880e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 'INBOUND', 20, 0, 20, 'Estoque inicial', 'INITIAL_STOCK_003', CURRENT_TIMESTAMP),
    (CAST('880e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 'INBOUND', 100, 0, 100, 'Estoque inicial', 'INITIAL_STOCK_004', CURRENT_TIMESTAMP)
) AS new_movements(id, product_id, store_id, movement_type, quantity, previous_quantity, new_quantity, reason, reference_id, created_at)
WHERE NOT EXISTS (SELECT 1 FROM tbl_stock_movements WHERE tbl_stock_movements.reference_id = new_movements.reference_id);