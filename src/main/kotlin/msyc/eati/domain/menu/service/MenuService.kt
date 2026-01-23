package msyc.eati.domain.menu.service

import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.adapter.inbound.web.admin.menu.dto.MenuCreateRequest
import msyc.eati.adapter.inbound.web.admin.menu.dto.MenuUpdateRequest
import msyc.eati.adapter.inbound.web.menu.dto.MenuResponse
import msyc.eati.adapter.outbound.persistence.menu.MenuRepository
import msyc.eati.domain.menu.model.Menu
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class MenuService(
    private val menuRepository: MenuRepository
) {

    /**
     * 전체 메뉴 목록 조회
     */
    fun getAllMenus(): List<MenuResponse> {
        log.info { "전체 메뉴 목록 조회" }
        return menuRepository.findAllByDeletedAtIsNull()
            .map { it.toResponse() }
    }

    /**
     * 카테고리별 메뉴 목록 조회
     */
    fun getMenusByCategory(categoryId: String): List<MenuResponse> {
        log.info { "카테고리별 메뉴 조회: categoryId=$categoryId" }
        return menuRepository.findAllByCategoryIdAndDeletedAtIsNull(categoryId)
            .map { it.toResponse() }
    }

    /**
     * 메뉴 단건 조회
     */
    fun getMenu(menuId: String): MenuResponse {
        val menu = menuRepository.findByMenuIdAndDeletedAtIsNull(menuId)
            ?: throw IllegalArgumentException("메뉴를 찾을 수 없습니다: $menuId")
        return menu.toResponse()
    }

    /**
     * 랜덤 메뉴 조회
     */
    fun getRandomMenu(): MenuResponse {
        val menu = menuRepository.findRandomMenu()
            ?: throw IllegalArgumentException("조회 가능한 메뉴가 없습니다")
        return menu.toResponse()
    }

    private fun Menu.toResponse() = MenuResponse(
        menuId = this.menuId!!,
        categoryId = this.categoryId!!,
        menuName = this.menuName ?: "",
        description = this.description,
        imageUrl = this.imageUrl
    )

    /**
     * 메뉴 생성
     */
    @Transactional
    fun createMenu(request: MenuCreateRequest): MenuResponse {
        log.info { "메뉴 생성: ${request.menuName}" }

        val menu = Menu(
            categoryId = request.categoryId,
            menuName = request.menuName,
            description = request.description,
            imageUrl = request.imageUrl,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedMenu = menuRepository.save(menu)
        return savedMenu.toResponse()
    }

    /**
     * 메뉴 수정
     */
    @Transactional
    fun updateMenu(menuId: String, request: MenuUpdateRequest): MenuResponse {
        log.info { "메뉴 수정: menuId=$menuId" }

        val menu = menuRepository.findById(menuId)
            .orElseThrow { IllegalArgumentException("메뉴를 찾을 수 없습니다: $menuId") }

        if (menu.deletedAt != null) {
            throw IllegalArgumentException("삭제된 메뉴입니다: $menuId")
        }

        menu.categoryId = request.categoryId
        menu.menuName = request.menuName
        menu.description = request.description
        menu.imageUrl = request.imageUrl

        return menu.toResponse()
    }

    /**
     * 메뉴 삭제 (soft delete)
     */
    @Transactional
    fun deleteMenu(menuId: String) {
        log.info { "메뉴 삭제: menuId=$menuId" }

        val menu = menuRepository.findById(menuId)
            .orElseThrow { IllegalArgumentException("메뉴를 찾을 수 없습니다: $menuId") }

        if (menu.deletedAt != null) {
            throw IllegalArgumentException("이미 삭제된 메뉴입니다: $menuId")
        }

        menu.deletedAt = LocalDateTime.now()
    }
}
