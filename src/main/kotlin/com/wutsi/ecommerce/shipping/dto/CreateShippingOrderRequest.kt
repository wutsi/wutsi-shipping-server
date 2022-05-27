package com.wutsi.ecommerce.shipping.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class CreateShippingOrderRequest(
    @get:NotBlank
    public val orderId: String = ""
)
