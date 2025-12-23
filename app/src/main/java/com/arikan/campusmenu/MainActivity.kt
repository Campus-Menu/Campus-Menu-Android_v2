package com.arikan.campusmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.arikan.campusmenu.data.StudentRepository
import com.arikan.campusmenu.ui.screens.AnnouncementsScreen
import com.arikan.campusmenu.ui.screens.CalendarScreen
import com.arikan.campusmenu.ui.screens.FavoritesScreen
import com.arikan.campusmenu.ui.screens.HomeScreen
import com.arikan.campusmenu.ui.screens.ProfileScreen
import com.arikan.campusmenu.ui.theme.CampusMenuTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Kaydedilmiş dili uygula
        val savedLanguage = com.arikan.campusmenu.data.ThemePreferences.getLanguage(this)
        com.arikan.campusmenu.data.LocaleHelper.setLocale(this, savedLanguage)
        
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var themeUpdateTrigger by remember { mutableStateOf(0) }
            var languageVersion by remember { mutableStateOf(0) }
            
            val isDarkMode = remember(themeUpdateTrigger) { 
                com.arikan.campusmenu.data.ThemePreferences.isDarkMode(context)
            }
            val themeName = remember(themeUpdateTrigger) {
                com.arikan.campusmenu.data.ThemePreferences.getTheme(context)
            }
            
            CampusMenuTheme(
                darkTheme = isDarkMode,
                themeName = themeName
            ) {
                StudentRepository.initialize(context)
                com.arikan.campusmenu.data.FavoritesRepository.initialize(context)
                
                CampusMenuApp(
                    languageVersion = languageVersion,
                    onThemeChanged = {
                        themeUpdateTrigger++
                    },
                    onLanguageChanged = {
                        languageVersion++
                    }
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun CampusMenuApp(
    languageVersion: Int = 0,
    onThemeChanged: () -> Unit = {},
    onLanguageChanged: () -> Unit = {}
) {
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    
    var currentDestination by rememberSaveable { 
        mutableStateOf(AppDestinations.HOME) 
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            // Tüm menüler
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME -> HomeScreen(
                selectedDate = selectedDate,
                languageVersion = languageVersion
            )
            AppDestinations.CALENDAR -> CalendarScreen(
                languageVersion = languageVersion,
                onDateSelected = { date ->
                    selectedDate = date
                    currentDestination = AppDestinations.HOME
                }
            )
            AppDestinations.ANNOUNCEMENTS -> AnnouncementsScreen(
                languageVersion = languageVersion
            )
            AppDestinations.FAVORITES -> FavoritesScreen(
                languageVersion = languageVersion
            )
            AppDestinations.PROFILE -> ProfileScreen(
                languageVersion = languageVersion,
                onThemeChanged = onThemeChanged,
                onLanguageChanged = onLanguageChanged
            )
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Ana Sayfa", Icons.Default.Home),
    CALENDAR("Takvim", Icons.Default.CalendarMonth),
    ANNOUNCEMENTS("Duyurular", Icons.Default.Announcement),
    FAVORITES("Favoriler", Icons.Default.Favorite),
    PROFILE("Profil", Icons.Default.AccountBox),
}