# 🔧 Solución al Problema de Conexiones Excedidas

## 🚨 ACCIÓN INMEDIATA: Cerrar Conexiones Antes de Redesplegar

**⚡ Método más rápido (2 minutos):**
1. Ve a [Clever Cloud Console](https://console.clever-cloud.com/)
2. Selecciona tu MySQL addon
3. Clic en "Restart" o "Reboot"
4. Espera 2 minutos

**💻 Método alternativo con herramienta:**
- Ver guía completa: `CERRAR_CONEXIONES_GUIA.md`
- Usar script: Doble clic en `close_connections.bat`
- Usar MySQL Workbench con el script: `kill_connections.sql`

---

## ❌ Problema
```
SQLSyntaxErrorException: User 'ues1f415lj3l7lcd' has exceeded the 'max_user_connections' resource (current value: 5)
```

## ✅ Solución Implementada

### 1. Configuración Optimizada de HikariCP
Se ha ajustado la configuración del pool de conexiones en `application-prod.properties`:

- **maximum-pool-size**: 3 (anteriormente 2, pero con mejor gestión)
- **minimum-idle**: 1 (conexión mínima activa)
- **idle-timeout**: 60000ms (1 minuto - anteriormente 5 minutos)
- **max-lifetime**: 120000ms (2 minutos - anteriormente 10 minutos)
- **keepalive-time**: 30000ms (30 segundos para mantener conexiones vivas)
- **connection-test-query**: SELECT 1 (verifica que la conexión esté activa)
- **leak-detection-threshold**: 30000ms (detecta conexiones no cerradas)

### 2. ¿Por qué Funciona?

**Tiempos más Cortos de Vida:**
- Las conexiones se reciclan cada 2 minutos en lugar de 10 minutos
- Las conexiones inactivas se cierran después de 1 minuto
- Esto libera conexiones huérfanas más rápidamente

**Detección de Fugas:**
- El threshold de 30 segundos ayuda a identificar conexiones que no se cierran correctamente

**Validación de Conexiones:**
- La query `SELECT 1` verifica que las conexiones estén realmente activas
- Keepalive cada 30 segundos evita que el servidor cierre conexiones

## 🚀 Pasos Inmediatos para Resolver

### Opción 1: Limpiar Conexiones desde MySQL (Recomendado)

Si tienes acceso a Clever Cloud, ejecuta:
```sql
SHOW PROCESSLIST;
-- Ver todas las conexiones activas

-- Si es necesario, matar conexiones específicas:
KILL <process_id>;
```

### Opción 2: Reiniciar la Aplicación

1. **En Clever Cloud:**
   - Ve al dashboard de tu aplicación
   - Haz clic en "Restart"
   - Espera a que se limpien las conexiones anteriores

2. **Redesplegar:**
   ```bash
   git add .
   git commit -m "fix: optimizar pool de conexiones HikariCP"
   git push origin main
   ```

### Opción 3: Esperar (Menos Recomendado)

Con la nueva configuración, las conexiones viejas se liberarán automáticamente en 2 minutos.

## 🔍 Monitoreo

Para verificar que no vuelva a ocurrir, observa los logs de HikariCP:
```
logging.level.com.zaxxer.hikari=DEBUG
```

Busca mensajes como:
- `HikariPool - Pool stats` → Muestra conexiones activas/idle
- `Connection leak detection` → Indica conexiones no cerradas

## 📋 Mejores Prácticas Implementadas

1. ✅ **Pool Size Conservador**: 3 conexiones máximo (60% del límite)
2. ✅ **Reciclaje Agresivo**: Conexiones se renuevan cada 2 minutos
3. ✅ **Validación Activa**: Se verifica cada conexión antes de usar
4. ✅ **Detección de Fugas**: Alerta si una conexión no se cierra en 30s
5. ✅ **Keepalive**: Mantiene conexiones saludables

## 🛡️ Prevención Futura

### En el Código:
- Siempre usar `try-with-resources` con EntityManager
- No mantener transacciones abiertas innecesariamente
- Cerrar explícitamente recursos en servicios

### Ejemplo Correcto:
```java
@Transactional
public void saveEntity(Entity entity) {
    repository.save(entity);
    // Spring cierra automáticamente la conexión al finalizar
}
```

### Ejemplo Incorrecto:
```java
EntityManager em = factory.createEntityManager();
// ... operaciones ...
// ❌ Nunca se cierra → fuga de conexión
```

## 🔗 Recursos

- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)
- [Spring Boot Datasource Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.data)

---
**Fecha de Aplicación:** 2025-11-10
**Estado:** ✅ Configuración Actualizada

