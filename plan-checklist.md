# Banking App Checklist

This checklist is derived from [plan.md](plan.md) and tracks the project at a high level.

## 1. Phân tích & Thiết kế
- [x] List core features and use cases
- [x] Define main entities and database scope
- [x] Outline backend API contract

## 2. Setup Môi Trường
- [x] Backend project initialized
- [x] Frontend project initialized
- [x] SQL Server environment configured
- [x] Required development tools installed

## 3. Database
- [x] Create core tables
- [x] Create supporting tables
- [x] Add indexes for key lookups
- [x] Create core stored procedures
- [x] Seed demo data
- [x] Add triggers
- [x] Add views
- [x] Add backup and restore scripts
- [x] Add execution-plan evidence for optimization

## 4. Backend
- [x] Create Spring Boot backend skeleton
- [x] Add JWT authentication
- [x] Add login endpoint
- [x] Add account endpoints
- [x] Add transfer and transaction endpoints
- [x] Add beneficiary APIs
- [x] Add bill payment APIs
- [x] Add card APIs
- [x] Add notification APIs
- [x] Add audit log APIs
- [x] Add Swagger/OpenAPI docs
- [ ] Add backend tests

## 5. Frontend Web
- [x] Create React + Vite frontend
- [x] Add Ant Design UI
- [x] Add login screen
- [x] Add dashboard screen
- [x] Add account cards
- [x] Add transfer modal
- [x] Add API client and auth store
- [ ] Add register page
- [ ] Add separate transfer page
- [ ] Add transaction history page
- [ ] Add beneficiaries page
- [ ] Add bill payment page
- [ ] Add cards page
- [ ] Add notifications page
- [ ] Add profile page
- [ ] Add admin screens
- [ ] Add frontend tests

## 6. Kiểm thử & Hoàn thiện
- [x] Verify backend login flow
- [x] Verify account/transaction endpoints
- [ ] Create Postman collection
- [ ] Add automated tests
- [ ] Finish documentation screenshots

## 7. Mobile App
- [ ] Start Android project
- [ ] Add Retrofit API layer
- [ ] Build mobile screens
- [ ] Connect mobile app to backend
