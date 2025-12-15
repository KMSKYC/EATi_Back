package msyc.eati.consumer.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.consumer.domain.UserEventDto
import msyc.eati.consumer.service.UserEventService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class UserEventConsumer(
    private val userEventService: UserEventService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(
        topics = ["\${kafka.topic.user-events}"],
        groupId = "\${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consumeUserEvent(message: String, acknowledgment: Acknowledgment) {
        try {
            logger.info { "Received message: $message" }

            val eventDto = objectMapper.readValue<UserEventDto>(message)
            userEventService.saveEvent(eventDto)

            acknowledgment.acknowledge()
            logger.info { "Successfully processed and acknowledged message" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to process message: $message" }
            // 에러 발생 시에도 acknowledge하여 무한 재시도 방지
            // 실제 운영에서는 DLQ(Dead Letter Queue)로 전송하는 것이 좋습니다
            acknowledgment.acknowledge()
        }
    }
}
