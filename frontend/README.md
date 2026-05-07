# Banking App - Frontend

React + Vite + Ant Design web application for the banking system.

## Features
- **Authentication**: JWT-based login with token management
- **Account Management**: View multiple accounts with balances
- **Transactions**: View transaction history and transfer money
- **Responsive UI**: Mobile-friendly design with Ant Design components

## Tech Stack
- **React 18** - UI library
- **Vite** - Fast build tool and dev server
- **Ant Design** - Component library
- **Axios** - HTTP client
- **React Router** - Navigation
- **Zustand** - State management

## Setup

### Prerequisites
- Node.js 16+ and npm

### Installation
```bash
npm install
```

### Environment Setup
Copy `.env.example` to `.env` and update if needed:
```bash
cp .env.example .env
```

### Development
```bash
npm run dev
```
The app will be available at `http://localhost:3000`

The dev server proxies `/api/*` requests to `http://localhost:8080` (backend).

### Build
```bash
npm run build
```

### Preview
```bash
npm run preview
```

## Project Structure
```
src/
├── components/          # Reusable UI components
│   ├── AccountCard.jsx
│   ├── Navbar.jsx
│   └── TransactionModal.jsx
├── pages/              # Page components
│   ├── LoginPage.jsx
│   └── DashboardPage.jsx
├── services/           # API client
│   └── api.js
├── store/              # Zustand store
│   └── authStore.js
├── styles/             # Global styles
│   └── index.css
├── App.jsx             # Main app component
└── main.jsx            # React entry point
```

## Demo Credentials
- **Username**: `customer1`
- **Password**: `password123`

## API Integration
The frontend communicates with the backend at `http://localhost:8080/api`. Ensure the backend is running before starting the dev server.

### Endpoints Used
- `POST /api/auth/login` - User login
- `GET /api/accounts/customer/{customerId}` - Get customer accounts
- `GET /api/transactions/account/{accountId}` - Get transaction history
- `POST /api/transactions/transfer` - Transfer money

## Notes
- Authentication tokens are stored in localStorage
- Automatic token-based request authentication via axios interceptors
- Unauthorized requests (401) trigger redirect to login
