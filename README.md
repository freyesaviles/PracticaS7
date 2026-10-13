# Gestión de productos

API REST de Servicios Web con Spring Boot, Spring Data JPA, PostgreSQL, Flyway y Spring Security.

## Práctica 9: Seguridad con JWT

La práctica agrega autenticación y autorización al CRUD existente. Antes, cualquier cliente podía modificar productos y categorías. Ahora el acceso depende del rol del usuario.

### Roles

| Rol | Permisos |
|---|---|
| Invitado | Saludo público y login |
| USER | Consultar productos y categorías |
| ADMIN | Consultar y modificar productos y categorías |

Usuarios de laboratorio (solo desarrollo):

```text
admin     / Admin123!
operador  / Operador123!
```

Los usuarios están en memoria; no se guardan en PostgreSQL.

## Qué se implementó

- Login con JWT firmado mediante HS256.
- Tokens stateless con duración de 30 minutos.
- Validación de firma, vencimiento y roles.
- Productos: lectura para `USER` y `ADMIN`; escritura solo para `ADMIN`.
- Categorías: lectura para `USER` y `ADMIN`; creación, actualización y eliminación solo para `ADMIN`.
- Proveedores y etiquetas: lectura para ambos roles; cambios para `ADMIN`.
- Proveedores reciben los datos de creación mediante `ProveedorRequestDTO`, con validación de nombre, correo y teléfono.
- Endpoint público: `GET /api/publico/saludo`.
- Reto categoría-productos: `GET /api/categorias` devuelve cada categoría con sus productos resumidos.

La relación JPA sigue siendo `Categoria 1:N Producto`. `Producto` conserva la referencia a `Categoria` porque el lado “muchos” almacena la clave foránea `categoria_id`. La API permite navegar desde categoría:

```text
GET /api/categorias/{categoriaId}/productos
```

## Configurar el secreto JWT

No se debe guardar el secreto en GitHub. Generar uno localmente:

```bash
openssl rand -base64 32
```

Agregar el resultado como variable de entorno en la configuración de ejecución de IntelliJ IDEA:

```text
JWT_SECRET_BASE64=valor_generado_localmente
```

La aplicación exige una clave de al menos 32 bytes. Si falta, no inicia.

## PostgreSQL

Valores locales predeterminados:

```text
Host:       localhost
Puerto:     5432
Base:       gestion_productos
Usuario:    postgres
Contraseña: postgres
```

En macOS con Homebrew:

```bash
brew services start postgresql@17
```

La base usa Flyway. Las migraciones V1–V4 crean las tablas y relaciones de categorías, productos, proveedores y etiquetas. Hibernate usa `ddl-auto=validate` para revisar el esquema sin modificarlo.

## Ejecutar

Cambiar a la rama de la práctica:

```bash
git switch seguridad-jwt
```

Iniciar la aplicación:

```bash
./mvnw spring-boot:run
```

También se puede ejecutar `GestionProductosApplication` desde IntelliJ IDEA.

## Probar con Postman

La colección general está en:

```text
postman/Practica9-Seguridad-JWT.postman_collection.json
```

Importarla en Postman y ejecutar las carpetas en orden:

1. **Acceso público y autenticación:** saludo, login ADMIN, login USER y login inválido.
2. **Preparar datos con ADMIN:** crea una categoría, un proveedor y un producto.
3. **Productos y permisos:** comprueba `401`, lectura con USER, `403` al modificar con USER y actualización con ADMIN.
4. **Categorías y reto final:** comprueba consultas con USER y modificaciones con USER/ADMIN.
5. **Etiquetas y limpieza:** prueba la relación muchos a muchos y elimina los datos creados para la demostración.

Los scripts de Postman guardan automáticamente los tokens y los IDs en variables de colección. Si se ejecuta una solicitud protegida antes del login correspondiente, se obtendrá `401`.

### Resultados esperados

| Prueba | Resultado |
|---|---:|
| Saludo público sin token | `200` |
| Login válido | `200` con JWT |
| Login inválido | `401` |
| Producto sin token | `401` |
| Consulta de producto con USER | `200` |
| Modificación con USER | `403` |
| Modificación con ADMIN | `200`, `201` o `204` |
| Token alterado | `401` |
| Consulta de categoría con USER | `200` |
| Modificación de categoría con USER | `403` |
| Modificación de categoría con ADMIN | `200`, `201` o `204` |

`401` significa que falta autenticación o el token no es válido. `403` significa que el token sí es válido, pero el rol no tiene permiso.

## Verificación rápida

```bash
./mvnw test
./mvnw package -DskipTests
```

También se debe comprobar que PostgreSQL esté activo y que Flyway muestre V1, V2, V3 y V4 con `success = true`:

```sql
SELECT version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;
```

## Seguridad

No subir a GitHub:

- `JWT_SECRET_BASE64`.
- Tokens JWT.
- Contraseñas reales.
- Archivos `.env` con secretos.

La configuración incluida es para laboratorio. En producción se necesitarían HTTPS, un gestor de secretos y normalmente un proveedor de identidad separado.
