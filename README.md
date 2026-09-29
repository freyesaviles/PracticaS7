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
./mvnw spring-boot:run
```

También se puede ejecutar desde IntelliJ IDEA iniciando `GestionProductosApplication`.

## Migraciones

Una migración es un cambio versionado en la estructura de la base de datos. Flyway ejecuta cada archivo una sola vez y registra el resultado en `flyway_schema_history`. El prefijo `V1`, `V2` y `V3` indica el orden en que se aplican.

| Migración | Cambio realizado | Evidencia que puede revisar el profesor |
|---|---|---|
| `V1__crear_tablas.sql` | Crea `categoria`, `producto` y la FK `producto.categoria_id`. | Tablas `categoria` y `producto`, además de la relación entre ambas. |
| `V2__agregar_descripcion_producto.sql` | Agrega la columna `producto.descripcion`. | Columna `descripcion` en la tabla y atributo equivalente en la entidad `Producto`. |
| `V3__crear_proveedores_y_relacionar_productos.sql` | Crea `proveedor`, agrega `producto.proveedor_id` y su FK. | Tabla `proveedor` y relación `Proveedor 1:N Producto`. |

`spring.jpa.hibernate.ddl-auto=validate` hace que Hibernate valide el esquema existente, mientras Flyway administra sus cambios.

### ¿Qué se debe validar?

Hay dos validaciones distintas:

1. **Validación de Flyway:** al iniciar la aplicación deben ejecutarse V1, V2 y V3 sin errores. La tabla `flyway_schema_history` debe mostrar las tres migraciones con `success = true`.
2. **Validación de Hibernate:** `ddl-auto=validate` compara las entidades Java con las tablas existentes. Si falta una tabla, columna, tipo o relación esperada, la aplicación no inicia. Hibernate no crea ni modifica tablas en esta configuración.

También se valida el funcionamiento de la API mediante Postman:

- `GET /api/categorias` responde `200`.
- Los `POST` de categorías y proveedores responden `201`.
- `POST /api/productos` guarda el producto usando IDs existentes de categoría y proveedor.
- `GET /api/productos` muestra las relaciones guardadas.

### Comandos de comprobación

Después de iniciar la aplicación, se pueden usar estas consultas:

```sql
SELECT installed_rank, version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;

SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_name IN ('categoria', 'producto', 'proveedor')
ORDER BY table_name;

SELECT table_name, column_name
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name IN ('categoria', 'producto', 'proveedor')
ORDER BY table_name, ordinal_position;
```

La primera consulta debe mostrar las versiones `1`, `2` y `3`, todas exitosas. La segunda debe mostrar las tres tablas. La tercera permite comprobar las columnas creadas por cada migración.

### Correspondencia con el historial de Git

Los cambios se separaron en commits para que el avance sea visible:

- `Agregar migración inicial y gestión de categorías`: V1 y entidad `Categoria`.
- `Implementar productos y migración de descripción`: V2, entidad `Producto` y su endpoint.
- `Agregar proveedores y relación con productos`: V3, entidad `Proveedor` y relación con `Producto`.
- `Documentar validación y ejecución de migraciones`: explicación de las comprobaciones y evidencias.

Para validar el proyecto completo:

```bash
./mvnw test
./mvnw package -DskipTests
```

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
