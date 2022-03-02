package com.wutsi.ecommerce.shipping.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_SHIPPING")
data class ShippingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val tenantId: Long = -1,
    val accountId: Long = -1,

    @Enumerated
    val type: ShippingType = ShippingType.LOCAL_DELIVERY,

    var message: String? = null,
    var enabled: Boolean = true,
    var deliveryTime: Int? = null,
    var rate: Double? = null,
    var currency: String = "",
    var country: String? = null,
    var cityId: Long? = null,

    val created: OffsetDateTime = OffsetDateTime.now(),
    val updated: OffsetDateTime = OffsetDateTime.now(),
)
