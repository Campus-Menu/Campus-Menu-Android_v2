package com.arikan.campusmenu.data

import java.time.LocalDate
import kotlin.random.Random

data class DailyMenu(
    val date: LocalDate,
    val items: List<MenuItem>
)

object MenuHistoryRepository {
    private val menuHistory = mutableMapOf<LocalDate, List<MenuItem>>()
    
    init {
        // 260 günlük menü oluştur (geçmiş ve gelecek)
        val today = LocalDate.now()
        // Geçmiş 130 gün
        for (i in 0..129) {
            val date = today.minusDays(i.toLong())
            menuHistory[date] = generateMenuForDate(date)
        }
        // Gelecek 130 gün
        for (i in 1..130) {
            val date = today.plusDays(i.toLong())
            menuHistory[date] = generateMenuForDate(date)
        }
    }
    
    private fun generateMenuForDate(date: LocalDate): List<MenuItem> {
        val dayOfMonth = date.dayOfMonth
        
        // Yemekler ve içerdikleri alerjenler
        data class MenuItemData(val name: String, val allergens: List<Allergen>)
        
        val baseMenus = listOf(
            // Çorbalar
            listOf(
                MenuItemData("Mercimek Çorbası", listOf()),
                MenuItemData("Tavuk Suyu Çorbası", listOf()),
                MenuItemData("Ezogelin Çorbası", listOf(Allergen.GLUTEN)),
                MenuItemData("Yayla Çorbası", listOf(Allergen.EGGS)),
                MenuItemData("Domates Çorbası", listOf()),
                MenuItemData("Tarhana Çorbası", listOf(Allergen.GLUTEN)),
                MenuItemData("Kelle Paça Çorbası", listOf()),
                MenuItemData("İşkembe Çorbası", listOf()),
                MenuItemData("Mantar Çorbası", listOf()),
                MenuItemData("Sebze Çorbası", listOf())
            ),
            // Ana Yemekler
            listOf(
                MenuItemData("Tavuk Şinitzel", listOf(Allergen.GLUTEN, Allergen.EGGS)),
                MenuItemData("Köfte", listOf(Allergen.GLUTEN, Allergen.EGGS)),
                MenuItemData("Makarna", listOf(Allergen.GLUTEN, Allergen.EGGS)),
                MenuItemData("Tavuk Döner", listOf(Allergen.GLUTEN)),
                MenuItemData("Izgara Tavuk", listOf()),
                MenuItemData("Mantı", listOf(Allergen.GLUTEN, Allergen.EGGS, Allergen.DAIRY)),
                MenuItemData("Karnıyarık", listOf()),
                MenuItemData("İmam Bayıldı", listOf()),
                MenuItemData("Etli Kuru Fasulye", listOf()),
                MenuItemData("Nohut Yemeği", listOf()),
                MenuItemData("Patlıcan Musakka", listOf()),
                MenuItemData("Sebzeli Tavuk", listOf()),
                MenuItemData("Balık", listOf(Allergen.SEAFOOD)),
                MenuItemData("Köri Soslu Tavuk", listOf()),
                MenuItemData("Güveç", listOf())
            ),
            // Yan Yemekler
            listOf(
                MenuItemData("Pilav", listOf()),
                MenuItemData("Patates Kızartması", listOf()),
                MenuItemData("Bulgur Pilavı", listOf(Allergen.GLUTEN)),
                MenuItemData("Makarna", listOf(Allergen.GLUTEN, Allergen.EGGS)),
                MenuItemData("Fırın Patates", listOf()),
                MenuItemData("Püreli Patates", listOf(Allergen.DAIRY)),
                MenuItemData("Haşlanmış Sebze", listOf()),
                MenuItemData("Soslu Makarna", listOf(Allergen.GLUTEN))
            ),
            // Salatalar
            listOf(
                MenuItemData("Mevsim Salata", listOf()),
                MenuItemData("Çoban Salata", listOf()),
                MenuItemData("Yeşil Salata", listOf()),
                MenuItemData("Rus Salatası", listOf(Allergen.EGGS)),
                MenuItemData("Akdeniz Salata", listOf()),
                MenuItemData("Ton Balıklı Salata", listOf(Allergen.SEAFOOD)),
                MenuItemData("Kırmızı Lahana Salatası", listOf())
            ),
            // Tatlılar
            listOf(
                MenuItemData("Sütlaç", listOf(Allergen.DAIRY)),
                MenuItemData("Kazandibi", listOf(Allergen.DAIRY, Allergen.EGGS)),
                MenuItemData("Aşure", listOf(Allergen.NUTS)),
                MenuItemData("Muhallebi", listOf(Allergen.DAIRY)),
                MenuItemData("Revani", listOf(Allergen.GLUTEN, Allergen.EGGS, Allergen.DAIRY)),
                MenuItemData("Şekerpare", listOf(Allergen.GLUTEN, Allergen.EGGS)),
                MenuItemData("Tulumba Tatlısı", listOf(Allergen.GLUTEN)),
                MenuItemData("Baklava", listOf(Allergen.GLUTEN, Allergen.NUTS)),
                MenuItemData("Künefe", listOf(Allergen.GLUTEN, Allergen.DAIRY)),
                MenuItemData("Keşkül", listOf(Allergen.DAIRY, Allergen.NUTS)),
                MenuItemData("Meyve", listOf())
            ),
            // İçecekler
            listOf(
                MenuItemData("Ayran", listOf(Allergen.DAIRY)),
                MenuItemData("Kola", listOf()),
                MenuItemData("Su", listOf()),
                MenuItemData("Meyve Suyu", listOf()),
                MenuItemData("Çay", listOf()),
                MenuItemData("Kahve", listOf()),
                MenuItemData("Limonata", listOf())
            )
        )
        
        val selectedItems = mutableListOf<MenuItem>()
        var idCounter = 1
        
        // Her kategoriden bir öğe seç
        baseMenus.forEachIndexed { categoryIndex, items ->
            val itemIndex = (dayOfMonth + categoryIndex) % items.size
            val menuItemData = items[itemIndex]
            val category = MenuCategory.entries[categoryIndex]
            
            selectedItems.add(
                MenuItem(
                    id = idCounter++,
                    name = menuItemData.name,
                    category = category,
                    calories = when(category) {
                        MenuCategory.SOUP -> Random.nextInt(150, 201)
                        MenuCategory.MAIN_COURSE -> Random.nextInt(400, 601)
                        MenuCategory.SIDE_DISH -> Random.nextInt(200, 351)
                        MenuCategory.SALAD -> Random.nextInt(50, 121)
                        MenuCategory.DESSERT -> Random.nextInt(180, 281)
                        MenuCategory.BEVERAGE -> Random.nextInt(50, 151)
                    },
                    price = when(category) {
                        MenuCategory.SOUP -> 15.0
                        MenuCategory.MAIN_COURSE -> Random.nextDouble(55.0, 75.0)
                        MenuCategory.SIDE_DISH -> Random.nextDouble(20.0, 30.0)
                        MenuCategory.SALAD -> 30.0
                        MenuCategory.DESSERT -> 35.0
                        MenuCategory.BEVERAGE -> Random.nextDouble(10.0, 20.0)
                    },
                    description = "Günün özel ${menuItemData.name} menüsü",
                    rating = Random.nextDouble(3.5, 5.0).toFloat(),
                    isAvailable = Random.nextInt(100) > 20,
                    allergens = menuItemData.allergens
                )
            )
        }
        
        return selectedItems
    }
    
    fun getMenuForDate(date: LocalDate): List<MenuItem> {
        return menuHistory[date] ?: emptyList()
    }
    
    fun getTodayMenu(): List<MenuItem> {
        return getMenuForDate(LocalDate.now())
    }
    
    fun getAllDates(): List<LocalDate> {
        return menuHistory.keys.sortedDescending()
    }
    
    fun updateMenuForDate(date: LocalDate, items: List<MenuItem>) {
        menuHistory[date] = items
    }
}
