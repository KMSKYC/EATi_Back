package msyc.eati.adapter.`in`.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @GetMapping("/", produces = ["text/html"])
    fun index(): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>EATi</title>
                <style>
                    body {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    }
                    .container {
                        text-align: center;
                        color: white;
                    }
                    h1 {
                        font-size: 3em;
                        margin: 0;
                    }
                    p {
                        font-size: 1.5em;
                        margin-top: 20px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🍽️ EATi</h1>
                    <p>Welcome to EATi API Server!</p>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    @GetMapping("/health")
    fun health(): Map<String, String> {
        return mapOf("status" to "OK")
    }
}
