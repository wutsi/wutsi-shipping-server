package com.wutsi.ecommerce.shipping.entity

enum class ShippingType(val addressType: AddressType) {
    LOCAL_PICKUP(AddressType.NONE),
    LOCAL_DELIVERY(AddressType.POSTAL),
    INTERNATIONAL_SHIPPING(AddressType.POSTAL),
    EMAIL_DELIVERY(AddressType.EMAIL)
}
