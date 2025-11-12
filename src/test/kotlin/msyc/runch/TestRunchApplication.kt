package msyc.runch

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<RunchApplication>().with(TestcontainersConfiguration::class).run(*args)
}
