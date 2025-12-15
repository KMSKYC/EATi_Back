package msyc.eati.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class UserEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    @Value("\${kafka.topic.user-events}")
    private lateinit var topic: String

    fun sendEvent(userId: String, eventType: String, eventData: Map<String, Any>, metadata: Map<String, String>? = null) {
        try {
            val event = mapOf(
                "userId" to userId,
                "eventType" to eventType,
                "eventData" to eventData,
                "metadata" to metadata
            )

            val message = objectMapper.writeValueAsString(event)
            kafkaTemplate.send(topic, userId, message)
                .whenComplete { result, ex ->
                    if (ex == null) {
                        logger.debug { "Event sent successfully: userId=$userId, eventType=$eventType, offset=${result?.recordMetadata?.offset()}" }
                    } else {
                        logger.error(ex) { "Failed to send event: userId=$userId, eventType=$eventType" }
                    }
                }
        } catch (e: Exception) {
            logger.error(e) { "Error sending event: userId=$userId, eventType=$eventType" }
        }
    }

    fun sendEventSync(userId: String, eventType: String, eventData: Map<String, Any>, metadata: Map<String, String>? = null) {
        try {
            val event = mapOf(
                "userId" to userId,
                "eventType" to eventType,
                "eventData" to eventData,
                "metadata" to metadata
            )

            val message = objectMapper.writeValueAsString(event)
            val result = kafkaTemplate.send(topic, userId, message).get()
            logger.debug { "Event sent successfully: userId=$userId, eventType=$eventType, offset=${result.recordMetadata.offset()}" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to send event: userId=$userId, eventType=$eventType" }
            throw e
        }
    }
}
