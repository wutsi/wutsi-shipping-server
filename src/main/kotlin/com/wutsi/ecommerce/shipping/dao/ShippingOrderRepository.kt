package com.wutsi.ecommerce.shipping.dao

import com.wutsi.ecommerce.shipping.entity.ShippingOrderEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ShippingOrderRepository : CrudRepository<ShippingOrderEntity, Long> {
    fun findByOrderId(orderId: String): List<ShippingOrderEntity>
}
