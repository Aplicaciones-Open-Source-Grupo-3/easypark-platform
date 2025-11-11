# 🚀 Deploy EasyPark Platform to Render with Docker

## 📋 Prerequisites

- ✅ GitHub repository
- ✅ Render account (free): https://render.com
- ✅ Dockerfile configurado
- ✅ render.yaml configurado
- ✅ Nueva base de datos Clever Cloud

---

## 🎯 Deployment Methods

### Method 1: Using render.yaml (RECOMMENDED) ⭐

### Method 2: Manual Configuration via Dashboard

---

## 🚀 METHOD 1: Using render.yaml (Automatic)

### Step 1: Push to GitHub

Asegúrate de que todos los cambios estén en GitHub:

```bash
git add .
git commit -m "chore: preparar proyecto para deploy en Render con Docker"
git push origin main
```

### Step 2: Create Render Account

1. Go to https://render.com
2. Sign up with GitHub (recommended)
3. Authorize Render to access your repositories

### Step 3: Create Blueprint

1. Click **"New +"** → **"Blueprint"**
2. Connect your GitHub repository
3. Select the repository: `platform` o tu nombre de repo
4. Render detectará automáticamente el archivo `render.yaml`
5. Click **"Apply"**

### Step 4: Configure Environment Variables

Render leerá las variables de `render.yaml`, pero necesitas configurar:

**En el Dashboard de Render:**
- Ve a tu servicio `easypark-backend`
- Click en **"Environment"**
- Verifica que estas variables estén configuradas:

```env
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:mysql://bfbh4n2ccukyxuo2sny4-mysql.services.clever-cloud.com:3306/bfbh4n2ccukyxuo2sny4?useSSL=true&requireSSL=true&serverTimezone=UTC
DATABASE_USERNAME=uzk5dknvpy7byyoo
DATABASE_PASSWORD=w7HGO2zBZN2qm2HEdV2s
JWT_SECRET=<generado-automáticamente>
JAVA_OPTS=-Xmx512m -Xms256m
```

### Step 5: Deploy!

- Render comenzará a construir tu aplicación usando Docker
- **Tiempo estimado:** 5-10 minutos
- **Progreso:** Ver en logs del dashboard

---

## 🛠️ METHOD 2: Manual Configuration (Alternative)

### Step 1: Create New Web Service

1. Click **"New +"** → **"Web Service"**
2. Connect GitHub repository
3. Select your repo: `platform`
4. Click **"Connect"**

### Step 2: Configure Service

**Basic Settings:**
```
Name: easypark-backend
Region: Oregon (or closest)
Branch: main
Root Directory: (leave empty)
```

**Build & Deploy:**
```
Runtime: Docker ✅
Dockerfile Path: ./Dockerfile (default)
Docker Build Context Directory: . (root)
```

**Instance Type:**
```
Plan: Free (0 USD/month)
```

### Step 3: Environment Variables

Add these in "Environment" section:

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DATABASE_URL` | `jdbc:mysql://bfbh4n2ccukyxuo2sny4-mysql.services.clever-cloud.com:3306/bfbh4n2ccukyxuo2sny4?useSSL=true&requireSSL=true&serverTimezone=UTC` |
| `DATABASE_USERNAME` | `uzk5dknvpy7byyoo` |
| `DATABASE_PASSWORD` | `w7HGO2zBZN2qm2HEdV2s` |
| `JWT_SECRET` | Generate (or use at least 32 chars) |
| `JAVA_OPTS` | `-Xmx512m -Xms256m` |

### Step 4: Health Check

```
Health Check Path: /actuator/health
```

### Step 5: Deploy

- Click **"Create Web Service"**
- Wait for build and deployment

---

## 🧪 Testing Your Deployment

### 1. Wait for Deployment

```
Status: Live ✅
```

### 2. Test Health Endpoint

```bash
curl https://easypark-backend.onrender.com/actuator/health
```

**Expected Response:**
```json
{
  "status": "UP"
}
```

### 3. Access Swagger UI

Open in browser:
```
https://easypark-backend.onrender.com/swagger-ui/index.html
```

### 4. Test API Endpoint

```bash
# Example: Get parking lots
curl https://easypark-backend.onrender.com/api/parking-lots
```

---

## 📊 Understanding the Build Process

### Stage 1: Build (Docker)
```
1. Pull Maven + JDK 21 Alpine image
2. Download dependencies (cached)
3. Compile source code
4. Package JAR file
Duration: 3-5 minutes
```

### Stage 2: Runtime (Docker)
```
1. Pull JRE 21 Alpine image
2. Copy JAR from build stage
3. Create non-root user
4. Configure health checks
5. Start application
Duration: 1-2 minutes
```

### Total Build Time: ~5-7 minutes

---

## 🔍 Monitoring & Logs

### View Logs

In Render Dashboard:
1. Go to your service
2. Click **"Logs"** tab
3. See real-time logs

### Key Log Messages

**✅ Successful Deployment:**
```
Started PlatformApplication in X.XXX seconds
Tomcat started on port(s): 8080
HikariPool-1 - Start completed
```

**❌ Common Errors:**
```
# Database connection error
Cannot create PoolableConnectionFactory

# Solution: Check DATABASE_URL and credentials
```

---

## 🛡️ Connection Pool Configuration

Your app is configured with:

```properties
maximum-pool-size: 2
minimum-idle: 1
idle-timeout: 60 seconds
max-lifetime: 120 seconds
```

**This means:**
- ✅ Uses only 2 connections to MySQL (out of 5 available)
- ✅ Leaves 3 connections free for tools/maintenance
- ✅ No "max_user_connections" errors

---

## 🚨 Important Notes

### Free Tier Limits

- ✅ **750 hours/month** (enough for 24/7)
- ⚠️ **Service sleeps after 15 minutes of inactivity**
- ⚠️ **Cold start: 30-60 seconds** on first request

### Database Connections

```
Render App: 2 connections
Available:  3 connections
Total:      5 connections (Clever Cloud limit)
```

### Auto-Deploy

- ✅ **Enabled** by default
- Every `git push origin main` triggers redeployment
- Build time: ~5-7 minutes

---

## 🔄 Keep Service Alive (Prevent Sleep)

### Option A: UptimeRobot (Recommended)

1. Go to https://uptimerobot.com
2. Create free account
3. Add New Monitor:
   ```
   Monitor Type: HTTP(s)
   URL: https://easypark-backend.onrender.com/actuator/health
   Monitoring Interval: 5 minutes
   ```

### Option B: Cron-Job.org

1. Go to https://cron-job.org
2. Create account
3. Add cron job:
   ```
   URL: https://easypark-backend.onrender.com/actuator/health
   Schedule: */10 * * * * (every 10 minutes)
   ```

### Option C: GitHub Actions (in repository)

Create `.github/workflows/keep-alive.yml` (optional)

---

## 🐛 Troubleshooting

### Problem: Build Fails

**Check:**
- ✅ Java version in pom.xml is 21
- ✅ Dockerfile syntax is correct
- ✅ All dependencies are in pom.xml

**Solution:**
```bash
# Test locally first
docker build -t easypark-test .
docker run -p 8080:8080 easypark-test
```

### Problem: App Starts but Crashes

**Check Logs for:**
- Database connection errors
- Missing environment variables
- Port binding issues

**Solution:**
- Verify all environment variables
- Check DATABASE_URL format
- Ensure PORT is set correctly

### Problem: 503 Service Unavailable

**Causes:**
- Cold start (wait 60 seconds)
- App is deploying (check status)
- Database connection timeout

**Solution:**
- Wait and retry
- Check database is accessible
- Verify connection pool settings

### Problem: Slow Responses

**Causes:**
- Cold start after inactivity
- Free tier limitations
- Database queries not optimized

**Solution:**
- Set up UptimeRobot
- Add database indexes
- Review slow queries

---

## 📱 Your URLs

After deployment, your service will be available at:

```
Backend:    https://easypark-backend.onrender.com
API Docs:   https://easypark-backend.onrender.com/swagger-ui/index.html
Health:     https://easypark-backend.onrender.com/actuator/health
```

---

## ✅ Deployment Checklist

Before deploying, ensure:

- [ ] ✅ Dockerfile exists and is configured
- [ ] ✅ render.yaml exists with correct settings
- [ ] ✅ All changes committed to GitHub
- [ ] ✅ Database credentials updated
- [ ] ✅ application-prod.properties configured
- [ ] ✅ Pool de conexiones configurado (2 max)
- [ ] ✅ Actuator enabled for health checks
- [ ] ✅ JWT secret configured

---

## 🎯 Expected Results

After successful deployment:

```
✅ Build Status: Success
✅ Service Status: Live
✅ Health Check: Passing
✅ Database: Connected (2/5 connections)
✅ API: Accessible via Swagger UI
✅ Auto-deploy: Enabled
```

---

## 📞 Support

- **Render Docs:** https://render.com/docs
- **Render Status:** https://status.render.com/
- **Community:** https://community.render.com/

---

**📅 Last Updated:** 2025-11-10  
**🐳 Docker:** Ready  
**🚀 Status:** Ready to Deploy  
**⏱️ Estimated Time:** 5-10 minutes

