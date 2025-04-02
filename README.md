# 🤖 Quick Car Hire (Android Application)

## 📌 Chapter 1: System Overview

### 🎯 1.1 Purpose
Quick Car Hire is designed to serve car showrooms by providing car rental services through a mobile application. This system helps car showrooms expand their offerings by allowing customers to rent cars effortlessly via their smartphones. Quick Car Hire maintains all details about cars, customers, bookings, and payments.

---

### 🔍 1.2 Scope
Our mobile application offers car rental services with features for managing:
- 🚘 Car details
- 👥 Customer information
- 📅 Bookings
- 💳 Payments
- ❌ Cancellations

Rent calculations are based on rental duration, measured in both **days** and **kilometers** selected.

---

### ⚙️ 1.3 Functional Requirements

#### 👤 Customer Functionality
1. 📝 **Registration with OTP**: Customers register using email, password, and OTP verification.
2. 🔎 **Search Filter**: Customers can filter cars based on brand, seats, fuel type, and transmission type.
3. 💰 **Payment**: Online payments via Razorpay, generating electronic invoices.
4. ❌ **Booking Cancellation**: 
   - If canceled **before 2 days**, a charge of ₹500 is applied.
   - If canceled **on the booking date**, no refund is issued.
5. ⭐ **Give Feedback**: Customers can share feedback after returning the car.

#### 🛠️ Admin Functionality
1. 🌍 **Manage City**: Admin can add, edit, and delete cities.
2. 📊 **View Reports**: Includes most booked cars, area-wise bookings, and top customers.
3. 💳 **View Payment Details**: Admin can view all transactions.
4. 📝 **View Feedback**: Admin can view and filter customer feedback.

---

### ✨ 1.4 Exciting Functionalities
1. 📄 **Report Download**: Users can download reports in **PDF format**.
2. 🔑 **Manage Role**: Admin can manage **user roles** and **menu access**.

---

### 🔒 1.5 Non-Functional Requirements
1. 🔐 **Security**: The system ensures security using **prepared statements** to prevent SQL injection.
2. ⚡ **Performance & Response Time**: The app is optimized for **fast response and smooth performance**.

---

### 🏁 1.6 Users of the System

#### 👥 Customer Characteristics
- 📌 Register via OTP verification.
- 🎁 View all available offers.
- 💰 Apply offer codes during payment for discounts.
- ⭐ Give feedback on their experience.
- 👤 Manage their profile.

#### 🏢 Admin Characteristics
- 👥 Manage all customer details.
- 🎁 Adjust and manage offers.
- 💳 View customer payments.
- ⭐ Access and review feedback.

---

### 🔧 1.7 Tools & Technologies

#### 📱 **Technology**
- Java / Kotlin
- XML (UI Design)
- Firebase / SQLite (Database)
- Android Jetpack Components
- Retrofit (API Calls)

#### 🛠️ **Tools**
- Android Studio
- Firebase Console
- Postman (API Testing)

#### 📚 **Library**
- Material Design Components
- Glide (Image Loading)
- Room Database
- Retrofit (Network Calls)

---

### 📢 1.8 Summary
- ✅ **Functional Requirements**: 9
- 🔐 **Non-Functional Requirements**: 2
- ✨ **Exciting Functionalities**: 2

---

### 🔄 Previous vs Current Semester Functionality

| **Customer Functionality** (Previous) | **Customer Functionality** (Current) |
|------------------------------|----------------------------|
| 📝 Registration & Login  | 📝 Registration with OTP |
| 🚗 Car Booking  | 🔎 Search Filter |
| 🎁 View Offer  | 💰 Payment |
| 📖 My Booking (History) | ❌ Booking Cancellation |
| 👤 Manage Profile  | ⭐ Give Feedback |

| **Admin Functionality** (Previous) | **Admin Functionality** (Current) |
|------------------------------|----------------------------|
| 🔑 Login  | 🌍 Manage City |
| 👥 View Customer Details  | 📊 View Reports |
| 🎁 Manage Offers  | 💳 View Payment Details |
| 🚗 Manage Car Details  | 📝 View Feedback |
| 📅 View Bookings  | |

| **Non-Functional Requirements** | **Exciting Functionalities** |
|------------------------------|----------------------------|
| 🔐 Security | 📄 Report Download |
| ⚡ Performance & Response Time | 🔑 Manage Role |

---

🚀 **Stay tuned for more updates!** 🎉
