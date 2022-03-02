package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.shipping.dto.Product
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.dto.SearchRateResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchRateController.sql"])
public class SearchRateControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun invoke() {
        val url = "http://localhost:$port/v1/rates/search"
        val request = SearchRateRequest(
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
    }

    @Test
    public fun noRate() {
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
