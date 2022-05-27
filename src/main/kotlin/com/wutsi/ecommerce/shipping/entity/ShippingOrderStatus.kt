package com.wutsi.ecommerce.shipping.entity

enum class ShippingOrderStatus {
    CREATED,
    IN_TRANSIT,
    READY_FOR_PICKUP,
    DELIVERED,
    CANCELLED
}
