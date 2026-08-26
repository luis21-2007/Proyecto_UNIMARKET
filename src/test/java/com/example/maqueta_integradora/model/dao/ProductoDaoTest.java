package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.utils.SQLConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private ProductoDao productoDao;

    private int idUsuarioTest;
    private int idCategoriaTest;

    @BeforeEach
    public void setUp() throws SQLException {
        // 1. Obtenemos la conexión real desde el pool
        conexionReal = SQLConnector.getConnection();
        conexionReal.setAutoCommit(false);

        // 2. PROXY NATIVO: Bloqueamos commit(), close() y setAutoCommit()
        Connection proxyConexion = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("commit".equals(method.getName()) ||
                            "close".equals(method.getName()) ||
                            "setAutoCommit".equals(method.getName())) {
                        return null;
                    }
                    return method.invoke(conexionReal, args);
                }
        );

        // 3. Interceptamos SQLConnector
        mockedConnector = mockStatic(SQLConnector.class);
        mockedConnector.when(SQLConnector::getConnection).thenReturn(proxyConexion);

        productoDao = new ProductoDao();

        // 4. Preparamos registros padre obligatorios (Usuario y Categoría)
        idUsuarioTest = crearUsuarioAuxiliar();
        idCategoriaTest = obtenerOCrearCategoriaAuxiliar();
    }

    // ==========================================
    // MÉTODOS AUXILIARES (PRE-REQUISITOS FK)
    // ==========================================

    private int crearUsuarioAuxiliar() throws SQLException {
        String correoUnico = "prod_user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena, carrera, telefono, rol, activo) " +
                "VALUES ('Vendedor', 'Prueba', ?, '12345', 'Sistemas', 7770001122, 'VENDEDOR', 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"id_usuario"})) {
            ps.setString(1, correoUnico);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    private int obtenerOCrearCategoriaAuxiliar() throws SQLException {
        String sqlSelect = "SELECT id_categoria FROM categoria FETCH FIRST 1 ROWS ONLY";
        try (Statement st = conexionReal.createStatement(); ResultSet rs = st.executeQuery(sqlSelect)) {
            if (rs.next()) return rs.getInt(1);
        }

        String sqlInsert = "INSERT INTO categoria (nombre) VALUES ('General')";
        try (PreparedStatement ps = conexionReal.prepareStatement(sqlInsert, new String[]{"id_categoria"})) {
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    private Producto crearObjetoProductoEjemplo() {
        Producto p = new Producto();
        p.setNombre("Laptop Laptop Gamer Test");
        p.setPrecio(15000.0);
        p.setDescripcion("Equipo de prueba en excelente estado");
        p.setImagenUrl("https://ejemplo.com/imagen.jpg");
        p.setIdCategoria(idCategoriaTest);
        p.setIdUsuario(idUsuarioTest);
        return p;
    }

    // ==========================================
    // PRUEBAS DE CREACIÓN Y LECTURA
    // ==========================================

    @Test
    public void testCreate() {
        Producto p = crearObjetoProductoEjemplo();
        boolean creado = productoDao.create(p);
        assertTrue(creado, "El producto debería registrarse exitosamente");
    }

    @Test
    public void testCreateAndGetId() {
        Producto p = crearObjetoProductoEjemplo();
        int idGenerado = productoDao.createAndGetId(p);
        assertTrue(idGenerado > 0, "Debería retornar un ID autogenerado válido mayor a 0");
    }

    @Test
    public void testGetById() {
        Producto p = crearObjetoProductoEjemplo();
        int idProducto = productoDao.createAndGetId(p);

        Producto obtenido = productoDao.getById(idProducto);

        assertNotNull(obtenido, "Debería encontrar el producto por su ID");
        assertEquals("Laptop Laptop Gamer Test", obtenido.getNombre());
    }

    @Test
    public void testGetAll() {
        Producto p = crearObjetoProductoEjemplo();
        productoDao.createAndGetId(p);

        List<Producto> lista = productoDao.getAll();

        assertNotNull(lista, "La lista de productos no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener al menos el producto activo creado");
    }

    @Test
    public void testGetByCategoria() {
        Producto p = crearObjetoProductoEjemplo();
        productoDao.createAndGetId(p);

        List<Producto> lista = productoDao.getByCategoria(idCategoriaTest);

        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Debería listar productos para la categoría configurada");
    }

    @Test
    public void testObtenerProductosPorUsuario() {
        Producto p = crearObjetoProductoEjemplo();
        productoDao.createAndGetId(p);

        List<Producto> lista = productoDao.obtenerProductosPorUsuario(idUsuarioTest);

        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Debería listar los productos asociados al usuario creador");
    }

    @Test
    public void testObtenerProductosPorUsuarioYCategoria() {
        Producto p = crearObjetoProductoEjemplo();
        productoDao.createAndGetId(p);

        List<Producto> lista = productoDao.obtenerProductosPorUsuarioYCategoria(idUsuarioTest, idCategoriaTest);

        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Debería filtrar por el usuario y la categoría especificados");
    }

    @Test
    public void testGetAllAdmin() {
        Producto p = crearObjetoProductoEjemplo();
        productoDao.createAndGetId(p);

        List<Producto> lista = productoDao.getAllAdmin();

        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "La vista de administrador debe listar todos los productos");
    }

    // ==========================================
    // PRUEBAS DE ACTUALIZACIÓN Y BORRADO
    // ==========================================

    @Test
    public void testUpdate() {
        Producto p = crearObjetoProductoEjemplo();
        int idProducto = productoDao.createAndGetId(p);

        p.setIdProducto(idProducto);
        p.setNombre("Laptop Editada");
        p.setPrecio(18000.0);
        p.setDescripcion("Descripción modificada");

        boolean actualizado = productoDao.update(p);

        assertTrue(actualizado, "El producto debería actualizarse correctamente");
        Producto modificado = productoDao.getById(idProducto);
        assertEquals("Laptop Editada", modificado.getNombre());
    }

    @Test
    public void testDelete() {
        Producto p = crearObjetoProductoEjemplo();
        int idProducto = productoDao.createAndGetId(p);

        boolean eliminado = productoDao.delete(idProducto);

        assertTrue(eliminado, "La baja lógica debería retornar true");
        Producto inactivo = productoDao.getById(idProducto);
        assertEquals(0, inactivo.getEstado(), "El estado del producto debería cambiar a 0");
    }

    // ==========================================
    // PRUEBAS DE ESTADOS (ACTIVAR, DESACTIVAR, PROCESO)
    // ==========================================

    @Test
    public void testCambiosDeEstado() {
        Producto p = crearObjetoProductoEjemplo();
        int idProducto = productoDao.createAndGetId(p);

        assertTrue(productoDao.desactivar(idProducto), "Debería cambiar estado a 0");
        assertEquals(0, productoDao.getById(idProducto).getEstado());

        assertTrue(productoDao.activar(idProducto), "Debería cambiar estado a 1");
        assertEquals(1, productoDao.getById(idProducto).getEstado());

        assertTrue(productoDao.proceso(idProducto), "Debería cambiar estado a 3");
        assertEquals(3, productoDao.getById(idProducto).getEstado());
    }

    // ==========================================
    // PRUEBAS DE IMÁGENES DE PRODUCTO
    // ==========================================

    @Test
    public void testGestionImagenesProducto() {
        Producto p = crearObjetoProductoEjemplo();
        int idProducto = productoDao.createAndGetId(p);
        String rutaImagen = "uploads/productos/test_foto.png";

        // 1. Guardar Imagen
        boolean guardada = productoDao.guardarImagenProducto(idProducto, rutaImagen);
        assertTrue(guardada, "Debería guardar la ruta de la imagen");

        // 2. Obtener Imágenes
        List<String> imagenes = productoDao.getImagenesByProductoId(idProducto);
        assertTrue(imagenes.contains(rutaImagen), "La lista de imágenes debe contener la ruta insertada");

        // 3. Eliminar Imagen Específica
        boolean eliminada = productoDao.eliminarImagenEspecifica(idProducto, rutaImagen);
        assertTrue(eliminada, "Debería eliminar la imagen de la base de datos");
    }

    // ==========================================
    // PRUEBAS DE COMPRA DIRECTA (TRANSACCIÓN COMPLEJA)
    // ==========================================

    @Test
    public void testProcesarCompraDirecta() throws SQLException {
        Producto p = crearObjetoProductoEjemplo();
        int idProducto = productoDao.createAndGetId(p);
        int idComprador = crearUsuarioAuxiliar();

        boolean procesada = productoDao.procesarCompraDirecta(idProducto, idComprador, idUsuarioTest, 15000.0);

        assertTrue(procesada, "La compra directa debe completarse con éxito");
        assertEquals(3, productoDao.getById(idProducto).getEstado(), "El producto debe pasar a estado 3 (En Proceso / Vendido)");
    }

    // ==========================================
    // LIMPIEZA AUTOMÁTICA CON ROLLBACK
    // ==========================================

    @AfterEach
    public void tearDown() throws SQLException {
        if (mockedConnector != null) {
            mockedConnector.close();
        }

        if (conexionReal != null && !conexionReal.isClosed()) {
            conexionReal.rollback();
            conexionReal.close();
        }
    }
}