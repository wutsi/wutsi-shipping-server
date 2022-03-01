package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.UpdateShippingRequest
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UpdateShippingDelegate(
    private val dao: ShippingRepository,
    private val securityManager: SecurityManager
) {
    @Transactional
    fun invoke(id: Long, request: UpdateShippingRequest) {
        val shipping = dao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.SHIPPING_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH
                        )
                    )
                )
            }
        securityManager.checkTenant(shipping)
        securityManager.checkOwnership(shipping)

        shipping.enabled = request.enabled
        shipping.message = request.message
        dao.save(shipping)
    }
}
