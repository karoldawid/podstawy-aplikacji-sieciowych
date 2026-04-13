# Sports Facility Management System 🏟️

## Overview
A full-stack web application designed for managing sports facilities, user accounts, and equipment rentals. Built with **Spring Boot** and **MongoDB** on the backend, and **React** with **TypeScript** on the frontend. The entire architecture is containerized using **Docker Compose** for seamless local deployment and testing.

## ✨ Features
- **Role-Based Access Control (RBAC)**: Secure access tailored for three distinct user roles: `Admin`, `Client`, and `FacilityManager`.
- **Facility Management**: Complete CRUD operations for various sports facilities including Gyms, Swimming Pools, and Tennis Courts.
- **Rental System**: Intuitive booking system allowing clients to reserve facilities and managers to oversee active rentals.
- **Robust Security**: Custom security layer utilizing JSON Web Tokens (JWT) for stateless authentication and JWS validation.
- **Responsive SPA**: Dynamic frontend built with React and Vite, featuring protected routes and TypeScript for type safety.

## 🛠️ Tech Stack

### Backend
- **Java 25 & Spring Boot**: Core REST API framework.
- **Spring Security & JWT**: Custom authentication filters and token generation.
- **MongoDB**: NoSQL database managed via Spring Data MongoDB.
- **Maven**: Dependency management.

### Frontend
- **React & TypeScript**: Component-based UI with strong typing.
- **Vite**: Next-generation frontend tooling for fast builds.
- **React Router**: Client-side routing with protected endpoint logic.

### Infrastructure
- **Docker & Docker Compose**: Multi-container setup for the backend, frontend, and database.

## 🚀 Getting Started

### Prerequisites
- Docker and Docker Compose installed on your machine.

### Running the application
1. Clone the repository:
   ```bash
   git clone [https://github.com/karoldawid/sports-facility-infrastructure.git](https://github.com/karoldawid/sports-facility-infrastructure.git)
   cd sports-facility-infrastructure```
2. Start the application environment using Docker Compose:
   ```bash
   docker-compose up --build```
3. Access the application:
Frontend UI: ```bash
http://localhost:5173```
Backend API: ```bash
http://localhost:8080```
