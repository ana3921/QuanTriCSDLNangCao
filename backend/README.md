# Banking App Backend (Spring Boot)

## Project Structure

```
src/main/java/com/banking/
├── BankingAppApplication.java        # Main Spring Boot app
├── config/
│   └── WebConfig.java                # CORS and web configuration
├── controller/
│   ├── AuthController.java           # Authentication endpoints
│   ├── AccountController.java        # Account management endpoints
│   └── TransactionController.java    # Transaction endpoints
├── dto/
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── AccountDTO.java
│   ├── TransferRequest.java
│   └── TransferResponse.java
├── entity/
│   ├── User.java
│   ├── Customer.java
│   ├── Account.java
│   ├── Transaction.java
│   └── Notification.java
├── exception/
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── UserRepository.java
│   ├── CustomerRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   └── NotificationRepository.java
└── service/
    ├── UserService.java
    ├── JwtTokenProvider.java
    ├── AccountService.java
    └── TransactionService.java
```

## Prerequisites

- Java 17+
- Maven 3.6+
- SQL Server with `banking_db` database already created (from db migrations)

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=banking_db;encrypt=false;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword
```

## Running the Backend

```bash
mvn clean install
mvn spring-boot:run
```

Server will start at `http://localhost:8080/api`

## API Endpoints

### Authentication
- **POST** `/api/auth/login` — Login with username/password

### Accounts
- **GET** `/api/accounts/customer/{customerId}` — Get all accounts for a customer
- **GET** `/api/accounts/number/{accountNumber}` — Get account by number
- **GET** `/api/accounts/customer/{customerId}/active` — Get active accounts

### Transactions
- **POST** `/api/transactions/transfer` — Transfer money between accounts
- **GET** `/api/transactions/account/{accountId}` — Get transaction history

## Next Steps

1. Test endpoints with Postman or curl
2. Build the React frontend to consume these APIs
3. Add more endpoints as needed (bill payments, beneficiaries, etc.)

