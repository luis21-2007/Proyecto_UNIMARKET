package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.utils.SQLConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private UserDao userDao;

    @BeforeEach

    public void setUp() throws SQLException {
        // 1. Obtenemos la conexión real desde HikariCP
        conexionReal = SQLConnector.getConnection();
        conexionReal.setAutoCommit(false);

        // 2. PROXY NATIVO: Bloqueamos commit(), close(), setAutoCommit() Y ROLLBACK
        // Evita que los métodos transaccionales del DAO borren los datos temporales
        Connection proxyConexion = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("commit".equals(method.getName()) ||
                            "close".equals(method.getName()) ||
                            "setAutoCommit".equals(method.getName()) ||
                            "rollback".equals(method.getName())) {
                        return null;
                    }
                    return method.invoke(conexionReal, args);
                }
        );

        // 3. Interceptamos SQLConnector
        mockedConnector = mockStatic(SQLConnector.class);
        mockedConnector.when(SQLConnector::getConnection).thenReturn(proxyConexion);

        userDao = new UserDao();
    }

    // ==========================================
    // MÉTODO AUXILIAR CON CAMPOS OBLIGATORIOS
    // ==========================================
    private User crearUsuarioAuxiliar() {
        String correoUnico = "test_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
        User u = new User();
        u.setNombre("Carlos");
        u.setApellido("Pérez");
        u.setCorreo(correoUnico);
        u.setContrasena("clave123");
        u.setCarrera("Desarrollo de software");
        u.setTelefono(7771234567L);
        u.setRol("usuario");
        u.setActivo(1);

        userDao.create(u);
        return userDao.obtenerPorCorreo(correoUnico);
    }

    @Test
    public void testCrearUsuario() {
        String correoUnico = "prueba_" + System.currentTimeMillis() + "@correo.com";

        User nuevoUsuario = new User();
        nuevoUsuario.setNombre("Prueba");
        nuevoUsuario.setApellido("Unitaria");
        nuevoUsuario.setCorreo(correoUnico);
        nuevoUsuario.setContrasena("12345");
        nuevoUsuario.setCarrera("Desarrollo de software");
        nuevoUsuario.setTelefono(1234567890L);
        nuevoUsuario.setRol("USUARIO");

        boolean resultado = userDao.create(nuevoUsuario);

        assertTrue(resultado, "El usuario debería haberse insertado temporalmente");
    }


    @Test
    public void testGetAll() {
        crearUsuarioAuxiliar();
        List<User> lista = userDao.getAll();

        assertNotNull(lista, "La lista de usuarios no debería ser nula");
        assertTrue(lista.size() > 0, "La lista debería contener al menos un usuario");
    }

    @Test
    public void testGetById() {
        User creado = crearUsuarioAuxiliar();

        User encontrado = userDao.getById(creado.getId());

        assertNotNull(encontrado, "Debería encontrar al usuario por su ID");
        assertEquals(creado.getCorreo(), encontrado.getCorreo(), "El correo debería coincidir");
    }

    @Test
    public void testObtenerPorCorreo() {
        User creado = crearUsuarioAuxiliar();

        User encontrado = userDao.obtenerPorCorreo(creado.getCorreo());

        assertNotNull(encontrado, "Debería encontrar al usuario por su correo");
        assertEquals(creado.getId(), encontrado.getId(), "Los IDs deberían coincidir");
    }

    @Test
    public void testExisteCorreo() {
        User creado = crearUsuarioAuxiliar();

        assertTrue(userDao.existeCorreo(creado.getCorreo()), "Debería retornar true si el correo existe");
        assertFalse(userDao.existeCorreo("falso_" + System.currentTimeMillis() + "@correo.com"), "Debería retornar false si no existe");
    }

    @Test
    public void testLogin() {
        String correo = "login_" + System.currentTimeMillis() + "@correo.com";
        User u = new User();
        u.setNombre("LoginTest");
        u.setApellido("Prueba");
        u.setCorreo(correo);
        u.setContrasena("miPassword123");
        u.setCarrera("Sistemas");
        u.setTelefono(7771234567L);
        u.setRol("COMPRADOR");
        u.setActivo(1);

        userDao.create(u);

        User creado = userDao.obtenerPorCorreo(correo);
        assertNotNull(creado, "El usuario creado para el login no debe ser nulo");
        userDao.activarUsuario(creado.getId());

        assertTrue(userDao.login(correo, "miPassword123"), "El login debe ser exitoso con credenciales correctas");
        assertFalse(userDao.login(correo, "claveErronea"), "El login debe fallar con contraseña incorrecta");
    }

    // ==========================================
    // PRUEBAS DE ACTUALIZACIÓN Y MODIFICACIÓN
    // ==========================================

    @Test
    public void testUpdate() {
        User creado = crearUsuarioAuxiliar();
        creado.setNombre("NombreModificado");
        creado.setApellido("ApellidoModificado");
        creado.setRol("ADMIN");
        creado.setActivo(1);

        boolean modificado = userDao.update(creado);

        assertTrue(modificado, "El método update debería retornar true");
        User actualizado = userDao.getById(creado.getId());
        assertEquals("NombreModificado", actualizado.getNombre());
    }

    @Test
    public void testUpdatePerfil() {
        User creado = crearUsuarioAuxiliar();

        boolean resultado = userDao.updatePerfil(creado.getId(), "NuevoNombrePerfil", 9998887766L);

        assertTrue(resultado, "Debería actualizar el perfil correctamente");
        User actualizado = userDao.getById(creado.getId());
        assertEquals("NuevoNombrePerfil", actualizado.getNombre());
        assertEquals(9998887766L, actualizado.getTelefono());
    }

    @Test
    public void testActualizarSesionActiva() {
        User creado = crearUsuarioAuxiliar();

        boolean resultado = userDao.actualizarSesionActiva(creado.getId(), 1);

        assertTrue(resultado, "Debería actualizar la sesión activa");
        User actualizado = userDao.obtenerPorCorreo(creado.getCorreo());
        assertEquals(1, actualizado.getSesionActiva());
    }

    // ==========================================
    // PRUEBAS DE ESTADOS (ACTIVAR / DESACTIVAR / DAR DE BAJA)
    // ==========================================

    @Test
    public void testActivarYDesactivarUsuario() {
        User creado = crearUsuarioAuxiliar();

        // Probar activación
        boolean activado = userDao.activarUsuario(creado.getId());
        assertTrue(activado, "Debería activar al usuario");

        // Probar desactivación
        boolean desactivado = userDao.desactivarUsuario(creado.getId());
        assertTrue(desactivado, "Debería desactivar al usuario y sus productos");
    }

    @Test
    public void testDarDeBaja() {
        User creado = crearUsuarioAuxiliar();

        boolean resultado = userDao.darDeBaja(creado.getId());

        assertTrue(resultado, "Dar de baja debería retornar true");
    }

    @Test
    public void testDelete() {
        assertFalse(userDao.delete(1), "El método delete actualmente no está implementado y retorna false");
    }

    // ==========================================
    // PRUEBAS DE SEGURIDAD (RECUPERACIÓN Y TOKENS)
    // ==========================================

    @Test
    public void testGuardarYValidarCodigoRecuperacion() {
        User creado = crearUsuarioAuxiliar();
        String codigo = "9988";

        boolean guardado = userDao.guardarCodigoRecuperacion(creado.getCorreo(), codigo);
        assertTrue(guardado, "Debería guardar el código de recuperación");

        boolean valido = userDao.validarCodigoYObtenerUsuario(creado.getCorreo(), codigo);
        assertTrue(valido, "El código debería ser válido antes de los 15 minutos");
    }

    @Test
    public void testActualizarContrasena() {
        User creado = crearUsuarioAuxiliar();
        String nuevaClave = "nuevaClave456";

        boolean actualizada = userDao.actualizarContrasena(creado.getCorreo(), nuevaClave);
        assertTrue(actualizada, "Debería actualizar la contraseña con hash");

        userDao.activarUsuario(creado.getId());
        assertTrue(userDao.login(creado.getCorreo(), nuevaClave), "Debería hacer login con la nueva contraseña");
    }

    @Test
    public void testActualizarTokenYVerificar() {
        User creado = crearUsuarioAuxiliar();
        String nuevoToken = "87654321";

        boolean tokenActualizado = userDao.actualizarTokenPorCorreo(creado.getCorreo(), nuevoToken);
        assertTrue(tokenActualizado, "Debería actualizar el token en tokens_verificacion");

        boolean verificado = userDao.verificarTokenYActivarUsuario(creado.getCorreo(), nuevoToken);
        assertTrue(verificado, "Debería validar el token y activar al usuario");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (mockedConnector != null) {
            mockedConnector.close();
        }

        if (conexionReal != null && !conexionReal.isClosed()) {
            conexionReal.rollback();
            conexionReal.close();
        }
    }
}