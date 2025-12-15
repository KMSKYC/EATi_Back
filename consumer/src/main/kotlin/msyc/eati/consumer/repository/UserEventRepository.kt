package msyc.eati.consumer.repository

import msyc.eati.consumer.domain.UserEvent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserEventRepository : MongoRepository<UserEvent, String> {
    fun findByUserId(userId: String): List<UserEvent>
    fun findByEventType(eventType: String): List<UserEvent>
}
