package com.wutsi.ecommerce.shipping.endpoint

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChangeShippingOrderStatusControllerTest {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun invoke() {
    }
}
