package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Calificacion;
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

public class CalificacionDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private CalificacionDao calificacionDao;

    private int idCompradorTest;
    private int idVendedorTest;
    private int idTransaccionTest;

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

        calificacionDao = new CalificacionDao();

        // 4. Jerarquía de datos pre-requisito (Usuario -> Categoría -> Producto -> Transacción)
        idVendedorTest = crearUsuarioAuxiliar("VENDEDOR");
        idCompradorTest = crearUsuarioAuxiliar("COMPRADOR");
        int idCategoria = obtenerOCrearCategoriaAuxiliar();
        int idProducto = crearProductoAuxiliar(idVendedorTest, idCategoria);
        idTransaccionTest = crearTransaccionAuxiliar(idCompradorTest, idVendedorTest, idProducto);
    }

    private int crearUsuarioAuxiliar(String rol) throws SQLException {
        String correoUnico = "calif_" + rol.toLowerCase() + "_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena, carrera, telefono, rol, activo) " +
                "VALUES ('Usuario', ?, ?, '12345', 'Desarrollo de software', 7779998877, ?, 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"id_usuario"})) {
            ps.setString(1, rol);
            ps.setString(2, correoUnico);
            ps.setString(3, rol);
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

    private int crearProductoAuxiliar(int idVendedor, int idCategoria) throws SQLException {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, id_usuario, estado, id_categoria) " +
                "VALUES ('Producto Evaluación', 'Descripción test', 250.0, ?, 1, ?)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"id_producto"})) {
            ps.setInt(1, idVendedor);
            ps.setInt(2, idCategoria);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    private int crearTransaccionAuxiliar(int idComprador, int idVendedor, int idProducto) throws SQLException {
        String sql = "INSERT INTO transaccion (id_comprador, id_vendedor, id_producto, monto, fecha_transaccion, estado) " +
                "VALUES (?, ?, ?, 250.0, CURRENT_TIMESTAMP, 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"id_transaccion"})) {
            ps.setInt(1, idComprador);
            ps.setInt(2, idVendedor);
            ps.setInt(3, idProducto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    @Test
    public void testGuardarCalificacion() {
        boolean guardada = calificacionDao.guardarCalificacion(
                idCompradorTest,
                idVendedorTest,
                idTransaccionTest,
                5,
                "Excelente vendedor, muy rápido"
        );

        assertTrue(guardada, "La calificación debería guardarse correctamente");
    }

    @Test
    public void testExisteCalificacion() {
        assertFalse(calificacionDao.existeCalificacion(idTransaccionTest), "No debería existir calificación previa para esta transacción");

        calificacionDao.guardarCalificacion(idCompradorTest, idVendedorTest, idTransaccionTest, 4, "Buena atención");

        assertTrue(calificacionDao.existeCalificacion(idTransaccionTest), "Debería retornar true una vez registrada la calificación");
    }

    @Test
    public void testObtenerResenasPorVendedor() {
        calificacionDao.guardarCalificacion(idCompradorTest, idVendedorTest, idTransaccionTest, 5, "Producto en perfecto estado");

        List<Calificacion> resenas = calificacionDao.obtenerResenasPorVendedor(idVendedorTest);

        assertNotNull(resenas, "La lista de reseñas no debe ser nula");
        assertFalse(resenas.isEmpty(), "Debería haber al menos una reseña registrada");
        assertEquals(5, resenas.get(0).getPuntuacion());
        assertEquals("Producto en perfecto estado", resenas.get(0).getComentario());
        assertNotNull(resenas.get(0).getNombreComprador());
    }

    @Test
    public void testObtenerPromedioCalificaciones() throws SQLException {
        // Primera calificación: 5 estrellas
        calificacionDao.guardarCalificacion(idCompradorTest, idVendedorTest, idTransaccionTest, 5, "Excelente");

        // Segunda calificación para el mismo vendedor: 3 estrellas
        int comprador2 = crearUsuarioAuxiliar("COMPRADOR");
        int categoria = obtenerOCrearCategoriaAuxiliar();
        int producto2 = crearProductoAuxiliar(idVendedorTest, categoria);
        int transaccion2 = crearTransaccionAuxiliar(comprador2, idVendedorTest, producto2);

        calificacionDao.guardarCalificacion(comprador2, idVendedorTest, transaccion2, 3, "Regular");

        // Promedio esperado: (5 + 3) / 2 = 4.0
        double promedio = calificacionDao.obtenerPromedioCalificaciones(idVendedorTest);

        assertEquals(4.0, promedio, 0.01, "El promedio de calificaciones debería ser 4.0");
    }

    @Test
    public void testObtenerPromedioCalificacionesSinResenas() {
        double promedio = calificacionDao.obtenerPromedioCalificaciones(idVendedorTest);
        assertEquals(0.0, promedio, "Debería retornar 0.0 si el vendedor no tiene calificaciones");
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