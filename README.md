# SmartDeal

SmartDeal is a product comparison platform that helps users find highly-rated products at competitive prices across different e-commerce platforms.

## 🚀 Features

- Compare products from multiple platforms
- Filter products by rating
- Find the cheapest available product
- Search products by name
- REST API based backend
- MySQL database integration

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- REST API

## 📌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/top-rated` | Get highly-rated products |
| GET | `/api/products/cheapest/{name}` | Find cheapest product |
| GET | `/api/products/search/{name}` | Search products |
| POST | `/api/products` | Add a product |

## ▶️ How to Run

1. Clone the repository
2. Configure MySQL
3. Create a database named `smartdeal`
4. Configure your database credentials locally
5. Run the Spring Boot application

## 📚 Project Status

SmartDeal is currently under development.

More features and the frontend will be added soon.