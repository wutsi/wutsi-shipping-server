package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.UpdateShippingAttributeRequest
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UpdateShippingAttributeDelegate(
    private val dao: ShippingRepository,
    private val securityManager: SecurityManager
) {
    @Transactional
    fun invoke(id: Long, name: String, request: UpdateShippingAttributeRequest) {
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

        when (name.lowercase()) {
            "enabled" -> shipping.enabled = toString(request.value)?.toBoolean() ?: false
            "message" -> shipping.message = toString(request.value)
            "rate" -> shipping.rate = toDouble(request.value)
            "delivery-time" -> shipping.deliveryTime = toInt(request.value)
            else -> throw BadRequestException(
                error = Error(
                    code = ErrorURN.ATTRIBUTE_INVALID.urn,
                    parameter = Parameter(
                        name = "name",
                        value = name,
                        type = ParameterType.PARAMETER_TYPE_PATH
                    )
                )
            )
        }
        dao.save(shipping)
    }

    private fun toString(value: String?): String? =
        if (value.isNullOrEmpty())
            null
        else
            value.toString().trim()

    private fun toDouble(value: String?): Double? =
        toString(value)?.toDouble()

    private fun toInt(value: String?): Int? =
        toString(value)?.toInt()
}
