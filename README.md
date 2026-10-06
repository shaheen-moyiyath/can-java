# Canteen Ordering System (Pure Java Stack + Relational SQL)

An enterprise-ready, digital meal ordering and kitchen preparation platform for college canteens, converted from the technical specification and report from **Amal College of Advanced Studies (Department of Computer Science)** into a pure **Java 17+ / Spring Boot 3.3.x** stack with a normalized **Relational SQL Database (H2 & MySQL)**, **Spring Security 6**, and **JWT Authentication**.

---

## 1. Architecture & Design Overview

```
                      +---------------------------------------+
                      |         Web Browser Clients           |
                      |  - Student Portal:  http://.../       |
                      |  - Admin Dashboard: http://.../admin  |
                      |  - Swagger UI:      /swagger-ui.html  |
                      +-------------------+-------------------+
                                          |
                                          | HTTP / JSON REST + JWT Bearer
                                          v
                      +---------------------------------------+
                      |       Spring Boot 3 REST API          |
                      |  - JwtAuthenticationFilter            |
                      |  - Role-Based Access (STUDENT, ADMIN) |
                      |  - GlobalExceptionHandler             |
                      +-------------------+-------------------+
                                          |
                        +-----------------+-----------------+
                        |                 |                 |
                        v                 v                 v
                 [ AuthService ]   [ MenuService ]   [ OrderService & ReportService ]
                        |                 |                 |
                        +-----------------+-----------------+
                                          |
                                          v
                      +---------------------------------------+
                      |        Spring Data JPA Layer          |
                      |  - Hibernate ORM                      |
                      |  - Aggregation Queries (GROUP BY/SUM) |
                      +-------------------+-------------------+
                                          |
                                          v
                      +---------------------------------------+
                      |       Relational SQL Database         |
                      |  - H2 Database (In-Memory/Dev)        |
                      |  - MySQL 8.x (Production Profile)     |
                      +---------------------------------------+
```

---

## 2. Tech Stack

- **Language & Runtime**: Java 17+ (Fully tested on Java 26 & Java 17 LTS)
- **Framework**: Spring Boot 3.3.4
- **Security & RBAC**: Spring Security 6 + JJWT 0.12.5 (Stateless Bearer Tokens)
- **Data Persistence**: Spring Data JPA / Hibernate 6.5
- **Databases**:
  - **Embedded H2**: Zero-configuration default with MySQL syntax compatibility mode
  - **MySQL 8.x**: Production-ready configuration profile (`mysql`)
- **API Documentation**: SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui.html`)
- **Build Tool**: Maven (`pom.xml`, `mvnw.cmd`)
- **Frontend UI**: Integrated Responsive Single-Page Application (HTML5, Tailwind CSS, Vanilla JS)

---

## 3. Project File Structure

```
d:/can-java/
├── pom.xml                               # Maven project definition and dependencies
├── mvnw.cmd                              # Maven Wrapper script for Windows
├── schema-mysql.sql                      # Production MySQL DDL script
├── README.md                             # Comprehensive project documentation
├── src/
│   ├── main/
│   │   ├── java/com/canteen/
│   │   │   ├── CanteenApplication.java   # Spring Boot entry point
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java         # Security filter chain, CORS, RBAC
│   │   │   │   ├── JwtTokenProvider.java       # JWT token generation and validation
│   │   │   │   ├── JwtAuthenticationFilter.java# Bearer token interception filter
│   │   │   │   ├── OpenApiConfig.java          # Swagger OpenAPI 3 with JWT scheme
│   │   │   │   └── DataInitializer.java        # Automatic DB seed verification
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java         # /api/auth (login, register, me)
│   │   │   │   ├── MenuController.java         # /api/menu (browse, add, edit, delete)
│   │   │   │   ├── OrderController.java        # /api/orders (place order, history)
│   │   │   │   └── AdminController.java        # /api/admin (tallies, verification, reports)
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponse.java            # Standard API envelope
│   │   │   │   ├── AuthRequest.java            # Login payload
│   │   │   │   ├── AuthResponse.java           # JWT token + user details
│   │   │   │   ├── RegisterRequest.java        # Student registration payload
│   │   │   │   ├── MenuItemRequest.java        # Menu creation/update payload
│   │   │   │   ├── MenuItemResponse.java       # Menu item JSON response
│   │   │   │   ├── OrderRequest.java           # Order placement payload
│   │   │   │   ├── OrderItemRequest.java       # Individual order item selection
│   │   │   │   ├── OrderResponse.java          # Detailed order representation
│   │   │   │   ├── OrderItemResponse.java      # Order line item representation
│   │   │   │   ├── OrderStatusUpdateRequest.java # Status transition request
│   │   │   │   ├── KitchenTallyResponse.java   # Aggregated dish counter
│   │   │   │   ├── DailyReportResponse.java    # Volume & revenue summary
│   │   │   │   └── ItemSalesResponse.java      # Item-by-item sales breakdown
│   │   │   ├── entity/
│   │   │   │   ├── User.java                   # Users entity (STUDENT, ADMIN)
│   │   │   │   ├── Role.java                   # Enum: STUDENT, ADMIN
│   │   │   │   ├── MenuItem.java               # Dishes, prices, availability
│   │   │   │   ├── Order.java                  # Orders, total amount, status, date
│   │   │   │   ├── OrderItem.java              # Order line items with historical unit_price
│   │   │   │   └── OrderStatus.java            # Enum: PENDING, PREPARING, COMPLETED, CANCELLED
│   │   │   ├── exception/
│   │   │   │   ├── ApiException.java           # Custom base runtime exception
│   │   │   │   ├── BadRequestException.java    # 400 Bad Request
│   │   │   │   ├── ResourceNotFoundException.java # 404 Not Found
│   │   │   │   ├── UnauthorizedException.java  # 401 / 403 Access Denied
│   │   │   │   └── GlobalExceptionHandler.java # Centralized @ControllerAdvice
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java         # User JPA repository
│   │   │   │   ├── MenuItemRepository.java     # Menu JPA repository
│   │   │   │   ├── OrderRepository.java        # Native live kitchen tally aggregation
│   │   │   │   ├── OrderItemRepository.java    # Native daily item popularity sales query
│   │   │   │   ├── KitchenTallyProjection.java # Projection for live kitchen tally
│   │   │   │   └── ItemSalesProjection.java    # Projection for daily report breakdown
│   │   │   └── service/
│   │   │       ├── AuthService.java            # User registration & BCrypt login
│   │   │       ├── CustomUserDetailsService.java# Spring Security UserDetails loader
│   │   │       ├── MenuService.java            # Menu CRUD & availability toggling
│   │   │       ├── OrderService.java           # Transactional order submission
│   │   │       └── ReportService.java          # Kitchen tallies & daily report analytics
│   │   └── resources/
│   │       ├── application.yml                 # Default H2 configuration & JWT secret
│   │       ├── application-mysql.yml           # MySQL production profile configuration
│   │       ├── schema.sql                      # ANSI SQL DDL script
│   │       ├── data.sql                        # Seed data (admin, students, initial dishes)
│   │       └── static/
│   │           ├── index.html                  # Student Portal (Menu, Cart, Orders)
│   │           ├── admin.html                  # Admin Dashboard (Live Tallies, Orders, Reports)
│   │           ├── app.js                      # Client state management & fetch API
│   │           └── style.css                   # Custom responsive styling
│   └── test/java/com/canteen/
│       └── CanteenApplicationTests.java        # Full MockMvc integration test suite
```

---

## 4. Relational Database Schema (SQL DDL)

### 4.1 Normalized Tables

```sql
-- 1. Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Menu Items Table
CREATE TABLE menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Orders Table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    order_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Order Items Table
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_menu FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);
```

### 4.2 Signature Live Kitchen Tally Query
As specified in the technical report (Section 4.4, page 21), real-time kitchen preparation tallies are computed using an outer join aggregation excluding cancelled orders:

```sql
SELECT 
    m.id AS itemId, 
    m.name AS itemName, 
    COALESCE(SUM(oi.quantity), 0) AS totalQuantity
FROM menu_items m
LEFT JOIN order_items oi ON m.id = oi.menu_item_id
LEFT JOIN orders o ON oi.order_id = o.id AND o.order_date = :orderDate AND o.status <> 'CANCELLED'
GROUP BY m.id, m.name
ORDER BY totalQuantity DESC, m.name ASC;
```

---

## 5. Default User Accounts & Seed Data

The application pre-seeds both Admin and Student accounts (passwords hashed with BCrypt):

| Role | Username / ID | Password | Full Name | Email |
| :--- | :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | Canteen Administrator | `admin@canteen.college.edu` |
| **STUDENT** | `student1` | `student123` | Muhammed Shaheen | `shaheen@student.college.edu` |
| **STUDENT** | `AZAYSCS043` | `student123` | Sainul Ashiqu N | `sainul@student.college.edu` |

Initial Menu Dishes:
1. **Chicken Biryani** - ₹120.00 (*Biryani*)
2. **Veg Thali / Kerala Meals** - ₹60.00 (*Meals*)
3. **Fish Curry Meals** - ₹90.00 (*Meals*)
4. **Masala Dosa** - ₹50.00 (*Breakfast*)
5. **Egg Puffs** - ₹20.00 (*Snacks*)
6. **Chicken Roll** - ₹60.00 (*Snacks*)
7. **Tea / Chai** - ₹12.00 (*Beverages*)
8. **Cold Coffee** - ₹35.00 (*Beverages*)

---

## 6. How to Run the Application

### Option A: Running with Embedded H2 (Default, Zero-Config)
No external database installation needed.

1. Open PowerShell or Terminal in the project directory:
   ```powershell
   cd d:\can-java
   ```
2. Run using Maven Wrapper:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
   *(Or with system Maven: `mvn spring-boot:run`)*

3. Access the application in your browser:
   - **Student Portal**: [http://localhost:8080/](http://localhost:8080/)
   - **Admin Dashboard**: [http://localhost:8080/admin.html](http://localhost:8080/admin.html)
   - **Interactive Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
     *(JDBC URL: `jdbc:h2:mem:canteendb`, Username: `sa`, Password: empty)*

---

### Option B: Running with MySQL in Production

1. Ensure MySQL is running on port `3306`.
2. Run the MySQL DDL script:
   ```bash
   mysql -u root -p < schema-mysql.sql
   ```
3. Set your environment variables (optional if using defaults `root` / `root`):
   ```powershell
   $env:DB_HOST="localhost"
   $env:DB_PORT="3306"
   $env:DB_NAME="canteen_db"
   $env:DB_USER="root"
   $env:DB_PASSWORD="your_mysql_password"
   ```
4. Start the Spring Boot application with the `mysql` profile:
   ```powershell
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

### Option C: Building & Running the Executable JAR

1. Package the project:
   ```powershell
   .\mvnw.cmd clean package
   ```
2. Run the generated production JAR:
   ```powershell
   java -jar target/canteen-ordering-system-1.0.0.jar
   ```

---

## 7. REST API Endpoints Specification

### 7.1 Authentication (`/api/auth`)

#### `POST /api/auth/login`
Authenticates a student or admin and returns a JWT token.
```json
// Request Body:
{
  "username": "student1",
  "password": "student123"
}

// Response (200 OK):
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "userId": 2,
    "username": "student1",
    "fullName": "Muhammed Shaheen",
    "email": "shaheen@student.college.edu",
    "role": "STUDENT"
  }
}
```

#### `POST /api/auth/register`
Registers a new student account.
```json
// Request Body:
{
  "username": "AZAYSCS099",
  "password": "mypassword123",
  "fullName": "New Student Name",
  "email": "student99@college.edu"
}
```

#### `GET /api/auth/me`
Header: `Authorization: Bearer <token>`  
Returns the profile details of the current authenticated user.

---

### 7.2 Menu Management (`/api/menu`)

#### `GET /api/menu`
Publicly accessible. Retrieves all items currently active and available for ordering today.

#### `GET /api/menu/all`
Admin endpoint. Retrieves all catalog items including disabled items.

#### `POST /api/menu` *(Admin Only)*
Header: `Authorization: Bearer <ADMIN_TOKEN>`
```json
{
  "name": "Chicken Shawarma",
  "description": "Grilled marinated chicken with garlic mayonnaise in rumali roti",
  "price": 80.00,
  "category": "Snacks",
  "is_available": true
}
```

#### `PUT /api/menu/{id}` *(Admin Only)*
Updates item name, category, price, or description.

#### `PATCH /api/menu/{id}/toggle-availability?available=false` *(Admin Only)*
Quick toggle to enable/disable dish availability for the day without deleting the item.

#### `DELETE /api/menu/{id}` *(Admin Only)*
Deletes the dish from the catalog.

---

### 7.3 Order Operations (`/api/orders`)

#### `POST /api/orders` *(Authenticated Student)*
Header: `Authorization: Bearer <STUDENT_TOKEN>`  
Places an order with single or multi-dish selection and quantities. Prices are calculated strictly on the backend to prevent client tampering.
```json
// Request Body:
{
  "items": [
    { "item_id": 1, "quantity": 2 },
    { "item_id": 5, "quantity": 1 }
  ]
}

// Response (201 Created):
{
  "success": true,
  "message": "Order placed successfully! Order ID: #3",
  "data": {
    "id": 3,
    "user_id": 2,
    "username": "student1",
    "student_name": "Muhammed Shaheen",
    "total_amount": 260.00,
    "status": "PENDING",
    "order_date": "2026-10-05",
    "items": [
      {
        "id": 4,
        "item_id": 1,
        "item_name": "Chicken Biryani",
        "quantity": 2,
        "unit_price": 120.00,
        "subtotal": 240.00
      },
      {
        "id": 5,
        "item_id": 5,
        "item_name": "Egg Puffs",
        "quantity": 1,
        "unit_price": 20.00,
        "subtotal": 20.00
      }
    ]
  }
}
```

#### `GET /api/orders/my-history` *(Authenticated Student)*
Retrieves all historical orders placed by the currently logged-in student, ordered from newest to oldest.

---

### 7.4 Admin Live Dashboard & Daily Reports (`/api/admin`)
All endpoints in this section require `Authorization: Bearer <ADMIN_TOKEN>`.

#### `GET /api/admin/dashboard/live-counts`
Calculates aggregated dish portion requirements for the kitchen:
```json
{
  "success": true,
  "message": "Live kitchen tallies retrieved successfully",
  "data": [
    { "item_id": 1, "name": "Chicken Biryani", "total_quantity": 35 },
    { "item_id": 2, "name": "Veg Thali / Kerala Meals", "total_quantity": 52 },
    { "item_id": 5, "name": "Egg Puffs", "total_quantity": 20 }
  ]
}
```

#### `GET /api/admin/orders?date=YYYY-MM-DD`
Detailed view of who placed which order with student name, college ID, items, time, and status.

#### `PUT /api/admin/orders/{id}/status`
Updates order status across lifecycle phases:
```json
// Request Body:
{
  "status": "COMPLETED" // Options: PENDING, PREPARING, COMPLETED, CANCELLED
}
```

#### `GET /api/admin/reports/daily?date=YYYY-MM-DD`
Generates comprehensive daily audit report including total volume, revenue, and dish popularity breakdown:
```json
{
  "success": true,
  "data": {
    "report_date": "2026-10-05",
    "total_orders": 12,
    "pending_orders": 2,
    "preparing_orders": 4,
    "completed_orders": 6,
    "cancelled_orders": 0,
    "total_revenue": 1420.00,
    "item_breakdown": [
      {
        "item_id": 1,
        "item_name": "Chicken Biryani",
        "category": "Biryani",
        "quantity_sold": 8,
        "total_sales": 960.00
      },
      {
        "item_id": 2,
        "item_name": "Veg Thali / Kerala Meals",
        "category": "Meals",
        "quantity_sold": 6,
        "total_sales": 360.00
      }
    ]
  }
}
```

---

## 8. Automated Test Coverage

The project includes automated integration tests in `src/test/java/com/canteen/CanteenApplicationTests.java`:
- Spring ApplicationContext initialization
- Public menu browsing without authentication
- Student login & JWT issuance
- Admin login & JWT with `ROLE_ADMIN`
- Student registration, transactional order placement, and history retrieval
- Admin live kitchen tally native query verification
- Admin daily report aggregation
- Protection of admin-only endpoints against unauthorized access

To run tests:
```powershell
.\mvnw.cmd test
```
Result: **`Tests run: 7, Failures: 0, Errors: 0, Skipped: 0 - BUILD SUCCESS`**.
