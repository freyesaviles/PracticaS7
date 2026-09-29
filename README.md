# Gestión de productos

API REST de la práctica guiada de la semana 7 de Servicios Web. El proyecto utiliza Spring Boot, Spring Data JPA, Hibernate, PostgreSQL y Flyway.

## Requisitos

- Java 21 o superior.
- Maven 3.9 o superior, o Maven Wrapper desde IntelliJ IDEA.
- PostgreSQL 15 o superior.

## Configuración de PostgreSQL en macOS

En este equipo se detectó PostgreSQL 17 instalado mediante Homebrew. Para iniciarlo:

```bash
brew services start postgresql@17
```

Para detenerlo:

```bash
brew services stop postgresql@17
```

La aplicación usa por defecto:

| Variable | Valor predeterminado |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/gestion_productos` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |

Las variables de entorno permiten usar otras credenciales sin modificar el código.

## Crear la base de datos

```sql
CREATE DATABASE gestion_productos;
```

Las tablas se crean automáticamente mediante las migraciones de Flyway. No se deben crear manualmente.

## Ejecutar

```bash
mvn spring-boot:run
```

También se puede ejecutar desde IntelliJ IDEA iniciando `GestionProductosApplication`.

## Migraciones

- `V1__crear_tablas.sql`: crea `categoria` y `producto` con la relación categoría-producto.
- `V2__agregar_descripcion_producto.sql`: agrega la descripción del producto.
- `V3__crear_proveedores_y_relacionar_productos.sql`: crea `proveedor` y agrega la relación proveedor-producto.

`spring.jpa.hibernate.ddl-auto=validate` hace que Hibernate valide el esquema existente, mientras Flyway administra sus cambios.

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/categorias` | Lista categorías |
| `POST` | `/api/categorias` | Crea una categoría |
| `GET` | `/api/productos` | Lista productos |
| `POST` | `/api/productos` | Crea un producto relacionado |
| `GET` | `/api/proveedores` | Lista proveedores |
| `POST` | `/api/proveedores` | Crea un proveedor |

Ejemplo de categoría:

```json
{
  "nombre": "Computadoras",
  "activa": true
}
```

Ejemplo de proveedor:

```json
{
  "nombre": "Distribuidora Centroamericana",
  "telefono": "2255-0101",
  "correo": "ventas@distribuidora.example",
  "activo": true
}
```

Ejemplo de producto. Se debe usar un ID real de categoría y proveedor existentes:

```json
{
  "codigo": "LAP-001",
  "nombre": "Laptop Lenovo",
  "descripcion": "Laptop para trabajo y estudio",
  "categoria": { "id": 1 },
  "proveedor": { "id": 1 },
  "precioVenta": 850.00,
  "existencia": 10
}
```
