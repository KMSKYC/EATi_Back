package msyc.eati.domain.user.model

import jakarta.persistence.*
import msyc.eati.common.util.IdGenerator
import java.time.LocalDate
import java.time.OffsetDateTime

/**
 * 사용자 엔티티
 * - 회원가입 정보, 인증 정보 저장
 * - Spring Security 인증에 사용됨
 */
@Entity
@Table(name = "users", schema = "webapp")
data class User(
    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    var userId: String? = null,

    @Column(nullable = false)
    var userPassword: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var status: String,

    @Column(nullable = false)
    var nickname: String,

    @Column
    var birthdate: LocalDate? = null,

    @Column(nullable = false)
    var gender: String,

    @Column
    var region: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var userRole: UserRole = UserRole.USER,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime? = null,

    @Column(name = "deleted_at")
    var deletedAt: OffsetDateTime? = null,

    @Column(name = "last_login_at")
    var lastLoginAt: OffsetDateTime? = null
) {
    /**
     * 엔티티 저장 전 실행
     * - userId 자동 생성 (10자리 랜덤 문자열)
     * - createdAt, updatedAt 자동 설정
     */
    @PrePersist
    fun prePersist() {
        if (userId == null) userId = IdGenerator.generate()
        val now = OffsetDateTime.now()
        if (createdAt == null) createdAt = now
        if (updatedAt == null) updatedAt = now
    }

    /**
     * 엔티티 업데이트 전 실행
     * - updatedAt 자동 갱신
     */
    @PreUpdate
    fun preUpdate() {
        updatedAt = OffsetDateTime.now()
    }
}
