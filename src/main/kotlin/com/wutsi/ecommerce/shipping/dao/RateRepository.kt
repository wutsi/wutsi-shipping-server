package com.wutsi.ecommerce.shipping.dao

import com.wutsi.ecommerce.shipping.entity.RateEntity
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RateRepository : CrudRepository<RateEntity, Long> {
    fun findByShippingAndCountryAndCityId(
        shipping: ShippingEntity,
        country: String,
        cityId: Long?
    ): Optional<RateEntity>

    fun findByShipping(shipping: ShippingEntity): List<RateEntity>
}
