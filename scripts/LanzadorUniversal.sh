#!/usr/bin/env bash
set -euo pipefail

#############################################
# launch-system.sh
#
# Script para Linux (bash) que:
#  - comprueba e instala Docker (apt/dnf/pacman) si se solicita
#  - ejecuta `docker compose up -d`
#  - espera a que MySQL dentro del contenedor `restaurant-mysql` responda
#  - compila el middleware (server) y lo arranca en background
#  - lanza el front (opciones: mvn exec o ejecutar clases empaquetadas)
#
# Uso:
#   sudo bash scripts/launch-system.sh    # para instalar Docker sin pedir sudo más tarde
#   bash scripts/launch-system.sh         # si Docker ya está instalado
#
# NOTAS:
# - Instalar Docker automáticamente requiere privilegios (sudo).
# - El script soporta sistemas basados en Debian/Ubuntu (apt), Fedora/RHEL (dnf/yum) y Arch (pacman).
# - El front es una aplicación de escritorio; asegúrate de ejecutar esto en una sesión gráfica si quieres que aparezca la UI.
#############################################

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
echo "[launch] Repo root: $REPO_ROOT"

check_cmd() {
  command -v "$1" >/dev/null 2>&1
}

ask_yes_no() {
  # $1 = prompt
  while true; do
    read -rp "$1 [y/N]: " yn
    case $yn in
      [Yy]*) return 0 ;;
      [Nn]*|"") return 1 ;;
    esac
  done
}

install_docker_apt() {
  echo "[install] Installing Docker (apt)..."
  sudo apt-get update
  sudo apt-get install -y ca-certificates curl gnupg lsb-release
  sudo mkdir -p /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list >/dev/null
  sudo apt-get update
  sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
  sudo systemctl enable --now docker || true
}

install_docker_dnf() {
  echo "[install] Installing Docker (dnf)..."
  sudo dnf -y install dnf-plugins-core
  sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo
  sudo dnf -y install docker-ce docker-ce-cli containerd.io docker-compose-plugin
  sudo systemctl enable --now docker || true
}

install_docker_pacman() {
  echo "[install] Installing Docker (pacman)..."
  sudo pacman -Sy --noconfirm docker
  sudo systemctl enable --now docker || true
}

install_docker() {
  if check_cmd apt-get; then
    install_docker_apt
  elif check_cmd dnf; then
    install_docker_dnf
  elif check_cmd pacman; then
    install_docker_pacman
  else
    echo "[install] No automatic installer for your distro. Please install Docker manually: https://docs.docker.com/engine/install/" >&2
    return 1
  fi
  return 0
}

ensure_docker() {
  if check_cmd docker; then
    echo "[check] Docker detected: $(docker --version)"
  else
    echo "[check] Docker no encontrado. Se puede intentar instalar automáticamente (requiere sudo)."
    if ask_yes_no "¿Quieres que intente instalar Docker ahora?"; then
      install_docker || { echo "[error] Instalación automática falló"; exit 1; }
    else
      echo "[abort] Instala Docker y vuelve a ejecutar el script." >&2
      exit 1
    fi
  fi

  # comprobar docker-compose (plugin integrado)
  if docker compose version >/dev/null 2>&1; then
    echo "[check] docker compose disponible"
  else
    echo "[check] docker compose no disponible como plugin. Intentando instalar docker-compose..."
    if check_cmd curl; then
      sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
      sudo chmod +x /usr/local/bin/docker-compose
      echo "[install] docker-compose instalado en /usr/local/bin/docker-compose"
    else
      echo "Instala docker compose manualmente." >&2
    fi
  fi
}

echo "[launch] Comprobando Docker..."
ensure_docker

echo "[launch] Ejecutando: docker compose up -d"
cd "$REPO_ROOT"
docker compose up -d

echo "[launch] Esperando a que el contenedor 'restaurant-mysql' responda (mysqladmin ping)..."
MAX_ATTEMPTS=60
ATTEMPT=0
MYSQL_READY=0
while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
  ATTEMPT=$((ATTEMPT+1))
  # comprobar si el contenedor existe en docker ps
  if docker ps --format '{{.Names}}' | grep -q '^restaurant-mysql$'; then
    docker exec restaurant-mysql mysqladmin ping -h localhost -u root -prootpass --silent >/dev/null 2>&1 || true
    if [ $? -eq 0 ]; then
      MYSQL_READY=1
      break
    fi
  fi
  echo "[launch] Esperando MySQL... intento $ATTEMPT/$MAX_ATTEMPTS"
  sleep 2
done

if [ $MYSQL_READY -ne 1 ]; then
  echo "[error] MySQL no respondió dentro del tiempo esperado. Revisa 'docker logs restaurant-mysql'" >&2
  exit 1
fi

echo "[launch] MySQL listo."

# Compilar server
if check_cmd mvn; then
  echo "[launch] Compilando server con Maven..."
  (cd server && mvn clean package -DskipTests)
else
  echo "[warn] Maven no está en PATH. Saltando compilación. Asegúrate de que 'server/target' contiene el JAR." >&2
fi

# Arrancar server en background (intenta con mvn spring-boot:run, si falla intenta JAR)
echo "[launch] Intentando arrancar el servidor en background..."
if check_cmd mvn; then
  nohup mvn -f server spring-boot:run > "$REPO_ROOT/server/server.log" 2>&1 &
  SERVER_PID=$!
  echo "[launch] mvn spring-boot:run lanzado (PID=$SERVER_PID). Logs: server/server.log"
else
  JAR_FILE=$(ls -t server/target/restaurant-server*.jar 2>/dev/null | head -n1 || true)
  if [ -n "$JAR_FILE" ]; then
    nohup java -jar "$JAR_FILE" > "$REPO_ROOT/server/server.log" 2>&1 &
    SERVER_PID=$!
    echo "[launch] JAR arrancado (PID=$SERVER_PID). Logs: server/server.log"
  else
    echo "[error] No hay manera de arrancar el servidor (ni mvn ni JAR disponibles)." >&2
    exit 1
  fi
fi

echo "[launch] Esperando health endpoint en http://localhost:8080/api/health"
HEALTH_OK=0
ATT=0
while [ $ATT -lt 60 ]; do
  ATT=$((ATT+1))
  HTTP_CODE=0
  if check_cmd curl; then
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" --max-time 2 http://localhost:8080/api/health || echo 000)
  else
    # sin curl, usar netcat como fallback (no fiable para http status)
    sleep 2
    HTTP_CODE=000
  fi
  if [ "$HTTP_CODE" != "000" ] && [ "$HTTP_CODE" -ge 200 ] 2>/dev/null; then
    HEALTH_OK=1
    break
  fi
  echo "[launch] Esperando servidor... intento $ATT/60"
  sleep 2
done

if [ $HEALTH_OK -eq 1 ]; then
  echo "[launch] Servidor listo en http://localhost:8080"
else
  echo "[warn] Servidor no respondió al endpoint de health. Revisa $REPO_ROOT/server/server.log" >&2
fi

# Lanzar front
echo "[launch] Lanzando front (si estás en entorno gráfico)."
if check_cmd mvn; then
  echo "[launch] Lanza el front en foreground (recomendado para GUI):"
  echo "  cd $REPO_ROOT && mvn -Dexec.mainClass=es.ull.esit.app.JavaApplication2 exec:java"
  if ask_yes_no "¿Quieres que lo intente lanzar ahora en background? (si no tienes sesión gráfica, no funcionará)"; then
    nohup mvn -Dexec.mainClass=es.ull.esit.app.JavaApplication2 exec:java > "$REPO_ROOT/front.log" 2>&1 &
    FRONT_PID=$!
    echo "[launch] Front lanzado en background (PID=$FRONT_PID). Logs: front.log"
  else
    echo "[launch] Omitting automatic front launch. Puedes lanzar el front manualmente con el comando mostrado más arriba."
  fi
else
  echo "[warn] Maven no disponible: puedes ejecutar el front si tienes clases compiladas:"
  echo "  java -cp \"target/classes:target/dependency/*\" es.ull.esit.app.JavaApplication2"
fi

echo "[launch] Script finalizado. Revisa logs en server/server.log y front.log si se generaron."
