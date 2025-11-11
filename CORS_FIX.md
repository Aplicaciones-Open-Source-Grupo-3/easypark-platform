# 🔒 Solución de Errores CORS

## ❌ Error Original

```
Access to XMLHttpRequest at 'https://easypark-platform.onrender.com/iam/authentication/sign-in' 
from origin 'https://easypark24.netlify.app' has been blocked by CORS policy: 
Response to preflight request doesn't pass access control check: 
No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

## ✅ Solución Implementada

Se creó la configuración CORS en:
```
src/main/java/com/easypark/platform/shared/infrastructure/web/CorsConfig.java
```

### ¿Qué hace esta configuración?

1. **Permite peticiones desde:**
   - `https://easypark24.netlify.app` (Producción)
   - `http://localhost:4200` (Angular dev)
   - `http://localhost:3000` (React/otros)

2. **Métodos HTTP permitidos:**
   - GET, POST, PUT, DELETE, PATCH, OPTIONS

3. **Headers permitidos:**
   - Todos (`*`)

4. **Credenciales:**
   - Habilitadas para enviar cookies y tokens

5. **Cache de preflight:**
   - 1 hora (reduce peticiones OPTIONS)

## 🔄 Pasos para Aplicar la Solución

### 1. Commit y Push

```bash
git add .
git commit -m "feat: Configuración CORS para permitir peticiones desde Netlify"
git push origin main
```

### 2. Despliegue en Render

**Opción A: Auto-Deploy (Recomendado)**
- Si ya está configurado, Render detectará el push automáticamente
- Espera 3-5 minutos para el despliegue

**Opción B: Manual Deploy**
1. Ve a [Render Dashboard](https://dashboard.render.com/)
2. Selecciona **easypark-platform**
3. Click en **Manual Deploy** → **Deploy latest commit**

### 3. Verificación

**Desde el navegador:**
1. Ve a: https://easypark24.netlify.app
2. Intenta iniciar sesión
3. Abre la consola del navegador (F12)
4. Verifica que NO aparezcan errores CORS

**Desde Swagger:**
1. Ve a: https://easypark-platform.onrender.com/swagger-ui/index.html
2. Prueba el endpoint `/iam/authentication/sign-in`

## 🎯 Prueba Rápida con cURL

```bash
curl -X POST https://easypark-platform.onrender.com/iam/authentication/sign-in \
  -H "Content-Type: application/json" \
  -H "Origin: https://easypark24.netlify.app" \
  -d '{"username":"admin","password":"admin123"}' \
  -v
```

**Verifica en la respuesta:**
```
< access-control-allow-origin: https://easypark24.netlify.app
< access-control-allow-credentials: true
```

## 🔧 Agregar Más Dominios

Si necesitas permitir más dominios (por ejemplo, un nuevo frontend):

```java
configuration.setAllowedOrigins(Arrays.asList(
    "https://easypark24.netlify.app",
    "https://tu-nuevo-dominio.com",  // 👈 Agregar aquí
    "http://localhost:4200",
    "http://localhost:3000"
));
```

Luego repite el proceso de commit y despliegue.

## ⚠️ Notas Importantes

### Seguridad
- ✅ Solo dominios específicos permitidos
- ✅ Credenciales habilitadas para tokens JWT
- ❌ NO usar `"*"` en producción con `allowCredentials: true`

### Tiempo de Aplicación
- El cambio toma **3-5 minutos** en aplicarse después del despliegue
- Si Render estaba dormido, la primera petición puede tardar 30-60 segundos

### Conexiones MySQL durante Despliegue
- Durante 30-60 segundos, podrías ver **4 conexiones** (2 instancia vieja + 2 instancia nueva)
- Después del despliegue, vuelve a **2 conexiones** automáticamente
- Las conexiones NO se acumulan entre despliegues
- Ver detalles en: `DESPLIEGUE_Y_CONEXIONES.md`

### Troubleshooting

**Si sigue sin funcionar:**

1. **Verifica que el servicio esté activo:**
   ```
   https://easypark-platform.onrender.com/swagger-ui/index.html
   ```

2. **Limpia caché del navegador:**
   - Chrome: Ctrl + Shift + Delete
   - O usa modo incógnito

3. **Verifica los logs de Render:**
   - Dashboard → easypark-platform → Logs
   - Busca errores de CORS o Spring Boot

4. **Verifica la configuración:**
   ```bash
   git log -1 --oneline
   # Debe mostrar tu último commit con CORS
   ```

## 📚 Referencias

- [Spring CORS Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc-cors.html)
- [MDN CORS Guide](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
- [Render Deployment Docs](https://render.com/docs/deploys)

