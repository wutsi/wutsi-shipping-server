CREATE TABLE T_SHIPPING(
    id              SERIAL NOT NULL,

    tenant_id       BIGINT NOT NULL,
    account_id      BIGINT NOT NULL,

    type            INT NOT NULL DEFAULT 0,
    message         TEXT,
    enabled         BOOL NOT NULL DEFAULT true,

    created         TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated         TIMESTAMPTZ NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE TABLE T_RATE(
    id              SERIAL NOT NULL,

    shipping_fk     BIGINT NOT NULL REFERENCES T_SHIPPING(id),
    amount          DECIMAL(20, 4) NOT NULL DEFAULT 0,
    country         VARCHAR(2) NOT NULL,
    city_id         BIGINT,

    UNIQUE(shipping_fk, country, city_id),
    PRIMARY KEY (id)
);
