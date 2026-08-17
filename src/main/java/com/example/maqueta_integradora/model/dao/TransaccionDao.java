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
/*
    // CONSULTAR TRANSACCIÓN POR ID (NUEVO MÉTODO PARA REPORTES/ADMIN)
    public Transaccion obtenerPorId(int idTransaccion) {
        String sql = "SELECT t.id_transaccion, t.id_producto, t.id_comprador, t.id_vendedor, t.monto, " +
                "t.fecha_transaccion, t.estado, " +
                "COALESCE(p.nombre, 'Producto no disponible') AS nombre_producto, " +
                "COALESCE(u1.nombre, 'Comprador') AS nombre_comprador, " +
                "COALESCE(u2.nombre, 'Vendedor') AS nombre_vendedor " +
                "FROM transaccion t " +
                "LEFT JOIN producto p ON t.id_producto = p.id_producto " +
                "LEFT JOIN usuario u1 ON t.id_comprador = u1.id_usuario " +
                "LEFT JOIN usuario u2 ON t.id_vendedor = u2.id_usuario " +
                "WHERE t.id_transaccion = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTransaccion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    t.setIdProducto(rs.getInt("id_producto"));
                    t.setIdComprador(rs.getInt("id_comprador"));
                    t.setIdVendedor(rs.getInt("id_vendedor"));
                    t.setMonto(rs.getDouble("monto"));
                    t.setFechaTransaccion(rs.getTimestamp("fecha_transaccion"));
                    t.setEstado(rs.getInt("estado"));
                    t.setNombreProducto(rs.getString("nombre_producto"));
                    t.setNombreComprador(rs.getString("nombre_comprador"));
                    t.setNombreVendedor(rs.getString("nombre_vendedor"));
                    return t;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener transacción por ID: " + e.getMessage());
        }
        return null;
    }

 */
    // ACTUALIZAR ESTADO DE TRANSACCIÓN Y DAR DE BAJA LÓGICA AL PRODUCTO SI SE COMPLETA
    public boolean actualizarEstado(int idTransaccion, int nuevoEstado) {
        String sqlUpdateTrans = "UPDATE transaccion SET estado = ? WHERE id_transaccion = ?";
        String sqlGetProducto = "SELECT id_producto FROM transaccion WHERE id_transaccion = ?";
        String sqlDesactivarProducto = "UPDATE producto SET estado = 2 WHERE id_producto = ?";

        Connection con = null;
        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false); // Iniciar transacción (Todo o Nada)

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

            // 2. Si el nuevo estado es 1 (Vendido / Completada), hacemos la BAJA LÓGICA del producto
            if (nuevoEstado == 1) {
                int idProducto = 0;

                // Obtener el ID del producto asociado a esta transacción
                try (PreparedStatement psGetProd = con.prepareStatement(sqlGetProducto)) {
                    psGetProd.setInt(1, idTransaccion);
                    try (ResultSet rs = psGetProd.executeQuery()) {
                        if (rs.next()) {
                            idProducto = rs.getInt("id_producto");
                        }
                    }
                }

                // Desactivar el producto (estado = 0)
                if (idProducto > 0) {
                    try (PreparedStatement psDesactivar = con.prepareStatement(sqlDesactivarProducto)) {
                        psDesactivar.setInt(1, idProducto);
                        psDesactivar.executeUpdate();
                    }
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
}