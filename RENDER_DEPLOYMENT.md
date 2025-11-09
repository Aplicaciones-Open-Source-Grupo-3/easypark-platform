# Deploy to Render - Quick Guide

## Prerequisites
- GitHub repository updated (✅ Done)
- Render account (free): https://render.com

## Step-by-Step Deployment

### 1. Create Render Account
- Go to https://render.com
- Sign up with GitHub

### 2. Create New Web Service
- Click "New +" → "Web Service"
- Connect your GitHub repository: `Aplicaciones-Open-Source-Grupo-3/easypark-platform`
- Click "Connect"

### 3. Configure Service

**Basic Settings:**
- Name: `easypark-backend`
- Region: Choose closest to you
- Branch: `main`
- Runtime: `Java`

**Build Settings:**
- Build Command: `./mvnw clean package -DskipTests`
- Start Command: `java -Dserver.port=$PORT -jar target/platform-1.0.0.jar`

**Instance Type:**
- Select: `Free` (0 USD/month)

### 4. Add Environment Variables

Click "Advanced" → "Add Environment Variable" and add these:

```
SPRING_PROFILES_ACTIVE=prod

DATABASE_URL=jdbc:mysql://bighqeizhtcbg2szlczt-mysql.services.clever-cloud.com:3306/bighqeizhtcbg2szlczt?useSSL=true&requireSSL=true&serverTimezone=UTC

DATABASE_USERNAME=ues1f415lj3l7lcd

DATABASE_PASSWORD=UDYMRQudWFk8FwJL25WF

JWT_SECRET=your-production-secret-key-at-least-32-characters-long-for-jwt-token-signing

JAVA_OPTS=-Xmx512m -Xms256m
```

### 5. Deploy
- Click "Create Web Service"
- Wait 5-10 minutes for build and deployment
- Your backend will be available at: `https://easypark-backend.onrender.com`

### 6. Test Your API

```bash
# Health check
curl https://easypark-backend.onrender.com/api/v1/health

# Swagger UI
https://easypark-backend.onrender.com/swagger-ui.html
```

### 7. Keep Service Alive (Optional)

To prevent the service from sleeping after 15 minutes of inactivity:

**Option A: Using cron-job.org**
1. Go to https://cron-job.org
2. Create free account
3. Add new cron job:
   - URL: `https://easypark-backend.onrender.com/api/v1/health`
   - Interval: Every 10 minutes

**Option B: Using GitHub Actions** (Already configured in repo)
- File: `.github/workflows/keep-alive.yml` will be created
- Pings your service every 10 minutes automatically

## Important Notes

- **First request**: Takes 30-60 seconds (cold start)
- **After inactivity**: Service sleeps after 15 minutes
- **Free tier**: 750 hours/month (enough for full month)
- **Logs**: Available in Render dashboard
- **Auto-deploy**: Pushes to `main` branch trigger automatic redeployment

## Troubleshooting

**Build fails:**
- Check logs in Render dashboard
- Verify Java version in pom.xml (should be 21)

**Database connection error:**
- Verify environment variables are correct
- Check Clever Cloud database is active

**Service not responding:**
- Wait 30-60 seconds for cold start
- Check logs for errors

## Your URLs

- **Backend**: https://easypark-backend.onrender.com
- **API Docs**: https://easypark-backend.onrender.com/swagger-ui.html
- **Health**: https://easypark-backend.onrender.com/api/v1/health

---

**Deployment Status:** Ready to deploy!
**Estimated time:** 5-10 minutes

