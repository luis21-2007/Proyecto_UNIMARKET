package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao implements Dao<Producto, Integer> {

    @Override
    public boolean create(Producto p) {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, imagen_url, id_categoria, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre().trim());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getDescripcion().trim());
            ps.setString(4, p.getImagenUrl());
            ps.setInt(5, p.getIdCategoria());
            ps.setInt(6, p.getIdUsuario()); // FK del vendedor que lo publica

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al publicar el producto.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Producto> getAll() {
        List<Producto> lista = new ArrayList<>();

        // Consulta con subquery para obtener la primera imagen de cada producto
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.descripcion,p.fecha_publicacion, p.estado, p.id_categoria, p.id_usuario,(SELECT img.imagen_url FROM imagen_producto img WHERE img.id_producto = p.id_producto FETCH FIRST 1 ROWS ONLY) AS imagen_principal  FROM producto p WHERE p.estado = 1 ORDER BY p.fecha_publicacion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                p.setEstado(rs.getInt("estado") == 1);
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdUsuario(rs.getInt("id_usuario"));

                // Asignamos la primera imagen obtenida de la subconsulta
                p.setImagenUrl(rs.getString("imagen_principal"));

                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos con imagen principal.");
            e.printStackTrace();
        }
        return lista;
    }
    @Override
    public Producto getById(Integer id) {
        String sql = "SELECT * FROM producto WHERE id_producto = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setImagenUrl(rs.getString("imagen_url"));
                    p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                    p.setEstado(rs.getInt("estado") == 1);
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar producto por ID.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, id_categoria = ? WHERE id_producto = ? AND id_usuario = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre().trim());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getDescripcion().trim());
            ps.setInt(4, p.getIdCategoria());
            ps.setInt(5, p.getIdProducto());
            ps.setInt(6, p.getIdUsuario()); // Seguridad: Solo el dueño puede editarlo

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        // Baja lógica
        String sql = "UPDATE producto SET estado = 0 WHERE id_producto = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en la baja lógica del producto.");
            e.printStackTrace();
        }
        return false;
    }
    public boolean guardarImagenProducto(int idProducto, String rutaImagen) {
        String sql = "INSERT INTO imagen_producto (id_producto, imagen_url) VALUES (?, ?)";

        try (Connection con = SQLConnector  .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.setString(2, rutaImagen);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar imagen del producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public int createAndGetId(Producto p) {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, id_categoria, id_usuario) VALUES (?, ?, ?, ?, ?)";
        // Colocamos el parámetro RETURN_GENERATED_KEYS o indicamos el nombre de la columna
        String[] keyColumn = {"id_producto"};

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, keyColumn)) {

            ps.setString(1, p.getNombre().trim());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getDescripcion().trim());
            ps.setInt(4, p.getIdCategoria());
            ps.setInt(5, p.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retorna el ID recién creado
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
        return 0; // Si falla
    }
    public List<String> getImagenesByProductoId(int idProducto) {
        List<String> listaImagenes = new ArrayList<>();
        String sql = "SELECT imagen_url FROM imagen_producto WHERE id_producto = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaImagenes.add(rs.getString("imagen_url"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener imágenes del producto.");
            e.printStackTrace();
        }
        return listaImagenes;
    }
}