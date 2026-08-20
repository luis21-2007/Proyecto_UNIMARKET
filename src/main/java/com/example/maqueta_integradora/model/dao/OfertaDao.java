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

        // Se agregaron o.id_producto y o.id_usuario a la consulta SQL
        String sql = "SELECT o.id_oferta, o.id_producto, o.id_usuario, o.monto_oferta, o.estado, o.fecha_oferta, " +
                "p.nombre AS nombre_producto, " +
                "u.nombre AS nombre_comprador " +
                "FROM oferta o " +
                "INNER JOIN producto p ON o.id_producto = p.id_producto " +
                "INNER JOIN usuario u ON o.id_usuario = u.id_usuario " +
                "WHERE p.id_usuario = ? " +
                "ORDER BY o.fecha_oferta DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta o = new Oferta();
                    o.setIdOferta(rs.getInt("id_oferta"));
                    o.setIdProducto(rs.getInt("id_producto"));
                    o.setIdUsuario(rs.getInt("id_usuario"));
                    o.setMontoOferta(rs.getDouble("monto_oferta"));
                    o.setEstado(rs.getInt("estado"));
                    o.setFechaOferta(rs.getTimestamp("fecha_oferta"));
                    o.setNombreProducto(rs.getString("nombre_producto"));
                    o.setNombreComprador(rs.getString("nombre_comprador"));

                    lista.add(o);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ofertas recibidas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    public boolean aceptarOfertaYRegistrarVenta(int idOferta) {
        String sqlBuscarOferta = "SELECT id_producto, id_usuario, monto_oferta FROM oferta WHERE id_oferta = ? AND estado = 0";
        String sqlBuscarVendedor = "SELECT id_usuario FROM producto WHERE id_producto = ?";
        String sqlActualizarOferta = "UPDATE oferta SET estado = 1 WHERE id_oferta = ?";

        // Insertamos la transacción con estado 2 (En Proceso) por defecto y NO tocamos el producto
        String sqlInsertarTransaccion = "INSERT INTO transaccion (id_producto, id_comprador, id_vendedor, monto, estado) VALUES (?, ?, ?, ?, 2)";

        Connection con = null;
        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false); // Transacción JDBC para asegurar que se ejecuten todas las consultas

            int idProducto = 0;
            int idComprador = 0;
            int idVendedor = 0;
            double monto = 0.0;

            // 1. Obtener datos de la oferta pendiente
            try (PreparedStatement ps = con.prepareStatement(sqlBuscarOferta)) {
                ps.setInt(1, idOferta);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idProducto = rs.getInt("id_producto");
                        idComprador = rs.getInt("id_usuario");
                        monto = rs.getDouble("monto_oferta");
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 2. Obtener ID del vendedor (dueño del producto)
            try (PreparedStatement ps = con.prepareStatement(sqlBuscarVendedor)) {
                ps.setInt(1, idProducto);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idVendedor = rs.getInt("id_usuario");
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 3. Marcar esta oferta como Aceptada (estado = 1)
            try (PreparedStatement ps = con.prepareStatement(sqlActualizarOferta)) {
                ps.setInt(1, idOferta);
                ps.executeUpdate();
            }

            // 4. Crear el registro en la tabla TRANSACCION (estado 2 = En Proceso)
            try (PreparedStatement ps = con.prepareStatement(sqlInsertarTransaccion)) {
                ps.setInt(1, idProducto);
                ps.setInt(2, idComprador);
                ps.setInt(3, idVendedor);
                ps.setDouble(4, monto);
                ps.executeUpdate();
            }

            con.commit(); // Guarda cambios permanentemente
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error al aceptar la oferta e insertar la transacción: " + e.getMessage());
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
    public int getEstadoOfertaUsuario(int idUsuario, int idProducto) {
        // Busca la última oferta hecha por el usuario para este producto
        String sql = "SELECT estado FROM oferta WHERE id_usuario = ? AND id_producto = ? ORDER BY id_oferta DESC FETCH FIRST 1 ROWS ONLY";;

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("estado"); // Retorna 0 (Pendiente), 1 (Aceptada), 2 (Rechazada), etc.
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar estado de oferta: " + e.getMessage());
        }
        return -1; // -1 indica que NO ha realizado ninguna oferta
    }
    public List<Oferta> getOfertasByProducto(int idProducto) {
        List<Oferta> lista = new ArrayList<>();

        String sql = "SELECT o.id_oferta, o.monto_oferta, o.estado, o.fecha_oferta, " +
                "u.nombre AS nombre_comprador " +
                "FROM oferta o " +
                "INNER JOIN usuario u ON o.id_usuario = u.id_usuario " + // JOIN para traer los datos del comprador
                "WHERE o.id_producto = ? " + // Filtro por el producto actual
                "ORDER BY o.fecha_oferta DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oferta o = new Oferta();
                    o.setIdOferta(rs.getInt("id_oferta"));
                    o.setMontoOferta(rs.getDouble("monto_oferta"));
                    o.setEstado(rs.getInt("estado"));
                    o.setFechaOferta(rs.getTimestamp("fecha_oferta"));
                    o.setNombreComprador(rs.getString("nombre_comprador"));

                    lista.add(o);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ofertas por producto: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    public boolean aceptarOfertaYActualizarProducto(int idOferta, int idProducto, int idComprador, int idVendedor, double monto) {
        String sqlActualizarOfertas = "UPDATE oferta SET estado = CASE WHEN id_oferta = ? THEN 1 ELSE 2 END " +
                "WHERE id_producto = ? AND (estado = 0 OR id_oferta = ?)";
        String sqlActualizarProducto = "UPDATE producto SET estado = 3 WHERE id_producto = ?";
        String sqlCrearTransaccion = "INSERT INTO transaccion (id_producto, id_comprador, id_vendedor, monto, estado, fecha_transaccion) " +
                "VALUES (?, ?, ?, ?, 2, CURRENT_TIMESTAMP)";

        Connection conn = null;
        try {
            conn = SQLConnector.getConnection();
            conn.setAutoCommit(false); // Inicia transacción

            try (PreparedStatement ps1 = conn.prepareStatement(sqlActualizarOfertas)) {
                ps1.setInt(1, idOferta);
                ps1.setInt(2, idProducto);
                ps1.setInt(3, idOferta);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlActualizarProducto)) {
                ps2.setInt(1, idProducto);
                ps2.executeUpdate();
            }

            try (PreparedStatement ps3 = conn.prepareStatement(sqlCrearTransaccion)) {
                ps3.setInt(1, idProducto);
                ps3.setInt(2, idComprador);
                ps3.setInt(3, idVendedor);
                ps3.setDouble(4, monto);
                ps3.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}