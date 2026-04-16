# 📖 LMS API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication

All endpoints (except `/auth/**` and Swagger UI) require JWT authentication.

### Headers
```
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

---

## 🔐 Authentication Endpoints

### Register User
```
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "Password123!",
  "firstName": "John",
  "lastName": "Doe",
  "role": "TEACHER"  # ADMIN, TEACHER, STUDENT
}

Response: 201 Created
{
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "token_type": "Bearer",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "TEACHER",
    "isActive": true
  }
}
```

### Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "Password123!"
}

Response: 200 OK
{
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "token_type": "Bearer",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "TEACHER",
    "isActive": true
  }
}
```

---

## 👥 User Endpoints

### Get All Users
```
GET /users
Authorization: Bearer <token>
Roles: ADMIN, TEACHER, STUDENT

Response: 200 OK
[
  {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "TEACHER",
    "isActive": true
  }
]
```

### Get User by ID
```
GET /users/{id}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "TEACHER",
  "isActive": true
}
```

### Create User
```
POST /users
Authorization: Bearer <token>
Roles: ADMIN

{
  "email": "new@example.com",
  "password": "SecurePass123!",
  "firstName": "Jane",
  "lastName": "Smith",
  "role": "STUDENT"
}

Response: 201 Created
```

### Update User
```
PUT /users/{id}
Authorization: Bearer <token>
Roles: ADMIN

{
  "firstName": "Jane",
  "lastName": "Smith",
  "role": "STUDENT"
}

Response: 200 OK
```

### Delete User
```
DELETE /users/{id}
Authorization: Bearer <token>
Roles: ADMIN

Response: 204 No Content
```

---

## 📚 Student Endpoints

### Get All Students
```
GET /students
Authorization: Bearer <token>
Roles: ADMIN, TEACHER

Response: 200 OK
[
  {
    "id": 1,
    "user": { "id": 1, "email": "student@example.com", ... },
    "studentNumber": "STU001",
    "group": { ... }
  }
]
```

### Get Student by ID
```
GET /students/{id}
Authorization: Bearer <token>
```

### Create Student
```
POST /students
Authorization: Bearer <token>
Roles: ADMIN, TEACHER

{
  "userId": 1,
  "studentNumber": "STU001",
  "groupId": 1
}

Response: 201 Created
```

### Get Student by Student Number
```
GET /students/student-number/{studentNumber}
Authorization: Bearer <token>
```

### Get Students by Group
```
GET /students/group/{groupId}
Authorization: Bearer <token>
```

---

## 🏫 Group Endpoints

### Get All Groups
```
GET /groups
Authorization: Bearer <token>
Roles: ADMIN, TEACHER
```

### Get Group by ID
```
GET /groups/{id}
```

### Get Group by Group Number
```
GET /groups/number/{groupNumber}
```

### Create Group
```
POST /groups
Authorization: Bearer <token>
Roles: ADMIN, TEACHER

{
  "groupNumber": "CS-101",
  "specialtyId": 1
}

Response: 201 Created
```

### Update Group
```
PUT /groups/{id}
Authorization: Bearer <token>

{
  "specialtyId": 1
}
```

### Delete Group
```
DELETE /groups/{id}
Authorization: Bearer <token>

Response: 204 No Content
```

---

## ✍️ Attendance Endpoints

### Mark Attendance
```
POST /attendance/mark
Authorization: Bearer <token>
Roles: TEACHER (Only)

{
  "lessonId": 1,
  "studentId": 5,
  "status": "PRESENT",  # PRESENT, ABSENT
  "remarks": "Late arrival"
}

Response: 201 Created (if successful)
OR
{
  "warning": true,
  "message": "Cannot change ABSENT to PRESENT after 15 minutes of lesson start",
  "currentStatus": "ABSENT",
  "requestedStatus": "PRESENT",
  "attendanceId": 1
}
```

### Get Student's Attendance
```
GET /attendance/student/{studentId}
Authorization: Bearer <token>
Roles: ADMIN, TEACHER, STUDENT

Response: 200 OK
[
  {
    "id": 1,
    "lessonId": 1,
    "student": { ... },
    "status": "PRESENT",
    "markedAt": "2026-04-16T10:30:00",
    "lastModifiedAt": "2026-04-16T10:30:00",
    "remarks": "On time"
  }
]
```

### Get Lesson's Attendance
```
GET /attendance/lesson/{lessonId}
Authorization: Bearer <token>
Roles: ADMIN, TEACHER
```

---

## 📊 Grade Endpoints

### Get All Grades
```
GET /grades
Authorization: Bearer <token>
```

### Get Grade by ID
```
GET /grades/{id}
```

### Create Grade
```
POST /grades
Authorization: Bearer <token>

{
  "studentId": 5,
  "subjectId": 3,
  "teacherId": 2,
  "attendanceScore": 8,
  "seminarScore": 7,
  "col1": 6,
  "col2": 7,
  "col3": 8,
  "examScore": 35
}

Response: 201 Created
```

### Get Grades by Student
```
GET /grades/student/{studentId}
```

### Get Grades by Teacher
```
GET /grades/teacher/{teacherId}
```

---

## ⚠️ Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2026-04-16T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input data",
  "path": "/api/v1/users"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

### 403 Forbidden
```json
{
  "error": "Forbidden",
  "message": "Access denied"
}
```

### 404 Not Found
```json
{
  "timestamp": "2026-04-16T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999",
  "path": "/api/v1/users/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2026-04-16T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "User already exists with email: existing@example.com",
  "path": "/api/v1/auth/register"
}
```

---

## 🔄 Common Workflow

### 1. Register as Teacher
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teacher@school.com",
    "password": "SecurePass123!",
    "firstName": "John",
    "lastName": "Smith",
    "role": "TEACHER"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teacher@school.com",
    "password": "SecurePass123!"
  }'
```

### 3. Save JWT Token
From the response, extract: `access_token`

### 4. Mark Attendance
```bash
curl -X POST http://localhost:8080/api/v1/attendance/mark \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "lessonId": 1,
    "studentId": 5,
    "status": "PRESENT",
    "remarks": "On time"
  }'
```

---

## 🎯 Role-Based Access Matrix

| Endpoint | ADMIN | TEACHER | STUDENT |
|----------|-------|---------|---------|
| GET /users | ✅ | ✅ (read) | ✅ (self) |
| POST /users | ✅ | ❌ | ❌ |
| PUT /users | ✅ | ❌ | ❌ |
| DELETE /users | ✅ | ❌ | ❌ |
| GET /students | ✅ | ✅ | ❌ |
| POST /students | ✅ | ✅ | ❌ |
| GET /groups | ✅ | ✅ | ❌ |
| POST /attendance/mark | ❌ | ✅ | ❌ |
| GET /attendance | ✅ | ✅ | ✅ (own) |
| GET /grades | ✅ | ✅ | ✅ (own) |

---

## 📞 Support

For more information, visit:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/v3/api-docs`
- GitHub: [Your Repository]


