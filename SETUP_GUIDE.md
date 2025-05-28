# Guía de Instalación y Ejecución de la API de Tiendas

Esta guía proporciona los pasos necesarios para instalar y ejecutar la API de gestión de tiendas de Mercadona. La aplicación está diseñada para funcionar en un entorno Docker, lo que facilita su despliegue y ejecución.

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes componentes:

1. **Docker**: Versión 20.10.0 o superior

2. **Docker Compose**: Versión 2.0.0 o superior

3. **Git**: Para clonar el repositorio (opcional)

4. **WSL**: Para poder ejecutar docker en un entorno virtualizado.

## Estructura del Proyecto

El proyecto consta de los siguientes componentes principales:

- **API de Tiendas**: Aplicación Java Spring Boot (puerto 8081)
- **Base de Datos PostgreSQL**: Para almacenar los datos de tiendas, secciones, trabajadores y asignaciones (puerto 5432)
- **API Externa de Tiendas**: Servicio externo consumido por nuestra API (puerto 8080)

## Pasos para la Instalación y Ejecución

### 1. Obtener el código fuente

Clona el repositorio o descomprime el archivo con el código fuente en tu directorio de trabajo.

### 2. Verificar la configuración

Revisa los archivos de configuración principales:

- **docker-compose.yml**: Define los servicios necesarios
- **Dockerfile**: Define cómo se construye la imagen de la API
- **db-scripts/init.sql**: Script de inicialización de la base de datos
- **src/main/resources/application.properties**: Configuración de la aplicación Spring Boot

### 3. Iniciar los servicios con Docker Compose

Desde el directorio raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`), ejecuta:

```powershell
docker-compose up -d
```

Este comando:

- Construirá la imagen Docker de la API utilizando el Dockerfile
- Descargará las imágenes necesarias de PostgreSQL y la API externa
- Iniciará todos los servicios en modo desconectado (-d)

### 4. Verificar que los servicios están funcionando

Comprueba el estado de los contenedores:

```powershell
docker-compose ps
```

Deberías ver tres contenedores en estado "Up":

- tienda_postgres
- tienda_api
- external-stores-api

### 5. Acceder a la API

La API estará disponible en:

- URL base: http://localhost:8081
- Documentación Swagger: http://localhost:8081/swagger-ui/index.html

## Estructura de la Base de Datos

La aplicación utiliza una base de datos PostgreSQL con las siguientes tablas:

1. **tienda**: Almacena información de las tiendas

   - id, nombre, codigo

2. **seccion**: Secciones de cada tienda

   - id, nombre, horas_necesarias, tienda_id

3. **trabajador**: Trabajadores asignados a las tiendas

   - id, nombre, apellidos, identificacion, horas_totales, tienda_id

4. **asignacion**: Asignación de trabajadores a secciones
   - id, horas_asignadas, trabajador_id, seccion_id

La base de datos se inicializa automáticamente con datos de ejemplo al arrancar.

## Desarrollo Local (Opcional)

Para desarrollo local sin Docker, necesitarás:

1. **Java 17 JDK** (versión especificada en el pom.xml)

   - [Descargar Java JDK](https://adoptium.net/)

2. **Maven 3.8+** (para gestión de dependencias)

   - [Descargar Maven](https://maven.apache.org/download.cgi)

3. **PostgreSQL** (instalado localmente o en Docker)

Configuración para desarrollo local:

```powershell
# Instalar dependencias y compilar
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

## Solución de Problemas

### La API no arranca correctamente

Verifica los logs de la aplicación:

```powershell
docker-compose logs api
```

### Problemas con la base de datos

Verifica los logs de PostgreSQL:

```powershell
docker-compose logs postgres
```

### Reiniciar los servicios

Si necesitas reiniciar los servicios:

```powershell
docker-compose restart
```

### Detener y eliminar todos los servicios

Para detener y eliminar los contenedores, redes y volúmenes:

```powershell
docker-compose down -v
```

## Información Técnica Adicional

### Versiones utilizadas

- **Spring Boot**: 3.5.0
- **Java**: 17
- **PostgreSQL**: 15
- **API Externa**: jameral/stores:latest

### Dependencias principales

- spring-boot-starter-data-jpa
- spring-boot-starter-web
- postgresql
- springdoc-openapi-starter-webmvc-ui (Swagger)
- lombok

## Recursos Adicionales

- [Documentación de Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Documentación de Docker Compose](https://docs.docker.com/compose/)
- [Documentación de PostgreSQL](https://www.postgresql.org/docs/)
