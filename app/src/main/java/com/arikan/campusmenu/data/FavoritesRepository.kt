package com.arikan.campusmenu.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

data class FavoriteItem(
    val userId: Int,
    val menuItemId: Int,
    val menuItemName: String,
    val timestamp: String
)

object FavoritesRepository {
    private const val FILE_NAME = "favorites.json"
    private val favorites = mutableListOf<FavoriteItem>()
    private val gson = Gson()
    
    fun initialize(context: Context) {
        loadFavorites(context)
    }
    
    private fun loadFavorites(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val json = file.readText()
                val type = object : TypeToken<List<FavoriteItem>>() {}.type
                val loadedFavorites: List<FavoriteItem> = gson.fromJson(json, type)
                favorites.clear()
                favorites.addAll(loadedFavorites)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun saveFavorites(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            val json = gson.toJson(favorites)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun addFavorite(context: Context, userId: Int, menuItemId: Int, menuItemName: String) {
        if (!isFavorite(userId, menuItemId)) {
            val favorite = FavoriteItem(
                userId = userId,
                menuItemId = menuItemId,
                menuItemName = menuItemName,
                timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
            favorites.add(favorite)
            saveFavorites(context)
        }
    }
    
    fun removeFavorite(context: Context, userId: Int, menuItemId: Int) {
        favorites.removeAll { it.userId == userId && it.menuItemId == menuItemId }
        saveFavorites(context)
    }
    
    fun isFavorite(userId: Int, menuItemId: Int): Boolean {
        return favorites.any { it.userId == userId && it.menuItemId == menuItemId }
    }
    
    fun getUserFavorites(userId: Int): List<FavoriteItem> {
        return favorites.filter { it.userId == userId }
    }
    
    fun toggleFavorite(context: Context, userId: Int, menuItemId: Int, menuItemName: String) {
        if (isFavorite(userId, menuItemId)) {
            removeFavorite(context, userId, menuItemId)
        } else {
            addFavorite(context, userId, menuItemId, menuItemName)
        }
    }
}
