package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.SearchShippingDelegate
import com.wutsi.ecommerce.shipping.dto.ListShippingResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Any

@RestController
public class SearchShippingController(
    private val `delegate`: SearchShippingDelegate
) {
    @PostMapping("/v1/shippings/search")
    @PreAuthorize(value = "hasAuthority('shipping-read')")
    public fun invoke(@Valid @RequestBody request: Any): ListShippingResponse =
        delegate.invoke(request)
}
