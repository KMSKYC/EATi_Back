package msyc.eati.consumer.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user_events")
data class UserEvent(
    @Id
    val id: String? = null,
    val userId: String,
    val eventType: String,
    val eventData: Map<String, Any>,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val metadata: Map<String, String>? = null
)

data class UserEventDto(
    val userId: String,
    val eventType: String,
    val eventData: Map<String, Any>,
    val metadata: Map<String, String>? = null
)
