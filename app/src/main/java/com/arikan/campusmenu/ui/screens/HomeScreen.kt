package com.arikan.campusmenu.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arikan.campusmenu.data.MenuCategory
import com.arikan.campusmenu.data.MenuItem
import com.arikan.campusmenu.data.MenuRepository
import com.arikan.campusmenu.data.MenuHistoryRepository
import com.arikan.campusmenu.ui.theme.CampusMenuTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    selectedDate: LocalDate = LocalDate.now(),
    user: com.arikan.campusmenu.data.User? = null,
    languageVersion: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedCategory by remember { mutableStateOf<MenuCategory?>(null) }
    val menuItems = remember(selectedDate) { MenuHistoryRepository.getMenuForDate(selectedDate) }
    
    // Alerji filtreleme
    val allergyFilteredItems = if (user != null && user.allergens.isNotEmpty()) {
        menuItems.filter { menuItem ->
            menuItem.allergens.none { it in user.allergens }
        }
    } else {
        menuItems
    }
    
    val filteredItems = if (selectedCategory != null) {
        allergyFilteredItems.filter { it.category == selectedCategory }
    } else {
        allergyFilteredItems
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        val menuTitle = remember(languageVersion) {
                            if (selectedDate == LocalDate.now()) 
                                com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "todays_menu")
                            else 
                                com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "menu_for")
                        }
                        Text(
                            text = menuTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                        Text(
                            text = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE", Locale("tr"))),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF7F8C8D)
                        )
                    }
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
            // Kategori Filtreleri
            CategoryFilterRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                languageVersion = languageVersion
            )
            
            // Menü Listesi
            if (filteredItems.isEmpty()) {
                EmptyMenuState()
            } else {
                MenuList(
                    items = filteredItems,
                    user = user,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: MenuCategory?,
    onCategorySelected: (MenuCategory?) -> Unit,
    languageVersion: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    LazyRow(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            val allText = remember(languageVersion) {
                com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "all")
            }
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text(allText) }
            )
        }
        
        items(MenuCategory.entries) { category ->
            val categoryName = remember(languageVersion) {
                category.getLocalizedName(context)
            }
            FilterChip(
                selected = selectedCategory == category,
                onClick = { 
                    onCategorySelected(if (selectedCategory == category) null else category)
                },
                label = { Text(categoryName) }
            )
        }
    }
}

@Composable
fun MenuList(
    items: List<MenuItem>,
    user: com.arikan.campusmenu.data.User? = null,
    modifier: Modifier = Modifier
) {
    var refreshTrigger by remember { mutableStateOf(0) }
    
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items, key = { it.id }) { menuItem ->
            MenuItemCard(
                item = menuItem,
                user = user,
                onReviewSubmitted = { refreshTrigger++ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemCard(
    item: MenuItem,
    user: com.arikan.campusmenu.data.User? = null,
    onReviewSubmitted: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showReviewsListDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Yorumları yükle
    LaunchedEffect(Unit) {
        com.arikan.campusmenu.data.ReviewRepository.initialize(context)
    }
    
    val menuItemReview by remember(item.id) {
        derivedStateOf {
            com.arikan.campusmenu.data.ReviewRepository.getMenuItemReviews(
                context,
                item.id,
                java.time.LocalDate.now().toString()
            )
        }
    }
    
    Card(
        onClick = { showDialog = true },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Görsel alan (yemek resmi yerine emoji)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = when (item.category) {
                        MenuCategory.SOUP -> Color(0xFFFFE5D9)
                        MenuCategory.MAIN_COURSE -> Color(0xFFFFD4A3)
                        MenuCategory.SIDE_DISH -> Color(0xFFFFF4B8)
                        MenuCategory.SALAD -> Color(0xFFD4F1D4)
                        MenuCategory.DESSERT -> Color(0xFFFFE4E4)
                        MenuCategory.BEVERAGE -> Color(0xFFD4E4FF)
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = when (item.category) {
                                MenuCategory.SOUP -> "🍲"
                                MenuCategory.MAIN_COURSE -> "🍗"
                                MenuCategory.SIDE_DISH -> "🍟"
                                MenuCategory.SALAD -> "🥗"
                                MenuCategory.DESSERT -> "🍰"
                                MenuCategory.BEVERAGE -> "🥤"
                            },
                            fontSize = 56.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Yemek Bilgileri
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Favori ve Değerlendirme butonları (sadece öğrenciler için)
                    if (user != null && user.role == com.arikan.campusmenu.data.UserRole.STUDENT) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Favori butonu
                            var isFavorite by remember { 
                                mutableStateOf(
                                    com.arikan.campusmenu.data.FavoritesRepository.isFavorite(user.id, item.id)
                                ) 
                            }
                            
                            IconButton(
                                onClick = { 
                                    com.arikan.campusmenu.data.FavoritesRepository.toggleFavorite(
                                        context, user.id, item.id, item.name
                                    )
                                    isFavorite = !isFavorite
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favori",
                                    tint = if (isFavorite) Color(0xFFE53935) else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            
                            // Değerlendirme butonu
                            IconButton(
                                onClick = { showReviewDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Değerlendir",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ortalama puan göster - Tıklanabilir
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showReviewsListDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (menuItemReview.reviewCount > 0) {
                                String.format("%.1f (%d)", (menuItemReview.averageRating * 10).toInt() / 10f, menuItemReview.reviewCount)
                            } else {
                                com.arikan.campusmenu.data.LocaleHelper.getLocalizedString(context, "no_reviews")
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7F8C8D)
                        )
                        if (menuItemReview.reviewCount > 0) {
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Yorumları gör",
                                modifier = Modifier.size(14.dp),
                                tint = Color.Gray
                            )
                        }
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.calories} kcal",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF7F8C8D)
                        )
                    }
                }
            }
            
            // Fiyat Badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 2.dp
            ) {
                Text(
                    text = "₺${String.format("%.0f", item.price)}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            
            // Müsaitlik Durumu
            if (!item.isAvailable) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE53935),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = "Bitti",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
    
    if (showDialog) {
        MenuItemDetailDialog(
            item = item,
            onDismiss = { showDialog = false }
        )
    }
    
    if (showReviewDialog && user != null) {
        ReviewDialog(
            menuItem = item,
            user = user,
            onDismiss = { showReviewDialog = false },
            onSubmit = {
                onReviewSubmitted()
            }
        )
    }
    
    if (showReviewsListDialog) {
        ReviewsListDialog(
            menuItem = item,
            onDismiss = { showReviewsListDialog = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuItemDetailDialog(
    item: MenuItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = item.category.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Divider()
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Kalori",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${item.calories} kcal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Fiyat",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "₺${String.format("%.2f", item.price)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${item.rating}/5.0",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                if (item.allergens.isNotEmpty()) {
                    Divider()
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "İçerik",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item.allergens.forEach { allergen ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(allergen.displayName) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        labelColor = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        }
    )
}

@Composable
fun EmptyMenuState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🍽️",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Bu kategoride yemek bulunamadı",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun getCurrentDate(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE", Locale("tr"))
    return LocalDate.now().format(formatter)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    CampusMenuTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemCardPreview() {
    CampusMenuTheme {
        MenuItemCard(
            item = MenuItem(
                id = 1,
                name = "Tavuk Şinitzel",
                category = MenuCategory.MAIN_COURSE,
                calories = 450,
                price = 65.0,
                description = "Kızarmış tavuk şinitzel, patates garnitürü ile",
                rating = 4.8f
            )
        )
    }
}
