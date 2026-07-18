package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class UserDao implements Dao<User,Integer> {

    @Override
    public boolean create(User entidad) {
        // 1. Sentencias SQL corregidas (el orden de los ? ahora coincide con tus sets)
        String sqlUsuario = "INSERT INTO usuario(nombre, apellido, correo, contrasena, carrera) VALUES(?, ?, ?, ?, ?)";
        String sqlToken = "INSERT INTO tokens_verificacion(token, id_usuario, fecha_expiracion) VALUES(?, ?, ?)";
        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psToken = null;
        ResultSet rsKeys = null;
        try {
            con = SQLConnector.getConnection();
            // Desactivamos el auto-commit para manejar la transacción manualmente
            con.setAutoCommit(false);

            // 2. Preparamos la inserción del usuario pidiendo que retorne la llave generada (id_usuario)
            psUsuario = con.prepareStatement(sqlUsuario, new String[]{"id_usuario"});

            psUsuario.setString(1, entidad.getNombre());
            psUsuario.setString(2, entidad.getApellido());
            psUsuario.setString(3, entidad.getCorreo());
            psUsuario.setString(4, entidad.getContrasena());
            psUsuario.setString(5, entidad.getCarrera());

            psUsuario.executeUpdate();

            // 3. Obtenemos el id_usuario recién generado por Oracle
            long idUsuarioGenerado = -1;
            rsKeys = psUsuario.getGeneratedKeys();
            if (rsKeys.next()) {
                idUsuarioGenerado = rsKeys.getLong(1);
            } else {
                throw new SQLException("No se pudo obtener el id_usuario generado.");
            }

            // 4. Generamos el token seguro de 8 números
            SecureRandom random = new SecureRandom();
            int numeroToken = 10000000 + random.nextInt(90000000);
            String token8Digitos = String.valueOf(numeroToken);

            entidad.setToken(token8Digitos);

            // 5. El token expirará en 24 horas a partir de ahora
            LocalDateTime fechaExpiracion = LocalDateTime.now().plusMinutes(15);

            // 6. Insertamos el token en la tabla tokens_verificacion
            psToken = con.prepareStatement(sqlToken);
            psToken.setString(1, token8Digitos);
            psToken.setLong(2, idUsuarioGenerado); // Usamos la ID que recuperamos en el paso 3
            psToken.setTimestamp(3, Timestamp.valueOf(fechaExpiracion));
            psToken.executeUpdate();

            // 7. Si todo salió bien, confirmamos la transacción en la BD
            con.commit();

            // Opcional: Aquí puedes enviar el correo con el 'token8Digitos'
            System.out.println("Token de 8 dígitos generado para " + entidad.getCorreo() + ": " + token8Digitos);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // Si algo falló, revertimos todo para no dejar datos corruptos o incompletos
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            // Cerramos todos los recursos para liberar memoria en el orden correcto
            try {
                if (rsKeys != null) rsKeys.close();
                if (psUsuario != null) psUsuario.close();
                if (psToken != null) psToken.close();
                if (con != null) {
                    con.setAutoCommit(true); // Siempre regresamos la conexión a su estado normal
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /*public boolean create(User entidad) {
        String sql = "INSERT INTO DUENOS(nombre, correo, contrasena, maqueta, apellido) VALUES(?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getCorreo());
            ps.setString(4, entidad.getContrasena());
            ps.setString(5, entidad.getCarrera());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    */

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(Integer id) {
        return null;
    }

    @Override
    public boolean update(User entidad) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
    public boolean login(String correo, String contrasena) {
        // 1. Agregamos el filtro 'activo = 1' a la consulta SQL
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ? AND contrasena = ? AND activo = 1";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Validación de seguridad para prevenir el NullPointerException anterior
            if (correo == null || contrasena == null) {
                return false;
            }

            ps.setString(1, correo.trim());
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Solo devolverá true si coinciden los datos Y el usuario está activo (activo = 1)
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar realizar el login.");
            e.printStackTrace();
        }
        return false;
    }

    public int loginConEstado(String correo, String contrasena) {
        // Seleccionamos directamente la columna activo
        String sql = "SELECT activo FROM usuario WHERE correo = ? AND contrasena = ?";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (correo == null || contrasena == null) return 0; // Datos incorrectos

            ps.setString(1, correo.trim());
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int activo = rs.getInt("activo");
                    if (activo == 1) {
                        return 1; // 1 = Login exitoso y cuenta activa
                    } else {
                        return 2; // 2 = Credenciales correctas pero cuenta INACTIVA
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // 0 = Correo o contraseña incorrectos / Error
    }

    public boolean verificarTokenYActivarUsuario(String correo, String token) {
        // 1. Buscamos si el token coincide con el correo del usuario y no ha expirado
        String sqlBuscarToken =
                "SELECT t.id, u.id_usuario " +
                        "FROM tokens_verificacion t " +
                        "JOIN usuario u ON t.id_usuario = u.id_usuario " +
                        "WHERE t.token = ? AND u.correo = ? AND t.fecha_expiracion > CURRENT_TIMESTAMP";

        // 2. Activamos al usuario (asumiendo que tienes una columna 'activo' o 'estado')
        // Ajusta 'activo = 1' según cómo tengas definida esta columna en tu tabla 'usuario'
        String sqlActivarUsuario = "UPDATE usuario SET activo = 1 WHERE id_usuario = ?";

        // 3. Eliminamos el token para que no pueda ser reutilizado
        String sqlEliminarToken = "DELETE FROM tokens_verificacion WHERE id = ?";

        Connection con = null;
        PreparedStatement psBuscar = null;
        PreparedStatement psActivar = null;
        PreparedStatement psEliminar = null;
        ResultSet rs = null;

        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false); // Transacción para asegurar consistencia

            psBuscar = con.prepareStatement(sqlBuscarToken);
            psBuscar.setString(1, token);
            psBuscar.setString(2, correo);
            rs = psBuscar.executeQuery();

            if (rs.next()) {
                long tokenId = rs.getLong("id");
                long idUsuario = rs.getLong("id_usuario");

                // Paso A: Activar el usuario
                psActivar = con.prepareStatement(sqlActivarUsuario);
                psActivar.setLong(1, idUsuario);
                psActivar.executeUpdate();

                // Paso B: Eliminar el token usado
                psEliminar = con.prepareStatement(sqlEliminarToken);
                psEliminar.setLong(1, tokenId);
                psEliminar.executeUpdate();

                con.commit(); // Todo bien, guardamos cambios
                return true;
            } else {
                // Token inválido, expirado o correo incorrecto
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
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Guarda el código de recuperación en la BD (expira en 15 minutos)
    public boolean guardarCodigoRecuperacion(String correo, String codigo) {
        String sql = "UPDATE usuario SET codigo_recuperacion = ?, limite_recuperacion = ? WHERE correo = ?";
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

    // Valida que el código sea correcto y no haya expirado
    public boolean validarCodigoYObtenerUsuario(String correo, String codigo) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ? AND codigo_recuperacion = ? AND limite_recuperacion > CURRENT_TIMESTAMP";
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

    // Actualiza la contraseña y limpia el código de recuperación
    public boolean actualizarContrasena(String correo, String nuevaContra) {
        String sql = "UPDATE usuario SET contrasena = ?, codigo_recuperacion = NULL, limite_recuperacion = NULL WHERE correo = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevaContra);
            ps.setString(2, correo.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

