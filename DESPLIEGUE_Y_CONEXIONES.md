# 🔄 Despliegue en Render y Gestión de Conexiones MySQL

## ❓ Pregunta Frecuente

**"Si despliego una nueva versión, ¿se duplicarán las conexiones a la base de datos?"**

## ✅ Respuesta: NO

### 🔍 ¿Qué pasa durante un despliegue?

```
┌─────────────────────────────────────────────────────────────┐
│  ANTES DEL DESPLIEGUE                                        │
├─────────────────────────────────────────────────────────────┤
│  Render Instance v1.0                                        │
│  ├─ Conexión MySQL #1 (activa)                              │
│  └─ Conexión MySQL #2 (idle en pool)                        │
│                                                              │
│  Total en BD: 2/5 conexiones                                │
└─────────────────────────────────────────────────────────────┘

                          ⬇️  git push

┌─────────────────────────────────────────────────────────────┐
│  DURANTE EL DESPLIEGUE (~2-3 minutos)                       │
├─────────────────────────────────────────────────────────────┤
│  Render Instance v1.0 (ACTIVA)                              │
│  ├─ Conexión MySQL #1 ✅                                    │
│  └─ Conexión MySQL #2 ✅                                    │
│                                                              │
│  Render Instance v2.0 (INICIANDO)                           │
│  ├─ Compilando código...                                    │
│  ├─ Construyendo Docker image...                            │
│  └─ Aún NO conecta a BD                                     │
│                                                              │
│  Total en BD: 2/5 conexiones                                │
└─────────────────────────────────────────────────────────────┘

                          ⬇️  Build completo

┌─────────────────────────────────────────────────────────────┐
│  TRANSICIÓN (~30-60 segundos)                               │
├─────────────────────────────────────────────────────────────┤
│  Render Instance v1.0 (FINALIZANDO)                         │
│  ├─ Conexión MySQL #1 ✅                                    │
│  └─ Conexión MySQL #2 ✅                                    │
│                                                              │
│  Render Instance v2.0 (INICIANDO APP)                       │
│  ├─ Spring Boot starting...                                 │
│  ├─ HikariCP inicializando...                               │
│  ├─ Conexión MySQL #3 ✅ (nueva instancia)                  │
│  └─ Conexión MySQL #4 ✅ (nueva instancia)                  │
│                                                              │
│  Total en BD: 4/5 conexiones ⚠️  (TEMPORAL)                 │
└─────────────────────────────────────────────────────────────┘

                          ⬇️  Health check OK

┌─────────────────────────────────────────────────────────────┐
│  DESPUÉS DEL DESPLIEGUE                                      │
├─────────────────────────────────────────────────────────────┤
│  Render Instance v1.0 (DESTRUIDA) ❌                        │
│  ├─ Conexión MySQL #1 ❌ CERRADA                            │
│  └─ Conexión MySQL #2 ❌ CERRADA                            │
│                                                              │
│  Render Instance v2.0 (ACTIVA) ✅                           │
│  ├─ Conexión MySQL #3 ✅                                    │
│  └─ Conexión MySQL #4 ✅                                    │
│                                                              │
│  Total en BD: 2/5 conexiones ✅                             │
└─────────────────────────────────────────────────────────────┘
```

## 📊 Resumen

| Fase | Instancias Activas | Conexiones MySQL | Estado |
|------|-------------------|------------------|--------|
| **Antes** | 1 (v1.0) | 2/5 | ✅ Normal |
| **Build** | 1 (v1.0) | 2/5 | ✅ Normal |
| **Transición** | 2 (v1.0 + v2.0) | 4/5 | ⚠️ Temporal (30-60s) |
| **Después** | 1 (v2.0) | 2/5 | ✅ Normal |

## ✅ Conclusión

**Las conexiones NO se acumulan** entre despliegues porque:

1. Render **solo mantiene 1 instancia activa** (plan gratuito)
2. La instancia antigua se **destruye automáticamente**
3. HikariCP **cierra todas las conexiones** al destruirse la instancia
4. MySQL **libera automáticamente** las conexiones cerradas

## ⚠️ Caso Especial: Pico Temporal

Durante **30-60 segundos** en la transición, podrías tener 4 conexiones:
- 2 de la instancia antigua
- 2 de la instancia nueva

Esto es **normal y seguro** porque:
- ✅ No excede el límite de 5 conexiones
- ✅ Dura muy poco tiempo
- ✅ Vuelve a 2 automáticamente

## 🔍 Monitorear Conexiones

### Opción 1: Script Python

```bash
python check_connections.py
```

**Salida esperada:**
```
📊 RESUMEN DE CONEXIONES
==========================================
  🔗 Total conexiones:     2/5
  🔄 Activas (ejecutando): 1
  💤 Inactivas (pool):     1
  🆓 Disponibles:          3

  🟢 Estado: ✅ EXCELENTE
```

### Opción 2: MySQL Workbench

```sql
SELECT 
    COUNT(*) as total_connections,
    user,
    host
FROM information_schema.processlist
WHERE user = 'uzk5dknvpy7byyoo'
GROUP BY user, host;
```

### Opción 3: Logs de Render

1. Ve a [Render Dashboard](https://dashboard.render.com/)
2. Selecciona **easypark-platform**
3. Click en **Logs**
4. Busca: `HikariPool` para ver inicialización del pool

## 🚀 Configuración Actual

**Producción (`application-prod.properties`):**
```properties
spring.datasource.hikari.maximum-pool-size=2
spring.datasource.hikari.minimum-idle=1
```

**Límite de Clever Cloud:** 5 conexiones simultáneas

**Distribución recomendada:**
- ✅ 2 conexiones: Aplicación Render (pool HikariCP)
- ✅ 1 conexión: MySQL Workbench (administración)
- ✅ 1 conexión: Herramientas de desarrollo
- ✅ 1 conexión: Reserva para picos

## 🔧 Troubleshooting

### Problema: "Too many connections"

**Causas posibles:**
1. ❌ Múltiples instancias de la app corriendo localmente
2. ❌ Conexiones no cerradas (memory leak)
3. ❌ Despliegue fallido (instancia zombie)

**Solución:**

1. **Verificar conexiones:**
   ```bash
   python check_connections.py
   ```

2. **Si hay más de 2 conexiones de Render:**
   - Ve a Render Dashboard
   - Verifica que solo haya 1 deploy activo
   - Si hay deploys fallidos, cancélalos

3. **Matar conexiones manualmente (último recurso):**
   ```sql
   -- Ver IDs de conexiones
   SELECT id, user, host, time, command
   FROM information_schema.processlist
   WHERE user = 'uzk5dknvpy7byyoo';

   -- Matar conexión específica
   KILL <connection_id>;
   ```

## 📈 Optimizaciones Futuras

Si necesitas más conexiones (plan paid):

```properties
# Para más tráfico
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=3

# Tiempos ajustados
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
```

## 🔗 Referencias

- [HikariCP Documentation](https://github.com/brettwooldridge/HikariCP)
- [Render Deployment](https://render.com/docs/deploys)
- [Clever Cloud MySQL Limits](https://www.clever-cloud.com/pricing/)

