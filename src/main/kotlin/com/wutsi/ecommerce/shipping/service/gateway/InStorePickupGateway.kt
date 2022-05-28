package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.ChangeStatusRequest
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.entity.OrderStatus
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.Gateway
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.entity.ToggleName
import org.springframework.stereotype.Service

@Service
class InStorePickupGateway(
    private val orderApi: WutsiOrderApi,
    private val logger: KVLogger,
) : Gateway {
    override fun enabled(tenant: Tenant): Boolean =
        tenant.toggles.find { it.name == ToggleName.SHIPPING_IN_STORE_PICKUP.name } != null

    /**
     * Accept only request in the same city
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean {
        logger.add("gateway_instore", true)
        return true
    }

    override fun onOrderDone(order: Order) {
        orderApi.changeStatus(
            id = order.id,
            request = ChangeStatusRequest(
                status = OrderStatus.READY_FOR_PICKUP.name
            )
        )
    }
}
