# GitHub Actions: Keep Render Service Alive

## ¿Qué es esto?

Este archivo (opcional) configurará una GitHub Action que hará ping a tu servicio cada 10 minutos para evitar que Render lo ponga en modo "sleep" después de 15 minutos de inactividad.

## ¿Lo necesito?

**No es obligatorio.** Solo si quieres que tu servicio responda instantáneamente sin cold starts.

## Cómo activarlo

### Opción 1: Crear manualmente

1. Crea la carpeta `.github/workflows/` en la raíz de tu proyecto
2. Crea el archivo `keep-alive.yml` dentro de esa carpeta
3. Copia el contenido de abajo
4. Reemplaza `YOUR_RENDER_URL` con tu URL real de Render
5. Commit y push

### Opción 2: Usar este contenido

```yaml
name: Keep Render Service Alive

on:
  schedule:
    # Ejecutar cada 10 minutos
    - cron: '*/10 * * * *'
  workflow_dispatch: # Permite ejecución manual

jobs:
  ping:
    runs-on: ubuntu-latest
    
    steps:
      - name: Ping Render Service
        run: |
          echo "Pinging Render service..."
          curl -f https://easypark-backend.onrender.com/actuator/health || echo "Ping failed"
          echo "Ping completed at $(date)"
```

## Alternativas más simples (RECOMENDADAS)

En lugar de GitHub Actions, usa estos servicios gratuitos:

### 1. UptimeRobot (⭐ Recomendado)
- **URL:** https://uptimerobot.com
- **Gratis:** Sí, hasta 50 monitores
- **Configuración:** 2 minutos
- **Intervalo:** Cada 5 minutos

**Pasos:**
1. Crear cuenta
2. Add New Monitor
3. Monitor Type: HTTP(s)
4. URL: `https://easypark-backend.onrender.com/actuator/health`
5. Monitoring Interval: 5 minutes
6. Save

**Ventajas:**
- ✅ Sin configuración en tu repo
- ✅ Dashboard con estadísticas
- ✅ Alertas por email
- ✅ Sin consumir GitHub Actions minutes

### 2. Cron-Job.org
- **URL:** https://cron-job.org
- **Gratis:** Sí
- **Configuración:** 3 minutos
- **Intervalo:** Cada 10 minutos

**Pasos:**
1. Crear cuenta
2. Create cronjob
3. Title: "Keep Render Alive"
4. URL: `https://easypark-backend.onrender.com/actuator/health`
5. Schedule: `*/10 * * * *` (cada 10 minutos)
6. Save

### 3. Freshping
- **URL:** https://www.freshworks.com/website-monitoring/
- **Gratis:** Sí, hasta 50 checks
- **Configuración:** 2 minutos
- **Intervalo:** Cada 1-5 minutos

## ¿Cuál usar?

| Servicio | Pros | Contras | Recomendación |
|----------|------|---------|---------------|
| **UptimeRobot** | Más popular, UI amigable | - | ⭐⭐⭐⭐⭐ |
| **Cron-Job.org** | Flexible, open source | UI menos intuitiva | ⭐⭐⭐⭐ |
| **GitHub Actions** | Todo en el repo | Consume minutes gratis | ⭐⭐⭐ |
| **Freshping** | Profesional, stats | Más complejo | ⭐⭐⭐⭐ |

## Nota

**El cold start de Render toma 30-60 segundos.** Usar un keep-alive service garantiza:
- ✅ Respuestas instantáneas
- ✅ Sin cold starts
- ✅ Mejor experiencia de usuario

**Pero también:**
- ⚠️ Consume más recursos del servidor
- ⚠️ Tu app estará "siempre activa"

**Decisión:** Si tu app es para desarrollo/demos, NO es necesario. Si es para producción con usuarios reales, SÍ es recomendado.

---

**Recomendación final:** Usa **UptimeRobot** - Es la solución más simple y efectiva.

