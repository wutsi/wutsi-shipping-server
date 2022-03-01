package com.wutsi.ecommerce.shipping.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.security.feign.FeignAuthorizationRequestInterceptor
import com.wutsi.platform.core.tracing.feign.FeignTracingRequestInterceptor
import com.wutsi.platform.tenant.WutsiTenantApi
import com.wutsi.platform.tenant.WutsiTenantApiBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

@Configuration
class TenantApiConfiguration(
    private val authorizationRequestInterceptor: FeignAuthorizationRequestInterceptor,
    private val tracingRequestInterceptor: FeignTracingRequestInterceptor,
    private val mapper: ObjectMapper,
    private val env: Environment
) {
    @Bean
    fun tenantApi(): WutsiTenantApi =
        WutsiTenantApiBuilder().build(
            env = environment(),
            mapper = mapper,
            interceptors = listOf(
                tracingRequestInterceptor,
                authorizationRequestInterceptor
            )
        )

    private fun environment(): com.wutsi.platform.tenant.Environment =
        if (env.acceptsProfiles(Profiles.of("prod")))
            com.wutsi.platform.tenant.Environment.PRODUCTION
        else
            com.wutsi.platform.tenant.Environment.SANDBOX
}
