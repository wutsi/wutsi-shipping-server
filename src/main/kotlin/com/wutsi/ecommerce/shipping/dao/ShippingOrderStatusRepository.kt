package com.wutsi.ecommerce.shipping.dao

import com.wutsi.ecommerce.shipping.entity.ShippingOrderEntity
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatusEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ShippingOrderStatusRepository : CrudRepository<ShippingOrderStatusEntity, Long> {
    fun findByShippingOrder(shippingOrder: ShippingOrderEntity): List<ShippingOrderStatusEntity>
}
