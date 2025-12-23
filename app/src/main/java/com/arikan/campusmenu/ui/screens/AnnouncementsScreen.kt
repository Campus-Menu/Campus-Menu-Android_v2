package com.arikan.campusmenu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arikan.campusmenu.data.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsScreen(
    languageVersion: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var announcements by remember { mutableStateOf<List<Announcement>>(emptyList()) }
    var selectedType by remember { mutableStateOf<AnnouncementType?>(null) }
    val isAdmin = false // Admin özellikleri kaldırıldı
    
    fun refreshAnnouncements() {
        announcements = AnnouncementRepository.getActiveAnnouncements(context)
    }
    
    LaunchedEffect(Unit) {
        AnnouncementRepository.initialize(context)
        refreshAnnouncements()
    }
    
    val filteredAnnouncements = if (selectedType != null) {
        announcements.filter { it.type == selectedType }
    } else {
        announcements
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Duyurular",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tip Filtreleri
            LazyRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedType == null,
                        onClick = { selectedType = null },
                        label = { Text("Tümü") }
                    )
                }
                
                items(AnnouncementType.entries.toTypedArray()) { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { 
                            selectedType = if (selectedType == type) null else type
                        },
                        label = { Text(type.displayName) },
                        leadingIcon = {
                            Icon(
                                imageVector = when (type) {
                                    AnnouncementType.CLOSURE -> Icons.Default.Lock
                                    AnnouncementType.HOLIDAY -> Icons.Default.Event
                                    AnnouncementType.MAINTENANCE -> Icons.Default.Build
                                    AnnouncementType.GENERAL -> Icons.Default.Info
                                },
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }
            
            // Duyuru Listesi
            if (filteredAnnouncements.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "Duyuru bulunmuyor",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredAnnouncements) { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            isAdmin = isAdmin,
                            onDelete = {
                                AnnouncementRepository.removeAnnouncement(context, announcement.id)
                                refreshAnnouncements()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    isAdmin: Boolean = false,
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr", "TR"))
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (announcement.type) {
                AnnouncementType.CLOSURE -> Color(0xFFFFEBEE)
                AnnouncementType.HOLIDAY -> Color(0xFFFFF3E0)
                AnnouncementType.MAINTENANCE -> Color(0xFFFFF9C4)
                AnnouncementType.GENERAL -> Color(0xFFE3F2FD)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (announcement.type) {
                            AnnouncementType.CLOSURE -> Color(0xFFD32F2F)
                            AnnouncementType.HOLIDAY -> Color(0xFFF57C00)
                            AnnouncementType.MAINTENANCE -> Color(0xFFFBC02D)
                            AnnouncementType.GENERAL -> Color(0xFF1976D2)
                        }
                    ) {
                        Icon(
                            imageVector = when (announcement.type) {
                                AnnouncementType.CLOSURE -> Icons.Default.Lock
                                AnnouncementType.HOLIDAY -> Icons.Default.Event
                                AnnouncementType.MAINTENANCE -> Icons.Default.Build
                                AnnouncementType.GENERAL -> Icons.Default.Info
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp).size(20.dp)
                        )
                    }
                    
                    Text(
                        text = announcement.type.displayName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = when (announcement.type) {
                            AnnouncementType.CLOSURE -> Color(0xFFD32F2F)
                            AnnouncementType.HOLIDAY -> Color(0xFFF57C00)
                            AnnouncementType.MAINTENANCE -> Color(0xFFFBC02D)
                            AnnouncementType.GENERAL -> Color(0xFF1976D2)
                        }
                    )
                }
                
                if (announcement.isImportant) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFF5252)
                    ) {
                        Text(
                            text = "ÖNEMLİ",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = announcement.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF424242)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${announcement.startDate.format(dateFormatter)} - ${announcement.endDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                if (isAdmin) {
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Duyuruyu Sil",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
    
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F)
                )
            },
            title = { Text("Duyuruyu Sil") },
            text = { Text("Bu duyuruyu silmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Sil", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
fun AddAnnouncementDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, AnnouncementType) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(AnnouncementType.GENERAL) }
    var showTypeDropdown by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = { Text("Yeni Duyuru Ekle") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Mesaj") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Box {
                    OutlinedTextField(
                        value = selectedType.displayName,
                        onValueChange = {},
                        label = { Text("Duyuru Tipi") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTypeDropdown = !showTypeDropdown }) {
                                Icon(
                                    imageVector = if (showTypeDropdown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    DropdownMenu(
                        expanded = showTypeDropdown,
                        onDismissRequest = { showTypeDropdown = false }
                    ) {
                        AnnouncementType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedType = type
                                    showTypeDropdown = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when (type) {
                                            AnnouncementType.CLOSURE -> Icons.Default.Lock
                                            AnnouncementType.HOLIDAY -> Icons.Default.Event
                                            AnnouncementType.MAINTENANCE -> Icons.Default.Build
                                            AnnouncementType.GENERAL -> Icons.Default.Info
                                        },
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (title.isNotBlank() && message.isNotBlank()) {
                        onConfirm(title, message, selectedType)
                    }
                },
                enabled = title.isNotBlank() && message.isNotBlank()
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
