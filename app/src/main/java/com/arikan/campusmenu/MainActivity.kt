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
import com.arikan.campusmenu.data.Allergen
import com.arikan.campusmenu.data.AuthRepository
import com.arikan.campusmenu.data.StudentRepository
import com.arikan.campusmenu.data.User
import com.arikan.campusmenu.data.UserRole
import com.arikan.campusmenu.ui.screens.AdminMenuScreen
import com.arikan.campusmenu.ui.screens.AnnouncementsScreen
import com.arikan.campusmenu.ui.screens.CalendarScreen
import com.arikan.campusmenu.ui.screens.FavoritesScreen
import com.arikan.campusmenu.ui.screens.HomeScreen
import com.arikan.campusmenu.ui.screens.LoginScreen
import com.arikan.campusmenu.ui.screens.ProfileScreen
import com.arikan.campusmenu.ui.screens.RoleSelectionScreen
import com.arikan.campusmenu.ui.screens.StudentLoginScreen
import com.arikan.campusmenu.ui.screens.StudentRegisterScreen
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
                
                var currentUser by rememberSaveable { mutableStateOf<User?>(null) }
                var showRoleSelection by rememberSaveable { mutableStateOf(true) }
                var showAdminLogin by rememberSaveable { mutableStateOf(false) }
                var showStudentLogin by rememberSaveable { mutableStateOf(false) }
                var showStudentRegister by rememberSaveable { mutableStateOf(false) }
                
                when {
                    currentUser != null -> {
                        CampusMenuApp(
                            user = currentUser!!,
                            languageVersion = languageVersion,
                            onLogout = { 
                                AuthRepository.logout()
                                currentUser = null
                                showRoleSelection = true
                                showAdminLogin = false
                                showStudentLogin = false
                                showStudentRegister = false
                            },
                            onThemeChanged = {
                                themeUpdateTrigger++
                            },
                            onLanguageChanged = {
                                languageVersion++
                            }
                        )
                    }
                    showStudentRegister -> {
                        StudentRegisterScreen(
                            onRegisterSuccess = {
                                showStudentRegister = false
                                showStudentLogin = true
                            },
                            onBackToLogin = {
                                showStudentRegister = false
                                showStudentLogin = true
                            }
                        )
                    }
                    showAdminLogin -> {
                        LoginScreen(
                            onLoginSuccess = { user -> 
                                currentUser = user
                                showAdminLogin = false
                            },
                            onNavigateToRegister = { 
                                showAdminLogin = false
                                showRoleSelection = true
                            }
                        )
                    }
                    showStudentLogin -> {
                        StudentLoginScreen(
                            onLoginSuccess = { user -> 
                                currentUser = user
                                showStudentLogin = false
                            },
                            onNavigateToRegister = {
                                showStudentLogin = false
                                showStudentRegister = true
                            },
                            onBackToRoleSelection = {
                                showStudentLogin = false
                                showRoleSelection = true
                            }
                        )
                    }
                    else -> {
                        RoleSelectionScreen(
                            onAdminSelected = {
                                showRoleSelection = false
                                showAdminLogin = true
                            },
                            onStudentSelected = {
                                showRoleSelection = false
                                showStudentLogin = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun CampusMenuApp(
    user: User,
    languageVersion: Int = 0,
    onLogout: () -> Unit = {},
    onThemeChanged: () -> Unit = {},
    onLanguageChanged: () -> Unit = {}
) {
    val isAdmin = user.role == UserRole.ADMIN
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var currentUser by remember { mutableStateOf(user) }
    
    var currentDestination by rememberSaveable { 
        mutableStateOf(if (isAdmin) AppDestinations.ADMIN else AppDestinations.HOME) 
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            if (isAdmin) {
                // Admin menüsü
                item(
                    icon = {
                        Icon(
                            AppDestinations.ADMIN.icon,
                            contentDescription = AppDestinations.ADMIN.label
                        )
                    },
                    label = { Text(AppDestinations.ADMIN.label) },
                    selected = currentDestination == AppDestinations.ADMIN,
                    onClick = { currentDestination = AppDestinations.ADMIN }
                )
            }
            
            // Ortak menüler
            AppDestinations.entries.filter { 
                it != AppDestinations.ADMIN 
            }.forEach {
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
                user = currentUser,
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
                user = currentUser,
                languageVersion = languageVersion
            )
            AppDestinations.FAVORITES -> FavoritesScreen(
                user = currentUser,
                languageVersion = languageVersion
            )
            AppDestinations.PROFILE -> ProfileScreen(
                user = currentUser,
                languageVersion = languageVersion,
                onLogout = onLogout,
                onThemeChanged = onThemeChanged,
                onLanguageChanged = onLanguageChanged,
                onAllergyUpdate = { allergens ->
                    currentUser = currentUser.copy(allergens = allergens)
                }
            )
            AppDestinations.ADMIN -> {
                if (isAdmin) {
                    AdminMenuScreen()
                } else {
                    HomeScreen(selectedDate = selectedDate, user = currentUser)
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    ADMIN("Yönetim", Icons.Default.Dashboard),
    HOME("Ana Sayfa", Icons.Default.Home),
    CALENDAR("Takvim", Icons.Default.CalendarMonth),
    ANNOUNCEMENTS("Duyurular", Icons.Default.Announcement),
    FAVORITES("Favoriler", Icons.Default.Favorite),
    PROFILE("Profil", Icons.Default.AccountBox),
}