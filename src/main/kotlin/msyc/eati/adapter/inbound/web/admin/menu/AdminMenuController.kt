package msyc.eati.adapter.inbound.web.admin.menu

import jakarta.validation.Valid
import msyc.eati.adapter.inbound.web.admin.menu.dto.MenuCreateRequest
import msyc.eati.adapter.inbound.web.admin.menu.dto.MenuUpdateRequest
import msyc.eati.adapter.inbound.web.menu.dto.MenuResponse
import msyc.eati.domain.menu.service.MenuService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/menus")
class AdminMenuController(
    private val menuService: MenuService
) {

    /**
     * 메뉴 등록
     */
    @PostMapping
    fun createMenu(
        @Valid @RequestBody request: MenuCreateRequest
    ): ResponseEntity<MenuResponse> {
        val menu = menuService.createMenu(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(menu)
    }

    /**
     * 메뉴 수정
     */
    @PutMapping("/{menuId}")
    fun updateMenu(
        @PathVariable menuId: String,
        @Valid @RequestBody request: MenuUpdateRequest
    ): ResponseEntity<MenuResponse> {
        val menu = menuService.updateMenu(menuId, request)
        return ResponseEntity.ok(menu)
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    fun deleteMenu(
        @PathVariable menuId: String
    ): ResponseEntity<Void> {
        menuService.deleteMenu(menuId)
        return ResponseEntity.noContent().build()
    }
}
