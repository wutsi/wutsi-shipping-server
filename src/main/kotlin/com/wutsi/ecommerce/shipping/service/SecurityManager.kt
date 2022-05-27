package com.wutsi.ecommerce.shipping.service

import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingOrderEntity
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.ForbiddenException
import com.wutsi.platform.core.security.WutsiPrincipal
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityManager(
    private val tracingContext: TracingContext
) {
    fun checkOwnership(shipping: ShippingEntity) {
        if (shipping.accountId != accountId())
            throw ForbiddenException(
                error = Error(
                    code = ErrorURN.ILLEGAL_SHIPPING_ACCESS.urn
                )
            )
    }

    fun checkTenant(shipping: ShippingEntity) {
        if (shipping.tenantId != tenantId())
            throw ForbiddenException(
                error = Error(
                    code = ErrorURN.ILLEGAL_TENANT_ACCESS.urn
                )
            )
    }

    fun checkTenant(shipping: ShippingOrderEntity) {
        if (shipping.tenantId != tenantId())
            throw ForbiddenException(
                error = Error(
                    code = ErrorURN.ILLEGAL_TENANT_ACCESS.urn
                )
            )
    }

    fun tenantId(): Long =
        tracingContext.tenantId()!!.toLong()

    fun accountId(): Long =
        principal().id.toLong()

    private fun principal(): WutsiPrincipal {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal
        return principal as WutsiPrincipal
    }
}
