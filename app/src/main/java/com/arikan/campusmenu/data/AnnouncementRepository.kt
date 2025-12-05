package com.arikan.campusmenu.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val type: AnnouncementType,
    val timestamp: String,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now().plusDays(1),
    val isImportant: Boolean = false,
    val isActive: Boolean = true
)

enum class AnnouncementType(val displayName: String) {
    CLOSURE("Kapalı"),
    HOLIDAY("Tatil"),
    MAINTENANCE("Bakım"),
    GENERAL("Genel")
}

data class CrowdStatus(
    val level: CrowdLevel,
    val message: String,
    val timestamp: String
)

enum class CrowdLevel(val displayName: String, val color: String) {
    EMPTY("Boş", "#2ecc71"),
    NORMAL("Normal", "#3498db"),
    CROWDED("Kalabalık", "#f39c12"),
    VERY_CROWDED("Çok Kalabalık", "#e74c3c")
}

object AnnouncementRepository {
    private const val FILE_NAME = "announcements.json"
    private const val CROWD_FILE = "crowd_status.json"
    private val announcements = mutableListOf<Announcement>()
    private var currentCrowdStatus: CrowdStatus = CrowdStatus(
        level = CrowdLevel.NORMAL,
        message = "Yemekhane normal yoğunlukta",
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
    private val gson = Gson()
    
    fun initialize(context: Context) {
        loadAnnouncements(context)
        loadCrowdStatus(context)
    }
    
    private fun loadAnnouncements(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val json = file.readText()
                val type = object : TypeToken<List<Announcement>>() {}.type
                val loaded: List<Announcement> = gson.fromJson(json, type)
                announcements.clear()
                announcements.addAll(loaded)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun saveAnnouncements(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            val json = gson.toJson(announcements)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun loadCrowdStatus(context: Context) {
        try {
            val file = File(context.filesDir, CROWD_FILE)
            if (file.exists()) {
                val json = file.readText()
                currentCrowdStatus = gson.fromJson(json, CrowdStatus::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun saveCrowdStatus(context: Context) {
        try {
            val file = File(context.filesDir, CROWD_FILE)
            val json = gson.toJson(currentCrowdStatus)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun addAnnouncement(
        context: Context,
        title: String,
        message: String,
        type: AnnouncementType
    ) {
        val announcement = Announcement(
            id = System.currentTimeMillis().toString(),
            title = title,
            message = message,
            type = type,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        announcements.add(0, announcement)
        saveAnnouncements(context)
    }
    
    fun removeAnnouncement(context: Context, id: String) {
        announcements.removeAll { it.id == id }
        saveAnnouncements(context)
    }
    
    fun updateCrowdStatus(context: Context, level: CrowdLevel, message: String) {
        currentCrowdStatus = CrowdStatus(
            level = level,
            message = message,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        saveCrowdStatus(context)
    }
    
    fun getActiveAnnouncements(context: Context): List<Announcement> {
        // İlk çağrıda örnek veriler ekle
        if (announcements.isEmpty()) {
            addSampleAnnouncements(context)
        }
        return announcements.filter { it.isActive }
    }
    
    private fun addSampleAnnouncements(context: Context) {
        val today = LocalDate.now()
        
        announcements.addAll(listOf(
            Announcement(
                id = "1",
                title = "Hafta Sonu Kapalı",
                message = "Kafeterya bu hafta sonu bakım çalışmaları nedeniyle kapalı olacaktır. Pazartesi günü normal hizmet saatlerimizde tekrar açılacağız.",
                type = AnnouncementType.CLOSURE,
                startDate = today,
                endDate = today.plusDays(2),
                isImportant = true,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ),
            Announcement(
                id = "2",
                title = "Yeni Menü Eklendi",
                message = "Bu hafta vegan ve vejeteryan seçeneklerimizi genişlettik. Ana yemek bölümünden yeni lezzetlerimizi keşfedin!",
                type = AnnouncementType.GENERAL,
                startDate = today,
                endDate = today.plusDays(7),
                isImportant = false,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ),
            Announcement(
                id = "3",
                title = "Bayram Tatili",
                message = "Ramazan Bayramı nedeniyle kafeterya ${today.plusDays(10).format(DateTimeFormatter.ofPattern("dd MMMM"))} - ${today.plusDays(13).format(DateTimeFormatter.ofPattern("dd MMMM"))} tarihleri arasında kapalı olacaktır.",
                type = AnnouncementType.HOLIDAY,
                startDate = today.plusDays(10),
                endDate = today.plusDays(13),
                isImportant = true,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ),
            Announcement(
                id = "4",
                title = "Öğle Saatleri Yoğun",
                message = "12:00-13:00 saatleri arasında kafeterya çok yoğun oluyor. Kuyruklardan kaçınmak için 11:30 veya 13:30 saatlerini tercih edebilirsiniz.",
                type = AnnouncementType.GENERAL,
                startDate = today,
                endDate = today.plusDays(30),
                isImportant = false,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        ))
        saveAnnouncements(context)
    }
    
    fun getAllAnnouncements(): List<Announcement> = announcements.toList()
    
    fun getCrowdStatus(): CrowdStatus = currentCrowdStatus
}
