# Event Handling

## Event Consumed

| Event                                   | Source      | Description                                                                |
|-----------------------------------------|-------------|----------------------------------------------------------------------------|
| `urn:wutsi:event:order:order-done`      | wutsi-order | This event start the shipping process of the order                         |
| `urn:wutsi:event:order:order-cancelled` | wutsi-order | This event cancel the active shippings associated with the cancelled order |

## Event Emitted

| Event                                                | Description                                                                            |
|------------------------------------------------------|----------------------------------------------------------------------------------------|
| `urn:wutsi:event:shipping:shipping-creates`          | This event is fired to notify that the shipping is created                             |
| `urn:wutsi:event:shipping:shipping-ready-for-pickup` | This event is fired to notify that the shipping is available for pickup                |
| `urn:wutsi:event:shipping:shipping-delivered`        | This event is fired to notify that the shipping has been cancelled                     |
| `urn:wutsi:event:shipping:shipping-in-transit`       | This event is fired to notify that the shipping is in transit to the delivery location |
| `urn:wutsi:event:shipping:shipping-cancelled`        | This event is fired to notify that the shipping has been cancelled                     |
