package com.wutsi.ecommerce.shipping.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.ecommerce.shipping.dto.GetShippingResponse
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetShippingController.sql"])
public class GetShippingControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun get() {
        val response = rest.getForEntity("http://localhost:$port/v1/shippings/100", GetShippingResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val shipping = response.body!!.shipping
        assertEquals(100, shipping.id)
        assertEquals(true, shipping.enabled)
        assertEquals(ShippingType.LOCAL_PICKUP.name, shipping.type)
        assertEquals("Local Pickup", shipping.message)
        assertEquals("CM", shipping.country)
        assertEquals(1111L, shipping.cityId)
        assertEquals(30000.0, shipping.rate)
        assertEquals("XAF", shipping.currency)
    }

    @Test
    fun notFound() {
        val url = "http://localhost:$port/v1/shippings/9999"
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, GetShippingResponse::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.SHIPPING_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun badTenant() {
        val url = "http://localhost:$port/v1/shippings/200"
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, GetShippingResponse::class.java)
        }

        assertEquals(403, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.ILLEGAL_TENANT_ACCESS.urn, response.error.code)
    }
}
