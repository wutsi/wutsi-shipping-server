package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.dao.RateRepository
import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.SaveRateRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SaveRateController.sql"])
class SaveRateControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var shippingDao: ShippingRepository

    @Autowired
    private lateinit var dao: RateRepository

    @Test
    fun add() {
        val url = "http://localhost:$port/v1/rates"
        val request = SaveRateRequest(
            shippingId = 100L,
            country = "CA",
            cityId = 111,
            amount = 150000.0
        )
        val response = rest.postForEntity(url, request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val shipping = shippingDao.findById(100).get()
        val rates = dao.findByShipping(shipping)

        assertEquals(1, rates.size)
        assertEquals(request.shippingId, rates[0].shipping.id)
        assertEquals(request.cityId, rates[0].cityId)
        assertEquals(request.country, rates[0].country)
        assertEquals(request.amount, rates[0].amount)
    }

    @Test
    fun update() {
        val url = "http://localhost:$port/v1/rates"
        val request = SaveRateRequest(
            shippingId = 200L,
            country = "FR",
            cityId = null,
            amount = 150000.0
        )
        val response = rest.postForEntity(url, request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val shipping = shippingDao.findById(200).get()
        val rates = dao.findByShipping(shipping)

        assertEquals(2, rates.size)
        assertEquals(request.shippingId, rates[0].shipping.id)
        assertEquals(null, rates[0].cityId)
        assertEquals("*", rates[0].country)
        assertEquals(20000.0, rates[0].amount)

        assertEquals(request.shippingId, rates[1].shipping.id)
        assertEquals(request.cityId, rates[1].cityId)
        assertEquals(request.country, rates[1].country)
        assertEquals(request.amount, rates[1].amount)
    }
}
