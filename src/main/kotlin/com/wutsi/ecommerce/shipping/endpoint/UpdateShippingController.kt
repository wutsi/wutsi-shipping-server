package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.UpdateShippingDelegate
import com.wutsi.ecommerce.shipping.dto.UpdateShippingRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class UpdateShippingController(
    private val `delegate`: UpdateShippingDelegate
) {
    @PostMapping("/v1/shippings/{id}")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: UpdateShippingRequest
    ) {
        delegate.invoke(id, request)
    }
}
