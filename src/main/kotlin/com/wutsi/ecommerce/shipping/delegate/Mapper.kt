package com.wutsi.ecommerce.shipping.delegate

import com.wutsi.ecommerce.shipping.dto.RateSummary
import com.wutsi.ecommerce.shipping.dto.Shipping
import com.wutsi.ecommerce.shipping.dto.ShippingSummary
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.Gateway

fun ShippingEntity.toShipping() = Shipping(
    id = this.id ?: -1,
    accountId = this.accountId,
    type = this.type.name,
    message = this.message,
    enabled = this.enabled,
    rate = this.rate,
    currency = this.currency,
    deliveryTime = this.deliveryTime,
    cityId = this.cityId,
    country = this.country,
    zipCode = this.zipCode,
    street = this.street,
)

fun ShippingEntity.toShippingSummary() = ShippingSummary(
    id = this.id ?: -1,
    accountId = this.accountId,
    type = this.type.name,
    enabled = this.enabled,
    rate = this.rate,
    currency = this.currency,
    deliveryTime = this.deliveryTime,
    cityId = this.cityId,
    country = this.country,
    zipCode = this.zipCode,
    street = this.street,
)

fun ShippingEntity.toRateSummary(gateway: Gateway) = RateSummary(
    shippingId = this.id ?: -1,
    shippingType = this.type.name,
    rate = gateway.computeRate(this),
    currency = this.currency,
    deliveryTime = this.deliveryTime
)
