CREATE TABLE T_SHIPPING_ORDER(
    id              SERIAL NOT NULL,

    shipping_fk     BIGINT NOT NULL REFERENCES T_SHIPPING(id),

    tenant_id       BIGINT NOT NULL,
    account_id      BIGINT NOT NULL,
    merchant_id     BIGINT NOT NULL,
    order_id        VARCHAR(36) NOT NULL,

    status          INT DEFAULT 0,
    created         TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated         TIMESTAMPTZ NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE TABLE T_SHIPPING_ORDER_STATUS(
    id                  SERIAL NOT NULL,

    shipping_order_fk   BIGINT NOT NULL REFERENCES T_SHIPPING_ORDER(id),

    status              INT NOT NULL DEFAULT 0,
    previous_status     INT,
    reason              VARCHAR(30),
    comment             TEXT,
    created             TIMESTAMPTZ NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

