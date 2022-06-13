package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.CreateShippingRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingResponse
import com.wutsi.ecommerce.shipping.entity.ShippingType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateShippingControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var dao: ShippingRepository

    @Test
    fun invoke() {
        val url = "http://localhost:$port/v1/shippings"
        val request = CreateShippingRequest(
            type = ShippingType.LOCAL_DELIVERY.name,
            message = "Hello world",
            country = "CM",
            cityId = 111,
            street = "3030 linton",
            zipCode = "11111"
        )
        val response = rest.postForEntity(url, request, CreateShippingResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val shipping = dao.findById(response.body!!.id).get()
        assertEquals(request.type, shipping.type.name)
        assertEquals(request.message, shipping.message)
        assertEquals(true, shipping.enabled)
        assertEquals(USER_ID, shipping.accountId)
        assertEquals(TENANT_ID, shipping.tenantId)
        assertEquals(request.country, shipping.country)
        assertEquals(request.cityId, shipping.cityId)
        assertEquals(request.zipCode, shipping.zipCode)
        assertEquals(request.street, shipping.street)
    }
}
