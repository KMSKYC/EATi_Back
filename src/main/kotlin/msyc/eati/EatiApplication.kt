package msyc.eati

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
	RedisAutoConfiguration::class,
	SecurityAutoConfiguration::class,               // 커스텀 Security 설정 사용 (SecurityConfig)
	ReactiveSecurityAutoConfiguration::class,
	ManagementWebSecurityAutoConfiguration::class,
	ReactiveManagementWebSecurityAutoConfiguration::class
])
class EatiApplication

fun main(args: Array<String>) {
	runApplication<EatiApplication>(*args)
}
