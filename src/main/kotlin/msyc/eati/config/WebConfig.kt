package msyc.eati.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Web MVC 설정
 */
@Configuration
class WebConfig : WebMvcConfigurer {

    /**
     * CORS 설정
     * - 프론트엔드에서 API 호출 시 CORS 오류 방지
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",      // 로컬 개발환경
                "http://localhost:5173",      // Vite 기본 포트
                "http://134.185.96.179",      // OCI 서버
                "https://134.185.96.179"      // HTTPS
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}