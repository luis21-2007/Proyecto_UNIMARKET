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
        String sql = "SELECT t.id_transaccion, t.monto, t.fecha_transaccion, t.estado, " +
                "COALESCE(p.nombre, 'Producto no disponible') AS nombre_producto, " +
                "COALESCE(u.nombre, 'Vendedor') AS nombre_vendedor, " +
                "u.telefono AS telefono_vendedor " +
                "FROM transaccion t " +
                "LEFT JOIN producto p ON t.id_producto = p.id_producto " +
                "LEFT JOIN usuario u ON t.id_vendedor = u.id_usuario " +
                "WHERE t.id_comprador = ? " +
                "ORDER BY t.fecha_transaccion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idComprador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setIdTransaccion(rs.getInt("id_transaccion"));
                    t.setMonto(rs.getDouble("monto"));
                    t.setFechaTransaccion(rs.getTimestamp("fecha_transaccion"));
                    t.setEstado(rs.getInt("estado"));
                    t.setNombreProducto(rs.getString("nombre_producto"));
                    t.setNombreVendedor(rs.getString("nombre_vendedor"));
                    t.setTelefonoVendedor(rs.getString("telefono_vendedor"));
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
        String sql = "SELECT t.id_transaccion, t.monto, t.fecha_transaccion, t.estado, " +
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
}