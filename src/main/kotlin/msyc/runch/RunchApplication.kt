package msyc.runch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
	DataSourceAutoConfiguration::class,
	HibernateJpaAutoConfiguration::class,
	R2dbcAutoConfiguration::class,
	RedisAutoConfiguration::class,
	SecurityAutoConfiguration::class,
	ReactiveSecurityAutoConfiguration::class,
	ManagementWebSecurityAutoConfiguration::class,
	ReactiveManagementWebSecurityAutoConfiguration::class
])
class RunchApplication

fun main(args: Array<String>) {
	runApplication<RunchApplication>(*args)
}
