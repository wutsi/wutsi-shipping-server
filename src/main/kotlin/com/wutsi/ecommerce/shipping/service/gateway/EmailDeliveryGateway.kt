package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.catalog.WutsiCatalogApi
import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.ChangeStatusRequest
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.entity.OrderStatus
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.EmailMessageBuilder
import com.wutsi.ecommerce.shipping.service.Gateway
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.mail.WutsiMailApi
import com.wutsi.platform.mail.dto.Party
import com.wutsi.platform.mail.dto.SendMessageRequest
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.entity.ToggleName
import org.springframework.stereotype.Service

@Service
class EmailDeliveryGateway(
    private val orderApi: WutsiOrderApi,
    private val mailApi: WutsiMailApi,
    private val catalogApi: WutsiCatalogApi,
    private val accountApi: WutsiAccountApi,
    private val logger: KVLogger,
) : Gateway {
    override fun enabled(tenant: Tenant): Boolean =
        tenant.toggles.find { it.name == ToggleName.SHIPPING_EMAIL_DELIVERY.name } != null

    /**
     * Accept if any product is numeric
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean {
        val result = request.products.find { it.productType == ProductType.NUMERIC.name } != null

        logger.add("gateway_email", result)
        return result
    }

    override fun onOrderDone(order: Order) {
        val email = order.shippingAddress?.email
            ?: return
        logger.add("recipient_email", email)

        // Digital product to ship
        val products = order.items.map {
            catalogApi.getProduct(it.productId).product
        }.filter { it.type == ProductType.NUMERIC.name }
        logger.add("product_count", products.size)
        if (products.isEmpty())
            return

        changeOrderStatus(order, OrderStatus.IN_TRANSIT)
        send(email, order, products)
        changeOrderStatus(order, OrderStatus.DELIVERED)
    }

    private fun changeOrderStatus(order: Order, status: OrderStatus) =
        orderApi.changeStatus(
            id = order.id,
            request = ChangeStatusRequest(
                status = status.name
            )
        )

    private fun send(email: String, order: Order, products: List<com.wutsi.ecommerce.catalog.dto.Product>) {
        val account = accountApi.getAccount(order.accountId).account
        val recipient = Party(
            email = email,
            displayName = order.shippingAddress!!.firstName + " " + order.shippingAddress!!.lastName
        )
        val messageId = mailApi.sendMessage(
            request = SendMessageRequest(
                recipient = recipient,
                content = EmailMessageBuilder(
                    template = "send",
                    language = account.language,
                    variables = mapOf(
                        "recipientFullName" to recipient.displayName!!,
                        "orderId" to order.id.uppercase().takeLast(4),
                        "products" to products
                    )
                ).build()
            )
        ).id
        logger.add("message_id", messageId)
    }
}
