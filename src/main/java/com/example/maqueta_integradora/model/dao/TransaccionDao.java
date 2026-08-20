package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Transaccion;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDao {

    // CONSULTAR COMPRAS DEL USUARIO
    public List<Transaccion> getComprasByUsuario(int idComprador) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT t.id_transaccion, t.id_vendedor, t.id_producto, t.monto, t.fecha_transaccion, t.estado, " +
                "COALESCE(p.nombre, 'Producto no disponible') AS nombre_producto, " +
                "COALESCE(u.nombre, 'Vendedor') AS nombre_vendedor, " +
                "u.telefono AS telefono_vendedor, " +
                "CASE WHEN c.id_calificacion IS NOT NULL THEN 1 ELSE 0 END AS ya_calificado " +
                "FROM transaccion t " +
                "LEFT JOIN producto p ON t.id_producto = p.id_producto " +
                "LEFT JOIN usuario u ON t.id_vendedor = u.id_usuario " +
                "LEFT JOIN calificacion c ON t.id_transaccion = c.id_transaccion " +
                "WHERE t.id_comprador = ? " +
                "ORDER BY t.fecha_transaccion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idComprador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    t.setIdVendedor(rs.getInt("id_vendedor"));
                    t.setIdProducto(rs.getInt("id_producto"));
                    t.setMonto(rs.getDouble("monto"));
                    t.setFechaTransaccion(rs.getTimestamp("fecha_transaccion"));
                    t.setEstado(rs.getInt("estado"));
                    t.setNombreProducto(rs.getString("nombre_producto"));
                    t.setNombreVendedor(rs.getString("nombre_vendedor"));
                    t.setTelefonoVendedor(rs.getString("telefono_vendedor"));
                    t.setYaCalificado(rs.getInt("ya_calificado") == 1);
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener compras: " + e.getMessage());
        }
        return lista;
    }

    // CONSULTAR VENTAS DEL USUARIO
    public List<Transaccion> getVentasByUsuario(int idVendedor) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT t.id_transaccion, t.id_comprador, t.id_producto, t.monto, t.fecha_transaccion, t.estado, " +
                "COALESCE(p.nombre, 'Producto no disponible') AS nombre_producto, " +
                "COALESCE(u.nombre, 'Comprador') AS nombre_comprador, " +
                "u.telefono AS telefono_comprador " +
                "FROM transaccion t " +
                "LEFT JOIN producto p ON t.id_producto = p.id_producto " +
                "LEFT JOIN usuario u ON t.id_comprador = u.id_usuario " +
                "WHERE t.id_vendedor = ? " +
                "ORDER BY t.fecha_transaccion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    t.setIdComprador(rs.getInt("id_comprador"));
                    t.setIdProducto(rs.getInt("id_producto"));
                    t.setMonto(rs.getDouble("monto"));
                    t.setFechaTransaccion(rs.getTimestamp("fecha_transaccion"));
                    t.setEstado(rs.getInt("estado"));
                    t.setNombreProducto(rs.getString("nombre_producto"));
                    t.setNombreComprador(rs.getString("nombre_comprador"));
                    t.setTelefonoComprador(rs.getString("telefono_comprador"));
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return lista;
    }
public boolean actualizarEstado(int idTransaccion, int nuevoEstado) {
    String sqlUpdateTrans = "UPDATE transaccion SET estado = ? WHERE id_transaccion = ?";
    String sqlGetProducto = "SELECT id_producto FROM transaccion WHERE id_transaccion = ?";
    String sqlUpdateProducto = "UPDATE producto SET estado = ? WHERE id_producto = ?";

    Connection con = null;
    try {
        con = SQLConnector.getConnection();
        con.setAutoCommit(false); // Transacción JDBC (Todo o Nada)

        // 1. Cambiar el estado de la transacción
        try (PreparedStatement ps = con.prepareStatement(sqlUpdateTrans)) {
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idTransaccion);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                con.rollback();
                return false;
            }
        }

        // 2. Obtener el ID del producto asociado a esta transacción
        int idProducto = 0;
        try (PreparedStatement psGetProd = con.prepareStatement(sqlGetProducto)) {
            psGetProd.setInt(1, idTransaccion);
            try (ResultSet rs = psGetProd.executeQuery()) {
                if (rs.next()) {
                    idProducto = rs.getInt("id_producto");
                }
            }
        }

        // 3. Actualizar estado del producto según la decisión del vendedor
        if (idProducto > 0) {
            int nuevoEstadoProducto = -1;

            if (nuevoEstado == 1) {
                nuevoEstadoProducto = 2; // VENDIDO: Pasa a estado 2
            } else if (nuevoEstado == 0) {
                nuevoEstadoProducto = 1; // CANCELADO: Vuelve a estar activo en el marketplace (estado 1)
            }

            // Si aplica un cambio de estado al producto, lo ejecutamos
            if (nuevoEstadoProducto != -1) {
                try (PreparedStatement psProd = con.prepareStatement(sqlUpdateProducto)) {
                    psProd.setInt(1, nuevoEstadoProducto);
                    psProd.setInt(2, idProducto);
                    psProd.executeUpdate();
                }
            }
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        if (con != null) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        e.printStackTrace();
        return false;
    } finally {
        if (con != null) {
            try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
    public boolean registrarCompraDirectaPendiente(int idComprador, int idVendedor, int idProducto, double monto) {
        String sql = "INSERT INTO transaccion (id_comprador, id_vendedor, id_producto, monto, fecha_transaccion, estado) " +
                "VALUES (?, ?, ?, ?, CURRENT_DATE, 2)"; // Estado 2 = en proceso o pendiente

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idComprador);
            ps.setInt(2, idVendedor);
            ps.setInt(3, idProducto);
            ps.setDouble(4, monto);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar la compra directa pendiente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean cancelarTransaccion(int idTransaccion, int idProducto) {
        String sqlTx = "UPDATE transaccion SET estado_transaccion = 'CANCELADA' WHERE id_transaccion = ?";
        String sqlProducto = "UPDATE producto SET estado = 1 WHERE id_producto = ?";

        Connection con = null;
        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Cancelar la transacción
            try (PreparedStatement psTx = con.prepareStatement(sqlTx)) {
                psTx.setInt(1, idTransaccion);
                psTx.executeUpdate();
            }

            // 2. Volver a poner disponible el producto (estado = 1)
            try (PreparedStatement psProd = con.prepareStatement(sqlProducto)) {
                psProd.setInt(1, idProducto);
                psProd.executeUpdate();
            }

            con.commit(); // Confirmar cambios
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;

        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    public boolean usuarioYaComproDirecto(int idUsuario, int idProducto) {
        // FILTRO CLAVE: AND estado != 0 (Para ignorar compras rechazadas/canceladas)
        String sql = "SELECT COUNT(*) FROM transaccion WHERE id_comprador = ? AND id_producto = ? AND estado != 0";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}