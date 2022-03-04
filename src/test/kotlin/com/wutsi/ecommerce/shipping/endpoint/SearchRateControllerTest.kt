package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.shipping.dto.Product
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.dto.SearchRateResponse
import com.wutsi.ecommerce.shipping.entity.ShippingType
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchRateController.sql"])
class SearchRateControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun search() {
        val url = "http://localhost:$port/v1/rates/search"
        val request = SearchRateRequest(
            accountId = USER_ID,
            country = "CM",
            cityId = 1000,
            products = listOf(
                Product(productId = 1L, productType = ProductType.NUMERIC.name),
                Product(productId = 2L, productType = ProductType.PHYSICAL.name),
                Product(productId = 3L, productType = ProductType.PHYSICAL.name),
            )
        )
        val response = rest.postForEntity(url, request, SearchRateResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val rates = response.body!!.rates
        assertEquals(2, rates.size)

        assertEquals(100, rates[0].shippingId)
        assertEquals(12, rates[0].deliveryTime)
        assertEquals(0.0, rates[0].rate)
        assertEquals("XAF", rates[0].currency)
        assertEquals(ShippingType.LOCAL_PICKUP.name, rates[0].shippingType)

        assertEquals(101, rates[1].shippingId)
        assertEquals(24, rates[1].deliveryTime)
        assertEquals(1500.0, rates[1].rate)
        assertEquals("XAF", rates[1].currency)
        assertEquals(ShippingType.LOCAL_DELIVERY.name, rates[1].shippingType)
    }

    @Test
    fun searchByShippingId() {
        val url = "http://localhost:$port/v1/rates/search"
        val request = SearchRateRequest(
            shippingId = 101L,
            accountId = USER_ID,
            country = "CM",
            cityId = 1000,
            products = listOf(
                Product(productId = 1L, productType = ProductType.NUMERIC.name),
                Product(productId = 2L, productType = ProductType.PHYSICAL.name),
                Product(productId = 3L, productType = ProductType.PHYSICAL.name),
            )
        )
        val response = rest.postForEntity(url, request, SearchRateResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val rates = response.body!!.rates
        assertEquals(1, rates.size)

        assertEquals(101, rates[0].shippingId)
        assertEquals(24, rates[0].deliveryTime)
        assertEquals(1500.0, rates[0].rate)
        assertEquals("XAF", rates[0].currency)
        assertEquals(ShippingType.LOCAL_DELIVERY.name, rates[0].shippingType)
    }

    @Test
    fun noRate() {
        val url = "http://localhost:$port/v1/rates/search"
        val request = SearchRateRequest(
            country = "US",
            cityId = null,
            products = listOf(
                Product(productId = 1L, productType = ProductType.NUMERIC.name),
                Product(productId = 2L, productType = ProductType.PHYSICAL.name),
                Product(productId = 3L, productType = ProductType.PHYSICAL.name),
            )
        )
        val response = rest.postForEntity(url, request, SearchRateResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val rates = response.body!!.rates
        assertEquals(0, rates.size)
    }
}
