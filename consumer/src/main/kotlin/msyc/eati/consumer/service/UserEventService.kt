package msyc.eati.consumer.service

import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.consumer.domain.UserEvent
import msyc.eati.consumer.domain.UserEventDto
import msyc.eati.consumer.repository.UserEventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Service
class UserEventService(
    private val userEventRepository: UserEventRepository
) {
    fun saveEvent(eventDto: UserEventDto) {
        try {
            val event = UserEvent(
                userId = eventDto.userId,
                eventType = eventDto.eventType,
                eventData = eventDto.eventData,
                timestamp = LocalDateTime.now(),
                metadata = eventDto.metadata
            )

            userEventRepository.save(event)
            logger.info { "Successfully saved user event: userId=${event.userId}, eventType=${event.eventType}" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to save user event: userId=${eventDto.userId}, eventType=${eventDto.eventType}" }
            throw e
        }
    }
}
