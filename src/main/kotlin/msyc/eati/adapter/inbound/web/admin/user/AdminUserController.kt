package msyc.eati.adapter.inbound.web.admin.user

import msyc.eati.adapter.inbound.web.auth.dto.UserResponse
import msyc.eati.domain.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/users")
class AdminUserController(
    private val userService: UserService
) {

    /**
     * 전체 회원 목록 조회
     */
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    /**
     * 회원 단건 조회
     */
    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: String
    ): ResponseEntity<UserResponse> {
        val user = userService.getUser(userId)
        return ResponseEntity.ok(user)
    }
}
