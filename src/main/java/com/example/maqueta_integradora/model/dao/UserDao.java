package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.utils.HashUtil;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class UserDao implements Dao<User,Integer> {

    @Override
    public boolean create(User entidad) {
        // 1. Sentencias SQL (Se agrega activo = 1 y es_verificado = 0 explícitamente)
        String sqlUsuario = "INSERT INTO usuario(nombre, apellido, correo, contrasena, carrera, telefono, activo, es_verificado) VALUES(?, ?, ?, ?, ?, ?, 1, 0)";
        String sqlToken = "INSERT INTO tokens_verificacion(token, id_usuario, fecha_expiracion) VALUES(?, ?, ?)";
        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psToken = null;
        ResultSet rsKeys = null;
        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false);

            psUsuario = con.prepareStatement(sqlUsuario, new String[]{"id_usuario"});

            String contraEncriptada = HashUtil.hashSHA256(entidad.getContrasena());

            String nombreLimpio = (entidad.getNombre() != null) ? entidad.getNombre().trim() : null;
            String apellidoLimpio = (entidad.getApellido() != null) ? entidad.getApellido().trim() : null;

            psUsuario.setString(1, nombreLimpio);
            psUsuario.setString(2, apellidoLimpio);
            psUsuario.setString(3, entidad.getCorreo());
            psUsuario.setString(4, contraEncriptada);
            psUsuario.setString(5, entidad.getCarrera());
            psUsuario.setLong(6, entidad.getTelefono());

            psUsuario.executeUpdate();

            // Obtenemos el id_usuario recién generado por Oracle
            long idUsuarioGenerado = -1;
            rsKeys = psUsuario.getGeneratedKeys();
            if (rsKeys.next()) {
                idUsuarioGenerado = rsKeys.getLong(1);
            } else {
                throw new SQLException("No se pudo obtener el id_usuario generado.");
            }

            // Generamos token seguro de 8 dígitos
            SecureRandom random = new SecureRandom();
            int numeroToken = 10000000 + random.nextInt(90000000);
            String token8Digitos = String.valueOf(numeroToken);

            entidad.setToken(token8Digitos);

            LocalDateTime fechaExpiracion = LocalDateTime.now().plusMinutes(15);

            psToken = con.prepareStatement(sqlToken);
            psToken.setString(1, token8Digitos);
            psToken.setLong(2, idUsuarioGenerado);
            psToken.setTimestamp(3, Timestamp.valueOf(fechaExpiracion));
            psToken.executeUpdate();

            con.commit();
            System.out.println("Token de 8 dígitos generado para " + entidad.getCorreo() + ": " + token8Digitos);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psUsuario != null) psUsuario.close();
                if (psToken != null) psToken.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean actualizarSesionActiva(int idUsuario, int estadoSesion) {
        String sql = "UPDATE usuario SET sesion_activa = ? WHERE id_usuario = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estadoSesion);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAll() {
        List<User> listaUsuarios = new java.util.ArrayList<>();
        String sql = "SELECT id_usuario, nombre, apellido, correo, carrera, telefono, rol, activo, es_verificado FROM usuario ORDER BY id_usuario DESC";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setCarrera(rs.getString("carrera"));
                u.setTelefono(rs.getLong("telefono"));
                u.setRol(rs.getString("rol"));
                u.setActivo(rs.getInt("activo"));
                u.setEsVerificado(rs.getInt("es_verificado"));

                listaUsuarios.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar la lista completa de usuarios:");
            e.printStackTrace();
        }

        return listaUsuarios;
    }

    @Override
    public User getById(Integer id) {
        if (id == null) {
            return null;
        }
        String sql = "SELECT id_usuario, nombre, apellido, correo, carrera, telefono, rol, activo, es_verificado FROM usuario WHERE id_usuario = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setCorreo(rs.getString("correo"));
                    u.setCarrera(rs.getString("carrera"));
                    u.setTelefono(rs.getLong("telefono"));
                    u.setRol(rs.getString("rol"));
                    u.setActivo(rs.getInt("activo"));
                    u.setEsVerificado(rs.getInt("es_verificado"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + id);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(User entidad) {
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, correo = ?, carrera = ?, telefono = ?, rol = ?, activo = ?, es_verificado = ? WHERE id_usuario = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido() != null ? entidad.getApellido() : "");
            ps.setString(3, entidad.getCorreo());
            ps.setString(4, entidad.getCarrera() != null ? entidad.getCarrera() : "");

            if (entidad.getTelefono() > 0) {
                ps.setLong(5, entidad.getTelefono());
            } else {
                ps.setNull(5, java.sql.Types.NUMERIC);
            }

            ps.setString(6, entidad.getRol() != null ? entidad.getRol() : "USUARIO");
            ps.setInt(7, entidad.getActivo());
            ps.setInt(8, entidad.getEsVerificado());
            ps.setInt(9, entidad.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar la información del usuario ID " + entidad.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    public boolean login(String correo, String contrasena) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE LOWER(correo) = LOWER(?) AND contrasena = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (correo == null || contrasena == null) {
                return false;
            }
            String contrasenaHash = HashUtil.hashSHA256(contrasena);

            ps.setString(1, correo.trim());
            ps.setString(2, contrasenaHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar realizar el login.");
            e.printStackTrace();
        }
        return false;
    }

    public User obtenerPorCorreo(String correo) {
        String sql = "SELECT id_usuario, nombre, apellido, correo, carrera, telefono, rol, sesion_activa, activo, es_verificado FROM usuario WHERE LOWER(correo) = LOWER(?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setCorreo(rs.getString("correo"));
                    u.setCarrera(rs.getString("carrera"));
                    u.setTelefono(rs.getLong("telefono"));
                    u.setRol(rs.getString("rol"));
                    u.setSesionActiva(rs.getInt("sesion_activa"));
                    u.setActivo(rs.getInt("activo"));
                    u.setEsVerificado(rs.getInt("es_verificado"));

                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean darDeBaja(int idUsuario) {
        String sql = "UPDATE usuario SET activo = 0, sesion_activa = 0 WHERE id_usuario = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verificarTokenYActivarUsuario(String correo, String token) {
        String sqlBuscarToken =
                "SELECT t.id, u.id_usuario " +
                        "FROM tokens_verificacion t " +
                        "JOIN usuario u ON t.id_usuario = u.id_usuario " +
                        "WHERE t.token = ? AND LOWER(u.correo) = LOWER(?) AND t.fecha_expiracion > CURRENT_TIMESTAMP";

        // Ahora actualiza es_verificado a 1 en lugar de activo
        String sqlActivarUsuario = "UPDATE usuario SET es_verificado = 1 WHERE id_usuario = ?";
        String sqlEliminarToken = "DELETE FROM tokens_verificacion WHERE id = ?";

        Connection con = null;
        PreparedStatement psBuscar = null;
        PreparedStatement psActivar = null;
        PreparedStatement psEliminar = null;
        ResultSet rs = null;

        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false);

            psBuscar = con.prepareStatement(sqlBuscarToken);
            psBuscar.setString(1, token);
            psBuscar.setString(2, correo.trim());
            rs = psBuscar.executeQuery();

            if (rs.next()) {
                long tokenId = rs.getLong("id");
                long idUsuario = rs.getLong("id_usuario");

                psActivar = con.prepareStatement(sqlActivarUsuario);
                psActivar.setLong(1, idUsuario);
                psActivar.executeUpdate();

                psEliminar = con.prepareStatement(sqlEliminarToken);
                psEliminar.setLong(1, tokenId);
                psEliminar.executeUpdate();

                con.commit();
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psBuscar != null) psBuscar.close();
                if (psActivar != null) psActivar.close();
                if (psEliminar != null) psEliminar.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE LOWER(correo) = LOWER(?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (correo != null) {
                ps.setString(1, correo.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean guardarCodigoRecuperacion(String correo, String codigo) {
        String sql = "UPDATE usuario SET codigo_recuperacion = ?, limite_recuperacion = ? WHERE LOWER(correo) = LOWER(?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)));
            ps.setString(3, correo.trim());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validarCodigoYObtenerUsuario(String correo, String codigo) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE LOWER(correo) = LOWER(?) AND codigo_recuperacion = ? AND limite_recuperacion > CURRENT_TIMESTAMP";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            ps.setString(2, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarContrasena(String correo, String nuevaContra) {
        String sql = "UPDATE usuario SET contrasena = ?, codigo_recuperacion = NULL, limite_recuperacion = NULL WHERE LOWER(correo) = LOWER(?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String contraHash = HashUtil.hashSHA256(nuevaContra);
            ps.setString(1, contraHash);
            ps.setString(2, correo.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarTokenPorCorreo(String correo, String nuevoToken) {
        String sql = "UPDATE tokens_verificacion SET token = ?, fecha_expiracion = SYSDATE + INTERVAL '15' MINUTE WHERE id_usuario = (SELECT id_usuario FROM usuario WHERE LOWER(correo) = LOWER(?))";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoToken);
            ps.setString(2, correo.trim());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String reagenerarToken(int idUsuario) {
        SecureRandom random = new SecureRandom();
        int numeroToken = 10000000 + random.nextInt(90000000);
        String nuevoToken = String.valueOf(numeroToken);

        // Intenta actualizar token existente o insertar si no existe en la tabla de tokens
        String sqlUpdate = "UPDATE tokens_verificacion SET token = ?, fecha_expiracion = SYSDATE + INTERVAL '15' MINUTE WHERE id_usuario = ?";
        String sqlInsert = "INSERT INTO tokens_verificacion (token, id_usuario, fecha_expiracion) VALUES (?, ?, SYSDATE + INTERVAL '15' MINUTE)";

        try (Connection con = SQLConnector.getConnection()) {
            try (PreparedStatement psUp = con.prepareStatement(sqlUpdate)) {
                psUp.setString(1, nuevoToken);
                psUp.setInt(2, idUsuario);
                int filas = psUp.executeUpdate();

                if (filas == 0) {
                    try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                        psIns.setString(1, nuevoToken);
                        psIns.setInt(2, idUsuario);
                        psIns.executeUpdate();
                    }
                }
            }
            return nuevoToken;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean desactivarUsuario(int idUsuario) {
        String sqlUsuario = "UPDATE usuario SET activo = 0, sesion_activa = 0 WHERE id_usuario = ?";
        String sqlProductos = "UPDATE producto SET estado = 0 WHERE id_usuario = ? AND estado = 1";

        Connection con = null;
        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psUser = con.prepareStatement(sqlUsuario)) {
                psUser.setInt(1, idUsuario);
                psUser.executeUpdate();
            }

            try (PreparedStatement psProd = con.prepareStatement(sqlProductos)) {
                psProd.setInt(1, idUsuario);
                psProd.executeUpdate();
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
            System.err.println("Error al desactivar al usuario ID " + idUsuario + " y sus productos activos.");
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

    public boolean activarUsuario(int idUsuario) {
        String sql = "UPDATE usuario SET activo = 1 WHERE id_usuario = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al activar usuario con ID " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePerfil(int idUsuario, String nombre, long telefono) {
        String sql = "UPDATE usuario SET nombre = ?, telefono = ? WHERE id_usuario = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            if (telefono > 0) {
                ps.setLong(2, telefono);
            } else {
                ps.setNull(2, Types.NUMERIC);
            }
            ps.setInt(3, idUsuario);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el perfil del usuario ID " + idUsuario + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}