# Documentación del repositorio:
# UNIMARKET
Marketplace web para la compra-venta de productos entre estudiantes universitarios, desarrollado como proyecto integrador con **Java (Jakarta Servlets + JSP)** y base de datos **Oracle**.

## Equipo de desarrollo

| Nombre de usuario (Git)   | Matrícula   | Rama principal de trabajo  |
|---------------------------|-------------|----------------------------|
| `luis felipe`             | 20253ds069  | `felipe` / `master`        |
| `Kexxox`                  | 20253ds063  | `Kex` / `dev` / `mains`    |
| `MagoDelTribal`           | 20253ds085  | `Cristian`                 |
| `sabrito1912`             | 20253ds090  | `Said`                     |
| `MiguelPapuBv`            | 20253ds075  | `Chavezuwu`                |

## Descripción del proyecto

**UNIMARKET** es una aplicación web tipo *marketplace* pensada para que estudiantes universitarios puedan comprar y vender productos entre ellos.
El sistema permite:

- **Registro e inicio de sesión** de usuarios, con verificación de cuenta por correo electrónico y recuperación/restablecimiento de contraseña.
- **Publicación de productos** (con imágenes, precio, descripción y categoría) por parte de los vendedores.
- **Exploración del marketplace** y detalle de cada producto.
- **Sistema de ofertas**: los compradores pueden enviar ofertas por un producto y el vendedor puede responderlas (aceptar/rechazar).
- **Gestión de compras y ventas**, con actualización de su estado.
- **Calificaciones y reseñas** entre usuarios tras una compra/venta.
- **Reportes de productos o usuarios**, supervisados por un administrador.
- **Panel de administración** para gestionar usuarios, productos, categorías y reportes desde un dashboard.

- ## Estructura del código

```
Proyecto_UNIMARKET/
├── pom.xml                        # Configuración Maven (Java 21, dependencias, empaquetado WAR)
├── src/
│   ├── main/
│   │   ├── java/com/example/maqueta_integradora/
│   │   │   ├── controller/        # Servlets: reciben peticiones HTTP y orquestan la lógica
│   │   │   ├── filter/            # AdminFilter: protege las rutas de administración
│   │   │   ├── model/             # Entidades (POJOs) del dominio
│   │   │   │   └── dao/           # Acceso a datos (JDBC) por entidad
│   │   │   └── utils/             # Utilidades: conexión a BD, hashing, envío de correos
│   │   ├── resources/
│   │   │   └── credentials.properties  # Credenciales de BD y SMTP (variables de entorno/config)
│   │   └── webapp/
│   │       ├── *.jsp              # Vistas de la aplicación
│   │       ├── layout/            # Fragmentos comunes (header, footer)
│   │       ├── assets/            # CSS, JS (jQuery, slick.js) e íconos (Bootstrap Icons)
│   │       └── WEB-INF/web.xml    # Configuración de páginas de error (404/500)
│   └── test/java/.../model/dao/   # Pruebas unitarias (JUnit 5)
```

### Capa `model/`
Contiene las clases de dominio (POJOs) que representan las entidades del negocio: `User`, `Producto`, `Categoria`, `Oferta`, `Transaccion`, `Calificacion` y `Reporte`. 
  Cada una expone únicamente atributos, constructores y getters/setters.

### Capa `model/dao/`
Un DAO (*Data Access Object*) por entidad (`UserDao`, `ProductoDao`, `OfertaDao`, `TransaccionDao`, `CategoriaDao`, `CalificacionDao`, `ReporteDao`), todos implementando una interfaz común `Dao`. 
  Encapsulan las consultas SQL contra Oracle usando JDBC, obteniendo la conexión a través de `SQLConnector`.

### Capa `controller/`
Cada funcionalidad del sistema tiene su propio **Servlet** (anotado con `@WebServlet`), que recibe la petición, invoca al DAO correspondiente y reenvía (`forward`) a la vista JSP adecuada.

### Capa `filter/`
`AdminFilter` intercepta las rutas y vistas de administración y solo permite el paso si en la sesión existe un `User` con `rol = "ADMIN"`; en caso contrario redirige al login.

### Capa `utils/`
- `SQLConnector`: gestiona el pool de conexiones a Oracle mediante **HikariCP**.
- `HashUtil`: hashing de contraseñas.
- `EmailSender`: envío de correos (verificación de cuenta, restablecimiento de contraseña) usando Jakarta Mail.

### `webapp/`
Vistas JSP (una por pantalla), fragmentos reutilizables en `layout/` (header/footer, variantes según usuario normal o administrador) y los recursos estáticos en `assets/` (CSS propios, Bootstrap Icons, jQuery y el plugin `slick.js` para carruseles).
