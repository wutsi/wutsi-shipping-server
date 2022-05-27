package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.dto.GetShippingOrderResponse
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
public class GetShippingOrderDelegate(
    private val shippingOrderDao: ShippingOrderRepository,
    private val securityManager: SecurityManager,
) {
    public fun invoke(id: Long): GetShippingOrderResponse {
        val shippingOrder = shippingOrderDao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.SHIPPING_ORDER_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH
                        )
                    )
                )
            }
        securityManager.checkTenant(shippingOrder)

        return GetShippingOrderResponse(
            shippingOrder = shippingOrder.toShippingOrder()
        )
    }
}
