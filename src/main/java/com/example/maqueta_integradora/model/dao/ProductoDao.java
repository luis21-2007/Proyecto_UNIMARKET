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

    public List<Producto> getAll() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.descripcion, p.fecha_publicacion, p.estado, p.id_categoria, p.id_usuario, " +
                "(SELECT img.imagen_url FROM imagen_producto img WHERE img.id_producto = p.id_producto FETCH FIRST 1 ROWS ONLY) AS imagen_principal " +
                "FROM producto p " +
                "INNER JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.estado = 1 AND u.activo = 1 " +
                "ORDER BY p.fecha_publicacion DESC";

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
                p.setEstado(rs.getInt("estado"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdUsuario(rs.getInt("id_usuario"));

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
                    p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                    p.setEstado(rs.getInt("estado"));
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
        // Baja lógica: cambia estado a 0
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

        try (Connection con = SQLConnector.getConnection();
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
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
        return 0;
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


    public List<Producto> obtenerProductosPorUsuario(int idUsuario) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.descripcion, " +
                "p.fecha_publicacion, p.estado, p.id_categoria, p.id_usuario, " +
                "(SELECT img.imagen_url FROM imagen_producto img WHERE img.id_producto = p.id_producto FETCH FIRST 1 ROWS ONLY) AS imagen_principal " +
                "FROM producto p WHERE p.id_usuario = ? ORDER BY p.fecha_publicacion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                    p.setEstado(rs.getInt("estado"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setImagenUrl(rs.getString("imagen_principal"));

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos por usuario.");
            e.printStackTrace();
        }
        return lista;
    }

    public List<Producto> obtenerProductosPorUsuarioYCategoria(int idUsuario, int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.descripcion, " +
                "p.fecha_publicacion, p.estado, p.id_categoria, p.id_usuario, " +
                "(SELECT img.imagen_url FROM imagen_producto img WHERE img.id_producto = p.id_producto FETCH FIRST 1 ROWS ONLY) AS imagen_principal " +
                "FROM producto p WHERE p.id_usuario = ? AND p.id_categoria = ? ORDER BY p.fecha_publicacion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                    p.setEstado(rs.getInt("estado"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setImagenUrl(rs.getString("imagen_principal"));

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos por usuario y categoría.");
            e.printStackTrace();
        }
        return lista;
    }
    public boolean eliminarImagenEspecifica(int idProducto, String rutaImagen) {
        String sql = "DELETE FROM imagen_producto WHERE id_producto = ? AND imagen_url = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.setString(2, rutaImagen);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar la imagen específica: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public List<Producto> getByCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();

        // Consulta por categoría: productos activos (p.estado = 1), pertenecientes a usuarios activos (u.activo = 1)
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.descripcion, p.fecha_publicacion, p.estado, p.id_categoria, p.id_usuario, " +
                "(SELECT img.imagen_url FROM imagen_producto img WHERE img.id_producto = p.id_producto FETCH FIRST 1 ROWS ONLY) AS imagen_principal " +
                "FROM producto p " +
                "INNER JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.estado = 1 AND u.activo = 1 AND p.id_categoria = ? " +
                "ORDER BY p.fecha_publicacion DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
                    p.setEstado(rs.getInt("estado"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setImagenUrl(rs.getString("imagen_principal"));

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos filtrados por categoría.");
            e.printStackTrace();
        }
        return lista;
    }

    public List<Producto> getAllAdmin() {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.descripcion, p.fecha_publicacion, p.estado, p.id_categoria, p.id_usuario, " +
                "u.nombre AS nombre_vendedor, " +
                "(SELECT img.imagen_url FROM imagen_producto img WHERE img.id_producto = p.id_producto FETCH FIRST 1 ROWS ONLY) AS imagen_principal " +
                "FROM producto p LEFT JOIN usuario u ON p.id_usuario = u.id_usuario ORDER BY p.id_producto DESC";

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
                p.setEstado(rs.getInt("estado"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setImagenUrl(rs.getString("imagen_principal"));
                p.setNombreVendedor(rs.getString("nombre_vendedor"));

                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos para administración.");
            e.printStackTrace();
        }
        return lista;
    }

    public boolean activar(int idProducto) {
        String sql = "UPDATE producto SET estado = 1 WHERE id_producto = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al activar producto ID " + idProducto + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivar(int idProducto) {
        String sql = "UPDATE producto SET estado = 0 WHERE id_producto = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al desactivar producto ID " + idProducto + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean proceso(int idProducto) {
        String sql = "UPDATE producto SET estado = 3 WHERE id_producto = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al desactivar producto ID " + idProducto + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean procesarCompraDirecta(int idProducto, int idComprador, int idVendedor, double monto) {
        // 1. Bloquea el registro del producto con FOR UPDATE para evitar la condición de carrera
        String sqlCheck = "SELECT estado FROM producto WHERE id_producto = ? FOR UPDATE";

        // 2. Cambia el estado a 3 ("En Proceso" / Vendido) solo si sigue estando disponible (estado = 1)
        String sqlUpdateProducto = "UPDATE producto SET estado = 3 WHERE id_producto = ? AND estado = 1";

        // 3. Registra la transacción con la fecha actual de Oracle
        String sqlInsertTransaccion = "INSERT INTO transaccion (id_producto, id_comprador, id_vendedor, monto, estado, fecha_transaccion) " +
                "VALUES (?, ?, ?, ?, 2, CURRENT_TIMESTAMP)";

        Connection conn = null;
        try {
            conn = SQLConnector.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción manual

            // A. Validar y Bloquear Producto
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idProducto);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        int estadoActual = rs.getInt("estado");
                        if (estadoActual != 1) { // Si ya no está disponible (ej. alguien lo ganó milisegundos antes)
                            conn.rollback();
                            return false;
                        }
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // B. Actualizar Estado del Producto
            int filasAfectadas = 0;
            try (PreparedStatement psUp = conn.prepareStatement(sqlUpdateProducto)) {
                psUp.setInt(1, idProducto);
                filasAfectadas = psUp.executeUpdate();
            }

            if (filasAfectadas == 0) { // No se pudo actualizar porque cambió su estado en paralelo
                conn.rollback();
                return false;
            }

            // C. Crear la Transacción
            try (PreparedStatement psIns = conn.prepareStatement(sqlInsertTransaccion)) {
                psIns.setInt(1, idProducto);
                psIns.setInt(2, idComprador);
                psIns.setInt(3, idVendedor);
                psIns.setDouble(4, monto);
                psIns.executeUpdate();
            }

            conn.commit(); // Confirmar la compra si todo salió bien
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error en transacción procesarCompraDirecta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
