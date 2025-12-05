package com.arikan.campusmenu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arikan.campusmenu.data.Allergen
import com.arikan.campusmenu.data.User
import com.arikan.campusmenu.data.UserRole
import com.arikan.campusmenu.data.LocaleHelper
import com.arikan.campusmenu.ui.theme.CampusMenuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User? = null,
    languageVersion: Int = 0,
    onLogout: () -> Unit = {},
    onThemeChanged: () -> Unit = {},
    onLanguageChanged: () -> Unit = {},
    onAllergyUpdate: (List<Allergen>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val currentUser = user ?: User(0, "Kullanıcı", "user@email.com", UserRole.STUDENT)
    
    var languageUpdateTrigger by remember { mutableStateOf(0) }
    
    // Tema ayarı
    var isDarkMode by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.isDarkMode(context)) 
    }
    
    var showAllergyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "profile"),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2C3E50)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Profil Başlığı
            Surface(
                color = if (currentUser.role == UserRole.ADMIN)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = if (currentUser.role == UserRole.ADMIN)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (currentUser.role == UserRole.ADMIN)
                                    Icons.Default.Shield
                                else
                                    Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = if (currentUser.role == UserRole.ADMIN)
                                    MaterialTheme.colorScheme.onError
                                else
                                    MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = currentUser.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = currentUser.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Rol Badge
                    Surface(
                        color = if (currentUser.role == UserRole.ADMIN)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = if (currentUser.role == UserRole.ADMIN) "Admin" else "Öğrenci",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (currentUser.role == UserRole.ADMIN)
                                MaterialTheme.colorScheme.onError
                            else
                                MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Menü Seçenekleri
            // Alerji Seçimi (sadece öğrenciler için)
            if (currentUser.role == UserRole.STUDENT) {
                ProfileMenuItem(
                    icon = Icons.Default.HealthAndSafety,
                    title = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "allergies"),
                    subtitle = if (currentUser.allergens.isEmpty()) 
                        com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "add_allergy_info")
                    else 
                        "${currentUser.allergens.size} ${com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "allergies_selected")}",
                    onClick = { showAllergyDialog = true }
                )
            }
            
            ProfileMenuItem(
                icon = Icons.Default.Settings,
                title = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "settings"),
                onClick = { showSettingsDialog = true }
            )
            
            ProfileMenuItem(
                icon = Icons.Default.Notifications,
                title = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "notifications"),
                onClick = { showNotificationsDialog = true }
            )
            
            ProfileMenuItem(
                icon = Icons.Default.Info,
                title = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "about"),
                onClick = { showAboutDialog = true }
            )
            
            ProfileMenuItem(
                icon = Icons.Default.Help,
                title = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "help_support"),
                onClick = { showHelpDialog = true }
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            ProfileMenuItem(
                icon = Icons.Default.ExitToApp,
                title = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "logout"),
                onClick = onLogout,
                isDestructive = true
            )
        }
    }
    
    if (showAllergyDialog) {
        AllergySelectionDialog(
            user = currentUser,
            onDismiss = { showAllergyDialog = false },
            onSave = { allergens ->
                onAllergyUpdate(allergens)
            }
        )
    }
    
    if (showAboutDialog) {
        AboutDialog(
            onDismiss = { showAboutDialog = false }
        )
    }
    
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
            onThemeChanged = {
                onThemeChanged()
                languageUpdateTrigger++
            },
            onLanguageChanged = {
                onLanguageChanged()
                languageUpdateTrigger++
            }
        )
    }
    
    if (showNotificationsDialog) {
        NotificationsDialog(
            onDismiss = { showNotificationsDialog = false }
        )
    }
    
    if (showHelpDialog) {
        HelpDialog(
            onDismiss = { showHelpDialog = false }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    subtitle: String? = null,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isDestructive) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Hakkında",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Campus Menu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Kampüs yemekhane menülerini kolayca takip edin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Divider()
                
                Text(
                    text = "Versiyon 2.0",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "© 2025 Campus Menu",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tamam")
            }
        }
    )
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onThemeChanged: () -> Unit = {},
    onLanguageChanged: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var isDarkMode by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.isDarkMode(context)) 
    }
    var selectedLanguage by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.getLanguage(context)) 
    }
    var selectedTheme by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.getTheme(context)) 
    }
    var showLanguageMenu by remember { mutableStateOf(false) }
    var showThemeMenu by remember { mutableStateOf(false) }
    
    val languages = listOf("Türkçe", "English", "Deutsch", "Français")
    val themes = listOf("Turuncu", "Mavi", "Yeşil", "Mor")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "settings"),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "app_settings"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Karanlık Mod
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "dark_mode"),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = {
                                    isDarkMode = it
                                    com.arikan.campusmenu.data.ThemePreferences.setDarkMode(context, it)
                                    onThemeChanged()
                                }
                            )
                        }
                        
                        Divider()
                        
                        // Dil
                        Surface(
                            onClick = { showLanguageMenu = true },
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Column {
                                        Text(
                                            text = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "language"),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = selectedLanguage,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false }
                        ) {
                            languages.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language) },
                                    onClick = {
                                        selectedLanguage = language
                                        com.arikan.campusmenu.data.ThemePreferences.setLanguage(context, language)
                                        showLanguageMenu = false
                                        onLanguageChanged()
                                    },
                                    leadingIcon = if (language == selectedLanguage) {
                                        { Icon(Icons.Default.Check, contentDescription = null) }
                                    } else null
                                )
                            }
                        }
                        
                        Divider()
                        
                        // Tema
                        Surface(
                            onClick = { showThemeMenu = true },
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Column {
                                        Text(
                                            text = com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "theme"),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = selectedTheme,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        DropdownMenu(
                            expanded = showThemeMenu,
                            onDismissRequest = { showThemeMenu = false }
                        ) {
                            themes.forEach { theme ->
                                DropdownMenuItem(
                                    text = { Text(theme) },
                                    onClick = {
                                        selectedTheme = theme
                                        com.arikan.campusmenu.data.ThemePreferences.setTheme(context, theme)
                                        showThemeMenu = false
                                        onThemeChanged()
                                    },
                                    leadingIcon = if (theme == selectedTheme) {
                                        { Icon(Icons.Default.Check, contentDescription = null) }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tamam")
            }
        }
    )
}

@Composable
fun NotificationsDialog(
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var dailyMenuNotification by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.getDailyMenuNotification(context)) 
    }
    var announcementsNotification by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.getAnnouncementsNotification(context)) 
    }
    var crowdingWarnings by remember { 
        mutableStateOf(com.arikan.campusmenu.data.ThemePreferences.getCrowdingWarnings(context)) 
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Bildirimler",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Bildirim Tercihleri",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = "Günlük Menü",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Switch(
                                checked = dailyMenuNotification,
                                onCheckedChange = { 
                                    dailyMenuNotification = it
                                    com.arikan.campusmenu.data.ThemePreferences.setDailyMenuNotification(context, it)
                                }
                            )
                        }
                        
                        Divider()
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = "Duyurular",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Switch(
                                checked = announcementsNotification,
                                onCheckedChange = { 
                                    announcementsNotification = it
                                    com.arikan.campusmenu.data.ThemePreferences.setAnnouncementsNotification(context, it)
                                }
                            )
                        }
                        
                        Divider()
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = "Yoğunluk Uyarıları",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Switch(
                                checked = crowdingWarnings,
                                onCheckedChange = { 
                                    crowdingWarnings = it
                                    com.arikan.campusmenu.data.ThemePreferences.setCrowdingWarnings(context, it)
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tamam")
            }
        }
    )
}

@Composable
fun HelpDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Help,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Yardım & Destek",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Yardıma mı ihtiyacınız var?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Sorularınız, önerileriniz veya sorunlarınız için bizimle iletişime geçebilirsiniz.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Divider()
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "İletişim",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Column {
                                Text(
                                    text = "İstek ve Şikayetler İçin:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "s.emrearikan@gmail.com",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                
                Divider()
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Sık Sorulan Sorular",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "• Menü ne zaman güncellenir?",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Favori yemeklerimi nasıl eklerim?",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Alerji bilgilerimi nasıl güncellerim?",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tamam")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    CampusMenuTheme {
        ProfileScreen()
    }
}
