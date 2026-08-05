package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CategoriaDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoriaServlet", value = "/categorias")
public class CategoriaServlet extends HttpServlet {

    private final CategoriaDao categoriaDao = new CategoriaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión de Admin
        HttpSession session = request.getSession(false);
        User admin = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (admin == null || !"ADMIN".equalsIgnoreCase(admin.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        // Procesar cambios de estado
        if (action != null && idStr != null) {
            try {
                int idCategoria = Integer.parseInt(idStr);
                boolean resultado = false;

                if ("desactivar".equals(action)) {
                    resultado = categoriaDao.desactivar(idCategoria);
                    if (resultado) {
                        response.sendRedirect("categorias?msg=desactivada");
                        return;
                    }
                } else if ("activar".equals(action)) {
                    resultado = categoriaDao.activar(idCategoria);
                    if (resultado) {
                        response.sendRedirect("categorias?msg=activada");
                        return;
                    }
                }

                if (!resultado) {
                    response.sendRedirect("categorias?error=true");
                    return;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("categorias?error=true");
                return;
            }
        }

        // Cargar lista completa (activas e inactivas) para la gestión
        List<Categoria> listaCategorias = categoriaDao.getAllAdmin();
        request.setAttribute("listaCategorias", listaCategorias);

        request.getRequestDispatcher("gestionCategorias.jsp").forward(request, response);
    }

    // POST: Procesar formularios de creación y edición
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión de Admin
        HttpSession session = request.getSession(false);
        User admin = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (admin == null || !"ADMIN".equalsIgnoreCase(admin.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equalsIgnoreCase(action)) {
            crearCategoria(request, response, admin.getId());
        } else if ("update".equalsIgnoreCase(action)) {
            actualizarCategoria(request, response, admin.getId());
        } else {
            response.sendRedirect("categorias");
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private void listarCategorias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Categoria> listaCategorias = categoriaDao.getAllAdmin();
        request.setAttribute("listaCategorias", listaCategorias);
        request.getRequestDispatcher("gestionCategorias.jsp").forward(request, response);
    }

    private void crearCategoria(HttpServletRequest request, HttpServletResponse response, int idAdmin)
            throws IOException, ServletException {

        String nombre = request.getParameter("nombreCategoria");

        if (nombre == null || nombre.trim().isEmpty()) {
            request.setAttribute("error", "El nombre de la categoría no puede estar vacío.");
            request.getRequestDispatcher("agregarCategoria.jsp").forward(request, response);
            return;
        }

        nombre = nombre.trim();

        if (categoriaDao.existeNombre(nombre)) {
            request.setAttribute("error", "La categoría '" + nombre + "' ya existe.");
            request.setAttribute("nombreCategoria", nombre);
            request.getRequestDispatcher("agregarCategoria.jsp").forward(request, response);
            return;
        }

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombreCategoria(nombre);
        nuevaCategoria.setIdAdminCreo(idAdmin);

        boolean creada = categoriaDao.create(nuevaCategoria);

        if (creada) {
            response.sendRedirect("categorias?msg=creada");
        } else {
            request.setAttribute("error", "Ocurrió un error al guardar la categoría en la base de datos.");
            request.setAttribute("nombreCategoria", nombre);
            request.getRequestDispatcher("agregarCategoria.jsp").forward(request, response);
        }
    }

    private void actualizarCategoria(HttpServletRequest request, HttpServletResponse response, int idAdmin)
            throws IOException {
        String idStr = request.getParameter("categoriaId");
        String nombre = request.getParameter("nombre");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendRedirect("categorias?error=nombre_vacio");
            return;
        }

        try {
            int idCategoria = Integer.parseInt(idStr);

            if (categoriaDao.existeNombreExcluyendoId(nombre, idCategoria)) {
                response.sendRedirect("categorias?error=nombre_duplicado");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoria);
            categoria.setNombreCategoria(nombre);
            categoria.setIdAdminModifico(idAdmin);

            boolean actualizada = categoriaDao.update(categoria);

            if (actualizada) {
                response.sendRedirect("categorias?msg=actualizada");
            } else {
                response.sendRedirect("categorias?error=no_actualizada");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("categorias?error=id_invalido");
        }
    }

    private void desactivarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");

        try {
            int idCategoria = Integer.parseInt(idStr);
            boolean desactivada = categoriaDao.desactivar(idCategoria); // O categoriaDao.delete(idCategoria); según tu DAO

            if (desactivada) {
                response.sendRedirect("categorias?msg=desactivada");
            } else {
                response.sendRedirect("categorias?error=no_desactivada");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("categorias?error=id_invalido");
        }
    }

    private void activarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");

        try {
            int idCategoria = Integer.parseInt(idStr);
            boolean activada = categoriaDao.activar(idCategoria);

            if (activada) {
                response.sendRedirect("categorias?msg=activada");
            } else {
                response.sendRedirect("categorias?error=no_activada");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("categorias?error=id_invalido");
        }
    }
}