package msyc.eati

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<EatiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
