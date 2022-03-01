package com.wutsi.ecommerce.shipping.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.UpdateShippingRequest
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/UpdateShippingController.sql"])
class UpdateShippingControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var dao: ShippingRepository

    @Test
    fun update() {
        val url = "http://localhost:$port/v1/shippings/100"
        val request = UpdateShippingRequest(
            message = "Hello world",
            enabled = true
        )
        val response = rest.postForEntity(url, request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val shipping = dao.findById(100).get()
        assertEquals(request.message, shipping.message)
        assertEquals(request.enabled, shipping.enabled)
    }

    @Test
    fun notFound() {
        val url = "http://localhost:$port/v1/shippings/9999"
        val request = UpdateShippingRequest(
            message = "Hello world",
            enabled = true
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, Any::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.SHIPPING_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun badUser() {
        val url = "http://localhost:$port/v1/shippings/120"
        val request = UpdateShippingRequest(
            message = "Hello world",
            enabled = true
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, Any::class.java)
        }

        assertEquals(403, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.ILLEGAL_SHIPPING_ACCESS.urn, response.error.code)
    }

    @Test
    fun badTenant() {
        val url = "http://localhost:$port/v1/shippings/200"
        val request = UpdateShippingRequest(
            message = "Hello world",
            enabled = true
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, Any::class.java)
        }

        assertEquals(403, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.ILLEGAL_TENANT_ACCESS.urn, response.error.code)
    }
}
