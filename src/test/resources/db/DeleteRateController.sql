INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled)
    VALUES
        (100, 1, 111, 1, 'This is ???', false),
        (200, 1, 333, 1, null, true)
    ;

INSERT INTO T_RATE(id, shipping_fk, country, city_id, amount)
    VALUES
        (1001, 100, '*', null, 20000),
        (1002, 100, 'FR', null, 10000)
    ;
