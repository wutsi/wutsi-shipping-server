CREATE OR REPLACE FUNCTION shipping_order_updated()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_shipping_order_updated
BEFORE UPDATE ON T_SHIPPING_ORDER
FOR EACH ROW
EXECUTE PROCEDURE shipping_order_updated();



CREATE OR REPLACE FUNCTION shipping_updated()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_shipping_updated
BEFORE UPDATE ON T_SHIPPING
FOR EACH ROW
EXECUTE PROCEDURE shipping_updated();
