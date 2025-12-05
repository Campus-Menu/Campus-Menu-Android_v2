package com.arikan.campusmenu.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class MenuReview(
    val id: String,
    val menuItemId: Int,
    val menuItemName: String,
    val date: String,
    val studentId: String,
    val studentName: String,
    val rating: Int, // 1-5
    val comment: String,
    val quickFeedback: List<String>, // "Soğuktu", "Tuzluydu", "Porsiyon az"
    val timestamp: String,
    val adminResponse: String? = null,
    val adminResponseTime: String? = null
)

data class MenuItemReview(
    val menuItemId: Int,
    val averageRating: Float,
    val reviewCount: Int,
    val reviews: List<MenuReview>
)

enum class QuickFeedbackOption(val displayName: String) {
    COLD("Soğuktu"),
    SALTY("Tuzluydu"),
    SMALL_PORTION("Porsiyon azdı"),
    BLAND("Tuzsuzdu"),
    DELICIOUS("Çok lezzetliydi"),
    FRESH("Tazeydi")
}

object ReviewRepository {
    private const val FILE_NAME = "reviews.json"
    private val reviews = mutableListOf<MenuReview>()
    private val gson = Gson()
    
    fun initialize(context: Context) {
        loadReviews(context)
    }
    
    private fun loadReviews(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val json = file.readText()
                val type = object : TypeToken<List<MenuReview>>() {}.type
                val loadedReviews: List<MenuReview> = gson.fromJson(json, type)
                reviews.clear()
                reviews.addAll(loadedReviews)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun saveReviews(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            val json = gson.toJson(reviews)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun addReview(
        context: Context,
        menuItemId: Int,
        menuItemName: String,
        date: String,
        studentId: String,
        studentName: String,
        rating: Int,
        comment: String,
        quickFeedback: List<String>
    ) {
        val review = MenuReview(
            id = System.currentTimeMillis().toString(),
            menuItemId = menuItemId,
            menuItemName = menuItemName,
            date = date,
            studentId = studentId,
            studentName = studentName,
            rating = rating,
            comment = comment,
            quickFeedback = quickFeedback,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        reviews.add(review)
        saveReviews(context)
    }
    
    fun addAdminResponse(context: Context, reviewId: String, response: String) {
        val review = reviews.find { it.id == reviewId }
        if (review != null) {
            val index = reviews.indexOf(review)
            reviews[index] = review.copy(
                adminResponse = response,
                adminResponseTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
            saveReviews(context)
        }
    }
    
    fun getReviewsForMenuItem(menuItemId: Int): List<MenuReview> {
        return reviews.filter { it.menuItemId == menuItemId }
    }
    
    fun getMenuItemReviews(context: Context, menuItemId: Int, date: String): MenuItemReview {
        val itemReviews = reviews.filter { it.menuItemId == menuItemId && it.date == date }
        val avgRating = if (itemReviews.isEmpty()) 0f else itemReviews.map { it.rating }.average().toFloat()
        
        return MenuItemReview(
            menuItemId = menuItemId,
            averageRating = avgRating,
            reviewCount = itemReviews.size,
            reviews = itemReviews
        )
    }
    
    fun getReviewsForDate(date: String): List<MenuReview> {
        return reviews.filter { it.date == date }
    }
    
    fun getAllReviews(): List<MenuReview> = reviews.toList()
    
    fun getAverageRating(menuItemId: Int): Float {
        val itemReviews = getReviewsForMenuItem(menuItemId)
        if (itemReviews.isEmpty()) return 0f
        return itemReviews.map { it.rating }.average().toFloat()
    }
    
    fun getReviewCount(menuItemId: Int): Int {
        return getReviewsForMenuItem(menuItemId).size
    }
    
    fun deleteReview(context: Context, reviewId: String) {
        reviews.removeAll { it.id == reviewId }
        saveReviews(context)
    }
}
