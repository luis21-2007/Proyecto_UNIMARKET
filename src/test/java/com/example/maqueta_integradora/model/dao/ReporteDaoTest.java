package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Reporte;
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

public class ReporteDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private ReporteDao reporteDao;

    private int idReportadorTest;
    private int idReportadoTest;

    @BeforeEach
    public void setUp() throws SQLException {
        // 1. Obtenemos la conexión real desde la base de datos
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

        reporteDao = new ReporteDao();

        // 4. Creación de usuarios de prueba (Reportador y Reportado)
        idReportadorTest = crearUsuarioAuxiliar("REPORTADOR");
        idReportadoTest = crearUsuarioAuxiliar("REPORTADO");
    }


    private int crearUsuarioAuxiliar(String rol) throws SQLException {
        String correoUnico = "rep_" + rol.toLowerCase() + "_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
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
            ps.setInt(1, idReportadorTest);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    private int crearProductoAuxiliar(int idVendedor, int idCategoria) throws SQLException {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, id_usuario, estado, id_categoria) " +
                "VALUES ('Producto Reportado', 'Para pruebas de reportes', 300.0, ?, 1, ?)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_PRODUCTO"})) {
            ps.setInt(1, idVendedor);
            ps.setInt(2, idCategoria);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    private int crearTransaccionAuxiliar(int idComprador, int idVendedor) throws SQLException {
        int idCat = obtenerOCrearCategoriaAuxiliar();
        int idProd = crearProductoAuxiliar(idVendedor, idCat);

        String sql = "INSERT INTO transaccion (id_producto, id_comprador, id_vendedor, monto, estado) VALUES (?, ?, ?, 300.0, 2)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_TRANSACCION"})) {
            ps.setInt(1, idProd);
            ps.setInt(2, idComprador);
            ps.setInt(3, idVendedor);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        String sqlMax = "SELECT MAX(id_transaccion) FROM transaccion WHERE id_comprador = ?";
        try (PreparedStatement psMax = conexionReal.prepareStatement(sqlMax)) {
            psMax.setInt(1, idComprador);
            try (ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) return rsMax.getInt(1);
            }
        }
        return 1;
    }

    private int crearReporteAuxiliar(int idReportador, int idReportado, int estado, String motivo) throws SQLException {
        String sql = "INSERT INTO reporte_usuario (id_reportador, id_reportado, id_transaccion, motivo, descripcion, estado) " +
                "VALUES (?, ?, NULL, ?, 'Descripción de prueba', ?)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"ID_REPORTE"})) {
            ps.setInt(1, idReportador);
            ps.setInt(2, idReportado);
            ps.setString(3, motivo);
            ps.setInt(4, estado);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        String sqlMax = "SELECT MAX(id_reporte) FROM reporte_usuario WHERE id_reportador = ? AND id_reportado = ?";
        try (PreparedStatement psMax = conexionReal.prepareStatement(sqlMax)) {
            psMax.setInt(1, idReportador);
            psMax.setInt(2, idReportado);
            try (ResultSet rsMax = psMax.executeQuery()) {
                if (rsMax.next()) return rsMax.getInt(1);
            }
        }
        return 0;
    }


    @Test
    public void testGuardarReporteSinTransaccion() {
        boolean guardado = reporteDao.guardarReporte(
                idReportadorTest,
                idReportadoTest,
                null,
                "Incumplimiento",
                "El vendedor no se presentó"
        );

        assertTrue(guardado, "El reporte sin transacción debería registrarse exitosamente");
    }

    @Test
    public void testGuardarReporteConTransaccion() throws SQLException {
        int idTransaccion = crearTransaccionAuxiliar(idReportadorTest, idReportadoTest);

        boolean guardado = reporteDao.guardarReporte(
                idReportadorTest,
                idReportadoTest,
                idTransaccion,
                "Fraude en Pago",
                "El producto recibido no corresponde"
        );

        assertTrue(guardado, "El reporte asociado a una transacción debería registrarse exitosamente");
    }



    @Test
    public void testObtenerTodos() throws SQLException {
        crearReporteAuxiliar(idReportadorTest, idReportadoTest, 0, "Conducta inapropiada");

        List<Reporte> reportes = reporteDao.obtenerTodos();

        assertNotNull(reportes, "La lista de reportes no debe ser nula");
        assertFalse(reportes.isEmpty(), "La lista debe incluir al menos el reporte generado");
        assertNotNull(reportes.get(0).getNombreReportador());
        assertNotNull(reportes.get(0).getNombreReportado());
    }

    @Test
    public void testYaReportoVendedor() throws SQLException {
        assertFalse(reporteDao.yaReportoVendedor(idReportadorTest, idReportadoTest), "Inicialmente no debe indicar que el usuario ha reportado al vendedor");

        crearReporteAuxiliar(idReportadorTest, idReportadoTest, 0, "Spam");

        assertTrue(reporteDao.yaReportoVendedor(idReportadorTest, idReportadoTest), "Debe retornar true tras haber registrado un reporte previo");
    }

    @Test
    public void testObtenerCantidadSancionesPorUsuario() throws SQLException {
        // Reporte en estado 0 (Pendiente) - No cuenta como sanción
        crearReporteAuxiliar(idReportadorTest, idReportadoTest, 0, "Queja leve");

        // Reporte en estado 2 (Sancionado/Aprobado) - Sí cuenta como sanción
        crearReporteAuxiliar(idReportadorTest, idReportadoTest, 2, "Fraude recurrente");

        int totalSanciones = reporteDao.obtenerCantidadSancionesPorUsuario(idReportadoTest);

        assertEquals(1, totalSanciones, "Debe contar únicamente los reportes con estado = 2");
    }


    @Test
    public void testActualizarEstado() throws SQLException {
        int idReporte = crearReporteAuxiliar(idReportadorTest, idReportadoTest, 0, "Revisión pendiente");

        boolean actualizado = reporteDao.actualizarEstado(idReporte, 2);

        assertTrue(actualizado, "Debería actualizar el estado del reporte a 2 (Sancionado)");
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