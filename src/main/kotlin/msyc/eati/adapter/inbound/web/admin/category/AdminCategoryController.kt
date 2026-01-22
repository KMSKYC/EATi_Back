package msyc.eati.adapter.inbound.web.admin.category

import jakarta.validation.Valid
import msyc.eati.adapter.inbound.web.admin.category.dto.CategoryCreateRequest
import msyc.eati.adapter.inbound.web.admin.category.dto.CategoryUpdateRequest
import msyc.eati.adapter.inbound.web.category.dto.CategoryResponse
import msyc.eati.domain.restaurant.service.CategoryService
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
@RequestMapping("/api/admin/categories")
class AdminCategoryController(
    private val categoryService: CategoryService
) {

    /**
     * 카테고리 등록
     */
    @PostMapping
    fun createCategory(
        @Valid @RequestBody request: CategoryCreateRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(category)
    }

    /**
     * 카테고리 수정
     */
    @PutMapping("/{categoryId}")
    fun updateCategory(
        @PathVariable categoryId: String,
        @Valid @RequestBody request: CategoryUpdateRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.updateCategory(categoryId, request)
        return ResponseEntity.ok(category)
    }

    /**
     * 카테고리 삭제
     */
    @DeleteMapping("/{categoryId}")
    fun deleteCategory(
        @PathVariable categoryId: String
    ): ResponseEntity<Void> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity.noContent().build()
    }
}
