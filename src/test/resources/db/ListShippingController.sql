INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled, country, city_id, currency)
    VALUES
        (100, 1, 111, 0, 'This is ???', true, 'CM', 11111, 'XAF'),
        (101, 1, 111, 2, 'This is ???', false, 'CM', 11111, 'XAF'),
        (120, 1, 2, 1, null, true, 'CM', 11111, 'XAF'),
        (200, 2, 222, 1, null, true, 'CM', 11111, 'XAF')
    ;
