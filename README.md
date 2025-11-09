# 🚗 EasyPark Platform

Sistema de gestión de estacionamientos con arquitectura **DDD (Domain-Driven Design)**.

## 📋 Características

- ✅ Gestión de negocios de estacionamiento
- ✅ API RESTful documentada con **Swagger/OpenAPI 3.0**
3. **(Opcional) Crear la base de datos manualmente:**
   - Password: (vacío)
  "adminName": "Juan Pérez",
- ✅ Persistencia de datos con **JPA/Hibernate** y **MySQL**
- **Swagger/OpenAPI 3.0**
- **Maven**

## 📦 Requisitos Previos

POST /api/v1/iam/authentication/sign-in

- **Java 21** o superior
- **MySQL 8.0** o superior
- **Maven 3.6+** 

## 🚀 Inicio Rápido

### 1. Clonar el Repositorio
#### 🅿️ Parking Management (18 endpoints)

**Vehículos:**
- `POST /api/v1/parking/vehicles/entry` - Registrar entrada
- `POST /api/v1/parking/vehicles/{id}/exit` - Registrar salida
- `GET /api/v1/parking/vehicles` - Listar vehículos
- `GET /api/v1/parking/vehicles/inside` - Vehículos dentro
- `GET /api/v1/parking/vehicles/{id}` - Obtener por ID
- `DELETE /api/v1/parking/vehicles/{id}` - Eliminar

**Operaciones:**
- `POST /api/v1/parking/operations/start` - Iniciar operación (manual)
- `POST /api/v1/parking/operations/close` - Cerrar operación actual
- `POST /api/v1/parking/operations/{id}/close` - Cerrar operación por ID
- `GET /api/v1/parking/operations/today` - Operación del día
- `GET /api/v1/parking/operations/{id}` - Obtener operación por ID
- `GET /api/v1/parking/operations` - Historial

> **Nota**: Se pueden crear múltiples operaciones por día. Solo debe cerrarse la operación anterior antes de abrir una nueva.

**Settings (Configuración):**
- `GET /api/v1/parking/settings` - Obtener configuración de parking
- `POST /api/v1/parking/settings` - Crear/actualizar configuración

**Incidentes:**
- `GET /api/v1/parking/incidents` - Listar todos
- `GET /api/v1/parking/incidents/pending` - Pendientes
- `POST /api/v1/parking/incidents` - Crear
- `PATCH /api/v1/parking/incidents/{id}` - Resolver

**Deudas:**
- `GET /api/v1/parking/debts` - Deudas pendientes
- `GET /api/v1/parking/debts/all` - Todas las deudas
- `POST /api/v1/parking/debts/{id}/pay` - Marcar como pagada

#### 💰 Accounting (5 endpoints)

- `GET /api/v1/accounting/records` - Listar registros
- `POST /api/v1/accounting/records` - Crear registro
- `GET /api/v1/accounting/records/{id}` - Obtener por ID
- `GET /api/v1/accounting/revenue/total` - Ingresos totales
- `GET /api/v1/accounting/revenue/by-date` - Ingresos por fecha

#### 👥 Clients (5 endpoints)

- `GET /api/v1/subscribers` - Listar suscriptores
- `POST /api/v1/subscribers` - Crear suscriptor
- `GET /api/v1/subscribers/{id}` - Obtener por ID
- `PUT /api/v1/subscribers/{id}` - Actualizar
- `DELETE /api/v1/subscribers/{id}` - Eliminar

#### 📊 Analytics (4 endpoints)

**Basado en datos de Accounting:**
- `GET /api/v1/analytics/stats` - Estadísticas generales (ingresos, vehículos, incidentes)
- `GET /api/v1/analytics/revenue-trend` - Tendencia de ingresos por fecha (desde Accounting)
- `GET /api/v1/analytics/occupancy-rate` - Tasa de ocupación de espacios
- `GET /api/v1/analytics/peak-hours` - Horas pico de entradas (desde Accounting)

> **Nota**: Los endpoints de Analytics obtienen los datos de ingresos y vehículos directamente desde el módulo de **Accounting**, asegurando consistencia en los reportes.

> **Documentación Completa**: Una vez ejecutando, visita `http://localhost:8080/swagger-ui.html` para ver todos los endpoints con ejemplos interactivos.
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

Una vez que el servidor esté ejecutándose, accede a la documentación interactiva de la API:

```
http://localhost:8080/swagger-ui/index.html
```

O simplemente:

```
http://localhost:8080/swagger-ui.html
```

> **Nota**: Ambas URLs funcionan. Si una no carga, prueba con la otra.

│   │   ├── java/com/easypark/platform/
│   │   │   ├── iam/                          # BC 1: Identity & Access Management
│   │   │   │   ├── application/internal/     # Command/Query Services
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/                # Aggregates, Entities, VOs
│   │   │   │   │   │   ├── aggregates/       # User, Business
│   │   │   │   │   │   ├── commands/         # SignUpCommand, SignInCommand
│   │   │   │   │   │   └── queries/          # GetUserByIdQuery
│   │   │   │   │   └── services/             # Interfaces de servicios
│   │   │   │   ├── infrastructure/           # Persistencia, Seguridad
│   │   │   │   │   ├── authorization/        # JWT, Spring Security
│   │   │   │   │   ├── hashing/              # BCrypt
│   │   │   │   │   ├── persistence/jpa/      # Repositories
│   │   │   │   │   └── tokens/               # JWT Service
│   │   │   │   └── interfaces/rest/          # Controllers, DTOs, Assemblers
│   │   │   │
│   │   │   ├── parking/                      # BC 2: Parking Management
│   │   │   │   ├── application/internal/     # Services Implementation
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── aggregates/       # Vehicle, Operation
│   │   │   │   │   │   ├── entities/         # Incident, VehicleDebt, ParkingSpace
│   │   │   │   │   │   ├── valueobjects/     # VehicleType, VehicleStatus
│   │   │   │   │   │   ├── commands/         # RegisterVehicleEntryCommand
│   │   │   │   │   │   └── queries/          # GetAllVehiclesQuery
│   │   │   │   │   └── services/             # Interfaces
│   │   │   │   ├── infrastructure/persistence/jpa/
│   │   │   │   └── interfaces/rest/
│   │   │   │
│   │   │   ├── accounting/                   # BC 3: Accounting
│   │   │   │   ├── application/
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── aggregates/       # AccountingRecord
│   │   │   │   │   │   ├── valueobjects/     # RecordType
│   │   │   │   │   │   └── commands/queries/
│   │   │   │   │   └── services/
│   │   │   │   ├── infrastructure/
│   │   │   │   └── interfaces/rest/
│   │   │   │
│   │   │   ├── clients/                      # BC 4: Clients (Subscribers)
│   │   │   │   ├── application/
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── aggregates/       # Subscriber
│   │   │   │   │   │   └── commands/queries/
│   │   │   │   │   └── services/
│   │   │   │   ├── infrastructure/
│   │   │   │   └── interfaces/rest/
│   │   │   │
│   │   │   ├── analytics/                    # BC 5: Analytics
│   │   │   │   ├── application/
│   │   │   │   ├── domain/services/
│   │   │   │   └── interfaces/rest/
│   │   │   │
│   │   │   ├── shared/                       # Código compartido
│   │   │   │   ├── domain/model/             # Base classes
│   │   │   │   ├── infrastructure/           # Swagger, Naming Strategy
│   │   │   │   └── interfaces/rest/
│   │   │   │
│   │   │   └── PlatformApplication.java      # Main Application
│   │   │
POST /iam/authentication/sign-up
│   │       ├── application.properties        # Configuración principal
│   │       └── application-h2.properties     # Configuración H2 (testing)
│   │

│
├── pom.xml
├── start-server.bat                          # Script para ejecutar (MySQL)
├── start-server-h2.bat                       # Script para ejecutar (H2)
└── README.md
  "businessName": "Mi Estacionamiento",

### Capas por Bounded Context

Cada bounded context sigue la estructura de Clean Architecture:

1. **Domain** (Capa de Dominio)
   - Aggregates (raíces de agregados)
   - Entities (entidades)
   - Value Objects (objetos de valor)
   - Commands (comandos CQRS)
   - Queries (consultas CQRS)
   - Services (interfaces de servicios)

2. **Application** (Capa de Aplicación)
   - Command Services (implementaciones)
   - Query Services (implementaciones)

3. **Infrastructure** (Capa de Infraestructura)
   - Persistence (JPA Repositories)
   - External Services

4. **Interfaces** (Capa de Presentación)
   - REST Controllers
   - Resources (DTOs)
   - Assemblers (transformadores)
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
- Multi-tenancy: cada negocio solo ve sus datos
```

## ⚠️ Troubleshooting - Problemas Comunes

### El servidor compila pero termina inmediatamente

**Causa**: No puede conectarse a MySQL.

**Solución 1** - Verifica MySQL:
```bash
# Windows
net start | findstr MySQL

# Si no está corriendo
net start MySQL80
```

**Solución 2** - Verifica credenciales en `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA_MYSQL
```

**Solución 3** - Usa H2 temporalmente (ver sección 2.B arriba)

### Error: "Access denied for user 'root'@'localhost'"

La contraseña de MySQL es incorrecta. Edita `application.properties` con la contraseña correcta.

### Error: "Port 8080 already in use"

Otro proceso usa el puerto 8080.

**Solución 1** - Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

**Solución 2** - Detén el proceso:
```bash
netstat -ano | findstr :8080
taskkill /PID [número] /F
```

### ⚠️ Cambios en el código no se reflejan

**Problema**: Modificaste el código pero los cambios no se ven.

**Solución**: Debes **reiniciar el servidor** para que Spring Boot recargue las clases:

**Windows (opción 1 - recomendada):**
```bash
# Detener el servidor actual (Ctrl+C en la ventana donde corre)
# Luego ejecutar:
start-server.bat
```

**Windows (opción 2 - desde cero):**
```bash
# 1. Detener todos los procesos Java del servidor
taskkill /F /IM java.exe

# 2. Recompilar y ejecutar (en CMD, no PowerShell)
mvnw.cmd clean compile spring-boot:run
```

**Hot Reload (desarrollo):**
Para evitar reiniciar constantemente, puedes usar **Spring Boot DevTools** (ya incluido en el proyecto):
```bash
mvnw.cmd spring-boot:run
```
Los cambios en código se recargan automáticamente (aunque algunos cambios estructurales aún requieren reinicio).

## 📊 Estadísticas del Proyecto

- **5 Bounded Contexts** implementados
- **6 Aggregates** principales
- **39+ Endpoints REST** documentados
- **100% arquitectura DDD** con CQRS
- **Multi-tenancy** completo
- **135+ archivos Java** compilados
- **Swagger/OpenAPI 3.0** integrado

## 🎯 Roadmap

- [ ] Dashboard web con React/Angular
- [ ] Notificaciones en tiempo real (WebSocket)
- [ ] Reportes PDF/Excel
- [ ] Integración con pasarelas de pago
- [ ] App móvil (iOS/Android)
- [ ] Sistema de reservas online
- [ ] Reconocimiento de placas (OCR)

**Solución 1** - Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

**Solución 2** - Detén el proceso:
```bash
netstat -ano | findstr :8090
taskkill /PID [número] /F
```
## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

Desarrollado con ❤️ usando arquitectura DDD y mejores prácticas de Spring Boot.

---

**¿Necesitas ayuda?** Revisa los archivos de documentación en la raíz del proyecto o abre un issue.


### El servidor inicia pero no veo los endpoints en Swagger

Verifica que hayas accedido a la URL correcta:
```
http://localhost:8080/swagger-ui/index.html
```
O:
```
http://localhost:8080/swagger-ui.html
```

### Token JWT expirado (Error 401)

Los tokens expiran después de 7 días. Vuelve a hacer sign-in para obtener un nuevo token.

## 📚 Documentación Adicional

El proyecto incluye varios archivos de documentación:

- `BOUNDED_CONTEXTS_IMPLEMENTATION.md` - Detalles de implementación de cada BC
- `PARKING_MODULE_STATUS.md` - Estado del módulo Parking
- `COMO_EJECUTAR_SERVIDOR.md` - Guía detallada de ejecución
- `ERROR_CONEXION_MYSQL.md` - Solución de problemas de MySQL

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

### 🔐 Cómo Autenticarse en Swagger (Botón Authorize)

Muchos endpoints están protegidos y requieren autenticación. Para usarlos en Swagger UI:

#### Paso 1: Obtener el Token
1. Usa el endpoint **POST /iam/authentication/sign-in** o **sign-up**
2. En la respuesta, **copia el valor del campo `token`**
   ```json
   {
     "id": 1,
     "username": "admin_central",
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbl9jZW50cmFsIi..."  ← Copia esto
   }
   ```

#### Paso 2: Configurar la Autorización
1. En la parte **superior derecha** de Swagger UI, haz click en el botón **"Authorize"** 🔓
2. En el campo que aparece, escribe:
   ```
   Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbl9jZW50cmFsIi...
   ```
   > ⚠️ **Importante**: Debe empezar con la palabra `Bearer` seguida de un espacio y luego tu token

3. Click en **"Authorize"**
4. Click en **"Close"**

#### Paso 3: Usar Endpoints Protegidos
¡Listo! Ahora todos los endpoints protegidos incluirán automáticamente tu token. Verás un candado cerrado 🔒 en los endpoints que requieren autenticación.

**Nota**: El token expira después de 7 días. Si recibes un error 401 (Unauthorized), necesitas hacer sign-in nuevamente para obtener un nuevo token.

## 🔒 Seguridad

- Las contraseñas se almacenan con hash **BCrypt**
- Autenticación basada en **JWT tokens**
- Tokens válidos por **7 días** (configurable)
- Endpoints protegidos con **Spring Security**
- CORS configurado para desarrollo




