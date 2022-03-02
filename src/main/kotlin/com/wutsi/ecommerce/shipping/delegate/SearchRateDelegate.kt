package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.dto.SearchRateResponse
import com.wutsi.ecommerce.shipping.service.GatewayProvider
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service

@Service
public class SearchRateDelegate(
    private val dao: ShippingRepository,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
    private val provider: GatewayProvider
) {
    public fun invoke(request: SearchRateRequest): SearchRateResponse {
        logger.add("country", request.country)
        logger.add("cityId", request.cityId)
        logger.add("product_count", request.products.size)
        logger.add("has_numeric_product", request.products.find { it.productType == ProductType.NUMERIC.name } != null)

        val accountId = securityManager.accountId()
        return SearchRateResponse(
            rates = dao.findByAccountId(accountId)
                .filter { it.enabled && provider.get(it).accept(request, it) }
                .map {
                    it.toRateSummary(provider.get(it))
                }
        )
    }
}
