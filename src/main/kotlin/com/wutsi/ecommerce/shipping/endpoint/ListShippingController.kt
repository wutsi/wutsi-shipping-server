package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.ListShippingDelegate
import com.wutsi.ecommerce.shipping.dto.ListShippingResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class ListShippingController(
    private val `delegate`: ListShippingDelegate
) {
    @GetMapping("/v1/shippings")
    @PreAuthorize(value = "hasAuthority('shipping-read')")
    public fun invoke(): ListShippingResponse = delegate.invoke()
}
