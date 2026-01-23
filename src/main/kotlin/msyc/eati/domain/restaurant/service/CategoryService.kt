package msyc.eati.domain.restaurant.service

import io.github.oshai.kotlinlogging.KotlinLogging
import msyc.eati.adapter.inbound.web.admin.category.dto.CategoryCreateRequest
import msyc.eati.adapter.inbound.web.admin.category.dto.CategoryUpdateRequest
import msyc.eati.adapter.inbound.web.category.dto.CategoryResponse
import msyc.eati.adapter.outbound.persistence.restaurant.CategoryRepository
import msyc.eati.domain.restaurant.model.Category
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    /**
     * 전체 카테고리 목록 조회 (flat)
     */
    fun getAllCategories(): List<CategoryResponse> {
        log.info { "전체 카테고리 목록 조회" }
        return categoryRepository.findAllByDeletedAtIsNull()
            .map { it.toResponse() }
    }

    /**
     * 계층형 카테고리 목록 조회 (트리 구조)
     */
    fun getCategoriesAsTree(): List<CategoryResponse> {
        log.info { "계층형 카테고리 목록 조회" }
        val allCategories = categoryRepository.findAllByDeletedAtIsNull()

        val categoryMap = allCategories.associateBy { it.categoryId }
        val rootCategories = mutableListOf<CategoryResponse>()

        allCategories.forEach { category ->
            if (category.parentId.isNullOrEmpty() || category.parentId == category.categoryId) {
                rootCategories.add(buildTree(category, allCategories))
            }
        }

        return rootCategories
    }

    /**
     * 특정 카테고리의 하위 카테고리 조회
     */
    fun getChildCategories(parentId: String): List<CategoryResponse> {
        log.info { "하위 카테고리 조회: parentId=$parentId" }
        return categoryRepository.findAllByParentIdAndDeletedAtIsNull(parentId)
            .map { it.toResponse() }
    }

    private fun buildTree(category: Category, allCategories: List<Category>): CategoryResponse {
        val children = allCategories
            .filter { it.parentId == category.categoryId && it.categoryId != category.categoryId }
            .map { buildTree(it, allCategories) }

        return CategoryResponse(
            categoryId = category.categoryId!!,
            parentId = category.parentId,
            categoryName = category.categoryName,
            children = children
        )
    }

    private fun Category.toResponse() = CategoryResponse(
        categoryId = this.categoryId!!,
        parentId = this.parentId,
        categoryName = this.categoryName
    )

    /**
     * 카테고리 생성
     */
    @Transactional
    fun createCategory(request: CategoryCreateRequest): CategoryResponse {
        log.info { "카테고리 생성: ${request.categoryName}" }

        val category = Category(
            parentId = request.parentId,
            categoryName = request.categoryName,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedCategory = categoryRepository.save(category)
        return savedCategory.toResponse()
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    fun updateCategory(categoryId: String, request: CategoryUpdateRequest): CategoryResponse {
        log.info { "카테고리 수정: categoryId=$categoryId" }

        val category = categoryRepository.findById(categoryId)
            .orElseThrow { IllegalArgumentException("카테고리를 찾을 수 없습니다: $categoryId") }

        if (category.deletedAt != null) {
            throw IllegalArgumentException("삭제된 카테고리입니다: $categoryId")
        }

        category.parentId = request.parentId
        category.categoryName = request.categoryName

        return category.toResponse()
    }

    /**
     * 카테고리 삭제 (soft delete)
     */
    @Transactional
    fun deleteCategory(categoryId: String) {
        log.info { "카테고리 삭제: categoryId=$categoryId" }

        val category = categoryRepository.findById(categoryId)
            .orElseThrow { IllegalArgumentException("카테고리를 찾을 수 없습니다: $categoryId") }

        if (category.deletedAt != null) {
            throw IllegalArgumentException("이미 삭제된 카테고리입니다: $categoryId")
        }

        category.deletedAt = LocalDateTime.now()
    }
}
