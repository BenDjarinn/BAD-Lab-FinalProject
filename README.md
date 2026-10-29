# SeRuput Teh - Desktop Application

A desktop-based tea sales management application built with JavaFX and MySQL.

## Tech Stack
- Java 17
- JavaFX 17
- MySQL
- JUnit 5 (unit testing)

## Dependencies
| Library | Purpose | Download |
|---------|---------|---------|
| JavaFX 17 | UI Framework | https://gluonhq.com/products/javafx/ |
| MySQL Connector/J | Database driver | https://dev.mysql.com/downloads/connector/j/ |

## Database Setup
1. Make sure MySQL is running
2. Import `seruput_teh.sql` into MySQL
3. Ensure database name is `seruput_teh`
4. Copy `db.properties.example` to `db.properties` and fill in your credentials:
   ```
   db.url=jdbc:mysql://127.0.0.1:3306/seruput_teh
   db.user=your_mysql_username
   db.password=your_mysql_password
   ```
   > ⚠️ `db.properties` is excluded from version control via `.gitignore` — never commit real credentials.

## How to Run
1. Clone this repository
2. Download JavaFX SDK from https://gluonhq.com/products/javafx/
3. Open project in Eclipse IDE (recommended)
4. Add JavaFX to Build Path:
   - Right-click Project Folder → Properties
   - Go to **Libraries** tab → click **Modulepath**
   - Click **Add External JARs** → select all `.jar` files from your JavaFX SDK `lib/` folder
5. Configure VM Arguments:
   - Right-click Project Folder → Run/Debug → Run Configurations
   - Go to **Arguments** tab → VM Arguments, add:
     ```
     --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
     ```
   - *(Adjust the path to match where you saved your JavaFX SDK)*
6. Also add MySQL Connector to Build Path (same way as step 4)
7. Place `db.properties` inside the `src/` folder so it is on the classpath
8. Run `Main.java`

## Default Credentials
| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | Admin |

## Testing
Unit tests are available in the `test/` folder:
- `ProductValidationTest` - validates product input logic
- `UserValidationTest` - validates user registration logic

To run: right-click the test file → Run As → JUnit Test

## Revamp Notes
- Centralized all database connections via Singleton pattern (previously hardcoded in 17 places across 6 files)
- Fixed encapsulation in `Database.java` (fields changed from public to private)
- Moved DB credentials out of source code into `db.properties` (excluded from version control)
- Added unit tests using JUnit 5

## What I Improved & Why

| # | Issue Found | Fix Applied | Impact |
|---|-------------|-------------|--------|
| 1 | Database credentials hardcoded in 17 places across 6 files | Centralized all connections via Singleton `Database.getInstance()` | Eliminates duplicate connections, single point of config change |
| 2 | `Database.java` fields were `public` | Changed all fields to `private` | Proper encapsulation, prevents direct field access from outside |
| 3 | `LoginView.java` had its own DB connection independent of Singleton | Refactored to use `Database.getInstance()` | Consistent connection management |
| 4 | No automated tests existed | Added 10 unit tests with JUnit 5 | Validates core business logic for product and user validation |
| 5 | Phone number validation logic was inverted | Fixed regex and accepted both `+62` and `08` formats | Register flow now works correctly |
| 6 | DB credentials (username & password) hardcoded in `Database.java` | Moved to `db.properties`, loaded at runtime, excluded via `.gitignore` | Credentials no longer exposed in version control |

## Test Coverage Summary

| Test Class | Test Cases | Status |
|------------|------------|--------|
| ProductValidationTest | 5 | ✅ All Passed |
| UserValidationTest | 5 | ✅ All Passed |
| **Total** | **10** | **10/10 Passed** |

## Known Issues (Identified, Pending Fix)

| # | Location | Issue |
|---|----------|-------|
| 1 | `ManageProductView.java` | Update Product form only allows price update, should allow full product edit (name, price, description) |
| 2 | `CartSceneView.java` & `TransactionView.java` | Quantity parsed via `charAt(0)` — breaks if quantity > 9 |
| 3 | `CartSceneViewController.java` | `updateCartItem()` has hardcoded `userID = "CU001"` |
| 4 | `HomeViewController.java` & `CartSceneViewController.java` | `fetchProductData()` and `fetchUserData()` duplicated across both controllers |
| 5 | `HomeViewController.java` | Hardcoded name `"Welcome, Stefanie"` in `handleProductSelection()` |
| 6 | `RegisterController.java` & DB | User passwords stored as plain text — should be hashed using BCrypt before storing |