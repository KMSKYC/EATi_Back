package msyc.eati.adapter.inbound.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @GetMapping("/health")
    fun health(): Map<String, String> {
        return mapOf("status" to "OK")
    }
}