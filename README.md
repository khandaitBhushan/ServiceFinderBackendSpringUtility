# ServiceFinder Backend API Documentation

## 🔐 AUTHENTICATION ENDPOINTS

### 1. Register User
**URL:** `POST http://localhost:8080/auth/register`  
**Bearer Token:** ❌ Not required  
**Body:**
```json
{
    "name": "John Doe",
    "email": "john@email.com",
    "password": "password123",
    "phone": "9876543210",
    "role": "USER",
    "address": "Lokmanya Nagar, Nagpur"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huQGVtYWlsLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzYyMzYwNDg4LCJleHAiOjE3NjI0NDY4ODh9.sRbioC_AXegMHKyWbpUxAPXe8B2mrHr_133EEyfm7mtTp8Z6Xhqx5pHXR1qo1bMd",
    "email": "john@email.com",
    "role": "USER",
    "message": "Registration successful!",
    "success": true
}
```

### 2. Register Service Provider
**URL:** `POST http://localhost:8080/auth/register`  
**Bearer Token:** ❌ Not required  
**Body:**
```json
{
    "name": "Raj Electrician",
    "email": "raj@email.com",
    "password": "password123",
    "phone": "9876543211",
    "role": "PROVIDER",
    "address": "Bardi, Nagpur"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyYWpAZW1haWwuY29tIiwicm9sZSI6IlBST1ZJREVSIiwiaWF0IjoxNzYyMzYwNjAxLCJleHAiOjE3NjI0NDcwMDF9.QxBTqcf72co1T-FLx-_m0kpAYV3Tk7fMsSmCcZXTjoV9z8GXDs6wO9YQWq_W0MPh",
    "email": "raj@email.com",
    "role": "PROVIDER",
    "message": "Registration successful!",
    "success": true
}
```

### 3. Login
**URL:** `POST http://localhost:8080/auth/login`  
**Bearer Token:** ❌ Not required  
**Body:**
```json
{
    "email": "john@email.com",
    "password": "password123"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huQGVtYWlsLmNvbSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzYyMzYwNjY5LCJleHAiOjE3NjI0NDcwNjl9.I2vfQuG2rGu3EpkGhZueZhQOh8t6OzseEKT2TuMEWyFrTMznc83qKi6YYHXh0MDE",
    "email": "john@email.com",
    "role": "USER",
    "message": "Login successful!",
    "success": true
}
```

---

## 👤 USER ENDPOINTS

### 4. Get Current User Profile
**URL:** `GET http://localhost:8080/users/me`  
**Bearer Token:** ✅ Required (User token)  
**Response:**
```json
{
    "id": 106,
    "name": "John Doe",
    "email": "john@email.com",
    "phone": "9876543210",
    "role": "USER",
    "address": "Lokmanya Nagar, Nagpur"
}
```

### 5. Update User Profile
**URL:** `PUT http://localhost:8080/users/update`  
**Bearer Token:** ✅ Required (User token)  
**Body:**
```json
{
    "name": "John Updated",
    "email": "john.updated@email.com",
    "phone": "9876543222",
    "address": "Hingna Road, Nagpur"
}
```
**Response:**
```json
{
    "id": 106,
    "name": "John Updated",
    "email": "john@email.com",
    "phone": "9876543222",
    "role": "USER",
    "address": "Hingna Road, Nagpur"
}
```

---

## 🛠️ PROVIDER ENDPOINTS

### 6. Register Service Provider (Additional Details)
**URL:** `POST http://localhost:8080/providers/register`  
**Bearer Token:** ✅ Required (Provider token)  
**Body:**
```json
{
    "companyName": "Raj Electrical Services",
    "serviceCategory": {"id": 1},
    "description": "Expert electrician services",
    "experienceYears": 5,
    "location": "Lokmanya Nagar, Nagpur",
    "latitude": 21.1190,
    "longitude": 79.0704,
    "hourlyRate": 300.00,
    "minServicePrice": 200.00,
    "pricingDescription": "Minimum charge ₹200"
}
```
**Response:**
```json
{
    "id": 6,
    "companyName": "Raj Electrical Services",
    "serviceCategory": {
        "id": 1,
        "name": null
    },
    "description": "Expert electrician services",
    "experienceYears": 5,
    "rating": 0.0,
    "location": "Lokmanya Nagar, Nagpur",
    "latitude": 21.119,
    "longitude": 79.0704,
    "hourlyRate": 300.0,
    "minServicePrice": 200.0,
    "pricingDescription": "Minimum charge ₹200"
}
```

### 7. Get All Providers
**URL:** `GET http://localhost:8080/providers`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 1,
        "companyName": "Raj Electrical Services",
        "serviceCategory": {
            "id": 1,
            "name": "Electrician"
        },
        "description": "Expert in all electrical repairs and installations. 24/7 emergency service available.",
        "experienceYears": 8,
        "rating": 4.5,
        "location": "Lokmanya Nagar, Nagpur",
        "latitude": 21.119,
        "longitude": 79.0704,
        "hourlyRate": 300.0,
        "minServicePrice": 200.0,
        "pricingDescription": "Minimum charge: ₹200 for first 30 minutes"
    }
]
```

### 8. Search Providers
**URL:** `GET http://localhost:8080/providers/search?category=Electrician&location=Nagpur`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 1,
        "companyName": "Raj Electrical Services",
        "serviceCategory": {
            "id": 1,
            "name": "Electrician"
        },
        "description": "Expert in all electrical repairs and installations. 24/7 emergency service available.",
        "experienceYears": 8,
        "rating": 4.5,
        "location": "Lokmanya Nagar, Nagpur",
        "latitude": 21.119,
        "longitude": 79.0704,
        "hourlyRate": 300.0,
        "minServicePrice": 200.0,
        "pricingDescription": "Minimum charge: ₹200 for first 30 minutes"
    },
    {
        "id": 6,
        "companyName": "Raj Electrical Services",
        "serviceCategory": {
            "id": 1,
            "name": "Electrician"
        },
        "description": "Expert electrician services",
        "experienceYears": 5,
        "rating": 0.0,
        "location": "Lokmanya Nagar, Nagpur",
        "latitude": 21.119,
        "longitude": 79.0704,
        "hourlyRate": 300.0,
        "minServicePrice": 200.0,
        "pricingDescription": "Minimum charge ₹200"
    }
]
```

### 9. Search Within Radius
**URL:** `GET http://localhost:8080/providers/search/radius?category=Electrician&latitude=21.1190&longitude=79.0704&radiusKm=5`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 1,
        "companyName": "Raj Electrical Services",
        "serviceCategory": {
            "id": 1,
            "name": "Electrician"
        },
        "description": "Expert in all electrical repairs and installations. 24/7 emergency service available.",
        "experienceYears": 8,
        "rating": 4.5,
        "location": "Lokmanya Nagar, Nagpur",
        "latitude": 21.119,
        "longitude": 79.0704,
        "hourlyRate": 300.0,
        "minServicePrice": 200.0,
        "pricingDescription": "Minimum charge: ₹200 for first 30 minutes"
    },
    {
        "id": 6,
        "companyName": "Raj Electrical Services",
        "serviceCategory": {
            "id": 1,
            "name": "Electrician"
        },
        "description": "Expert electrician services",
        "experienceYears": 5,
        "rating": 0.0,
        "location": "Lokmanya Nagar, Nagpur",
        "latitude": 21.119,
        "longitude": 79.0704,
        "hourlyRate": 300.0,
        "minServicePrice": 200.0,
        "pricingDescription": "Minimum charge ₹200"
    }
]
```

### 10. Get Providers with Distance
**URL:** `GET http://localhost:8080/providers/with-distance?category=Electrician&latitude=21.1190&longitude=79.0704`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "provider": {
            "id": 1,
            "companyName": "Raj Electrical Services",
            "serviceCategory": {
                "id": 1,
                "name": "Electrician"
            },
            "description": "Expert in all electrical repairs and installations. 24/7 emergency service available.",
            "experienceYears": 8,
            "rating": 4.5,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.119,
            "longitude": 79.0704,
            "hourlyRate": 300.0,
            "minServicePrice": 200.0,
            "pricingDescription": "Minimum charge: ₹200 for first 30 minutes"
        },
        "distance": 0.0
    },
    {
        "provider": {
            "id": 6,
            "companyName": "Raj Electrical Services",
            "serviceCategory": {
                "id": 1,
                "name": "Electrician"
            },
            "description": "Expert electrician services",
            "experienceYears": 5,
            "rating": 0.0,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.119,
            "longitude": 79.0704,
            "hourlyRate": 300.0,
            "minServicePrice": 200.0,
            "pricingDescription": "Minimum charge ₹200"
        },
        "distance": 0.0
    }
]
```

### 11. Get Nearby Providers
**URL:** `GET http://localhost:8080/providers/nearby?latitude=21.1190&longitude=79.0704&maxDistance=10`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "provider": {
            "id": 1,
            "companyName": "Raj Electrical Services",
            "serviceCategory": {
                "id": 1,
                "name": "Electrician"
            },
            "description": "Expert in all electrical repairs and installations. 24/7 emergency service available.",
            "experienceYears": 8,
            "rating": 4.5,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.119,
            "longitude": 79.0704,
            "hourlyRate": 300.0,
            "minServicePrice": 200.0,
            "pricingDescription": "Minimum charge: ₹200 for first 30 minutes"
        },
        "distance": 0.0
    },
    {
        "provider": {
            "id": 6,
            "companyName": "Raj Electrical Services",
            "serviceCategory": {
                "id": 1,
                "name": "Electrician"
            },
            "description": "Expert electrician services",
            "experienceYears": 5,
            "rating": 0.0,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.119,
            "longitude": 79.0704,
            "hourlyRate": 300.0,
            "minServicePrice": 200.0,
            "pricingDescription": "Minimum charge ₹200"
        },
        "distance": 0.0
    },
    {
        "provider": {
            "id": 4,
            "companyName": "Singh AC Services",
            "serviceCategory": {
                "id": 4,
                "name": "AC Repair"
            },
            "description": "AC installation, repair and maintenance. All brands serviced.",
            "experienceYears": 5,
            "rating": 4.2,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.12,
            "longitude": 79.072,
            "hourlyRate": 400.0,
            "minServicePrice": 300.0,
            "pricingDescription": "Gas charging extra"
        },
        "distance": 0.2
    },
    {
        "provider": {
            "id": 3,
            "companyName": "Verma Wood Works",
            "serviceCategory": {
                "id": 3,
                "name": "Carpenter"
            },
            "description": "Custom furniture and carpentry services. Door/window repair specialist.",
            "experienceYears": 10,
            "rating": 4.7,
            "location": "Hingna Road, Nagpur",
            "latitude": 21.115,
            "longitude": 79.065,
            "hourlyRate": 350.0,
            "minServicePrice": 250.0,
            "pricingDescription": "Material cost extra"
        },
        "distance": 0.72
    },
    {
        "provider": {
            "id": 2,
            "companyName": "Patil Plumbing Works",
            "serviceCategory": {
                "id": 2,
                "name": "Plumber"
            },
            "description": "Professional plumbing services for homes and offices. Water tank cleaning available.",
            "experienceYears": 6,
            "rating": 4.3,
            "location": "Bardi, Nagpur",
            "latitude": 21.125,
            "longitude": 79.075,
            "hourlyRate": 250.0,
            "minServicePrice": 150.0,
            "pricingDescription": "Free inspection for first time customers"
        },
        "distance": 0.82
    },
    {
        "provider": {
            "id": 5,
            "companyName": "Kumar Painting Services",
            "serviceCategory": {
                "id": 5,
                "name": "Painter"
            },
            "description": "Interior and exterior painting. Waterproofing services available.",
            "experienceYears": 7,
            "rating": 4.4,
            "location": "Bardi, Nagpur",
            "latitude": 21.127,
            "longitude": 79.078,
            "hourlyRate": 280.0,
            "minServicePrice": 180.0,
            "pricingDescription": "Paint material extra"
        },
        "distance": 1.19
    }
]
```

### 12. Get Nearby by Category
**URL:** `GET http://localhost:8080/providers/nearby/category?category=Electrician&latitude=21.1190&longitude=79.0704&maxDistance=5`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "provider": {
            "id": 1,
            "companyName": "Raj Electrical Services",
            "serviceCategory": {
                "id": 1,
                "name": "Electrician"
            },
            "description": "Expert in all electrical repairs and installations. 24/7 emergency service available.",
            "experienceYears": 8,
            "rating": 4.5,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.119,
            "longitude": 79.0704,
            "hourlyRate": 300.0,
            "minServicePrice": 200.0,
            "pricingDescription": "Minimum charge: ₹200 for first 30 minutes"
        },
        "distance": 0.0
    },
    {
        "provider": {
            "id": 6,
            "companyName": "Raj Electrical Services",
            "serviceCategory": {
                "id": 1,
                "name": "Electrician"
            },
            "description": "Expert electrician services",
            "experienceYears": 5,
            "rating": 0.0,
            "location": "Lokmanya Nagar, Nagpur",
            "latitude": 21.119,
            "longitude": 79.0704,
            "hourlyRate": 300.0,
            "minServicePrice": 200.0,
            "pricingDescription": "Minimum charge ₹200"
        },
        "distance": 0.0
    }
]
```

### 13. Get All Categories
**URL:** `GET http://localhost:8080/providers/categories`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 4,
        "name": "AC Repair"
    },
    {
        "id": 3,
        "name": "Carpenter"
    },
    {
        "id": 1,
        "name": "Electrician"
    },
    {
        "id": 5,
        "name": "Painter"
    },
    {
        "id": 2,
        "name": "Plumber"
    }
]
```

### 14. Create Category
**URL:** `POST http://localhost:8080/providers/categories?name=Cleaner`  
**Bearer Token:** ✅ Required (Provider token)  
**Response:**
```json
[
    {
        "id": 4,
        "name": "AC Repair"
    },
    {
        "id": 3,
        "name": "Carpenter"
    },
    {
        "id": 1,
        "name": "Electrician"
    },
    {
        "id": 5,
        "name": "Painter"
    },
    {
        "id": 2,
        "name": "Plumber"
    }
]
```

---

## 🔧 PROVIDER SERVICES ENDPOINTS

### 15. Add Service (Provider)
**URL:** `POST http://localhost:8080/provider-services`  
**Bearer Token:** ✅ Required (Provider token)  
**Body:**
```json
{
    "serviceName": "Switch Installation",
    "description": "Electrical switch and socket installation",
    "price": 200.00,
    "durationMinutes": 60,
    "isActive": true
}
```
**Response:**
```json
{
    "id": 16,
    "serviceName": "Switch Installation",
    "description": "Electrical switch and socket installation",
    "price": 200.0,
    "durationMinutes": 60,
    "isActive": true
}
```

### 16. Get My Services (Provider)
**URL:** `GET http://localhost:8080/provider-services/my-services`  
**Bearer Token:** ✅ Required (Provider token)  
**Response:**
```json
[
    {
        "id": 16,
        "serviceName": "Switch Installation",
        "description": "Electrical switch and socket installation",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    }
]
```

### 17. Get Provider's Services
**URL:** `GET http://localhost:8080/provider-services/provider/1`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 1,
        "serviceName": "Switch & Socket Installation",
        "description": "Installation of electrical switches, sockets and boards",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    },
    {
        "id": 2,
        "serviceName": "Fan & Light Fitting",
        "description": "Ceiling fan and light fixture installation",
        "price": 250.0,
        "durationMinutes": 45,
        "isActive": true
    },
    {
        "id": 3,
        "serviceName": "Wiring Repair",
        "description": "Complete electrical wiring repair and troubleshooting",
        "price": 500.0,
        "durationMinutes": 120,
        "isActive": true
    }
]
```

### 18. Search Services
**URL:** `GET http://localhost:8080/provider-services/search?serviceName=installation`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 1,
        "serviceName": "Switch & Socket Installation",
        "description": "Installation of electrical switches, sockets and boards",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    },
    {
        "id": 5,
        "serviceName": "Pipe Installation",
        "description": "New water pipe installation and replacement",
        "price": 400.0,
        "durationMinutes": 90,
        "isActive": true
    },
    {
        "id": 12,
        "serviceName": "AC Installation",
        "description": "New AC unit installation",
        "price": 1000.0,
        "durationMinutes": 180,
        "isActive": true
    },
    {
        "id": 16,
        "serviceName": "Switch Installation",
        "description": "Electrical switch and socket installation",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    }
]
```

### 19. Get All Active Services
**URL:** `GET http://localhost:8080/provider-services`  
**Bearer Token:** ❌ Not required  
**Response:**
```json
[
    {
        "id": 1,
        "serviceName": "Switch & Socket Installation",
        "description": "Installation of electrical switches, sockets and boards",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    },
    {
        "id": 2,
        "serviceName": "Fan & Light Fitting",
        "description": "Ceiling fan and light fixture installation",
        "price": 250.0,
        "durationMinutes": 45,
        "isActive": true
    },
    {
        "id": 3,
        "serviceName": "Wiring Repair",
        "description": "Complete electrical wiring repair and troubleshooting",
        "price": 500.0,
        "durationMinutes": 120,
        "isActive": true
    },
    {
        "id": 4,
        "serviceName": "Tap & Faucet Repair",
        "description": "Leaking tap and faucet repair service",
        "price": 150.0,
        "durationMinutes": 30,
        "isActive": true
    },
    {
        "id": 5,
        "serviceName": "Pipe Installation",
        "description": "New water pipe installation and replacement",
        "price": 400.0,
        "durationMinutes": 90,
        "isActive": true
    },
    {
        "id": 6,
        "serviceName": "Bathroom Fitting",
        "description": "Complete bathroom fixture installation",
        "price": 600.0,
        "durationMinutes": 180,
        "isActive": true
    },
    {
        "id": 7,
        "serviceName": "Furniture Repair",
        "description": "Wooden furniture repair and polishing",
        "price": 300.0,
        "durationMinutes": 120,
        "isActive": true
    },
    {
        "id": 8,
        "serviceName": "Custom Cabinet Making",
        "description": "Custom wooden cabinet design and installation",
        "price": 1200.0,
        "durationMinutes": 240,
        "isActive": true
    },
    {
        "id": 9,
        "serviceName": "Door Repair",
        "description": "Wooden door repair and hinge replacement",
        "price": 250.0,
        "durationMinutes": 60,
        "isActive": true
    },
    {
        "id": 10,
        "serviceName": "AC Gas Charging",
        "description": "AC gas charging and pressure check",
        "price": 800.0,
        "durationMinutes": 90,
        "isActive": true
    },
    {
        "id": 11,
        "serviceName": "AC Servicing",
        "description": "Complete AC cleaning and maintenance",
        "price": 500.0,
        "durationMinutes": 120,
        "isActive": true
    },
    {
        "id": 12,
        "serviceName": "AC Installation",
        "description": "New AC unit installation",
        "price": 1000.0,
        "durationMinutes": 180,
        "isActive": true
    },
    {
        "id": 13,
        "serviceName": "Wall Painting",
        "description": "Interior wall painting service",
        "price": 180.0,
        "durationMinutes": 60,
        "isActive": true
    },
    {
        "id": 14,
        "serviceName": "Waterproofing",
        "description": "Terrace and bathroom waterproofing",
        "price": 1200.0,
        "durationMinutes": 240,
        "isActive": true
    },
    {
        "id": 15,
        "serviceName": "Wood Polish",
        "description": "Furniture and door wood polishing",
        "price": 400.0,
        "durationMinutes": 120,
        "isActive": true
    },
    {
        "id": 16,
        "serviceName": "Switch Installation",
        "description": "Electrical switch and socket installation",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    }
]
```

---

## 📅 BOOKING ENDPOINTS

### 20. Create Simple Booking
**URL:** `POST http://localhost:8080/bookings?providerId=1&dateTime=2025-01-15T10:00:00`  
**Bearer Token:** ✅ Required (User token)  
**Response:**
```json
{
    "id": 4,
    "service": null,
    "date": "2025-01-15T10:00:00",
    "status": "PENDING",
    "finalPrice": null,
    "durationMinutes": null,
    "userNotes": null,
    "serviceAddress": null
}
```

### 21. Create Booking with Service Details
**URL:** `POST http://localhost:8080/bookings/with-service?providerId=1&serviceId=1&dateTime=2025-01-15T10:00:00&userNotes=Need urgent service&serviceAddress=Lokmanya Nagar, Nagpur`  
**Bearer Token:** ✅ Required (User token)  
**Response:**
```json
{
    "id": 5,
    "service": {
        "id": 1,
        "serviceName": "Switch & Socket Installation",
        "description": "Installation of electrical switches, sockets and boards",
        "price": 200.0,
        "durationMinutes": 60,
        "isActive": true
    },
    "date": "2025-01-15T10:00:00",
    "status": "PENDING",
    "finalPrice": 200.0,
    "durationMinutes": 60,
    "userNotes": "Need urgent service",
    "serviceAddress": "Lokmanya Nagar, Nagpur"
}
```

### 22. Get User Bookings
**URL:** `GET http://localhost:8080/bookings/user`  
**Bearer Token:** ✅ Required (User token)  
**Response:**
```json
[
    {
        "id": 4,
        "service": null,
        "date": "2025-01-15T10:00:00",
        "status": "PENDING",
        "finalPrice": null,
        "durationMinutes": null,
        "userNotes": null,
        "serviceAddress": null
    },
    {
        "id": 5,
        "service": {
            "id": 1,
            "serviceName": "Switch & Socket Installation",
            "description": "Installation of electrical switches, sockets and boards",
            "price": 200.0,
            "durationMinutes": 60,
            "isActive": true
        },
        "date": "2025-01-15T10:00:00",
        "status": "PENDING",
        "finalPrice": 200.0,
        "durationMinutes": 60,
        "userNotes": "Need urgent service",
        "serviceAddress": "Lokmanya Nagar, Nagpur"
    }
]
```

### 23. Get Provider Bookings
**URL:** `GET http://localhost:8080/bookings/provider`  
**Bearer Token:** ✅ Required (Provider token)  
**Response:**
```json
[]
```

### 24. Update Booking Status
**URL:** `PUT http://localhost:8080/bookings/1/status?status=CONFIRMED`  
**Bearer Token:** ✅ Required (Provider token)  
**Status Values:** `PENDING`, `CONFIRMED`, `REJECTED`, `COMPLETED`

### 25. Cancel Booking
**URL:** `PUT http://localhost:8080/bookings/1/cancel`  
**Bearer Token:** ✅ Required (User/Provider token)

---

## 🐛 BACKEND ISSUES TO FIX

### 1. Search Providers - Case Sensitivity & Partial Matching
**Issue:** Search only looks for exact "Nagpur" instead of partial matches like "Lokmanya Nagar, Nagpur"
**Fix Needed:** Update repository to use LIKE queries with case-insensitive matching

### 2. Booking Status Update - 403 Forbidden
**Issue:** PUT `/bookings/{id}/status` returns 403 even with provider token
**Fix Needed:** Update SecurityConfig to properly authorize provider role for booking status updates

### 3. Booking Cancel - 403 Forbidden  
**Issue:** PUT `/bookings/{id}/cancel` returns 403 even with valid tokens
**Fix Needed:** Update SecurityConfig to allow both USER and PROVIDER roles for booking cancellation

### 4. Service Category Name Null
**Issue:** Provider registration response shows `"name": null` for serviceCategory
**Fix Needed:** Ensure service category name is properly populated in response

---

## 🎯 FRONTEND INTEGRATION GUIDE

### User Side Views:
- **Authentication:** Login/Register (Endpoints 1-3)
- **User Profile:** Get/Update profile (Endpoints 4-5)  
- **Service Discovery:** Browse providers/services (Endpoints 7-19)
- **Booking Management:** Create/view bookings (Endpoints 20-22, 25)

### Provider Side Views:
- **Provider Registration:** Complete provider details (Endpoint 6)
- **Service Management:** Add/view services (Endpoints 15-16)
- **Booking Management:** View/update booking status (Endpoints 23-24)

### Common Features:
- **Location-based Search:** Use endpoints 9-12 for distance filtering
- **Category Filtering:** Use endpoint 13 for categories
- **Service Search:** Use endpoint 18 for service name search

All endpoints are ready for frontend integration with complete response data preserved!
