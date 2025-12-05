# Instructions for Copilot / Cloud Sonnet

Bu dosya, Kampüs Yemek Menüsü uygulaması geliştirilirken uyulması gereken kesin teknik talimatları içerir.  
Copilot ve Cloud Sonnet, bu kuralların dışına çıkmadan kod üretmelidir.

---

# 1. GENEL KURALLAR

- Uygulama **React Native** ile geliştirilecek.
- Veri depolama **AsyncStorage** ve **lokal JSON dosyası (menuData.json)** üzerinden yapılacak.
- Uygulama hem **Admin** hem **Öğrenci** rolüne göre farklı ekranlar açacak.
- Kodun içinde gerekli yerlerde **Türkçe açıklamalar** bulunacak.
- UI temiz, sade, okunabilir olacak; gereksiz karmaşıklık yok.

---

# 2. LOGIN SİSTEMİ

## Admin Login

- Admin giriş bilgileri sabittir:
  - username: `admin`
  - password: `mukemmelapp`
- Giriş başarılı ise **Admin Panel** açılacak.
- Başarısız girişte hata mesajı gösterilecek.
- Admin, menü CRUD işlemlerini yapabilen **tek rol** olacaktır.

## Öğrenci Login

- Öğrenciden sadece username istenir.
- Öğrenci login olduktan sonra:
  - Günlük menü görüntüleme
  - Menü puanlama
  - Geçmiş menü arşivi  
    ekranlarına erişebilir.
- Öğrenciler menü üzerinde değişiklik yapamaz.

---

# 3. VERİ YAPISI

## JSON Veri Yapısı (menuData.json)

Her menü objesi şu formatta olmalıdır:

```json
{
  "id": 1,
  "date": "2025-02-01",
  "items": ["Mercimek Çorbası", "Tavuk Sote", "Pilav", "Ayran"],
  "rating": {
    "average": 4.2,
    "votes": 57
  }
}
```
