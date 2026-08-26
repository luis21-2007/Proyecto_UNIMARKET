package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.utils.SQLConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoriaDaoTest {

    private Connection conexionReal;
    private MockedStatic<SQLConnector> mockedConnector;
    private CategoriaDao categoriaDao;
    private int idAdminTest;

    @BeforeEach
    public void setUp() throws SQLException {
        // 1. Obtenemos la conexión real desde el pool
        conexionReal = SQLConnector.getConnection();
        conexionReal.setAutoCommit(false);

        // 2. PROXY NATIVO: Interceptamos commit(), close() y setAutoCommit()
        Connection proxyConexion = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("commit".equals(method.getName()) ||
                            "close".equals(method.getName()) ||
                            "setAutoCommit".equals(method.getName())) {
                        return null;
                    }
                    return method.invoke(conexionReal, args);
                }
        );

        // 3. Interceptamos SQLConnector
        mockedConnector = mockStatic(SQLConnector.class);
        mockedConnector.when(SQLConnector::getConnection).thenReturn(proxyConexion);

        categoriaDao = new CategoriaDao();

        // 4. Creamos un usuario admin auxiliar para satisfacer la FK (id_admin_creo)
        idAdminTest = crearAdminAuxiliar();
    }



    private int crearAdminAuxiliar() throws SQLException {
        String correoUnico = "cat_admin_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "@correo.com";
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena, carrera, telefono, rol, activo) " +
                "VALUES ('Admin', 'Prueba', ?, '12345', 'Desarrollo de software', 7773334455, 'ADMIN', 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"id_usuario"})) {
            ps.setString(1, correoUnico);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 1;
    }

    private int crearCategoriaAuxiliar(String nombreCat) throws SQLException {
        String sql = "INSERT INTO categoria (nombre_categoria, id_admin_creo, estado) VALUES (?, ?, 1)";

        try (PreparedStatement ps = conexionReal.prepareStatement(sql, new String[]{"id_categoria"})) {
            ps.setString(1, nombreCat);
            ps.setInt(2, idAdminTest);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }



    @Test
    public void testCreate() {
        Categoria cat = new Categoria();
        cat.setNombreCategoria("Electrónica " + System.currentTimeMillis());
        cat.setIdAdminCreo(idAdminTest);

        boolean creada = categoriaDao.create(cat);

        assertTrue(creada, "La categoría debería haberse registrado correctamente");
    }

    @Test
    public void testGetById() throws SQLException {
        String nombreUnico = "Hogar " + System.currentTimeMillis();
        int idCat = crearCategoriaAuxiliar(nombreUnico);

        Categoria obtenida = categoriaDao.getById(idCat);

        assertNotNull(obtenida, "Debería encontrar la categoría por su ID");
        assertEquals(nombreUnico, obtenida.getNombreCategoria());
        assertEquals(idAdminTest, obtenida.getIdAdminCreo());
    }

    @Test
    public void testGetAll() throws SQLException {
        String nombreUnico = "Libros " + System.currentTimeMillis();
        crearCategoriaAuxiliar(nombreUnico);

        List<Categoria> lista = categoriaDao.getAll();

        assertNotNull(lista, "La lista no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener al menos la categoría activa creada");
    }

    @Test
    public void testGetAllAdmin() throws SQLException {
        String nombreUnico = "Ropa " + System.currentTimeMillis();
        crearCategoriaAuxiliar(nombreUnico);

        List<Categoria> lista = categoriaDao.getAllAdmin();

        assertNotNull(lista, "La lista de admin no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista de admin debe traer categorías y el conteo de productos");
    }



    @Test
    public void testExisteNombre() throws SQLException {
        String nombreUnico = "Deportes " + System.currentTimeMillis();
        crearCategoriaAuxiliar(nombreUnico);

        assertTrue(categoriaDao.existeNombre(nombreUnico), "Debería retornar true si el nombre existe");
        assertTrue(categoriaDao.existeNombre(nombreUnico.toLowerCase()), "Debería validar sin importar mayúsculas/minúsculas");
        assertFalse(categoriaDao.existeNombre("Inexistente_" + System.currentTimeMillis()), "Debería retornar false si no existe");
    }

    @Test
    public void testExisteNombreExcluyendoId() throws SQLException {
        String nombreUnico = "Juguetes " + System.currentTimeMillis();
        int idCat = crearCategoriaAuxiliar(nombreUnico);

        // Si consultamos con su propio ID debe retornar false (se excluye a sí misma)
        assertFalse(categoriaDao.existeNombreExcluyendoId(nombreUnico, idCat));

        // Si consultamos con otro ID inexistente (ej. 0) debe retornar true (ya existe el nombre)
        assertTrue(categoriaDao.existeNombreExcluyendoId(nombreUnico, 0));
    }


    @Test
    public void testUpdate() throws SQLException {
        int idCat = crearCategoriaAuxiliar("Calzado " + System.currentTimeMillis());

        Categoria catEdit = new Categoria();
        catEdit.setIdCategoria(idCat);
        catEdit.setNombreCategoria("Calzado Deportivo " + System.currentTimeMillis());
        catEdit.setIdAdminModifico(idAdminTest);

        boolean actualizada = categoriaDao.update(catEdit);

        assertTrue(actualizada, "Debería actualizar la categoría exitosamente");
        Categoria consultada = categoriaDao.getById(idCat);
        assertEquals(catEdit.getNombreCategoria(), consultada.getNombreCategoria());
        assertEquals(idAdminTest, consultada.getIdAdminModifico());
    }

    @Test
    public void testDelete() throws SQLException {
        int idCat = crearCategoriaAuxiliar("Herramientas " + System.currentTimeMillis());

        boolean eliminada = categoriaDao.delete(idCat);

        assertTrue(eliminada, "La baja lógica debería retornar true");
    }

    @Test
    public void testActivarYDesactivar() throws SQLException {
        int idCat = crearCategoriaAuxiliar("Mascotas " + System.currentTimeMillis());

        // Probar Desactivación
        boolean desactivada = categoriaDao.desactivar(idCat);
        assertTrue(desactivada, "Debería desactivar la categoría");

        // Probar Activación
        boolean activada = categoriaDao.activar(idCat);
        assertTrue(activada, "Debería activar la categoría nuevamente");
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