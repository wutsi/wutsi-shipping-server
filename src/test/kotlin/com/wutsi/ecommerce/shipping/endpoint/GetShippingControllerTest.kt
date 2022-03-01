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
        assertEquals(ShippingType.LOCAL_DELIVERY.name, shipping.type)
        assertEquals("Yo Man", shipping.message)

        assertEquals(2, shipping.rates.size)

        assertEquals(1001L, shipping.rates[0].id)
        assertEquals(null, shipping.rates[0].cityId)
        assertEquals("*", shipping.rates[0].country)
        assertEquals(20000.0, shipping.rates[0].amount)

        assertEquals(1002L, shipping.rates[1].id)
        assertEquals(1111L, shipping.rates[1].cityId)
        assertEquals("FR", shipping.rates[1].country)
        assertEquals(25000.0, shipping.rates[1].amount)
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
