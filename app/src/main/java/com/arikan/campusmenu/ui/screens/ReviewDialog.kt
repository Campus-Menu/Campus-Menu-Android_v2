package com.arikan.campusmenu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arikan.campusmenu.data.*
import com.arikan.campusmenu.ui.theme.CampusMenuTheme

@Composable
fun ReviewDialog(
    menuItem: MenuItem,
    user: User,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var selectedFeedback by remember { mutableStateOf(setOf<String>()) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Başlık
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Değerlendir",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Kapat")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Yemek adı
                Text(
                    text = menuItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Yıldız Değerlendirme
                Text(
                    text = "Puanın",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { rating = index + 1 },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "${index + 1} yıldız",
                                tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Hızlı Geri Bildirim
                Text(
                    text = "Hızlı Geri Bildirim",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                QuickFeedbackOption.entries.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { option ->
                            val isSelected = selectedFeedback.contains(option.displayName)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedFeedback = if (isSelected) {
                                        selectedFeedback - option.displayName
                                    } else {
                                        selectedFeedback + option.displayName
                                    }
                                },
                                label = { Text(option.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Yorum
                Text(
                    text = "Yorumun (İsteğe bağlı)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Yemeğin hakkında düşüncelerini paylaş...") },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Gönder Butonu
                Button(
                    onClick = {
                        if (rating > 0) {
                            ReviewRepository.initialize(context)
                            ReviewRepository.addReview(
                                context = context,
                                menuItemId = menuItem.id,
                                menuItemName = menuItem.name,
                                date = java.time.LocalDate.now().toString(),
                                studentId = user.id.toString(),
                                studentName = user.name,
                                rating = rating,
                                comment = comment,
                                quickFeedback = selectedFeedback.toList()
                            )
                            onSubmit()
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = rating > 0
                ) {
                    Text(
                        text = "Gönder",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
