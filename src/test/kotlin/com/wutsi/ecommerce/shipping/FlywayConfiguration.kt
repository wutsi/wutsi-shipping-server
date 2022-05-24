package com.wutsi.ecommerce.shipping

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.`annotation`.Bean
import org.springframework.context.`annotation`.Configuration
import kotlin.Boolean

@Configuration
public class FlywayConfiguration {
    @Bean
    public fun flywayMigrationStrategy(): FlywayMigrationStrategy = FlywayMigrationStrategy {
            flyway ->
        if (!cleaned) {
            flyway.clean()
            cleaned = true
        }
        flyway.migrate()
    }

    public companion object {
        public var cleaned: Boolean = false
    }
}
