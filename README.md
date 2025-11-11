# 🚗 EasyPark Platform

Sistema de gestión de estacionamientos con arquitectura **DDD (Domain-Driven Design)**.

## 📋 Características

- **Java 21** 
- **Spring Boot 3.x**
- **MySQL 8.0** (Clever Cloud)
- **Maven 3.6+**
- **HikariCP** (Pool de conexiones optimizado) 

## 🚀 Inicio Rápido

### 1. Clonar el Repositorio

### 3. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

### 4. Acceder a Swagger UI

Una vez que el servidor esté ejecutándose, accede a la documentación interactiva de la API:

```
http://localhost:8080/swagger-ui/index.html
```

O simplemente:

```
http://localhost:8080/swagger-ui.html
## 📊 Monitoreo de Conexiones MySQL

La aplicación utiliza un pool de conexiones optimizado (HikariCP) con las siguientes configuraciones:

- **Máximo de conexiones:** 2
- **Conexiones mínimas idle:** 1
- **Timeout idle:** 60 segundos
- **Lifetime máximo:** 120 segundos

### Verificar Conexiones Activas

Para monitorear cuántas conexiones está usando tu aplicación:

```bash
python check_connections.py
```

**Requisitos:**
```bash
pip install pymysql cryptography
```

**Salida esperada:**
```
📊 RESUMEN DE CONEXIONES
======================================================================
  🔗 Total conexiones:     2/5
  🔄 Activas (ejecutando): 0
  💤 Inactivas (pool):     2
  🆓 Disponibles:          3
  🟢 Estado: ✅ EXCELENTE
```

### Interpretación de Estados

| Conexiones | Estado | Descripción |
|------------|--------|-------------|
| **1-2/5** | 🟢 Excelente | Pool funcionando correctamente |
| **3/5** | 🟡 Bueno | Uso normal, espacio para herramientas |
| **4/5** | 🟠 Alto | Considera cerrar herramientas no esenciales |
| **5/5** | 🔴 Crítico | Límite alcanzado, no se pueden conectar más clientes |

### Notas Importantes

- **La aplicación NO consume 1 conexión por usuario/dispositivo**
- Las 2 conexiones del pool son **compartidas** por todos los usuarios
- Cada request usa una conexión temporalmente (~10-100ms) y la libera
- Con 2 conexiones puedes manejar **miles de usuarios simultáneos**
- Siempre hay 3 conexiones libres para herramientas (MySQL Workbench, DBeaver, etc)

---

## 🗄️ Base de Datos

### Configuración de Producción

La aplicación está configurada para usar MySQL en Clever Cloud:

- **Host:** bfbh4n2ccukyxuo2sny4-mysql.services.clever-cloud.com
- **Base de datos:** bfbh4n2ccukyxuo2sny4
- **Puerto:** 3306
- **Límite de conexiones:** 5 (plan gratuito)

### Conectar con MySQL Workbench

```
Host: bfbh4n2ccukyxuo2sny4-mysql.services.clever-cloud.com
Port: 3306
Username: uzk5dknvpy7byyoo
Password: w7HGO2zBZN2qm2HEdV2s
Database: bfbh4n2ccukyxuo2sny4
```

**⚠️ Importante:** Cierra Workbench cuando no lo uses para liberar conexiones.

---

```





