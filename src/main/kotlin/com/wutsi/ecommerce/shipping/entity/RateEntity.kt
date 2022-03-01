package com.wutsi.ecommerce.shipping.entity

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "T_RATE")
data class RateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_fk")
    val shipping: ShippingEntity = ShippingEntity(),

    val country: String = "",
    val cityId: Long? = null,
    var amount: Double = 0.0,
)
