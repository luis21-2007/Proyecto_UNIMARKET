package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Calificacion;
import com.example.maqueta_integradora.utils.SQLConnector; // O la clase de conexión de tu proyecto

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalificacionDao {

    public boolean guardarCalificacion(int idComprador, int idVendedor, int idTransaccion, int puntuacion, String comentario) {
        String sql = "INSERT INTO calificacion (id_comprador, id_vendedor, id_transaccion, puntuacion, comentario) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idComprador);
            ps.setInt(2, idVendedor);
            ps.setInt(3, idTransaccion);
            ps.setInt(4, puntuacion);
            ps.setString(5, comentario);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar la calificación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean existeCalificacion(int idTransaccion) {
        String sql = "SELECT COUNT(*) FROM calificacion WHERE id_transaccion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTransaccion);
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
    public List<Calificacion> obtenerResenasPorVendedor(int idVendedor) {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT c.id_calificacion, c.puntuacion, c.comentario, c.fecha_calificacion, " +
                "u.nombre AS nombre_comprador, " +
                "COALESCE(p.nombre, 'Producto no disponible') AS nombre_producto " +
                "FROM calificacion c " +
                "JOIN usuario u ON c.id_comprador = u.id_usuario " +
                "LEFT JOIN transaccion t ON c.id_transaccion = t.id_transaccion " +
                "LEFT JOIN producto p ON t.id_producto = p.id_producto " +
                "WHERE c.id_vendedor = ? " +
                "ORDER BY c.fecha_calificacion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Calificacion calif = new Calificacion();
                    calif.setIdCalificacion(rs.getInt("id_calificacion"));
                    calif.setPuntuacion(rs.getInt("puntuacion"));
                    calif.setComentario(rs.getString("comentario"));
                    calif.setFechaCalificacion(rs.getTimestamp("fecha_calificacion"));
                    calif.setNombreComprador(rs.getString("nombre_comprador"));
                    calif.setNombreProducto(rs.getString("nombre_producto"));

                    lista.add(calif);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reseñas del vendedor: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // OBTENER PROMEDIO DE CALIFICACIONES DE UN VENDEDOR
    public double obtenerPromedioCalificaciones(int idVendedor) {
        String sql = "SELECT COALESCE(AVG(puntuacion), 0.0) AS promedio FROM calificacion WHERE id_vendedor = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("promedio");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular promedio de calificaciones: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
}