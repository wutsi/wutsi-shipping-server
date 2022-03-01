package com.wutsi.ecommerce.shipping.dao

import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ShippingRepository : CrudRepository<ShippingEntity, Long> {
    fun findByAccountId(accountId: Long): List<ShippingEntity>
}
