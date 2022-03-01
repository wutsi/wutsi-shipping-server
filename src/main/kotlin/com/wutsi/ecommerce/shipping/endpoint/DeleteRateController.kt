package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.DeleteRateDelegate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class DeleteRateController(
    private val `delegate`: DeleteRateDelegate
) {
    @DeleteMapping("/v1/rates/{id}")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(@PathVariable(name = "id") id: Long) {
        delegate.invoke(id)
    }
}
