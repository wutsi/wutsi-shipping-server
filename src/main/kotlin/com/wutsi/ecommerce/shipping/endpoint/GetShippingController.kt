package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.GetShippingDelegate
import com.wutsi.ecommerce.shipping.dto.GetShippingResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class GetShippingController(
    private val `delegate`: GetShippingDelegate
) {
    @GetMapping("/v1/shippings/{id}")
    @PreAuthorize(value = "hasAuthority('shipping-read')")
    public fun invoke(@PathVariable(name = "id") id: Long): GetShippingResponse = delegate.invoke(id)
}
