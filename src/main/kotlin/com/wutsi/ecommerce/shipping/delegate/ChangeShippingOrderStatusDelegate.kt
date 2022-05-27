package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.dao.ShippingOrderStatusRepository
import com.wutsi.ecommerce.shipping.dto.ChangeStatusRequest
import com.wutsi.ecommerce.shipping.entity.ShippingOrderEntity
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatusEntity
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.ecommerce.shipping.event.EventURN
import com.wutsi.ecommerce.shipping.event.ShippingOrderEventPayload
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.core.stream.EventStream
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.AbstractPersistable_
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
public class ChangeShippingOrderStatusDelegate(
    private val shippingOrderDao: ShippingOrderRepository,
    private val statusDao: ShippingOrderStatusRepository,
    private val securityManager: SecurityManager,
    private val eventStream: EventStream,
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ChangeShippingOrderStatusDelegate::class.java)
    }

    fun invoke(id: Long, request: ChangeStatusRequest) {
        val shippingOrder = shippingOrderDao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.SHIPPING_ORDER_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH
                        )
                    )
                )
            }
        securityManager.checkTenant(shippingOrder)

        if (request.status.equals(shippingOrder.status.name, true))
            return
        else if (request.status.equals(ShippingOrderStatus.READY_FOR_PICKUP.name, true))
            readyForPickup(shippingOrder, request)
        else if (request.status.equals(ShippingOrderStatus.DELIVERED.name, true))
            delivered(shippingOrder, request)
        else if (request.status.equals(ShippingOrderStatus.IN_TRANSIT.name, true))
            inTransit(shippingOrder, request)
        else if (request.status.equals(ShippingOrderStatus.DELIVERED.name, true))
            delivered(shippingOrder, request)
        else if (request.status.equals(ShippingOrderStatus.CANCELLED.name, true))
            cancel(shippingOrder, request)
        else
            invalidStatus(shippingOrder.status, request)
    }

    private fun readyForPickup(shippingOrder: ShippingOrderEntity, request: ChangeStatusRequest) {
        if (shippingOrder.status == ShippingOrderStatus.DELIVERED || shippingOrder.status == ShippingOrderStatus.CANCELLED)
            throw invalidStatus(shippingOrder.status, request)

        changeStatus(shippingOrder, request, EventURN.SHIPPING_READY_FOR_PICKUP)
    }

    private fun delivered(shippingOrder: ShippingOrderEntity, request: ChangeStatusRequest) {
        if (shippingOrder.status != ShippingOrderStatus.READY_FOR_PICKUP && shippingOrder.status != ShippingOrderStatus.IN_TRANSIT)
            throw invalidStatus(shippingOrder.status, request)

        changeStatus(shippingOrder, request, EventURN.SHIPPING_DELIVERED)
    }

    private fun inTransit(shippingOrder: ShippingOrderEntity, request: ChangeStatusRequest) {
        if (shippingOrder.status != ShippingOrderStatus.CREATED)
            throw invalidStatus(shippingOrder.status, request)

        changeStatus(shippingOrder, request, EventURN.SHIPPING_IN_TRANSIT)
    }

    private fun cancel(shippingOrder: ShippingOrderEntity, request: ChangeStatusRequest) {
        if (shippingOrder.status == ShippingOrderStatus.DELIVERED)
            throw invalidStatus(shippingOrder.status, request)

        changeStatus(shippingOrder, request, EventURN.SHIPPING_CANCELLED)
    }

    private fun changeStatus(shippingOrder: ShippingOrderEntity, request: ChangeStatusRequest, event: EventURN?) {
        val now = OffsetDateTime.now()

        val previousStatus = shippingOrder.status
        shippingOrder.status = ShippingOrderStatus.valueOf(request.status.uppercase())
        shippingOrderDao.save(shippingOrder)

        statusDao.save(
            ShippingOrderStatusEntity(
                shippingOrder = shippingOrder,
                status = shippingOrder.status,
                reason = request.reason,
                created = now,
                previousStatus = previousStatus,
                comment = request.comment
            )
        )

        if (event != null)
            publish(shippingOrder, event)
    }

    private fun publish(shippingOrder: ShippingOrderEntity, event: EventURN) {
        try {
            eventStream.publish(event.urn, ShippingOrderEventPayload(shippingOrder.id ?: -1))
        } catch (ex: Exception) {
            LOGGER.warn("Unable to push event", ex)
        }
    }

    private fun invalidStatus(status: ShippingOrderStatus, request: ChangeStatusRequest) =
        ConflictException(
            error = Error(
                code = ErrorURN.ILLEGAL_STATUS.urn,
                data = mapOf(
                    "status" to status,
                    "request_status" to request.status
                ),
                parameter = Parameter(
                    name = "id",
                    value = AbstractPersistable_.id,
                    type = ParameterType.PARAMETER_TYPE_PATH,
                ),
            )
        )
}
