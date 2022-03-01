package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.dao.RateRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/DeleteRateController.sql"])
public class DeleteRateControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Autowired
    private lateinit var dao: RateRepository

    @Test
    public fun invoke() {
        val url = "http://localhost:$port/v1/rates/1001"
        rest.delete(url)

        val rate = dao.findById(1001)
        assertTrue(rate.isEmpty)
    }
}
