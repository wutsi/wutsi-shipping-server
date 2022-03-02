package com.wutsi.ecommerce.shipping.dto

public data class SearchRateResponse(
    public val rates: List<RateSummary> = emptyList()
)
