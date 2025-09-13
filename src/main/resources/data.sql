-- Inserir lojas exemplo (apenas se não existirem)
INSERT INTO tbl_stores (id, store_code, name, address, city, state, zip_code, phone, email, active, created_at, updated_at)
SELECT * FROM (VALUES
                   (CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 'STORE001', 'Loja Centro SP', 'Rua Augusta, 123', 'São Paulo', 'SP', '01305-000', '(11) 1234-5678', 'centro.sp@mercadolibre.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 'STORE002', 'Loja Zona Sul RJ', 'Av. Copacabana, 456', 'Rio de Janeiro', 'RJ', '22070-011', '(21) 9876-5432', 'zonasul.rj@mercadolibre.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 'STORE003', 'Loja Savassi BH', 'Rua Pernambuco, 789', 'Belo Horizonte', 'MG', '30112-000', '(31) 5555-1234', 'savassi.bh@mercadolibre.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                  ) AS new_stores(id, store_code, name, address, city, state, zip_code, phone, email, active, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM tbl_stores WHERE tbl_stores.store_code = new_stores.store_code);

-- Inserir produtos exemplo (apenas se não existirem)
INSERT INTO tbl_products (id, sku, name, description, category, price, created_at, updated_at)
SELECT * FROM (VALUES
                   -- VEÍCULOS
                   (CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID), 'VEHICLE001', 'Honda Civic 2024', 'Honda Civic Sedan LX 2.0 Flex 4P Automático', 'Veículos', 125000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), 'VEHICLE002', 'Yamaha MT-07', 'Moto Yamaha MT-07 689cc 2024', 'Veículos', 45000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- SUPERMERCADO
                   (CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), 'SUPER001', 'Arroz Tio João', 'Arroz Branco Tipo 1 Tio João 5kg', 'Supermercado', 22.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), 'SUPER002', 'Óleo de Soja Soya', 'Óleo de Soja Soya 900ml', 'Supermercado', 8.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440005' AS UUID), 'SUPER003', 'Café Pilão', 'Café Torrado e Moído Pilão 500g', 'Supermercado', 14.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- TECNOLOGIA
                   (CAST('660e8400-e29b-41d4-a716-446655440006' AS UUID), 'TECH001', 'iPhone 15 Pro', 'Apple iPhone 15 Pro 256GB Azul Titânio', 'Tecnologia', 8499.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440007' AS UUID), 'TECH002', 'Samsung Galaxy S24', 'Samsung Galaxy S24 Ultra 512GB com S Pen', 'Tecnologia', 7299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440008' AS UUID), 'TECH003', 'MacBook Air M2', 'Apple MacBook Air 13"" M2 8GB 256GB SSD', 'Tecnologia', 9999.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440009' AS UUID), 'TECH004', 'PlayStation 5', 'Console PlayStation 5 825GB SSD', 'Tecnologia', 4299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544000a' AS UUID), 'TECH005', 'Smart TV Samsung 55"', 'Smart TV Samsung 55"" 4K UHD Crystal', 'Tecnologia', 2799.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- CASA E MÓVEIS
                   (CAST('660e8400-e29b-41d4-a716-44665544000b' AS UUID), 'HOME001', 'Sofá Retrátil 3 Lugares', 'Sofá Retrátil e Reclinável 3 Lugares Cinza', 'Casa e Móveis', 1899.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544000c' AS UUID), 'HOME002', 'Mesa de Jantar 6 Lugares', 'Mesa de Jantar Retangular 6 Lugares Madeira', 'Casa e Móveis', 1299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544000d' AS UUID), 'HOME003', 'Guarda-Roupa 6 Portas', 'Guarda-Roupa Casal 6 Portas com Espelho', 'Casa e Móveis', 2199.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- ELETRODOMÉSTICOS
                   (CAST('660e8400-e29b-41d4-a716-44665544000e' AS UUID), 'ELETRO001', 'Geladeira Brastemp', 'Geladeira Brastemp Frost Free Duplex 375L', 'Eletrodomésticos', 2899.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544000f' AS UUID), 'ELETRO002', 'Micro-ondas Panasonic', 'Micro-ondas Panasonic 32L Branco', 'Eletrodomésticos', 599.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440010' AS UUID), 'ELETRO003', 'Air Fryer Mondial', 'Fritadeira Elétrica Air Fryer 5.5L Digital', 'Eletrodomésticos', 449.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- ESPORTES E FITNESS
                   (CAST('660e8400-e29b-41d4-a716-446655440011' AS UUID), 'SPORT001', 'Bicicleta Caloi Elite', 'Bicicleta Caloi Elite Carbon Sport 21V', 'Esportes e Fitness', 2299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440012' AS UUID), 'SPORT002', 'Esteira Elétrica', 'Esteira Elétrica Movement LX 160S 1.6HP', 'Esportes e Fitness', 1899.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440013' AS UUID), 'SPORT003', 'Halteres Ajustáveis', 'Kit Halteres Ajustáveis 2kg até 24kg', 'Esportes e Fitness', 899.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- FERRAMENTAS
                   (CAST('660e8400-e29b-41d4-a716-446655440014' AS UUID), 'TOOL001', 'Furadeira Bosch', 'Furadeira de Impacto Bosch GSB 550 RE', 'Ferramentas', 299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440015' AS UUID), 'TOOL002', 'Kit Chaves Phillips', 'Kit Chaves de Fenda e Phillips 6 Peças', 'Ferramentas', 45.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- CONSTRUÇÃO
                   (CAST('660e8400-e29b-41d4-a716-446655440016' AS UUID), 'CONST001', 'Cimento CP2', 'Cimento Portland CP2 50kg Votoran', 'Construção', 32.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440017' AS UUID), 'CONST002', 'Cerâmica Portinari', 'Cerâmica Portinari 60x60 Polido Marmorizado', 'Construção', 89.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- INDÚSTRIA E COMÉRCIO
                   (CAST('660e8400-e29b-41d4-a716-446655440018' AS UUID), 'IND001', 'Impressora Térmica', 'Impressora Térmica Não Fiscal 80mm USB', 'Indústria e Comércio', 299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- PARA SEU NEGÓCIO
                   (CAST('660e8400-e29b-41d4-a716-446655440019' AS UUID), 'BIZ001', 'Balança Digital', 'Balança Digital Comercial 40kg Toledo', 'Para seu Negócio', 899.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- PET SHOP
                   (CAST('660e8400-e29b-41d4-a716-44665544001a' AS UUID), 'PET001', 'Ração Golden Cães', 'Ração Golden Fórmula Cães Adultos 15kg', 'Pet Shop', 179.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544001b' AS UUID), 'PET002', 'Casinha para Cães', 'Casinha de Madeira para Cães Médio Porte', 'Pet Shop', 299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- SAÚDE
                   (CAST('660e8400-e29b-41d4-a716-44665544001c' AS UUID), 'HEALTH001', 'Termômetro Digital', 'Termômetro Digital G-Tech Testa e Ouvido', 'Saúde', 89.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544001d' AS UUID), 'HEALTH002', 'Suplemento Whey Protein', 'Whey Protein Concentrado 900g Chocolate', 'Saúde', 149.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- ACESSÓRIOS PARA VEÍCULOS
                   (CAST('660e8400-e29b-41d4-a716-44665544001e' AS UUID), 'AUTO001', 'Pneu Michelin 185/65R15', 'Pneu Michelin Energy XM2 185/65R15 88H', 'Acessórios para Veículos', 389.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544001f' AS UUID), 'AUTO002', 'Som Automotivo Pioneer', 'Som Automotivo Pioneer MP3 USB Bluetooth', 'Acessórios para Veículos', 299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- BELEZA E CUIDADO PESSOAL
                   (CAST('660e8400-e29b-41d4-a716-446655440020' AS UUID), 'BEAUTY001', 'Perfume Boticário', 'Perfume Lily Boticário Feminino 75ml', 'Beleza e Cuidado Pessoal', 199.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440021' AS UUID), 'BEAUTY002', 'Creme Anti-idade', 'Creme Anti-idade L''Oréal Revitalift 50ml', 'Beleza e Cuidado Pessoal', 89.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- MODA
                   (CAST('660e8400-e29b-41d4-a716-446655440022' AS UUID), 'FASHION001', 'Camiseta Polo Lacoste', 'Camiseta Polo Lacoste Masculina Algodão', 'Moda', 299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440023' AS UUID), 'FASHION002', 'Tênis Nike Air Max', 'Tênis Nike Air Max SC Masculino Branco', 'Moda', 449.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440024' AS UUID), 'FASHION003', 'Vestido Farm Rio', 'Vestido Farm Rio Estampado Feminino', 'Moda', 299.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- BEBÊS
                   (CAST('660e8400-e29b-41d4-a716-446655440025' AS UUID), 'BABY001', 'Carrinho de Bebê', 'Carrinho de Bebê Galzerano Reversível', 'Bebês', 699.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440026' AS UUID), 'BABY002', 'Berço Americano', 'Berço Americano Matic Móveis Branco', 'Bebês', 899.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- BRINQUEDOS
                   (CAST('660e8400-e29b-41d4-a716-446655440027' AS UUID), 'TOY001', 'LEGO Creator Expert', 'LEGO Creator Expert Taj Mahal 2056 Peças', 'Brinquedos', 1299.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-446655440028' AS UUID), 'TOY002', 'Boneca Barbie', 'Boneca Barbie Fashionista Morena', 'Brinquedos', 89.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- IMÓVEIS
                   (CAST('660e8400-e29b-41d4-a716-446655440029' AS UUID), 'REAL001', 'Apartamento 2 Quartos', 'Apartamento 2 Quartos Vila Madalena SP', 'Imóveis', 850000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                   -- MAIS VENDIDOS (produtos populares de várias categorias)
                   (CAST('660e8400-e29b-41d4-a716-44665544002a' AS UUID), 'BEST001', 'Carregador iPhone', 'Carregador iPhone Lightning Original Apple', 'Mais vendidos', 199.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                   (CAST('660e8400-e29b-41d4-a716-44665544002b' AS UUID), 'BEST002', 'Película iPhone', 'Película de Vidro Temperado iPhone 14/15', 'Mais vendidos', 29.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                  ) AS new_products(id, sku, name, description, category, price, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM tbl_products WHERE tbl_products.sku = new_products.sku);

-- Inserir estoque exemplo (apenas se não existir a combinação produto+loja)
INSERT INTO tbl_stock (id, product_id, store_id, quantity, reserved_quantity, available_quantity, created_at, updated_at, version)
SELECT * FROM (VALUES
                   -- TECNOLOGIA
                   -- iPhone 15 Pro (TECH001)
                   (CAST('770e8400-e29b-41d4-a716-446655440001' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440006' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 15, 2, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440006' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 12, 1, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440006' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 8, 0, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Samsung Galaxy S24 (TECH002)
                   (CAST('770e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440007' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 18, 3, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440005' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440007' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 14, 1, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- MacBook Air M2 (TECH003)
                   (CAST('770e8400-e29b-41d4-a716-446655440006' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440008' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 10, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440007' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440008' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 8, 0, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- PlayStation 5 (TECH004)
                   (CAST('770e8400-e29b-41d4-a716-446655440008' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440009' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 12, 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440009' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440009' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 8, 1, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Smart TV Samsung (TECH005)
                   (CAST('770e8400-e29b-41d4-a716-44665544000a' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 25, 3, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-44665544000b' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 20, 2, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- SUPERMERCADO
                   -- Arroz Tio João (SUPER001)
                   (CAST('770e8400-e29b-41d4-a716-44665544000c' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 200, 20, 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-44665544000d' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 150, 15, 135, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-44665544000e' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 180, 10, 170, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Óleo de Soja (SUPER002)
                   (CAST('770e8400-e29b-41d4-a716-44665544000f' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 120, 10, 110, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440010' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 100, 8, 92, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- CASA E MÓVEIS
                   -- Sofá Retrátil (HOME001)
                   (CAST('770e8400-e29b-41d4-a716-446655440011' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000b' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 5, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440012' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000b' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 3, 0, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Mesa de Jantar (HOME002)
                   (CAST('770e8400-e29b-41d4-a716-446655440013' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000c' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 8, 1, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440014' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000c' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 6, 0, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- ELETRODOMÉSTICOS
                   -- Geladeira Brastemp (ELETRO001)
                   (CAST('770e8400-e29b-41d4-a716-446655440015' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000e' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 15, 2, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440016' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544000e' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 12, 1, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Air Fryer (ELETRO003)
                   (CAST('770e8400-e29b-41d4-a716-446655440017' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440010' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 45, 5, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440018' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440010' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 35, 3, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- MODA
                   -- Tênis Nike (FASHION002)
                   (CAST('770e8400-e29b-41d4-a716-446655440019' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440023' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 60, 8, 52, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-44665544001a' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440023' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 45, 5, 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Camiseta Polo Lacoste (FASHION001)
                   (CAST('770e8400-e29b-41d4-a716-44665544001b' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440022' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 30, 3, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-44665544001c' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440022' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 25, 2, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- PET SHOP
                   -- Ração Golden (PET001)
                   (CAST('770e8400-e29b-41d4-a716-44665544001d' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544001a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 80, 8, 72, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-44665544001e' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544001a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 65, 5, 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- MAIS VENDIDOS
                   -- Carregador iPhone (BEST001)
                   (CAST('770e8400-e29b-41d4-a716-44665544001f' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544002a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 150, 15, 135, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440020' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544002a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 120, 12, 108, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440021' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544002a' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 100, 10, 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Película iPhone (BEST002)
                   (CAST('770e8400-e29b-41d4-a716-446655440022' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544002b' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 200, 20, 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   (CAST('770e8400-e29b-41d4-a716-446655440023' AS UUID), CAST('660e8400-e29b-41d4-a716-44665544002b' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 180, 18, 162, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),

                   -- PRODUTOS COM ESTOQUE ZERADO (ADICIONADOS)
                   -- Yamaha MT-07 (VEHICLE002) na loja de SP
                   (CAST('770e8400-e29b-41d4-a716-446655440024' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID), 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Vestido Farm Rio (FASHION003) na loja do RJ
                   (CAST('770e8400-e29b-41d4-a716-446655440025' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440024' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID), 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
                   -- Furadeira Bosch (TOOL001) na loja de BH
                   (CAST('770e8400-e29b-41d4-a716-446655440026' AS UUID), CAST('660e8400-e29b-41d4-a716-446655440014' AS UUID), CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID), 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)

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