package com.wutsi.ecommerce.shipping.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "T_SHIPPING_ORDER_STATUS")
data class ShippingOrderStatusEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_fk")
    val shippingOrder: ShippingOrderEntity = ShippingOrderEntity(),

    val previousStatus: ShippingOrderStatus? = null,
    val status: ShippingOrderStatus = ShippingOrderStatus.CREATED,
    val reason: String? = null,
    val comment: String? = null,
    val created: OffsetDateTime = OffsetDateTime.now(),
)
