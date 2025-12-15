package msyc.eati.adapter.inbound.web.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)
