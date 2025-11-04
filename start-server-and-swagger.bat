@echo off
echo ========================================
echo   INICIANDO EASYPARK PLATFORM
echo ========================================
echo.
echo Iniciando servidor Spring Boot...
echo Puerto: 8080
echo.
echo Espera a que veas el mensaje:
echo "Started PlatformApplication in X.XXX seconds"
echo.
echo Luego podras acceder a:
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo - API Docs: http://localhost:8080/v3/api-docs
echo.
echo ========================================
echo.

cd /d "C:\Users\WINDOWS 10\IdeaProjects\platform"

start "EasyPark Platform" cmd /k ".\mvnw.cmd spring-boot:run"

echo.
echo Esperando que el servidor inicie (30 segundos)...
timeout /t 30 /nobreak > nul

echo.
echo Abriendo Swagger UI en el navegador...
start http://localhost:8080/swagger-ui.html

echo.
echo ========================================
echo   SERVIDOR INICIADO
echo ========================================
echo.
echo Swagger UI deberia abrirse en tu navegador
echo Si no se abre automaticamente, visita:
echo http://localhost:8080/swagger-ui.html
echo.
echo Presiona Ctrl+C en la ventana del servidor para detenerlo
echo.
pause

