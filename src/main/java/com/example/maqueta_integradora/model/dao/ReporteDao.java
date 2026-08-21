package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Reporte;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDao {

    public boolean guardarReporte(int idReportador, int idReportado, Integer idTransaccion, String motivo, String descripcion) {
        String sql = "INSERT INTO reporte_usuario (id_reportador, id_reportado, id_transaccion, motivo, descripcion, estado) VALUES (?, ?, ?, ?, ?, 0)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idReportador);
            ps.setInt(2, idReportado);

            // Si viene un ID válido se guarda; si es null o <= 0, se inserta NULL en la BD
            if (idTransaccion != null && idTransaccion > 0) {
                ps.setInt(3, idTransaccion);
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setString(4, motivo);
            ps.setString(5, descripcion);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar el reporte: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // 2. CONSULTAR TODOS LOS REPORTES PARA EL PANEL DE ADMINISTRACIÓN
    public List<Reporte> obtenerTodos() {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT r.id_reporte, r.id_reportador, r.id_reportado, " +
                "r.motivo, r.descripcion, r.estado, r.fecha_reporte, " +
                "u1.nombre AS nombre_reportador, u1.correo AS correo_reportador, " +
                "u2.nombre AS nombre_reportado, u2.correo AS correo_reportado " +
                "FROM reporte_usuario r " +
                "JOIN usuario u1 ON r.id_reportador = u1.id_usuario " +
                "JOIN usuario u2 ON r.id_reportado = u2.id_usuario " +
                "ORDER BY r.fecha_reporte DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reporte r = new Reporte();
                r.setIdReporte(rs.getInt("id_reporte"));
                r.setIdReportador(rs.getInt("id_reportador"));
                r.setIdReportado(rs.getInt("id_reportado"));
                r.setMotivo(rs.getString("motivo"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setEstado(rs.getInt("estado"));
                r.setFechaReporte(rs.getTimestamp("fecha_reporte"));

                r.setNombreReportador(rs.getString("nombre_reportador"));
                r.setCorreoReportador(rs.getString("correo_reportador"));
                r.setNombreReportado(rs.getString("nombre_reportado"));
                r.setCorreoReportado(rs.getString("correo_reportado"));

                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener lista de reportes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // 3. CAMBIAR EL ESTADO DEL REPORTE (ADMIN)
    public boolean actualizarEstado(int idReporte, int nuevoEstado) {
        String sql = "UPDATE reporte_usuario SET estado = ? WHERE id_reporte = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idReporte);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del reporte: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public int obtenerCantidadSancionesPorUsuario(int idUsuario) {
        int total = 0;
        // Estado 2 representa "Sancionado/Aprobado" en el sistema de reportes
        String sql = "SELECT COUNT(*) FROM reporte_usuario WHERE id_reportado = ? AND estado = 2";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public boolean yaReportoVendedor(int idUsuario, int idVendedor) {
        String sql = "SELECT COUNT(*) FROM reporte_usuario WHERE id_reportador = ? AND id_reportado = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVendedor);

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