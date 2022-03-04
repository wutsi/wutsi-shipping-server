INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled, country, city_id, rate, currency, delivery_time)
    VALUES
        (100, 1, 111, 0, 'Local Pickup', true, 'CM', 1000, 0, 'XAF', 12),
        (101, 1, 111, 1, 'Local Delivery', true, 'CM', 1000, 1500, 'XAF', 24),
        (102, 1, 111, 2, 'International Delivery', true, 'FR', null, 30000, 'XAF', 240),
        (103, 1, 111, 3, 'Email Delivery', false, 'CM', null, 0.0, 'XAF', 0),
        (120, 1, 2, 1, null, true, 'CM', null, 0, 'XAF', null),
        (200, 2, 222, 1, null, true, 'CM', null, 0, 'XAF', null)
    ;
