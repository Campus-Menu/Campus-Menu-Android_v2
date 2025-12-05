package com.arikan.campusmenu.data

enum class Allergen(val displayName: String) {
    GLUTEN("Gluten"),
    DAIRY("Süt"),
    EGGS("Yumurta"),
    NUTS("Fındık/Fıstık"),
    SEAFOOD("Deniz Ürünleri"),
    SOY("Soya"),
    SESAME("Susam")
}

data class MenuItem(
    val id: Int,
    val name: String,
    val category: MenuCategory,
    val calories: Int,
    val price: Double,
    val description: String,
    val isAvailable: Boolean = true,
    val rating: Float = 0f,
    val allergens: List<Allergen> = emptyList()
)

enum class MenuCategory(val displayName: String, val key: String) {
    SOUP("Çorbalar", "soups"),
    MAIN_COURSE("Ana Yemekler", "main_courses"),
    SIDE_DISH("Yan Yemekler", "side_dishes"),
    SALAD("Salatalar", "salads"),
    DESSERT("Tatlılar", "desserts"),
    BEVERAGE("İçecekler", "beverages");
    
    fun getLocalizedName(context: android.content.Context): String {
        return LocaleHelper.getLocalizedString(context, key)
    }
}

object MenuRepository {
    fun getTodayMenu(): List<MenuItem> = listOf(
        MenuItem(
            id = 1,
            name = "Mercimek Çorbası",
            category = MenuCategory.SOUP,
            calories = 180,
            price = 15.0,
            description = "Geleneksel kırmızı mercimek çorbası",
            rating = 4.5f
        ),
        MenuItem(
            id = 2,
            name = "Tavuk Şinitzel",
            category = MenuCategory.MAIN_COURSE,
            calories = 450,
            price = 65.0,
            description = "Kızarmış tavuk şinitzel, patates garnitürü ile",
            rating = 4.8f
        ),
        MenuItem(
            id = 3,
            name = "Makarna",
            category = MenuCategory.MAIN_COURSE,
            calories = 380,
            price = 45.0,
            description = "Domates soslu makarna",
            rating = 4.2f
        ),
        MenuItem(
            id = 4,
            name = "Pilav",
            category = MenuCategory.SIDE_DISH,
            calories = 250,
            price = 20.0,
            description = "Tereyağlı pirinç pilavı",
            rating = 4.3f
        ),
        MenuItem(
            id = 5,
            name = "Patates Kızartması",
            category = MenuCategory.SIDE_DISH,
            calories = 320,
            price = 25.0,
            description = "Çıtır patates kızartması",
            rating = 4.6f
        ),
        MenuItem(
            id = 6,
            name = "Mevsim Salata",
            category = MenuCategory.SALAD,
            calories = 85,
            price = 30.0,
            description = "Taze mevsim yeşillikleri",
            rating = 4.4f
        ),
        MenuItem(
            id = 7,
            name = "Sütlaç",
            category = MenuCategory.DESSERT,
            calories = 220,
            price = 35.0,
            description = "Fırın sütlaç",
            rating = 4.7f
        ),
        MenuItem(
            id = 8,
            name = "Ayran",
            category = MenuCategory.BEVERAGE,
            calories = 70,
            price = 10.0,
            description = "Soğuk ayran",
            rating = 4.9f
        ),
        MenuItem(
            id = 9,
            name = "Kola",
            category = MenuCategory.BEVERAGE,
            calories = 140,
            price = 20.0,
            description = "Soğuk kola (330ml)",
            rating = 4.1f
        )
    )
    
    fun getMenuByCategory(category: MenuCategory): List<MenuItem> {
        return getTodayMenu().filter { it.category == category }
    }
}
