package com.wutsi.ecommerce.shipping.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.SubjectType.USER
import com.wutsi.platform.core.security.spring.SpringAuthorizationRequestInterceptor
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.core.test.TestRSAKeyProvider
import com.wutsi.platform.core.test.TestTokenProvider
import com.wutsi.platform.core.test.TestTracingContext
import com.wutsi.platform.core.tracing.spring.SpringTracingRequestInterceptor
import com.wutsi.platform.core.util.URN
import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.dto.GetTenantResponse
import com.wutsi.platform.tenant.dto.Logo
import com.wutsi.platform.tenant.dto.MobileCarrier
import com.wutsi.platform.tenant.dto.PhonePrefix
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.dto.Toggle
import com.wutsi.platform.tenant.entity.ToggleName
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.client.RestTemplate

abstract class AbstractSecuredController {
    companion object {
        const val TENANT_ID = 1L
        const val USER_ID = 111L
    }

    @MockBean
    protected lateinit var tenantApi: WutsiTenantApi

    protected lateinit var rest: RestTemplate

    @BeforeEach
    open fun setUp() {
        val tenant = Tenant(
            id = 1,
            name = "test",
            logos = listOf(
                Logo(type = "PICTORIAL", url = "http://www.goole.com/images/1.png")
            ),
            countries = listOf("CM"),
            languages = listOf("en", "fr"),
            currency = "XAF",
            domainName = "www.wutsi.com",
            mobileCarriers = listOf(
                MobileCarrier(
                    code = "mtn",
                    name = "MTN",
                    countries = listOf("CM"),
                    phonePrefixes = listOf(
                        PhonePrefix(
                            country = "CM",
                            prefixes = listOf("+2379")
                        ),
                    ),
                    logos = listOf(
                        Logo(type = "PICTORIAL", url = "http://www.goole.com/images/mtn.png")
                    )
                ),
                MobileCarrier(
                    code = "orange",
                    name = "ORANGE",
                    countries = listOf("CM"),
                    phonePrefixes = listOf(
                        PhonePrefix(
                            country = "CM",
                            prefixes = listOf("+237")
                        ),
                    ),
                    logos = listOf(
                        Logo(type = "PICTORIAL", url = "http://www.goole.com/images/orange.png")
                    )
                )
            ),
            toggles = listOf(
                Toggle(name = ToggleName.SHIPPING_LOCAL_PICKUP.name),
                Toggle(name = ToggleName.SHIPPING_LOCAL_DELIVERY.name),
                Toggle(name = ToggleName.SHIPPING_INTERNATIONAL_DELIVERY.name),
                Toggle(name = ToggleName.SHIPPING_EMAIL_DELIVERY.name)
            )
        )
        doReturn(GetTenantResponse(tenant)).whenever(tenantApi).getTenant(any())

        rest = createResTemplate()
    }

    protected fun createResTemplate(
        scope: List<String> = listOf("shipping-read", "shipping-manage"),
        subjectId: Long = USER_ID,
        subjectType: SubjectType = USER,
        admin: Boolean = false,
        tenantId: Long = TENANT_ID
    ): RestTemplate {
        val rest = RestTemplate()

        val tokenProvider = TestTokenProvider(
            JWTBuilder(
                subject = subjectId.toString(),
                name = URN.of("user", subjectId.toString()).value,
                subjectType = subjectType,
                scope = scope,
                keyProvider = TestRSAKeyProvider(),
                admin = admin,
            ).build()
        )

        rest.interceptors.add(SpringTracingRequestInterceptor(TestTracingContext(tenantId = tenantId.toString())))
        rest.interceptors.add(SpringAuthorizationRequestInterceptor(tokenProvider))
        return rest
    }
}
