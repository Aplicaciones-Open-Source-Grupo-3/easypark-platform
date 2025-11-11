"""
Script para verificar el número de conexiones activas a la base de datos MySQL
Útil para monitorear el uso del pool de conexiones de HikariCP
"""
import sys
from datetime import datetime

print("=" * 70)
print("  📊 MONITOR DE CONEXIONES MYSQL - EASYPARK PLATFORM")
print("=" * 70)
print()

# Verificar dependencias
try:
    import pymysql
    print("✅ pymysql instalado")
except ImportError:
    print("❌ ERROR: pymysql no está instalado")
    print()
    print("Instala con: pip install pymysql cryptography")
    print()
    input("Presiona Enter para salir...")
    sys.exit(1)

# Configuración de la base de datos
config = {
    'host': 'bfbh4n2ccukyxuo2sny4-mysql.services.clever-cloud.com',
    'port': 3306,
    'user': 'uzk5dknvpy7byyoo',
    'password': 'w7HGO2zBZN2qm2HEdV2s',
    'database': 'bfbh4n2ccukyxuo2sny4',
    'connect_timeout': 10
}

print()
print(f"🔌 Conectando a: {config['host']}")
print(f"📅 Fecha/Hora: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
print()

try:
    conn = pymysql.connect(**config)
    cursor = conn.cursor()

    # Consultar estadísticas de conexiones
    cursor.execute("""
        SELECT
            COUNT(*) as total,
            SUM(CASE WHEN command != 'Sleep' THEN 1 ELSE 0 END) as active,
            SUM(CASE WHEN command = 'Sleep' THEN 1 ELSE 0 END) as idle
        FROM information_schema.processlist
        WHERE user = %s
    """, (config['user'],))

    result = cursor.fetchone()
    total, active, idle = result
    available = 5 - total

    # Mostrar resumen
    print("=" * 70)
    print("📊 RESUMEN DE CONEXIONES")
    print("=" * 70)
    print(f"  🔗 Total conexiones:     {total}/5")
    print(f"  🔄 Activas (ejecutando): {active}")
    print(f"  💤 Inactivas (pool):     {idle}")
    print(f"  🆓 Disponibles:          {available}")
    print()

    # Indicador de estado
    if total <= 2:
        status = "✅ EXCELENTE"
        color = "🟢"
    elif total <= 3:
        status = "⚠️  BUENO"
        color = "🟡"
    elif total <= 4:
        status = "⚠️  ALTO"
        color = "🟠"
    else:
        status = "🚨 CRÍTICO"
        color = "🔴"

    print(f"  {color} Estado: {status}")
    print("=" * 70)
    print()

    # Detalle de cada conexión
    cursor.execute("""
        SELECT
            id,
            host,
            db,
            command,
            time,
            state,
            info
        FROM information_schema.processlist
        WHERE user = %s
        ORDER BY time DESC
    """, (config['user'],))

    connections = cursor.fetchall()

    print("=" * 70)
    print("📋 DETALLE DE CADA CONEXIÓN")
    print("=" * 70)

    for idx, row in enumerate(connections, 1):
        conn_id, host, db, command, time_sec, state, info = row

        print(f"\n🔗 Conexión #{idx}")
        print(f"   ID:      {conn_id}")
        print(f"   Host:    {host}")
        print(f"   DB:      {db or 'N/A'}")
        print(f"   Estado:  {command}")
        print(f"   Tiempo:  {time_sec}s")

        if state:
            print(f"   Info:    {state}")

        if info:
            query_preview = info[:60].replace('\n', ' ')
            print(f"   Query:   {query_preview}{'...' if len(info) > 60 else ''}")

        print("-" * 70)

    # Recomendaciones
    print()
    print("=" * 70)
    print("💡 RECOMENDACIONES")
    print("=" * 70)

    if total <= 2:
        print("  ✅ El pool está funcionando correctamente")
        print("  ✅ Hay espacio suficiente para herramientas (Workbench, etc)")
    elif total <= 3:
        print("  ⚠️  El uso está dentro del rango normal")
        print("  ℹ️  Puedes conectarte con herramientas si necesitas")
    elif total <= 4:
        print("  ⚠️  Uso alto de conexiones")
        print("  ℹ️  Considera cerrar herramientas no esenciales")
    else:
        print("  🚨 LÍMITE ALCANZADO")
        print("  ⚠️  No podrás conectar herramientas adicionales")
        print("  💡 Espera 2 minutos para que se liberen conexiones")
        print("  💡 O cierra herramientas (Workbench, DBeaver, etc)")

    print("=" * 70)
    print()

    # Información adicional sobre la configuración
    print("=" * 70)
    print("⚙️  CONFIGURACIÓN ACTUAL")
    print("=" * 70)
    print("  Pool configurado en application-prod.properties:")
    print("    • maximum-pool-size: 2")
    print("    • minimum-idle: 1")
    print("    • idle-timeout: 60 segundos")
    print("    • max-lifetime: 120 segundos")
    print()
    print("  Distribución esperada:")
    print("    • Aplicación Spring Boot: 1-2 conexiones")
    print("    • MySQL Workbench/Tools: 0-2 conexiones")
    print("    • Margen de seguridad: 1 conexión")
    print("=" * 70)

    # Cerrar conexión
    cursor.close()
    conn.close()

    print()
    print("✅ Monitoreo completado exitosamente")
    print()

except pymysql.err.OperationalError as e:
    print()
    print("=" * 70)
    print("❌ ERROR DE CONEXIÓN")
    print("=" * 70)
    print(f"Error: {e}")
    print()

    if "max_user_connections" in str(e):
        print("⚠️  No se puede conectar: Límite de conexiones alcanzado")
        print()
        print("Soluciones:")
        print("  1. Espera 2 minutos (las conexiones se liberarán automáticamente)")
        print("  2. Cierra MySQL Workbench u otras herramientas")
        print("  3. Reinicia el addon MySQL en Clever Cloud Console")
    else:
        print("Verifica:")
        print("  • Conexión a internet")
        print("  • Credenciales en el script")
        print("  • Estado del servidor MySQL en Clever Cloud")

    print("=" * 70)

except Exception as e:
    print()
    print("=" * 70)
    print("❌ ERROR INESPERADO")
    print("=" * 70)
    print(f"Tipo: {type(e).__name__}")
    print(f"Mensaje: {str(e)}")
    print("=" * 70)

print()
input("Presiona Enter para salir...")

