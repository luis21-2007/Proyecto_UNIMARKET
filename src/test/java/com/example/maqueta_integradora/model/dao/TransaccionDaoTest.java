package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Transaccion;
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

public class TransaccionDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private TransaccionDao transaccionDao;

    @BeforeEach
    public void setUp() throws SQLException {
        // 1. Obtenemos la conexión real desde el pool
        conexionReal = SQLConnector.getConnection();
        conexionReal.setAutoCommit(false);

        // 2. PROXY NATIVO: Bloqueamos commit(), close(), setAutoCommit() y rollback()
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

        transaccionDao = new TransaccionDao();
    }

    // ==========================================
    // MÉTODOS AUXILIARES PARA CREAR Y OBTENER DATOS
    // ==========================================

    private int crearUsuarioAuxiliar(String rol) throws SQLException {
        String correoUnico = "tx_user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena, carrera, telefono, rol, activo) " +
                "VALUES ('Prueba', 'Transaccion', ?, '12345', 'Sistemas', 7771234567, ?, 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_USUARIO"})) {
            ps.setString(1, correoUnico);
            ps.setString(2, rol);
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

        String sqlInsert = "INSERT INTO categoria (nombre) VALUES ('General')";
        try (PreparedStatement ps = conexionReal.prepareStatement(sqlInsert, new String[]{"ID_CATEGORIA"})) {
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

    private int crearProductoAuxiliar(int idVendedor) throws SQLException {
        int idCategoria = obtenerOCrearCategoriaAuxiliar();
        String sql = "INSERT INTO producto (nombre, descripcion, precio, id_usuario, estado, id_categoria) " +
                "VALUES ('Producto Test', 'Descripción de prueba', 150.0, ?, 1, ?)";

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

    private int obtenerIdTransaccionAuxiliar(int idComprador, int idProducto) throws SQLException {
        String sql = "SELECT MAX(id_transaccion) FROM transaccion WHERE id_comprador = ? AND id_producto = ?";
        try (PreparedStatement ps = conexionReal.prepareStatement(sql)) {
            ps.setInt(1, idComprador);
            ps.setInt(2, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ==========================================
    // PRUEBAS DE REGISTRO Y CONSULTAS
    // ==========================================

    @Test
    public void testRegistrarCompraDirectaPendiente() throws SQLException {
        int idVendedor = crearUsuarioAuxiliar("VENDEDOR");
        int idComprador = crearUsuarioAuxiliar("COMPRADOR");
        int idProducto = crearProductoAuxiliar(idVendedor);

        boolean registrada = transaccionDao.registrarCompraDirectaPendiente(idComprador, idVendedor, idProducto, 150.0);

        assertTrue(registrada, "La compra directa pendiente debería registrarse exitosamente");
    }

    @Test
    public void testGetComprasByUsuario() throws SQLException {
        int idVendedor = crearUsuarioAuxiliar("VENDEDOR");
        int idComprador = crearUsuarioAuxiliar("COMPRADOR");
        int idProducto = crearProductoAuxiliar(idVendedor);

        transaccionDao.registrarCompraDirectaPendiente(idComprador, idVendedor, idProducto, 150.0);

        List<Transaccion> compras = transaccionDao.getComprasByUsuario(idComprador);

        assertNotNull(compras, "La lista de compras no debe ser nula");
        assertFalse(compras.isEmpty(), "La lista de compras debe contener al menos la transacción creada");
    }

    @Test
    public void testGetVentasByUsuario() throws SQLException {
        int idVendedor = crearUsuarioAuxiliar("VENDEDOR");
        int idComprador = crearUsuarioAuxiliar("COMPRADOR");
        int idProducto = crearProductoAuxiliar(idVendedor);

        transaccionDao.registrarCompraDirectaPendiente(idComprador, idVendedor, idProducto, 150.0);

        List<Transaccion> ventas = transaccionDao.getVentasByUsuario(idVendedor);

        assertNotNull(ventas, "La lista de ventas no debe ser nula");
        assertFalse(ventas.isEmpty(), "La lista de ventas debe contener al menos la transacción registrada");
    }

    @Test
    public void testUsuarioYaComproDirecto() throws SQLException {
        int idVendedor = crearUsuarioAuxiliar("VENDEDOR");
        int idComprador = crearUsuarioAuxiliar("COMPRADOR");
        int idProducto = crearProductoAuxiliar(idVendedor);

        assertFalse(transaccionDao.usuarioYaComproDirecto(idComprador, idProducto));

        transaccionDao.registrarCompraDirectaPendiente(idComprador, idVendedor, idProducto, 150.0);

        assertTrue(transaccionDao.usuarioYaComproDirecto(idComprador, idProducto));
    }

    // ==========================================
    // PRUEBAS DE CAMBIOS DE ESTADO Y CANCELACIÓN
    // ==========================================

    @Test
    public void testActualizarEstado() throws SQLException {
        int idVendedor = crearUsuarioAuxiliar("VENDEDOR");
        int idComprador = crearUsuarioAuxiliar("COMPRADOR");
        int idProducto = crearProductoAuxiliar(idVendedor);

        transaccionDao.registrarCompraDirectaPendiente(idComprador, idVendedor, idProducto, 150.0);
        int idTransaccion = obtenerIdTransaccionAuxiliar(idComprador, idProducto);

        boolean actualizado = transaccionDao.actualizarEstado(idTransaccion, 1);

        assertTrue(actualizado, "Debería actualizar el estado de la transacción y del producto");
    }

    @Test
    public void testCancelarTransaccion() throws SQLException {
        int idVendedor = crearUsuarioAuxiliar("VENDEDOR");
        int idComprador = crearUsuarioAuxiliar("COMPRADOR");
        int idProducto = crearProductoAuxiliar(idVendedor);

        transaccionDao.registrarCompraDirectaPendiente(idComprador, idVendedor, idProducto, 150.0);
        int idTransaccion = obtenerIdTransaccionAuxiliar(idComprador, idProducto);

        boolean cancelada = transaccionDao.cancelarTransaccion(idTransaccion, idProducto);

        assertTrue(cancelada, "Debería marcar la transacción como CANCELADA y liberar el producto");
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