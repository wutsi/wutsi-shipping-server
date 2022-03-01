package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.CreateShippingRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingResponse
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.service.SecurityManager
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CreateShippingDelegate(
    private val dao: ShippingRepository,
    private val securityManager: SecurityManager
) {
    @Transactional
    fun invoke(request: CreateShippingRequest): CreateShippingResponse {
        val shipping = dao.save(
            ShippingEntity(
                type = ShippingType.valueOf(request.type),
                message = request.message,
                accountId = securityManager.accountId(),
                tenantId = securityManager.tenantId()
            )
        )
        return CreateShippingResponse(id = shipping.id!!)
    }
}
