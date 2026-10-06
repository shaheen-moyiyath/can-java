-- ====================================================================
-- Canteen Ordering System Seed Data (data.sql)
-- Default Passwords:
--   admin / admin123
--   student1 / student123
--   AZAYSCS043 / student123 (Sainul Ashiqu N from the project document)
-- ====================================================================

-- 1. Insert Default Users (BCrypt hashes for 'admin123' and 'student123')
INSERT INTO users (id, username, password_hash, full_name, email, role, created_at) VALUES
(1, 'admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Canteen Administrator', 'admin@canteen.college.edu', 'ADMIN', CURRENT_TIMESTAMP),
(2, 'student1', '$2a$10$e8w.K2gA.wI5EwV6GkK8y.U9JmUoJjA2hYF8qBwP2tW5dYm8qZ0Ce', 'Muhammed Shaheen', 'shaheen@student.college.edu', 'STUDENT', CURRENT_TIMESTAMP),
(3, 'AZAYSCS043', '$2a$10$e8w.K2gA.wI5EwV6GkK8y.U9JmUoJjA2hYF8qBwP2tW5dYm8qZ0Ce', 'Sainul Ashiqu N', 'sainul@student.college.edu', 'STUDENT', CURRENT_TIMESTAMP);

-- 2. Insert Menu Items from Technical Report & Campus Specials
INSERT INTO menu_items (id, name, description, price, category, is_available, created_at) VALUES
(1, 'Chicken Biryani', 'Fragrant Basmati rice cooked with tender spiced chicken, egg, and raita', 120.00, 'Biryani', TRUE, CURRENT_TIMESTAMP),
(2, 'Veg Thali / Kerala Meals', 'Traditional vegetarian meal with rice, sambar, thoran, rasam, and pickle', 60.00, 'Meals', TRUE, CURRENT_TIMESTAMP),
(3, 'Fish Curry Meals', 'Kerala rice meal served with hot spicy sardine/mackerel fish curry', 90.00, 'Meals', TRUE, CURRENT_TIMESTAMP),
(4, 'Masala Dosa', 'Crispy fermented crepe stuffed with savory mashed spiced potatoes', 50.00, 'Breakfast', TRUE, CURRENT_TIMESTAMP),
(5, 'Egg Puffs', 'Flaky golden baked pastry filled with spiced egg filling', 20.00, 'Snacks', TRUE, CURRENT_TIMESTAMP),
(6, 'Chicken Roll', 'Shredded chicken sauteed in onion and peppers rolled in flatbread', 60.00, 'Snacks', TRUE, CURRENT_TIMESTAMP),
(7, 'Tea / Chai', 'Freshly brewed hot cardamom tea', 12.00, 'Beverages', TRUE, CURRENT_TIMESTAMP),
(8, 'Cold Coffee', 'Chilled blended milk coffee with chocolate drizzle', 35.00, 'Beverages', TRUE, CURRENT_TIMESTAMP);

-- 3. Initial Sample Orders (Today's date)
INSERT INTO orders (id, user_id, total_amount, status, order_date, created_at) VALUES
(1, 2, 140.00, 'PREPARING', CURRENT_DATE, CURRENT_TIMESTAMP),
(2, 3, 120.00, 'PENDING', CURRENT_DATE, CURRENT_TIMESTAMP);

-- 4. Initial Sample Order Items
INSERT INTO order_items (id, order_id, menu_item_id, quantity, unit_price) VALUES
(1, 1, 1, 1, 120.00), -- 1x Chicken Biryani
(2, 1, 5, 1, 20.00),  -- 1x Egg Puffs
(3, 2, 1, 1, 120.00); -- 1x Chicken Biryani
