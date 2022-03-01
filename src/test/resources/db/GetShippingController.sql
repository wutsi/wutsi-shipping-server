INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled)
    VALUES
        (100, 1, 111, 1, 'Yo Man', true),
        (120, 1, 2, 1, null, true),
        (200, 2, 222, 1, null, true)
    ;

INSERT INTO T_RATE(id, shipping_fk, country, city_id, amount)
    VALUES
        (1001, 100, '*', null, 20000),
        (1002, 100, 'FR', 1111, 25000);
