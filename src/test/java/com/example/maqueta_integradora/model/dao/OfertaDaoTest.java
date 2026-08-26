package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Oferta;
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

public class OfertaDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private OfertaDao ofertaDao;

    private int idCompradorTest;
    private int idVendedorTest;
    private int idProductoTest;

    @BeforeEach
    public void setUp() throws SQLException {
        // 1. Obtenemos la conexión real desde la base de datos
        conexionReal = SQLConnector.getConnection();
        conexionReal.setAutoCommit(false);

        // 2. PROXY NATIVO: Bloqueamos commit(), close(), setAutoCommit() y ROLLBACK
        // Interceptar rollback evita que los métodos transaccionales del DAO cancelen la prueba
        Connection proxyConexion = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("commit".equals(method.getName()) ||
                            "close".equals(method.getName()) ||
                            "setAutoCommit".equals(method.getName()) ||
                            "rollback".equals(method.getName())) {
                        return null;
                    }
                    return method.invoke(conexionReal, args);
                }
        );

        // 3. Interceptamos SQLConnector
        mockedConnector = mockStatic(SQLConnector.class);
        mockedConnector.when(SQLConnector::getConnection).thenReturn(proxyConexion);

        ofertaDao = new OfertaDao();

        // 4. Carga de datos base en cascada (Usuario -> Categoría -> Producto)
        idVendedorTest = crearUsuarioAuxiliar("VENDEDOR");
        idCompradorTest = crearUsuarioAuxiliar("COMPRADOR");
        int idCategoria = obtenerOCrearCategoriaAuxiliar();
        idProductoTest = crearProductoAuxiliar(idVendedorTest, idCategoria);
    }


    private int crearUsuarioAuxiliar(String rol) throws SQLException {
        String correoUnico = "ofr_" + rol.toLowerCase() + "_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena, carrera, telefono, rol, activo) " +
                "VALUES ('Usuario', ?, ?, '12345', 'Sistemas', 7771112233, ?, 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_USUARIO"})) {
            ps.setString(1, rol);
            ps.setString(2, correoUnico);
            ps.setString(3, rol);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String sqlMax = "SELECT MAX(id_usuario) FROM usuario WHERE correo = ?";
        try (PreparedStatement psMax = conexionReal.prepareStatement(sqlMax)) {
            psMax.setString(1, correoUnico);
            try (ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) return rsMax.getInt(1);
            }
        }
        return 1;
    }

    private int obtenerOCrearCategoriaAuxiliar() throws SQLException {
        String sqlSelect = "SELECT id_categoria FROM categoria FETCH FIRST 1 ROWS ONLY";
        try (Statement st = conexionReal.createStatement(); ResultSet rs = st.executeQuery(sqlSelect)) {
            if (rs.next()) return rs.getInt(1);
        }

        String sqlInsert = "INSERT INTO categoria (nombre_categoria, id_admin_creo, estado) VALUES ('General', ?, 1)";
        try (PreparedStatement ps = conexionReal.prepareStatement(sqlInsert, new String[]{"ID_CATEGORIA"})) {
            ps.setInt(1, idVendedorTest);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String sqlMax = "SELECT MAX(id_categoria) FROM categoria";
        try (Statement st = conexionReal.createStatement(); ResultSet rs = st.executeQuery(sqlMax)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    private int crearProductoAuxiliar(int idVendedor, int idCategoria) throws SQLException {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, id_usuario, estado, id_categoria) " +
                "VALUES ('Producto Oferta', 'Para probar subastas/ofertas', 1000.0, ?, 1, ?)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_PRODUCTO"})) {
            ps.setInt(1, idVendedor);
            ps.setInt(2, idCategoria);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String sqlMax = "SELECT MAX(id_producto) FROM producto WHERE id_usuario = ?";
        try (PreparedStatement psMax = conexionReal.prepareStatement(sqlMax)) {
            psMax.setInt(1, idVendedor);
            try (ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) return rsMax.getInt(1);
            }
        }
        return 1;
    }

    private int crearOfertaAuxiliar(int idComprador, int idProducto, double monto) throws SQLException {
        String sql = "INSERT INTO oferta (monto_oferta, estado, id_usuario, id_producto) VALUES (?, 0, ?, ?)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_OFERTA"})) {
            ps.setDouble(1, monto);
            ps.setInt(2, idComprador);
            ps.setInt(3, idProducto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String sqlMax = "SELECT MAX(id_oferta) FROM oferta WHERE id_usuario = ? AND id_producto = ?";
        try (PreparedStatement psMax = conexionReal.prepareStatement(sqlMax)) {
            psMax.setInt(1, idComprador);
            psMax.setInt(2, idProducto);
            try (ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) return rsMax.getInt(1);
            }
        }
        return 0;
    }


    @Test
    public void testGuardarOferta() {
        Oferta o = new Oferta();
        o.setMontoOferta(850.0);
        o.setIdUsuario(idCompradorTest);
        o.setIdProducto(idProductoTest);

        boolean guardada = ofertaDao.guardarOferta(o);

        assertTrue(guardada, "La oferta debería registrarse exitosamente");
    }

    @Test
    public void testGetOfertasByComprador() throws SQLException {
        crearOfertaAuxiliar(idCompradorTest, idProductoTest, 900.0);

        List<Oferta> ofertas = ofertaDao.getOfertasByComprador(idCompradorTest);

        assertNotNull(ofertas, "La lista de ofertas del comprador no debe ser nula");
        assertFalse(ofertas.isEmpty(), "Debería listar las ofertas realizadas por el comprador");
        assertEquals(900.0, ofertas.get(0).getMontoOferta());
    }

    @Test
    public void testGetOfertasByVendedor() throws SQLException {
        crearOfertaAuxiliar(idCompradorTest, idProductoTest, 950.0);

        List<Oferta> ofertas = ofertaDao.getOfertasByVendedor(idVendedorTest);

        assertNotNull(ofertas, "La lista de ofertas recibidas por el vendedor no debe ser nula");
        assertFalse(ofertas.isEmpty(), "Debería listar las ofertas recibidas para los productos del vendedor");
    }

    @Test
    public void testGetOfertasByProducto() throws SQLException {
        crearOfertaAuxiliar(idCompradorTest, idProductoTest, 880.0);

        List<Oferta> ofertas = ofertaDao.getOfertasByProducto(idProductoTest);

        assertNotNull(ofertas, "La lista de ofertas por producto no debe ser nula");
        assertFalse(ofertas.isEmpty(), "Debería contener las ofertas ligadas al producto");
    }

    @Test
    public void testGetEstadoOfertaUsuario() throws SQLException {
        assertEquals(-1, ofertaDao.getEstadoOfertaUsuario(idCompradorTest, idProductoTest), "Debe retornar -1 si el usuario no ha realizado ofertas");

        crearOfertaAuxiliar(idCompradorTest, idProductoTest, 920.0);

        assertEquals(0, ofertaDao.getEstadoOfertaUsuario(idCompradorTest, idProductoTest), "Debe retornar 0 (Pendiente) para la última oferta realizada");
    }

    @Test
    public void testActualizarEstado() throws SQLException {
        int idOferta = crearOfertaAuxiliar(idCompradorTest, idProductoTest, 700.0);

        boolean actualizado = ofertaDao.actualizarEstado(idOferta, 2);

        assertTrue(actualizado, "El estado de la oferta debe actualizarse correctamente");
        assertEquals(2, ofertaDao.getEstadoOfertaUsuario(idCompradorTest, idProductoTest));
    }

    @Test
    public void testAceptarOfertaYRegistrarVenta() throws SQLException {
        int idOferta = crearOfertaAuxiliar(idCompradorTest, idProductoTest, 950.0);

        boolean procesado = ofertaDao.aceptarOfertaYRegistrarVenta(idOferta);

        assertTrue(procesado, "Debe procesar la venta y crear la transacción en estado 'En Proceso'");
        assertEquals(1, ofertaDao.getEstadoOfertaUsuario(idCompradorTest, idProductoTest), "La oferta debe pasar a estado 1 (Aceptada)");
    }

    @Test
    public void testAceptarOfertaYActualizarProducto() throws SQLException {
        int idOfertaAceptada = crearOfertaAuxiliar(idCompradorTest, idProductoTest, 900.0);

        boolean exito = ofertaDao.aceptarOfertaYActualizarProducto(
                idOfertaAceptada,
                idProductoTest,
                idCompradorTest,
                idVendedorTest,
                900.0
        );

        assertTrue(exito, "La transacción completa de oferta/producto debe ejecutarse exitosamente");
        assertEquals(1, ofertaDao.getEstadoOfertaUsuario(idCompradorTest, idProductoTest), "La oferta aceptada debe tener estado 1");
    }


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