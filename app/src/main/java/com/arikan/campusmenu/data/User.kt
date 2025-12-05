package com.arikan.campusmenu.data

enum class UserRole {
    STUDENT,
    ADMIN
}

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: UserRole,
    val allergens: List<Allergen> = emptyList()
)

object AuthRepository {
    private var currentUser: User? = null
    
    // Örnek kullanıcılar
    private val users = listOf(
        User(1, "Admin", "admin@campus.com", UserRole.ADMIN),
        User(2, "Öğrenci", "ogrenci@campus.com", UserRole.STUDENT)
    )
    
    fun login(email: String, password: String): User? {
        // Basit giriş kontrolü - gerçek uygulamada API'den gelecek
        val user = when {
            email == "admin@campus.com" && password == "admin123" -> users[0]
            email == "ogrenci@campus.com" && password == "123456" -> users[1]
            else -> null
        }
        currentUser = user
        return user
    }
    
    fun getCurrentUser(): User? = currentUser
    
    fun logout() {
        currentUser = null
    }
    
    fun isAdmin(): Boolean = currentUser?.role == UserRole.ADMIN
    
    fun isStudent(): Boolean = currentUser?.role == UserRole.STUDENT
}
