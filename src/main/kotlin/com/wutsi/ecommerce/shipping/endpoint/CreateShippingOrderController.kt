package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.CreateShippingOrderDelegate
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class CreateShippingOrderController(
    private val `delegate`: CreateShippingOrderDelegate
) {
    @PostMapping("/v1/shipping-orders")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(@Valid @RequestBody request: CreateShippingOrderRequest):
        CreateShippingOrderResponse = delegate.invoke(request)
}
