package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.GetShippingOrderDelegate
import com.wutsi.ecommerce.shipping.dto.GetShippingOrderResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class GetShippingOrderController(
    private val `delegate`: GetShippingOrderDelegate
) {
    @GetMapping("/v1/shipping-orders/{id}")
    @PreAuthorize(value = "hasAuthority('shipping-read')")
    public fun invoke(@PathVariable(name = "id") id: Long): GetShippingOrderResponse =
        delegate.invoke(id)
}
