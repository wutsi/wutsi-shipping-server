package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.ChangeShippingOrderStatusDelegate
import com.wutsi.ecommerce.shipping.dto.ChangeStatusRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class ChangeShippingOrderStatusController(
    private val `delegate`: ChangeShippingOrderStatusDelegate
) {
    @PostMapping("/v1/shipping-orders/{id}/status")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: ChangeStatusRequest
    ) {
        delegate.invoke(id, request)
    }
}
