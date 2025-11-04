# 🚗 EasyPark Platform

Sistema de gestión de estacionamientos con arquitectura **DDD (Domain-Driven Design)**.

## 📋 Características

- ✅ Autenticación y autorización con **JWT**
- ✅ Gestión de usuarios y roles (Admin, Operador)
- ✅ Gestión de negocios de estacionamiento
- ✅ API RESTful documentada con **Swagger/OpenAPI 3.0**
- ✅ Seguridad implementada con **Spring Security**
- ✅ Persistencia de datos con **JPA/Hibernate** y **MySQL**

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **Swagger/OpenAPI 3.0**
- **Maven**

## 📦 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java 21** o superior
- **MySQL 8.0** o superior
- **Maven 3.6+** (incluido en el proyecto con Maven Wrapper)

## 🚀 Inicio Rápido

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/easypark-platform.git
cd easypark-platform
```

### 2. Configurar la Base de Datos

Crea una base de datos MySQL o edita las credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easypark_db
spring.datasource.username=root
spring.datasource.password=tu_password
```

> **Nota**: La base de datos se creará automáticamente si no existe (`createDatabaseIfNotExist=true`)

### 3. Ejecutar la Aplicación

#### Opción A: Usando Maven Wrapper (Recomendado)

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

#### Opción B: Usando el script batch (Solo Windows)

```bash
start-server.bat
```

#### Opción C: Usando JAR compilado

```bash
.\mvnw.cmd clean package
java -jar target/platform-1.0.0.jar
```

### 4. Acceder a Swagger UI

Una vez que la aplicación esté corriendo (verás el mensaje `Started PlatformApplication`), abre tu navegador en:

```
http://localhost:8080/swagger-ui.html
```

## 📖 Documentación de la API

### Endpoints Disponibles

#### Autenticación

**Sign Up (Registro)**
```http
POST /iam/authentication/sign-up
Content-Type: application/json

{
  "businessName": "Mi Estacionamiento",
  "address": "Av. Principal 123",
  "phone": "555-1234",
  "email": "contact@myparking.com",
  "taxId": "20123456789",
  "maxCapacity": 50,
  "motorcycleRate": 5.0,
  "carTruckRate": 10.0,
  "nightRate": 8.0,
  "openingTime": "08:00",
  "closingTime": "22:00",
  "currency": "USD",
  "adminUsername": "admin",
  "adminEmail": "admin@myparking.com",
  "adminPassword": "Admin123!",
  "adminName": "Administrador Principal"
}
```

**Sign In (Login)**
```http
POST /iam/authentication/sign-in
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin123!"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@myparking.com",
  "name": "Administrador Principal",
  "role": "ROLE_ADMIN",
  "businessId": 1,
  "businessName": "Mi Estacionamiento",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Usar el Token JWT

Para endpoints protegidos, incluye el token en el header:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

En Swagger UI:
1. Click en el botón **"Authorize"** 🔓
2. Ingresa: `Bearer [tu-token]`
3. Click en **"Authorize"**

## 🔒 Seguridad

- Las contraseñas se almacenan con hash **BCrypt**
- Autenticación basada en **JWT tokens**
- Tokens válidos por **7 días** (configurable)
- Endpoints protegidos con **Spring Security**
- CORS configurado para desarrollo




