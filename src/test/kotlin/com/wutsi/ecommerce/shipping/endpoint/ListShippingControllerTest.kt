package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.dto.ListShippingResponse
import com.wutsi.ecommerce.shipping.entity.ShippingType
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/ListShippingController.sql"])
public class ListShippingControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun invoke() {
        val url = "http://localhost:$port/v1/shippings"
        val response = rest.getForEntity(url, ListShippingResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val shippings = response.body!!.shippings
        assertEquals(2, shippings.size)
        assertEquals(100, shippings[0].id)
        assertEquals(true, shippings[0].enabled)
        assertEquals(ShippingType.LOCAL_PICKUP.name, shippings[0].type)

        assertEquals(101, shippings[1].id)
        assertEquals(false, shippings[1].enabled)
        assertEquals(ShippingType.EMAIL_DELIVERY.name, shippings[1].type)
    }
}
