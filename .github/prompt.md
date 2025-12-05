# Campus Meal Menu App – Full Stack Prompt for Copilot / Cloud Sonnet

Aşağıdaki talimatlara göre **React Native + Local JSON storage** temelli bir mobil uygulama geliştir.  
Uygulamanın ismi: **Kampüs Yemek Menüsü**

Bu uygulama iki tip kullanıcı girişi içermelidir:

1. **Admin Login**

   - Username: `admin`
   - Password: `mukemmelapp`
   - Admin panelinden: Günlük menü ekleme, silme, düzenleme yapılabilmeli.

2. **Öğrenci Login**
   - Sadece username sorulsun (şifre yok).
   - Giriş sonrası tüm uygulamaya erişebilir ama menü düzenleyemez.

---

## 🎯 Uygulama Özeti

Uygulama, kampüs yemek menüsünü gün gün gösterir.  
Öğrenciler günlük yemekleri **puanlayabilir**, geçmiş menülere bakabilir ve menüler offline olarak da görülebilir.

---

## 📌 Teknik Gereksinimler

### 1. JSON veri sistemi

Uygulama içinde `menuData.json` benzeri bir lokal depolama olmalı.

Her menü objesi şu formatta olmalı:

```json
{
  "id": 1,
  "date": "2025-02-01",
  "items": ["Mercimek Çorbası", "Tavuk Sote", "Pirinç Pilavı", "Ayran"],
  "rating": {
    "average": 4.2,
    "votes": 57
  }
}
```
