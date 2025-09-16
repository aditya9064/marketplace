# Campus Marketplace

A Spring Boot-based marketplace application designed for university campuses, allowing students to buy and sell items within their campus community.

## Features

### Current Features
- User authentication with JWT
- User registration and login
- Product management (CRUD operations)
- Category-based product organization
- Condition tracking for items
- Secure endpoints with role-based access

### Planned Features
- Image upload for products
- In-app messaging between buyers and sellers
- Search and filtering capabilities
- User ratings and reviews
- Campus email verification
- Saved/Favorite listings

## Technology Stack

- Backend:
  - Java 17
  - Spring Boot 3.5.5
  - Spring Security with JWT
  - JPA/Hibernate
  - H2 Database (for development)

- Frontend (In Progress):
  - React.js
  - Material-UI

## API Endpoints

### Authentication
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login (returns JWT token)

### Products
- `GET /api/products` - Get all available products
- `GET /api/products/{id}` - Get specific product
- `POST /api/products` - Create new product listing (authenticated)
- `PUT /api/products/{id}` - Update product (seller only)
- `DELETE /api/products/{id}` - Delete product (seller only)

## Setup and Installation

1. Clone the repository
```bash
git clone https://github.com/aditya9064/marketplace.git
```

2. Build the project
```bash
./gradlew build
```

3. Run the application
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Contributing

Feel free to fork the repository and submit pull requests.

## License

This project is licensed under the MIT License.
