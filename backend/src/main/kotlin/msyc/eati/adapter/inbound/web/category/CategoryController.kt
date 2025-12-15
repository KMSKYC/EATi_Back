package msyc.eati.adapter.inbound.web.category

import msyc.eati.adapter.inbound.web.category.dto.CategoryResponse
import msyc.eati.domain.restaurant.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    /**
     * 카테고리 목록 조회
     */
    @GetMapping
    fun getCategories(
        @RequestParam(defaultValue = "false") tree: Boolean
    ): ResponseEntity<List<CategoryResponse>> {
        val categories = if (tree) {
            categoryService.getCategoriesAsTree()
        } else {
            categoryService.getAllCategories()
        }
        return ResponseEntity.ok(categories)
    }

    /**
     * 특정 카테고리의 하위 카테고리 조회
     */
    @GetMapping("/{parentId}/children")
    fun getChildCategories(
        @PathVariable parentId: String
    ): ResponseEntity<List<CategoryResponse>> {
        val children = categoryService.getChildCategories(parentId)
        return ResponseEntity.ok(children)
    }
}
