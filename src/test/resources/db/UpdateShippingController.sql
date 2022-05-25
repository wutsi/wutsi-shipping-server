INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled, country, city_id, currency)
    VALUES
        (100, 1, 111, 2, 'This is ???', false, 'US', 11111, 'XAF'),
        (120, 1, 2, 2, null, true, 'US', 11111, 'XAF'),
        (200, 2, 222, 2, null, true, 'US', 11111, 'XAF')
    ;
