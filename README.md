# Gestión de productos

API REST desarrollada para las prácticas guiadas de Servicios Web. El proyecto usa Spring Boot, Spring Data JPA, Hibernate, PostgreSQL y Flyway.

## Laboratorio 1: continuación de la práctica de la semana 7

### ¿Qué problema había en la semana 7?

La API de la semana 7 funcionaba, pero todavía estaba incompleta para crecer de forma ordenada:

- `ProductoController` accedía directamente al repositorio para listar productos.
- El servicio solo cubría el guardado.
- Faltaban el DTO, el CRUD completo y la consulta por categoría.
- La relación `Categoria–Producto` no tenía el lado `@OneToMany`.
- No existían etiquetas ni la relación muchos a muchos.

No era un error que impidiera arrancar la aplicación; era una oportunidad de mejorar la arquitectura y completar las funcionalidades solicitadas en el Laboratorio 1.

### ¿Cómo se arregló?

Se separaron las responsabilidades así:

```text
Cliente → Controller → Service → Repository → PostgreSQL
```

Ahora el proyecto tiene los paquetes `controller`, `service`, `repository`, `entity` y `dto`. Además, se implementaron el CRUD completo de productos, la consulta por categoría, la relación bidireccional `Categoria 1:N Producto` y la relación `Producto N:N Etiqueta` mediante `producto_etiqueta`.

## Estructura

```text
src/main/java/ni/edu/uam/gestionproductos/
├── controller/
├── dto/
├── entity/
├── repository/
└── service/
```

## Requisitos y configuración

- Java 21 o superior.
- PostgreSQL 15 o superior.
- Maven 3.9 o Maven Wrapper.

En macOS, si PostgreSQL fue instalado con Homebrew:

```bash
brew services start postgresql@17
```

La aplicación usa por defecto:

```text
URL:      jdbc:postgresql://localhost:5432/gestion_productos
Usuario:  postgres
Clave:    postgres
```

Crear la base de datos una sola vez:

```sql
CREATE DATABASE gestion_productos;
```

Luego iniciar la API:

```bash
./mvnw spring-boot:run
```

También se puede ejecutar `GestionProductosApplication` desde IntelliJ IDEA.

## Migraciones

Flyway actualiza la base de datos automáticamente al iniciar la aplicación. Cada migración se ejecuta una sola vez y queda registrada en `flyway_schema_history`.

| Versión | Qué agrega |
|---|---|
| V1 | Tablas `categoria` y `producto`. |
| V2 | Columna `producto.descripcion`. |
| V3 | Tabla `proveedor` y relación con `producto`. |
| V4 | Tablas `etiqueta` y `producto_etiqueta`. |

Hibernate usa `ddl-auto=validate`, por lo que valida que las entidades coincidan con la base de datos, pero no crea ni modifica tablas por su cuenta.

Para revisar el historial desde pgAdmin o `psql`:

```sql
SELECT installed_rank, version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;
```

Se deben observar V1, V2, V3 y V4 con `success = true`.

## Endpoints del Laboratorio 1

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Buscar un producto |
| POST | `/api/productos` | Crear un producto |
| PUT | `/api/productos/{id}` | Actualizar un producto |
| DELETE | `/api/productos/{id}` | Eliminar un producto |
| GET | `/api/productos/categoria/{categoriaId}` | Productos de una categoría |
| GET | `/api/etiquetas` | Listar etiquetas |
| POST | `/api/etiquetas` | Crear una etiqueta |
| POST | `/api/productos/{productoId}/etiquetas/{etiquetaId}` | Asociar una etiqueta |
| DELETE | `/api/productos/{productoId}/etiquetas/{etiquetaId}` | Quitar solo la asociación |
| GET | `/api/productos/etiqueta/{etiquetaId}` | Productos de una etiqueta |

También se mantienen los endpoints de categorías y proveedores de la práctica anterior.

## Cómo probarlo en Postman

Usar `http://localhost:8080` como base URL. Los IDs son ejemplos; hay que reemplazarlos por los que devuelva la API.

### 1. Obtener IDs existentes

```text
GET /api/categorias
GET /api/proveedores
```

Se necesita una categoría y, si se utiliza, un proveedor existente.

### 2. Crear un producto

```text
POST /api/productos
Content-Type: application/json
```

```json
{
  "codigo": "LAB1-001",
  "nombre": "Teclado mecanico",
  "descripcion": "Producto de prueba del Laboratorio 1",
  "precioVenta": 75.50,
  "existencia": 20,
  "categoriaId": 1,
  "proveedorId": 1
}
```

Guardar el `id` que devuelva la respuesta.

### 3. Probar el CRUD y la consulta por categoría

```text
GET    /api/productos
GET    /api/productos/{productoId}
PUT    /api/productos/{productoId}
GET    /api/productos/categoria/{categoriaId}
```

Para el `PUT` se puede usar el mismo JSON del `POST`, cambiando algún dato, por ejemplo el precio o la existencia. El `DELETE /api/productos/{productoId}` conviene dejarlo para el final porque elimina el producto.

### 4. Crear las etiquetas

Crear al menos estas cinco mediante `POST /api/etiquetas`:

```text
Oferta
Importado
Empresarial
Portátil
Gaming
```

Ejemplo del cuerpo:

```json
{
  "nombre": "Oferta"
}
```

### 5. Probar la relación muchos a muchos

Para cada etiqueta que se quiera asociar:

```text
POST /api/productos/{productoId}/etiquetas/{etiquetaId}
```

Después comprobar:

```text
GET /api/productos/etiqueta/{etiquetaId}
```

### 6. Probar los retos finales

Eliminar solo una asociación:

```text
DELETE /api/productos/{productoId}/etiquetas/{etiquetaId}
```

Luego verificar que el producto y la etiqueta siguen existiendo. Finalmente se puede probar:

```text
DELETE /api/productos/{productoId}
```

Resultado esperado: `204 No Content`.

Para la entrega conviene tomar capturas de los GET, POST, PUT y DELETE, de la consulta por categoría, de la tabla `producto_etiqueta` en pgAdmin y de los dos retos finales.

## Verificación rápida

```bash
./mvnw test
./mvnw package -DskipTests
```

La aplicación fue probada contra PostgreSQL local. Flyway aplicó V4 correctamente y se verificaron el CRUD, las consultas por categoría, las etiquetas y las asociaciones.

## Commits del Laboratorio 1

Los cambios de esta actividad están identificados con el prefijo `Lab1:`:

- `Lab1: Reorganizar arquitectura por capas y completar CRUD de productos`
- `Lab1: Agregar migración V4 y relación muchos a muchos`
- `Lab1: Documentar problema, pruebas y solución del Laboratorio 1`
