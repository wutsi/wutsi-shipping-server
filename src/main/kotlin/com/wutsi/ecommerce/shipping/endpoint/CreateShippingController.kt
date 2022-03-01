package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.CreateShippingDelegate
import com.wutsi.ecommerce.shipping.dto.CreateShippingRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class CreateShippingController(
    private val `delegate`: CreateShippingDelegate
) {
    @PostMapping("/v1/shippings")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(@Valid @RequestBody request: CreateShippingRequest): CreateShippingResponse =
        delegate.invoke(request)
}
