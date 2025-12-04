#!/usr/bin/env bash

# launch-system.sh
# Script para lanzar el sistema completo: backend (Spring Boot + MySQL) y frontend (JavaFX).
#
# Flujo principal:
#  1. Determina la ruta raíz del repositorio.
#  2. Comprueba si Docker está instalado y ofrece instalarlo automáticamente según la distro (apt/dnf/pacman).
#  3. Detecta el comando correcto de Docker Compose (plugin "docker compose" o binario "docker-compose").
#  4. Levanta los contenedores en segundo plano usando docker compose.
#  5. Espera a que el contenedor de MySQL esté listo y responda.
#  6. Compila el servidor Spring Boot con Maven si está disponible; si no, usa el JAR ya compilado.
#  7. Arranca el servidor en background y espera el endpoint /api/health.
#  8. Ofrece lanzar el frontend (JavaFX) en background si Maven está disponible.
#
# Funciones principales:
#  - check_cmd(cmd): Comprueba si un comando existe en PATH.
#  - ask_yes_no(pregunta): Pide confirmación al usuario (sí/no) y devuelve 0 si sí.
#  - detect_compose(): Detecta si usar "docker compose" (plugin) o "docker-compose" (binario clásico).
#  - install_docker_*(): Instala Docker según gestor de paquetes (apt/dnf/pacman) y habilita el servicio.
#  - install_docker(): Selecciona automáticamente el método de instalación según la distro.
#  - ensure_docker(): Verifica la presencia de Docker y ofrece instalarlo si falta.

set -euo pipefail

#############################################
# FUNCIONES AUXILIARES
#############################################

# check_cmd: devuelve 0 si el comando indicado existe, 1 si no.
check_cmd() { command -v "$1" >/dev/null 2>&1; }

# ask_yes_no: bucle de entrada de usuario que acepta y/Y como sí; n/N o vacío como no.
ask_yes_no() {
  while true; do
    read -rp "$1 [y/N]: " yn
    case $yn in
      [Yy]*) return 0 ;;
      [Nn]*|"") return 1 ;;
    esac
  done
}

# detect_compose: determina si el sistema tiene el plugin "docker compose" o el binario "docker-compose".
# Configura DOCKER_COMPOSE con el comando apropiado y aborta si ninguno está disponible.
detect_compose() {
  if docker compose version >/dev/null 2>&1; then
    echo "[check] Usando plugin integrado: docker compose"
    DOCKER_COMPOSE="docker compose"
  elif docker-compose version >/dev/null 2>&1; then
    echo "[check] Usando binario clásico: docker-compose"
    DOCKER_COMPOSE="docker-compose"
  else
    echo "[error] No se encontró docker compose ni docker-compose."
    echo "        Instala uno con:"
    echo "        sudo apt install docker-compose-plugin"
    exit 1
  fi
}

# install_docker_apt: instalación de Docker en sistemas basados en Debian/Ubuntu mediante apt.
# Añade el repositorio oficial de Docker, instala paquete y habilita el servicio.
install_docker_apt() {
  echo "[install] Installing Docker (apt)..."
  sudo apt-get update
  sudo apt-get install -y ca-certificates curl gnupg lsb-release
  sudo mkdir -p /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | \
    sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
    https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
    $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list >/dev/null
  sudo apt-get update
  sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
  sudo systemctl enable --now docker || true
}

# install_docker_dnf: instalación de Docker en sistemas Fedora/RHEL/CentOS con dnf.
install_docker_dnf() {
  echo "[install] Installing Docker (dnf)..."
  sudo dnf -y install dnf-plugins-core
  sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo
  sudo dnf -y install docker-ce docker-ce-cli containerd.io docker-compose-plugin
  sudo systemctl enable --now docker || true
}

# install_docker_pacman: instalación de Docker en sistemas Arch/Manjaro con pacman.
install_docker_pacman() {
  echo "[install] Installing Docker (pacman)..."
  sudo pacman -Sy --noconfirm docker
  sudo systemctl enable --now docker || true
}

# install_docker: decide el método de instalación según el gestor de paquetes disponible.
install_docker() {
  if check_cmd apt-get; then install_docker_apt
  elif check_cmd dnf; then install_docker_dnf
  elif check_cmd pacman; then install_docker_pacman
  else
    echo "[error] Tu distro no soporta instalación automática"
    exit 1
  fi
}

# ensure_docker: verifica si Docker está instalado; si no, ofrece instalarlo.
ensure_docker() {
  if check_cmd docker; then
    echo "[check] Docker detected: $(docker --version)"
  else
    echo "[check] Docker no encontrado."
    if ask_yes_no "¿Quieres instalar Docker ahora?"; then
      install_docker
    else
      echo "[abort] Debes instalar Docker antes de continuar."
      exit 1
    fi
  fi
}

#############################################
# FLUJO PRINCIPAL
#############################################

# 1. Definir ruta raíz del repositorio.
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
echo "[launch] Repo root: $REPO_ROOT"

# 2. Comprueba/instala Docker según la distro.
echo "[launch] Comprobando Docker..."
ensure_docker

# 3. Detecta el comando correcto de Docker Compose: docker compose ó docker-compose.
detect_compose

# 4. Levanta los contenedores en segundo plano.
echo "[launch] Levantando contenedores con $DOCKER_COMPOSE up -d..."
cd "$REPO_ROOT"
$DOCKER_COMPOSE up -d

# 5. Espera a que el contenedor de MySQL (restaurant-mysql) esté listo y responda a ping.
echo "[launch] Esperando a que MySQL responda..."

MAX_ATTEMPTS=60
for ATTEMPT in $(seq 1 $MAX_ATTEMPTS); do
  if docker ps --format '{{.Names}}' | grep -q '^restaurant-mysql$'; then
    if docker exec restaurant-mysql mysqladmin ping -h localhost \
      -u root -prootpass --silent >/dev/null 2>&1; then

      echo "[launch] MySQL está listo."
      break
    fi
  fi
  echo "[launch] MySQL no responde todavía ($ATTEMPT/$MAX_ATTEMPTS)..."
  sleep 2
done

# Si no se logra conexión dentro del tiempo, aborta y sugiere revisar logs del contenedor.
if [ "$ATTEMPT" -eq "$MAX_ATTEMPTS" ]; then
  echo "[error] MySQL no arrancó. Revisa: docker logs restaurant-mysql"
  exit 1
fi

# 6. Compila el servidor Spring Boot con Maven si está disponible; si no, usa el JAR ya compilado.
if check_cmd mvn; then
  echo "[launch] Compilando server con Maven..."
  (cd server && mvn clean package -DskipTests)
else
  echo "[warn] Maven no está instalado. Debes compilar el server manualmente."
fi

# 7. Arranca el servidor Spring Boot en background. Si hay Maven, usa spring-boot:run; si no, ejecuta el JAR más reciente.
echo "[launch] Arrancando servidor Spring Boot..."

if check_cmd mvn; then
  nohup mvn -f server spring-boot:run > "$REPO_ROOT/server/server.log" 2>&1 &
  SERVER_PID=$!
  echo "[launch] Servidor arrancado (PID=$SERVER_PID). Log: server/server.log"
else
  JAR_FILE=$(ls -t server/target/restaurant-server*.jar | head -n1)
  nohup java -jar "$JAR_FILE" > "$REPO_ROOT/server/server.log" 2>&1 &
  SERVER_PID=$!
  echo "[launch] JAR arrancado (PID=$SERVER_PID)."
fi

# 7. Sondea el endpoint /api/health en localhost:8080 y espera a que el servidor responda con HTTP 200.
# /api/health es el endpoint de salud definido en el backend Spring Boot. HTTP 200 indica que el servidor está listo para aceptar peticiones.
echo "[launch] Esperando http://localhost:8080/api/health..."

for i in $(seq 1 60); do
  if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/health | grep -q "200"; then
    echo "[launch] Servidor OK."
    break
  fi
  echo "[launch] Aún no responde ($i/60)..."
  sleep 2
done

#  8. Ofrece lanzar el frontend (JavaFX) en background con Maven si está disponible, o muestra cómo hacerlo manualmente.
echo "[launch] Preparado para lanzar front."

if check_cmd mvn; then
  echo ""
  echo "[info] Para arrancar el front manualmente:"
  echo "       mvn -Dexec.mainClass=es.ull.esit.app.JavaApplication2 exec:java"
  echo ""

  if ask_yes_no "¿Quieres lanzar el front ahora (en background)?"; then
    (
      cd "$REPO_ROOT"
      # Compila el front y luego lo ejecuta
      nohup mvn -DskipTests compile exec:java \
           -Dexec.mainClass=es.ull.esit.app.JavaApplication2 \
           > "$REPO_ROOT/front.log" 2>&1 &
    )
    echo "[launch] Front arrancado. Log: front.log"
  else
    echo "[launch] No se lanzó el front automáticamente."
  fi
else
  echo "[warn] Maven no disponible, no se puede lanzar el front automáticamente."
fi

echo "[launch] Todo listo"
