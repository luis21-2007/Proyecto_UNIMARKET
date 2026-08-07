package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Oferta;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfertaDao {

    public boolean guardarOferta(Oferta oferta) {
        String sql = "INSERT INTO oferta (monto_oferta, estado, id_usuario, id_producto) VALUES (?, 0, ?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, oferta.getMontoOferta());
            ps.setInt(2, oferta.getIdUsuario());
            ps.setInt(3, oferta.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar la oferta.");
            e.printStackTrace();
            return false;
        }
    }
    public List<Oferta> getOfertasByComprador(int idComprador) {
        List<Oferta> lista = new ArrayList<>();

        // Usamos LEFT JOIN para que la oferta siga apareciendo aunque el producto o vendedor cambien de estado
        String sql = "SELECT o.id_oferta, o.monto_oferta, o.estado, o.fecha_oferta, " +
                "COALESCE(p.nombre, 'Producto no disponible') AS nombre_producto, " +
                "COALESCE(u.nombre, 'Vendedor') AS nombre_vendedor, " +
                "u.telefono AS telefono_vendedor " +
                "FROM oferta o " +
                "LEFT JOIN producto p ON o.id_producto = p.id_producto " +
                "LEFT JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE o.id_usuario = ? " +
                "ORDER BY o.fecha_oferta DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idComprador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta o = new Oferta();
                    o.setIdOferta(rs.getInt("id_oferta"));
                    o.setMontoOferta(rs.getDouble("monto_oferta"));
                    o.setEstado(rs.getInt("estado"));
                    o.setFechaOferta(rs.getTimestamp("fecha_oferta"));
                    o.setNombreProducto(rs.getString("nombre_producto"));
                    o.setNombreVendedor(rs.getString("nombre_vendedor"));
                    o.setTelefonoVendedor(rs.getString("telefono_vendedor"));

                    lista.add(o);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ofertas del comprador: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    public boolean actualizarEstado(int idOferta, int nuevoEstado) {
        String sql = "UPDATE oferta SET estado = ? WHERE id_oferta = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idOferta);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de oferta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<Oferta> getOfertasByVendedor(int idVendedor) {
        List<Oferta> lista = new ArrayList<>();

        String sql = "SELECT o.id_oferta, o.monto_oferta, o.estado, o.fecha_oferta, " +
                "p.nombre AS nombre_producto, " +
                "u.nombre AS nombre_comprador " +
                "FROM oferta o " +
                "INNER JOIN producto p ON o.id_producto = p.id_producto " +
                "INNER JOIN usuario u ON o.id_usuario = u.id_usuario " + // JOIN al COMPRADOR
                "WHERE p.id_usuario = ? " + // ID del VENDEDOR (dueño del producto)
                "ORDER BY o.fecha_oferta DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta o = new Oferta();
                    o.setIdOferta(rs.getInt("id_oferta"));
                    o.setMontoOferta(rs.getDouble("monto_oferta"));
                    o.setEstado(rs.getInt("estado"));
                    o.setFechaOferta(rs.getTimestamp("fecha_oferta"));
                    o.setNombreProducto(rs.getString("nombre_producto"));
                    o.setNombreComprador(rs.getString("nombre_comprador")); // Recuerda agregar este atributo en Oferta.java

                    lista.add(o);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ofertas recibidas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}