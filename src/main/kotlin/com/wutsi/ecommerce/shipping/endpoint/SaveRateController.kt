package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.`delegate`.SaveRateDelegate
import com.wutsi.ecommerce.shipping.dto.SaveRateRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class SaveRateController(
    private val `delegate`: SaveRateDelegate
) {
    @PostMapping("/v1/rates")
    @PreAuthorize(value = "hasAuthority('shipping-manage')")
    public fun invoke(@Valid @RequestBody request: SaveRateRequest) {
        delegate.invoke(request)
    }
}
