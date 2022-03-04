package com.wutsi.ecommerce.shipping.dto

import kotlin.collections.List

public data class SearchRateResponse(
    public val rates: List<RateSummary> = emptyList()
)
