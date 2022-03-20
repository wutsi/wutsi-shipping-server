package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.CreateShippingRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingResponse
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.tenant.WutsiTenantApi
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CreateShippingDelegate(
    private val dao: ShippingRepository,
    private val securityManager: SecurityManager,
    private val tenantApi: WutsiTenantApi
) {
    @Transactional
    fun invoke(request: CreateShippingRequest): CreateShippingResponse {
        val tenant = tenantApi.getTenant(securityManager.tenantId()).tenant
        val shipping = dao.save(
            ShippingEntity(
                type = ShippingType.valueOf(request.type),
                message = request.message,
                accountId = securityManager.accountId(),
                tenantId = tenant.id,
                currency = tenant.currency,
                cityId = request.cityId,
                country = request.country,
                zipCode = request.zipCode,
                street = request.street
            )
        )
        return CreateShippingResponse(id = shipping.id!!)
    }
}
