package com.arikan.campusmenu.data

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun setLocale(activity: Activity, languageName: String) {
        val locale = when (languageName) {
            "English" -> Locale.ENGLISH
            "Deutsch" -> Locale.GERMAN
            "Français" -> Locale.FRENCH
            else -> Locale("tr", "TR") // Türkçe
        }
        
        Locale.setDefault(locale)
        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    }
    
    fun getLocalizedString(context: Context, key: String): String {
        val language = ThemePreferences.getLanguage(context)
        return when (language) {
            "English" -> getEnglishString(key)
            "Deutsch" -> getGermanString(key)
            "Français" -> getFrenchString(key)
            else -> getTurkishString(key) // Türkçe (Varsayılan)
        }
    }
    
    private fun getTurkishString(key: String): String = when (key) {
        "profile" -> "Profil"
        "home" -> "Ana Sayfa"
        "favorites" -> "Favoriler"
        "calendar" -> "Takvim"
        "announcements" -> "Duyurular"
        "admin_panel" -> "Yönetim Paneli"
        "settings" -> "Ayarlar"
        "notifications" -> "Bildirimler"
        "about" -> "Hakkında"
        "help_support" -> "Yardım & Destek"
        "logout" -> "Çıkış Yap"
        "dark_mode" -> "Karanlık Mod"
        "language" -> "Dil"
        "theme" -> "Tema"
        "app_settings" -> "Uygulama Ayarları"
        "notification_prefs" -> "Bildirim Tercihleri"
        "daily_menu" -> "Günlük Menü"
        "crowding_warnings" -> "Yoğunluk Uyarıları"
        "allergies" -> "Alerjilerim"
        "add_allergy_info" -> "Alerji bilgisi ekle"
        "allergies_selected" -> "alerji seçili"
        
        // Home Screen
        "todays_menu" -> "ABÜN Yemekhane Menüsü"
        "no_menu" -> "Bugün için menü bulunmuyor"
        "soup" -> "Çorba"
        "main_course" -> "Ana Yemek"
        "side_dish" -> "Yan Yemek"
        "extra" -> "Ekstra"
        "add_to_favorites" -> "Favorilere Ekle"
        "remove_from_favorites" -> "Favorilerden Çıkar"
        "rate_menu" -> "Menüyü Değerlendir"
        "reviews" -> "Değerlendirmeler"
        "no_reviews" -> "Henüz değerlendirme yok"
        "stars" -> "yıldız"
        "admin_response" -> "Yönetici Yanıtı"
        
        // Calendar Screen
        "select_date" -> "Tarih Seçin"
        "menu_for" -> "ABÜN Yemekhane Menüsü"
        
        // Favorites Screen
        "my_favorites" -> "Favorilerim"
        "no_favorites" -> "Favori yemek bulunmuyor"
        "add_favorites_hint" -> "Ana sayfadan yemekleri favorilere ekleyebilirsiniz"
        
        // Admin Menu Screen
        "menu_management" -> "Menü Yönetimi"
        "add_menu_item" -> "Yeni Yemek Ekle"
        "edit" -> "Düzenle"
        "delete" -> "Sil"
        "confirm_delete" -> "Silmek istediğinizden emin misiniz?"
        "cancel" -> "İptal"
        "save" -> "Kaydet"
        "menu_name" -> "Yemek Adı"
        "category" -> "Kategori"
        "price" -> "Fiyat"
        "calories" -> "Kalori"
        "available" -> "Mevcut"
        "unavailable" -> "Mevcut Değil"
        
        // Announcements
        "add_announcement" -> "Duyuru Ekle"
        "announcement_title" -> "Başlık"
        "announcement_message" -> "Mesaj"
        "announcement_type" -> "Duyuru Tipi"
        "general" -> "Genel"
        "closure" -> "Kapalı"
        "holiday" -> "Tatil"
        "maintenance" -> "Bakım"
        "important" -> "ÖNEMLİ"
        "no_announcements" -> "Duyuru bulunmuyor"
        "all" -> "Tümü"
        "add" -> "Ekle"
        "delete_announcement" -> "Duyuruyu Sil"
        "delete_announcement_confirm" -> "Bu duyuruyu silmek istediğinizden emin misiniz?"
        
        // Login
        "welcome" -> "Hoş Geldiniz"
        "email" -> "E-posta"
        "password" -> "Şifre"
        "login" -> "Giriş Yap"
        "register" -> "Kayıt Ol"
        "student_number" -> "Öğrenci Numarası"
        "name" -> "Ad"
        "confirm_password" -> "Şifreyi Onayla"
        "select_role" -> "Rol Seçin"
        "admin" -> "Yönetici"
        "student" -> "Öğrenci"
        
        // Categories
        "soups" -> "Çorbalar"
        "main_courses" -> "Ana Yemekler"
        "side_dishes" -> "Yan Yemekler"
        "salads" -> "Salatalar"
        "desserts" -> "Tatlılar"
        "beverages" -> "İçecekler"
        
        else -> key
    }
    
    private fun getEnglishString(key: String): String = when (key) {
        "profile" -> "Profile"
        "home" -> "Home"
        "favorites" -> "Favorites"
        "calendar" -> "Calendar"
        "announcements" -> "Announcements"
        "admin_panel" -> "Admin Panel"
        "settings" -> "Settings"
        "notifications" -> "Notifications"
        "about" -> "About"
        "help_support" -> "Help & Support"
        "logout" -> "Logout"
        "dark_mode" -> "Dark Mode"
        "language" -> "Language"
        "theme" -> "Theme"
        "app_settings" -> "App Settings"
        "notification_prefs" -> "Notification Preferences"
        "daily_menu" -> "Daily Menu"
        "crowding_warnings" -> "Crowding Warnings"
        "allergies" -> "My Allergies"
        "add_allergy_info" -> "Add allergy information"
        "allergies_selected" -> "allergies selected"
        
        // Home Screen
        "todays_menu" -> "ABUN Cafeteria Menu"
        "no_menu" -> "No menu available for today"
        "soup" -> "Soup"
        "main_course" -> "Main Course"
        "side_dish" -> "Side Dish"
        "extra" -> "Extra"
        "add_to_favorites" -> "Add to Favorites"
        "remove_from_favorites" -> "Remove from Favorites"
        "rate_menu" -> "Rate Menu"
        "reviews" -> "Reviews"
        "no_reviews" -> "No reviews yet"
        "stars" -> "stars"
        "admin_response" -> "Admin Response"
        
        // Calendar Screen
        "select_date" -> "Select Date"
        "menu_for" -> "ABUN Cafeteria Menu"
        
        // Favorites Screen
        "my_favorites" -> "My Favorites"
        "no_favorites" -> "No favorite meals"
        "add_favorites_hint" -> "You can add meals to favorites from the home page"
        
        // Admin Menu Screen
        "menu_management" -> "Menu Management"
        "add_menu_item" -> "Add New Meal"
        "edit" -> "Edit"
        "delete" -> "Delete"
        "confirm_delete" -> "Are you sure you want to delete?"
        "cancel" -> "Cancel"
        "save" -> "Save"
        "menu_name" -> "Meal Name"
        "category" -> "Category"
        "price" -> "Price"
        "calories" -> "Calories"
        "available" -> "Available"
        "unavailable" -> "Unavailable"
        
        // Announcements
        "add_announcement" -> "Add Announcement"
        "announcement_title" -> "Title"
        "announcement_message" -> "Message"
        "announcement_type" -> "Announcement Type"
        "general" -> "General"
        "closure" -> "Closure"
        "holiday" -> "Holiday"
        "maintenance" -> "Maintenance"
        "important" -> "IMPORTANT"
        "no_announcements" -> "No announcements"
        "all" -> "All"
        "add" -> "Add"
        "delete_announcement" -> "Delete Announcement"
        "delete_announcement_confirm" -> "Are you sure you want to delete this announcement?"
        
        // Login
        "welcome" -> "Welcome"
        "email" -> "Email"
        "password" -> "Password"
        "login" -> "Login"
        "register" -> "Register"
        "student_number" -> "Student Number"
        "name" -> "Name"
        "confirm_password" -> "Confirm Password"
        "select_role" -> "Select Role"
        "admin" -> "Admin"
        "student" -> "Student"
        
        // Categories
        "soups" -> "Soups"
        "main_courses" -> "Main Courses"
        "side_dishes" -> "Side Dishes"
        "salads" -> "Salads"
        "desserts" -> "Desserts"
        "beverages" -> "Beverages"
        
        else -> key
    }
    
    private fun getGermanString(key: String): String = when (key) {
        "profile" -> "Profil"
        "home" -> "Startseite"
        "favorites" -> "Favoriten"
        "calendar" -> "Kalender"
        "announcements" -> "Ankündigungen"
        "admin_panel" -> "Verwaltung"
        "settings" -> "Einstellungen"
        "notifications" -> "Benachrichtigungen"
        "about" -> "Über"
        "help_support" -> "Hilfe & Support"
        "logout" -> "Abmelden"
        "dark_mode" -> "Dunkler Modus"
        "language" -> "Sprache"
        "theme" -> "Thema"
        "app_settings" -> "App-Einstellungen"
        "notification_prefs" -> "Benachrichtigungseinstellungen"
        "daily_menu" -> "Tagesmenü"
        "crowding_warnings" -> "Überlastungswarnungen"
        "allergies" -> "Meine Allergien"
        "add_allergy_info" -> "Allergie-Info hinzufügen"
        "allergies_selected" -> "Allergien ausgewählt"
        else -> key
    }
    
    private fun getFrenchString(key: String): String = when (key) {
        "profile" -> "Profil"
        "home" -> "Accueil"
        "favorites" -> "Favoris"
        "calendar" -> "Calendrier"
        "announcements" -> "Annonces"
        "admin_panel" -> "Panneau d'administration"
        "settings" -> "Paramètres"
        "notifications" -> "Notifications"
        "about" -> "À propos"
        "help_support" -> "Aide & Support"
        "logout" -> "Déconnexion"
        "dark_mode" -> "Mode Sombre"
        "language" -> "Langue"
        "theme" -> "Thème"
        "app_settings" -> "Paramètres de l'application"
        "notification_prefs" -> "Préférences de notification"
        "daily_menu" -> "Menu du jour"
        "crowding_warnings" -> "Alertes d'affluence"
        "allergies" -> "Mes allergies"
        "add_allergy_info" -> "Ajouter info allergie"
        "allergies_selected" -> "allergies sélectionnées"
        else -> key
    }
}
