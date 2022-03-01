INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled)
    VALUES
        (100, 1, 111, 1, 'This is ???', false),
        (200, 1, 111, 1, null, true)
    ;

INSERT INTO T_RATE(id, shipping_fk, country, city_id, amount)
    VALUES
        (2001, 200, '*', null, 20000),
        (2002, 200, 'FR', null, 10000);
