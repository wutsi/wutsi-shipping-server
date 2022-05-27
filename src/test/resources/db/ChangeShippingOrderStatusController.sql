INSERT INTO T_SHIPPING(id, tenant_id, account_id, type, message, enabled, country, city_id, rate, currency)
    VALUES
        (100, 1, 111, 1, 'Local Pickup', true, 'CM', 1111, 30000, 'XAF'),
        (101, 1, 111, 2, 'Local Delivery', true, 'CM', 500, 0, 'XAF'),
        (102, 1, 111, 3, 'International Delivery', true, 'CM', 30000, 0, 'XAF'),
        (120, 1, 2, 1, null, true, 'CM', null, 0, 'XAF'),
        (200, 2, 222, 1, null, true, 'CM', null, 0, 'XAF')
    ;

INSERT INTO T_SHIPPING_ORDER(id, shipping_fk, tenant_id, merchant_id, order_id, customer_id, status)
    VALUES
        (100, 100, 1, 666, '777', 111, 0),
        (101, 100, 1, 666, '777', 111, 1),
        (102, 100, 1, 666, '777', 111, 2),
        (103, 100, 1, 666, '777', 111, 3),
        (104, 100, 1, 666, '777', 111, 4)
;
