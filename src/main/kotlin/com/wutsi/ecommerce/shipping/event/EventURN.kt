package com.wutsi.ecommerce.shipping.event

enum class EventURN(val urn: String) {
    SHIPPING_READY_FOR_PICKUP("urn:wutsi:event:shipping:shipping-ready-for-pickup"),
    SHIPPING_DELIVERED("urn:wutsi:event:shipping:shipping-delivered"),
    SHIPPING_IN_TRANSIT("urn:wutsi:event:shipping:shipping-in-transit"),
    SHIPPING_CANCELLED("urn:wutsi:event:shipping:shipping-cancelled"),
}
