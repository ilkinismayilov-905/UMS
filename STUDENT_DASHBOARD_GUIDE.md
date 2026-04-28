# Student Dashboard Feature - Implementation Guide

## Overview
A secure REST API for students to view their dashboard information including profile, academic status, and enrolled subjects. The implementation follows Spring Security best practices with Role-Based Access Control (RBAC) and no exposure of student IDs in URL paths.

## Architecture Components

### 1. Data Transfer Objects (DTOs)

#### StudentProfileResponse
**File**: `StudentProfileResponse.java`
- Returns the authenticated student's profile information
- **Fields**:
  - `firstName` - Student's first name
  - `lastName` - Student's last name
  - `email` - Student's email address
  - `groupNumber` - Academic group number
  - `studentNumber` - Unique student identifier
  - `specialty` - Major/Specialty name

#### SubjectAcademicStatusResponse
**File**: `SubjectAcademicStatusResponse.java`
- Returns subject-specific academic information
- **Fields**:
  - `subjectId` - Subject identifier
  - `subjectName` - Subject name
  - `credits` - Course credits
  - `grades` - List of all total scores for the subject
  - `totalAbsences` - Total absence count for the subject

#### EnrolledSubjectResponse
**File**: `EnrolledSubjectResponse.java`
- Returns basic subject information for enrolled subjects
- **Fields**:
  - `id` - Subject identifier
  - `name` - Subject name
  - `credits` - Course credits
  - `absenceLimit` - Maximum allowed absences

### 2. Repository Query Methods

#### StudentRepository.java
**New Method**: `findByUserIdWithDetails(Long userId)`
- Returns a Student with eagerly loaded user, group, and specialty details
- **Purpose**: Avoids N+1 queries by using JOIN FETCH
- **Used by**: Student Profile endpoint

#### GradeRepository.java
**New Method**: `findAllByStudentIdWithSubjectDetails(Long studentId)`
- Returns all grades for a student with subject information pre-loaded
- **Purpose**: Prevents N+1 queries when loading grades and their associated subjects
- **Used by**: Academic Status endpoint

#### TeacherGroupSubjectRepository.java
**New Method**: `findDistinctSubjectsByGroupIdWithDetails(Long groupId)`
- Returns distinct subjects assigned to a group
- **Purpose**: Gets all subjects a student is enrolled in via their group
- **Used by**: Enrolled Subjects endpoint

### 3. Mappers

#### StudentDashboardMapper.java
**Type**: MapStruct interface (componentModel = "spring")
- Maps entities to dashboard DTOs
- **Methods**:
  - `toStudentProfileResponse(Student)` - Maps Student entity to profile DTO
  - `toEnrolledSubjectResponse(Subject)` - Maps Subject entity to enrolled subject DTO

### 4. Service Layer

#### StudentDashboardService.java
**Type**: Spring Service (`@Service` with `@Transactional(readOnly = true)`)

**Methods**:

1. **`getStudentProfile(Long userId)`**
   - Retrieves authenticated student's profile
   - Uses `findByUserIdWithDetails()` for optimized loading
   - Thrown exceptions: `StudentNotFoundException`

2. **`getStudentAcademicStatus(Long userId)`**
   - Returns academic status grouped by subject
   - Fetches grades using optimized query
   - Groups grades by subject dynamically
   - Retrieves absence count per subject
   - Returns: `List<SubjectAcademicStatusResponse>`

3. **`getStudentEnrolledSubjects(Long userId)`**
   - Gets subjects enrolled for the student's group
   - Uses optimized query to fetch distinct subjects
   - Returns: `List<EnrolledSubjectResponse>`

### 5. REST Controller

#### StudentDashboardController.java
**Type**: Spring REST Controller
**Base Path**: `/api/dashboard/student`
**Security**: All endpoints require `@PreAuthorize("hasRole('STUDENT')")`
**Authentication**: Uses Spring Security `Authentication` context

**Endpoints**:

##### 1. GET /api/dashboard/student/profile
- **Description**: Retrieve authenticated student's profile information
- **Security**: STUDENT role required
- **Authentication Method**: @AuthenticationPrincipal or Authentication parameter
- **Response**: `StudentProfileResponse`
- **Example Response**:
```json
{
  "firstName": "Ilkin",
  "lastName": "Ismayilov",
  "email": "ilkin@example.com",
  "groupNumber": "BS-001",
  "studentNumber": "STU-2024-001",
  "specialty": "Computer Science"
}
```

##### 2. GET /api/dashboard/student/academic-status
- **Description**: Retrieve academic status with grades and absences per subject
- **Security**: STUDENT role required
- **Response**: `List<SubjectAcademicStatusResponse>`
- **Example Response**:
```json
[
  {
    "subjectId": 1,
    "subjectName": "Data Structures",
    "credits": 4,
    "grades": [85, 90, 88],
    "totalAbsences": 2
  },
  {
    "subjectId": 2,
    "subjectName": "Algorithms",
    "credits": 3,
    "grades": [92, 95],
    "totalAbsences": 0
  }
]
```

##### 3. GET /api/dashboard/student/subjects
- **Description**: Retrieve list of enrolled subjects
- **Security**: STUDENT role required
- **Response**: `List<EnrolledSubjectResponse>`
- **Example Response**:
```json
[
  {
    "id": 1,
    "name": "Data Structures",
    "credits": 4,
    "absenceLimit": 5
  },
  {
    "id": 2,
    "name": "Algorithms",
    "credits": 3,
    "absenceLimit": 3
  }
]
```

## Security Implementation

### Key Security Features

1. **No Student ID in URLs**
   - All endpoints extract user ID from Spring Security context
   - Uses `Authentication.getPrincipal()` to get `UserDetailsImpl`
   - No path variables for student/user identification

2. **Role-Based Access Control (RBAC)**
   - All endpoints secured with `@PreAuthorize("hasRole('STUDENT')")`
   - SecurityConfig updated to restrict `/api/dashboard/student/**` to STUDENT role
   - Method-level security enabled with `@EnableMethodSecurity(prePostEnabled = true)`

3. **Authentication Context Extraction**
   ```java
   UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
   Long userId = userDetails.getId();
   ```

### Security Configuration Update

**File**: `SecurityConfig.java`
- Added new security rule:
```java
.requestMatchers("/api/dashboard/student/**").hasRole("STUDENT")
```

## Performance Optimizations

### N+1 Query Prevention
The implementation uses optimized JPA queries with JOIN FETCH:

1. **Student Profile**: Single query fetches Student + User + Group + Specialty
2. **Academic Status**: Single query fetches all Grades with Subject details
   - Grouping done in-memory to avoid multiple queries
3. **Enrolled Subjects**: Single query fetches distinct Subjects for a group

### Database Query Examples

**Profile Query**:
```sql
SELECT s FROM Student s 
LEFT JOIN FETCH s.user u 
LEFT JOIN FETCH s.group g 
LEFT JOIN FETCH g.specialty sp 
WHERE s.user.id = ?
```

**Academic Status Query**:
```sql
SELECT g FROM Grade g 
LEFT JOIN FETCH g.subject s 
WHERE g.student.id = ? 
ORDER BY s.id ASC
```

**Enrolled Subjects Query**:
```sql
SELECT DISTINCT s FROM TeacherGroupSubject tgs 
LEFT JOIN FETCH tgs.subject s 
WHERE tgs.group.id = ?
```

## Error Handling

### Exception Types
- `StudentNotFoundException` - Thrown when student profile not found for the authenticated user

### HTTP Status Codes
- **200 OK**: Successful retrieval
- **401 Unauthorized**: Authentication required or invalid token
- **403 Forbidden**: User lacks STUDENT role
- **404 Not Found**: Student profile not found

## Testing Scenarios

### Sample cURL Requests

1. **Get Student Profile**:
```bash
curl -X GET http://localhost:8080/api/dashboard/student/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

2. **Get Academic Status**:
```bash
curl -X GET http://localhost:8080/api/dashboard/student/academic-status \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

3. **Get Enrolled Subjects**:
```bash
curl -X GET http://localhost:8080/api/dashboard/student/subjects \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Integration Steps

1. **Compile the project**:
```bash
mvn clean install
```

2. **Run the application**:
```bash
mvn spring-boot:run
```

3. **Access Swagger Documentation**:
```
http://localhost:8080/swagger-ui.html
```

4. **Authenticate as a STUDENT**:
   - Use the `/api/v1/auth/login` endpoint with student credentials
   - Receive JWT token
   - Use token for dashboard endpoints

## Database Requirements

The feature relies on existing database schema:
- `users` table - For user authentication details
- `students` table - Links users to students
- `academic_groups` table - Group information
- `specialties` table - Specialty/Major information
- `grades` table - Student grades
- `subjects` table - Subject definitions
- `student_subject_absences` table - Absence tracking
- `teacher_group_subjects` table - Subject enrollment per group

## Files Created/Modified

### New Files Created:
1. `/src/main/java/com/example/controller/StudentDashboardController.java` - REST Controller
2. `/src/main/java/com/example/service/StudentDashboardService.java` - Business Logic
3. `/src/main/java/com/example/dto/mapper/StudentDashboardMapper.java` - Entity Mapper
4. `/src/main/java/com/example/dto/response/StudentProfileResponse.java` - DTO
5. `/src/main/java/com/example/dto/response/SubjectAcademicStatusResponse.java` - DTO
6. `/src/main/java/com/example/dto/response/EnrolledSubjectResponse.java` - DTO

### Modified Files:
1. `/src/main/java/com/example/repository/StudentRepository.java` - Added optimized query methods
2. `/src/main/java/com/example/repository/GradeRepository.java` - Added optimized query methods
3. `/src/main/java/com/example/repository/TeacherGroupSubjectRepository.java` - Added optimized query methods
4. `/src/main/java/com/example/config/SecurityConfig.java` - Added security rule for dashboard endpoints

## Dependencies

The implementation uses existing project dependencies:
- Spring Security
- Spring Data JPA
- MapStruct
- Lombok
- Jakarta Persistence API

No new dependencies need to be added.

## Best Practices Implemented

✅ **No ID Exposure in URLs** - All IDs extracted from Security Context
✅ **RBAC Implementation** - Role-based access control at method and class level
✅ **DTO Mapping** - Entities never exposed directly, mapped to DTOs
✅ **Query Optimization** - JOIN FETCH prevents N+1 queries
✅ **Transactional Safety** - Read-only transactions for query operations
✅ **Logging & Monitoring** - All methods have appropriate logging
✅ **Exception Handling** - Proper exception types and HTTP status codes
✅ **Documentation** - Comprehensive JavaDoc and inline comments

## Support & Troubleshooting

### Common Issues

1. **401 Unauthorized Response**
   - Ensure JWT token is valid and not expired
   - Check token is sent in Authorization header with "Bearer " prefix

2. **403 Forbidden Response**
   - Verify user has STUDENT role
   - Check SecurityConfig has correct role restriction

3. **404 Student Not Found**
   - Ensure student record exists in database linked to the user
   - Verify student is assigned to a group

4. **Empty Grades/Subjects List**
   - Check if grades exist in database for the student
   - Verify TeacherGroupSubject records exist for the student's group

## Future Enhancements

- Add pagination for academic status and enrolled subjects
- Implement filtering by subject or grade range
- Add export functionality (PDF, Excel)
- Include performance metrics and GPA calculation
- Add notifications for absences near limit
- Implement real-time updates via WebSocket

