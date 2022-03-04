package com.wutsi.ecommerce.shipping.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public data class SearchRateRequest(
    public val shippingId: Long? = null,
    public val accountId: Long = 0,
    @get:NotBlank
    public val country: String = "",
    public val cityId: Long? = null,
    @get:NotNull
    @get:NotEmpty
    public val products: List<Product> = emptyList()
)
