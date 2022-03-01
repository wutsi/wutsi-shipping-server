package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.UpdateShippingAttributeDelegate
import com.wutsi.ecommerce.shipping.dto.UpdateShippingAttributeRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long
import kotlin.String

@RestController
public class UpdateShippingAttributeController(
    private val `delegate`: UpdateShippingAttributeDelegate
) {
    @PostMapping("/v1/shippings/{id}/attributes/{name}")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @PathVariable(name = "name") name: String,
        @Valid @RequestBody request: UpdateShippingAttributeRequest
    ) {
        delegate.invoke(id, name, request)
    }
}
