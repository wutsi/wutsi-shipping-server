package com.wutsi.ecommerce.shipping.delegate

import com.wutsi.ecommerce.shipping.dto.Rate
import com.wutsi.ecommerce.shipping.dto.Shipping
import com.wutsi.ecommerce.shipping.dto.ShippingSummary
import com.wutsi.ecommerce.shipping.entity.RateEntity
import com.wutsi.ecommerce.shipping.entity.ShippingEntity

fun ShippingEntity.toShipping() = Shipping(
    id = this.id ?: -1,
    accountId = this.accountId,
    type = this.type.name,
    message = this.message,
    enabled = this.enabled,
    rate = this.rate,
    deliveryTime = this.deliveryTime,
    rates = this.rates.map { it.toRate() }
)

fun ShippingEntity.toShippingSummary() = ShippingSummary(
    id = this.id ?: -1,
    accountId = this.accountId,
    type = this.type.name,
    enabled = this.enabled,
    rate = this.rate,
    deliveryTime = this.deliveryTime,
)

fun RateEntity.toRate() = Rate(
    id = this.id ?: -1,
    country = this.country,
    cityId = this.cityId,
    amount = this.amount
)
