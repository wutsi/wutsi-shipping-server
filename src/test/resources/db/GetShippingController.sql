INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled, country, city_id, rate, currency)
    VALUES
        (100, 1, 111, 0, 'Local Pickup', true, 'CM', 1111, 30000, 'XAF'),
        (101, 1, 111, 1, 'Local Delivery', true, 'CM', 500, 0, 'XAF'),
        (102, 1, 111, 2, 'International Delivery', true, 'CM', 30000, 0, 'XAF'),
        (120, 1, 2, 1, null, true, 'CM', null, 0, 'XAF'),
        (200, 2, 222, 1, null, true, 'CM', null, 0, 'XAF')
    ;
