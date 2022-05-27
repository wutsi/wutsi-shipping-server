package com.wutsi.ecommerce.shipping.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "T_SHIPPING_ORDER")
data class ShippingOrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val tenantId: Long = -1,
    val accountId: Long = -1,
    val merchantId: Long = -1,
    val orderId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_fk")
    val shipping: ShippingEntity = ShippingEntity(),

    @Enumerated
    var status: ShippingOrderStatus = ShippingOrderStatus.CREATED,

    val created: OffsetDateTime = OffsetDateTime.now(),
    val updated: OffsetDateTime = OffsetDateTime.now(),
)
