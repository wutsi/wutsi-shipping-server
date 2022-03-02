package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.SearchRateDelegate
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.dto.SearchRateResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class SearchRateController(
    private val `delegate`: SearchRateDelegate
) {
    @PostMapping("/v1/rates/search")
    @PreAuthorize(value = "hasAuthority('shipping-read')")
    public fun invoke(@Valid @RequestBody request: SearchRateRequest): SearchRateResponse =
        delegate.invoke(request)
}
