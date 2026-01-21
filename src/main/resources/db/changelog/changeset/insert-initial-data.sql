INSERT INTO categories (name, created_at) VALUES
('Indoor Plants', CURRENT_TIMESTAMP),
('Succulents', CURRENT_TIMESTAMP),
('Pots & Planters', CURRENT_TIMESTAMP),
('Gardening Tools', CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, stock_quantity, category_id, created_at) VALUES
('Monstera Deliciosa', 'Popular tropical plant with split leaves. Perfect for indoor decoration.', 25.50, 50, (SELECT id FROM categories WHERE name = 'Indoor Plants'), CURRENT_TIMESTAMP),
('Snake Plant', 'Hardy plant that tolerates low light and infrequent watering.', 15.00, 30, (SELECT id FROM categories WHERE name = 'Indoor Plants'), CURRENT_TIMESTAMP),
('Aloe Vera', 'Medicinal succulent plant, great for sunny spots.', 10.00, 40, (SELECT id FROM categories WHERE name = 'Succulents'), CURRENT_TIMESTAMP),
('Terracotta Pot', 'Classic clay pot with drainage hole. Diameter: 15cm.', 5.99, 100, (SELECT id FROM categories WHERE name = 'Pots & Planters'), CURRENT_TIMESTAMP),
('Watering Can', 'Modern metal watering can, 1.5L capacity.', 18.50, 20, (SELECT id FROM categories WHERE name = 'Gardening Tools'), CURRENT_TIMESTAMP);

INSERT INTO users (email, password, first_name, last_name, role, created_at) VALUES
('admin@greenleaf.com', '$2a$10$dx.Z.i.o.7.3.1.5.9.Q.u.Y.u.I.o.p.A.s.d.F.g.H.j.K.l.Z.x.C', 'Admin', 'Superuser', 'ADMIN', CURRENT_TIMESTAMP),
('user@greenleaf.com', '$2a$10$dx.Z.i.o.7.3.1.5.9.Q.u.Y.u.I.o.p.A.s.d.F.g.H.j.K.l.Z.x.C', 'John', 'Doe', 'USER', CURRENT_TIMESTAMP);

INSERT INTO orders (user_id, status, total_price, created_at) VALUES
    ((SELECT id FROM users WHERE email = 'user@greenleaf.com'), 'COMPLETED', 41.49, CURRENT_TIMESTAMP);

INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES
((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'user@greenleaf.com')), (SELECT id FROM products WHERE name = 'Monstera Deliciosa'), 1, 25.50),
((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'user@greenleaf.com')), (SELECT id FROM products WHERE name = 'Snake Plant'), 1, 15.99);