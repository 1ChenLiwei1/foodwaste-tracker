# FoodWaste – Android Mobile Application

FoodWaste is an Android application designed to help users reduce household food waste by tracking inventory, monitoring expiry dates, scanning barcodes, generating recipe suggestions, and managing a shopping list. The app encourages sustainable living through smart reminders and efficient food management.

---

##  Key Features

### 1. Inventory Management
- Add, edit, and delete food items  
- Colour indicators:
  - **Yellow** – Items expiring soon  
  - **Red** – Items already expired
- Local notifications for expiry alerts  
- Manual addition + barcode input

###  2. Barcode Scanning (ML Kit)
- Scan items such as *Spring Onion*  
- Automatically insert scanned items into inventory  
- Helps users quickly record groceries

###  3. Recipes Page
- Multiple preset recipes  
- Tap to view full ingredients and steps  

###  4. AI Recipe Suggestions
- Generates recipe ideas based on current inventory  
- Uses Retrofit + API integration  
- Helps reduce waste by recommending dishes using soon-to-expire items  

###  5. Shopping List
- Auto-adds items when stock is low  
- Manual addition supported  
- Items can be marked as completed  

###  6. User Profile Page
- Displays user information  
- Allows theme configuration (Light Blue theme)

###  7. Expiry Notification System
- Sends scheduled notifications for:
  - Items expiring soon (yellow)
  - Items already expired (red)  

---

## Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Room Database**
- **Retrofit (API Integration)**
- **ML Kit – Barcode Scanning**
- **Material 3 UI**
- **Android Notification Manager**

---

##  Project Structure
com.example.foodwaste\
├── data/ # Room database, DAO, entities\
├── repository/ # Data management & logic\
├── viewmodel/ # ViewModels (UI + Data handling)\
├── ui/ # Compose screen UI\
├── components/ # Reusable Compose components\
└── util/ # Date helpers, formatting, constants\
---

##  Testing (Unit Tests Included)

This project includes **local unit tests** to ensure core logic works correctly.  
Tests are located in:
###  1. Expiry Calculation Tests
Tests ensure that the application correctly identifies:

- Expired items  
- Items expiring within 3 days  
- Items still safe to consume  

These tests validate the logic used to display **red** and **yellow** inventory indicators.

### 2. String Formatting Tests
Barcode scanning may produce inconsistent naming such as `"   spring onion "`.

Tests verify that:
- Names are trimmed  
- First letter is automatically capitalized  
- Output becomes `"Spring onion"`  

This ensures clean and consistent item display.

### 3. Shopping List Auto-Add Tests
Validates the logic that automatically adds an item to the shopping list when:
- Inventory quantity is **0** or **less**

This ensures the auto-generation of shopping list entries works correctly.

### 4. Basic Example Test
Verifies the test environment is working (`2 + 2 = 4`).  
Ensures JUnit configuration is functioning properly.

---

##  Requirements
- Android Studio Koala or later  
- Minimum SDK: **24**  
- Recommended device: Android 12/13  

---

## Author
**Chen Liwei**  
14527658
James Cook University Singapore  
2025
