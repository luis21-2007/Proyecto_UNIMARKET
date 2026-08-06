package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao implements Dao<Categoria, Integer> {

    @Override
    public boolean create(Categoria entidad) {
        String sql = "INSERT INTO categoria (nombre_categoria, id_admin_creo) VALUES (?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombreCategoria().trim());
            ps.setInt(2, entidad.getIdAdminCreo());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear categoría.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Categoria> getAll() {
        List<Categoria> lista = new ArrayList<>();
        // Filtramos solo las que están activas (estado = 1)
        String sql = "SELECT id_categoria, nombre_categoria, fecha_creacion, fecha_modificacion, id_admin_creo, id_admin_modifico, estado FROM categoria WHERE estado = 1 ORDER BY nombre_categoria ASC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("id_categoria"));
                cat.setNombreCategoria(rs.getString("nombre_categoria"));
                cat.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                cat.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
                cat.setIdAdminCreo(rs.getInt("id_admin_creo"));

                int idMod = rs.getInt("id_admin_modifico");
                cat.setIdAdminModifico(rs.wasNull() ? null : idMod);

                cat.setEstado(rs.getInt("estado") == 1); // 1 es true, 0 es false

                lista.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las categorías.");
            e.printStackTrace();
        }
        return lista;
    }


    @Override
    public Categoria getById(Integer id) {
        String sql = "SELECT id_categoria, nombre_categoria, fecha_creacion, fecha_modificacion, id_admin_creo, id_admin_modifico FROM categoria WHERE id_categoria = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setIdCategoria(rs.getInt("id_categoria"));
                    cat.setNombreCategoria(rs.getString("nombre_categoria"));
                    cat.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    cat.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
                    cat.setIdAdminCreo(rs.getInt("id_admin_creo"));

                    int idMod = rs.getInt("id_admin_modifico");
                    cat.setIdAdminModifico(rs.wasNull() ? null : idMod);

                    return cat;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categoría por ID.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Categoria entidad) {
        // guardar la fecha/hora actual del servidor
        String sql = "UPDATE categoria SET nombre_categoria = ?, fecha_modificacion = SYSDATE, id_admin_modifico = ? WHERE id_categoria = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombreCategoria().trim());

            if (entidad.getIdAdminModifico() != null) {
                ps.setInt(2, entidad.getIdAdminModifico());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setInt(3, entidad.getIdCategoria());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        // Baja lógica: Cambiamos el estado a 0 (Inactivo)
        String sql = "UPDATE categoria SET estado = 0, fecha_modificacion = SYSDATE WHERE id_categoria = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al realizar la baja lógica de la categoría.");
            e.printStackTrace();
        }
        return false;
    }

    //validar duplicados al CREAR una nueva categoría
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre_categoria) = LOWER(?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nombre != null) {
                ps.setString(1, nombre.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de nombre de categoría.");
            e.printStackTrace();
        }
        return false;
    }

    // validar duplicados al EDITAR (ignora el ID de la categoría actual)
    public boolean existeNombreExcluyendoId(String nombre, int idCategoria) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre_categoria) = LOWER(?) AND id_categoria <> ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nombre != null) {
                ps.setString(1, nombre.trim());
                ps.setInt(2, idCategoria);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar duplicado de nombre excluyendo ID.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean desactivar(int idCategoria) {
        String sql = "UPDATE categoria SET estado = 0 WHERE id_categoria = ?";

        try (Connection con = SQLConnector.getConnection(); // Ajusta a tu método de conexión JDBC
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al desactivar categoría con ID: " + idCategoria);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Activa una categoría cambiando su estado a 1 (Activa).
     */

    public boolean activar(int idCategoria) {
        String sql = "UPDATE categoria SET estado = 1 WHERE id_categoria = ?";

        try (Connection con = SQLConnector.getConnection(); // Ajusta a tu método de conexión JDBC
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al activar categoría con ID: " + idCategoria);
            e.printStackTrace();
            return false;
        }
    }
    public List<Categoria> getAllAdmin() {
        List<Categoria> lista = new ArrayList<>();

        // c es alias de categoria, p es alias de producto
        // COUNT(p.id_producto) cuenta cuántos productos tienen asignado ese id_categoria
        String sql = "SELECT c.id_categoria, c.nombre_categoria, c.estado, COUNT(p.id_producto) AS total_productos FROM categoria c LEFT JOIN producto p ON c.id_categoria = p.id_categoria GROUP BY c.id_categoria, c.nombre_categoria, c.estado ORDER BY c.id_categoria DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("id_categoria"));
                cat.setNombreCategoria(rs.getString("nombre_categoria"));
                cat.setEstado(rs.getBoolean("estado"));

                // Leemos el resultado del alias 'total_productos' que calculó el COUNT de SQL
                cat.setTotalProductos(rs.getInt("total_productos"));

                lista.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar los productos por categoría.");
            e.printStackTrace();
        }
        return lista;
    }
}