package com.wutsi.ecommerce.shipping.error

enum class ErrorURN(val urn: String) {
    ATTRIBUTE_INVALID("urn:wutsi:error:shipping:invalid-attribute"),
    SHIPPING_NOT_FOUND("urn:wutsi:error:shipping:shipping-not-found"),
    ILLEGAL_TENANT_ACCESS("urn:wutsi:error:account:illegal-tenant-access"),
    ILLEGAL_SHIPPING_ACCESS("urn:wutsi:error:account:illegal-shipping-access"),
}
