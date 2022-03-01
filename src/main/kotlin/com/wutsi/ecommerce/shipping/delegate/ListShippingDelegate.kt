package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.ListShippingResponse
import org.springframework.stereotype.Service

@Service
public class ListShippingDelegate(
    private val dao: ShippingRepository,
    private val securityManager: com.wutsi.ecommerce.shipping.service.SecurityManager
) {
    public fun invoke(): ListShippingResponse =
        ListShippingResponse(
            shippings = dao.findByAccountId(securityManager.accountId()).map { it.toShippingSummary() }
        )
}
