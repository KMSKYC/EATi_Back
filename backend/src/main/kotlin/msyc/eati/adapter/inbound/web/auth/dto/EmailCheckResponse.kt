package msyc.eati.adapter.inbound.web.auth.dto

data class EmailCheckResponse(
    val email: String,
    val available: Boolean,
    val message: String
)
