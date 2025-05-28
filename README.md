# Guía de Instalación y Ejecución de la API de Tiendas

Esta guía proporciona los pasos necesarios para instalar y ejecutar la API de gestión de tiendas de Mercadona. La aplicación está diseñada para funcionar en un entorno Docker, lo que facilita su despliegue y ejecución.

## Requisitos Previos

Antes de comenzar, asegurarse de tener instalados los siguientes componentes:

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

Clonar el repositorio en tu directorio de trabajo mediante GIT.

### 2. Problemas con API externa (jameral/stores:latest)

He encontrado problemas a la hora de arrancar la imagen de la api externa `jameral/stores:latest` por
incompatibilidad de arquitecturas ya que está construida para arm64 y mi sistema es amd64, para solucionarlo
he utilizado QEMU y para instalarlo he ejecutado los siguientes comandos dentro de WSL:

```powershell
docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
docker run --rm --privileged tonistiigi/binfmt --install all
```

Con el siguiente comando comprobamos que esté bien configurado:

```powershell
docker run --rm --platform linux/arm64 alpine uname -m
```

El resultado esperado si todo ha ido correctamente es:

```powershell
aarch64
```

_Como comentario adicional, en las pruebas que he ido haciendo en entorno local, la imagen externa a veces falla indicando un error de BD, es inconsistente y con un reinicio se soluciona._

### 3. Iniciar los servicios con Docker Compose

Desde el directorio raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`), ejecutar:

```powershell
docker-compose up -build
```

Este comando:

- Construirá la imagen Docker de la API utilizando el Dockerfile
- Descargará las imágenes necesarias de PostgreSQL y la API externa

### 4. Verificar que los servicios están funcionando

Comprobar el estado de los contenedores:

```powershell
docker-compose ps
```

Deberían aparecer los siguientes servicios "Up":

- tienda_postgres
- tienda_api
- external-stores-api

### 5. Acceder a la API

La API estará disponible en:

- URL base: http://localhost:8081
- Documentación Swagger: http://localhost:8081/swagger-ui/index.html
- La base de datos se inicializa automáticamente con datos de ejemplo al arrancar.

## Solución de Problemas

### La API no arranca correctamente

Verificar los logs de la aplicación:

```powershell
docker-compose logs api
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

## Colección de Datos y Uso de la API

La aplicación viene pre-cargada con datos de ejemplo para facilitar las pruebas. A continuación se muestra la estructura de los datos y cómo utilizarlos con los diferentes endpoints disponibles.

### Estructura de Datos Inicial

#### Tiendas

La base de datos contiene 3 tiendas pre-cargadas:

| ID  | Nombre               | Código |
| --- | -------------------- | ------ |
| 1   | Supermercado Central | SC001  |
| 2   | Supermercado Local   | SL002  |
| 3   | Supermercado Express | SE003  |

#### Secciones

Cada tienda tiene 5 secciones estándar:

| Nombre     | Horas Necesarias |
| ---------- | ---------------- |
| Horno      | 8                |
| Cajas      | 16               |
| Pescadería | 16               |
| Verduras   | 16               |
| Droguería  | 16               |

#### Trabajadores

Hay 3 trabajadores por tienda:

**Tienda SC001 (Supermercado Central)**:

- Laura Martínez Gómez (12345678A) - 8 horas totales
- Carlos Ruiz Fernández (23456789B) - 6 horas totales
- Ana López Torres (34567890C) - 4 horas totales

**Tienda SL002 (Supermercado Local)**:

- Pedro García Pérez (45678901D) - 8 horas totales
- Marta Sánchez Jiménez (56789012E) - 6 horas totales
- Luis Hernández Díaz (67890123F) - 4 horas totales

**Tienda SE003 (Supermercado Express)**:

- Elena Moreno Ruiz (78901234G) - 8 horas totales
- Javier Jiménez López (89012345H) - 6 horas totales
- Sofía Torres García (90123456I) - 4 horas totales

### Uso de los Endpoints

- Para hacer uso de la API se puede hacer mediante el swagger en http://localhost:8081/swagger-ui/index.html o mediante la colección de Postman que se encuentra en la raiz del proyecto _API_Tiendas_Mercadona.postman_collection.json_

- La API está securizada, existe un método de autenticación (/authenticate) que devuelve un token para utilizar en los demás endpoints.
  ```powershell
  username: store_admin
  password: admin_pass
  ```
