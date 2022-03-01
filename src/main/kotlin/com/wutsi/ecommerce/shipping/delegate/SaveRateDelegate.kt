package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.RateRepository
import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.SaveRateRequest
import com.wutsi.ecommerce.shipping.entity.RateEntity
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class SaveRateDelegate(
    private val shippingDao: ShippingRepository,
    private val rateDao: RateRepository,
    private val securityManager: SecurityManager
) {
    @Transactional
    public fun invoke(request: SaveRateRequest) {
        // Shipping
        val shipping = shippingDao.findById(request.shippingId)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.SHIPPING_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "request.shippingId",
                            value = request.shippingId,
                            type = ParameterType.PARAMETER_TYPE_PAYLOAD
                        )
                    )
                )
            }
        securityManager.checkTenant(shipping)
        securityManager.checkOwnership(shipping)

        // Rate
        val rate = rateDao.findByShippingAndCountryAndCityId(shipping, request.country, request.cityId)
            .orElse(
                RateEntity(
                    shipping = shipping,
                    country = request.country,
                    cityId = request.cityId
                )
            )
        rate.amount = request.amount
        rateDao.save(rate)
    }
}
