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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User admin = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (admin == null || !"ADMIN".equalsIgnoreCase(admin.getRol())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if ("create".equalsIgnoreCase(action)) {
            crearCategoriaJson(request, response, admin.getId());
        } else if ("update".equalsIgnoreCase(action)) {
            actualizarCategoriaJson(request, response, admin.getId());
        } else if ("desactivar".equalsIgnoreCase(action)) {
            desactivarCategoriaJson(request, response);
        } else if ("activar".equalsIgnoreCase(action)) {
            activarCategoriaJson(request, response);
        }
    }

// MÉTODOS AUXILIARES NATIVOS (Sin Gson ni librerías)

    private void crearCategoriaJson(HttpServletRequest request, HttpServletResponse response, int idAdmin) throws IOException {
        String nombre = request.getParameter("nombreCategoria");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.getWriter().write("{\"success\": false, \"message\": \"El nombre no puede estar vacío\"}");
            return;
        }

        nombre = nombre.trim();
        if (categoriaDao.existeNombre(nombre)) {
            response.getWriter().write("{\"success\": false, \"message\": \"La categoría ya existe\"}");
            return;
        }

        Categoria nueva = new Categoria();
        nueva.setNombreCategoria(nombre);
        nueva.setIdAdminCreo(idAdmin);

        boolean creada = categoriaDao.create(nueva);
        response.getWriter().write("{\"success\": " + creada + ", \"message\": \"" + (creada ? "Creada con éxito" : "Error al crear") + "\"}");
    }

    private void actualizarCategoriaJson(HttpServletRequest request, HttpServletResponse response, int idAdmin) throws IOException {
        String idStr = request.getParameter("categoriaId");
        String nombre = request.getParameter("nombre");

        try {
            int idCategoria = Integer.parseInt(idStr);

            if (categoriaDao.existeNombreExcluyendoId(nombre, idCategoria)) {
                response.getWriter().write("{\"success\": false, \"message\": \"El nombre ya está en uso\"}");
                return;
            }

            Categoria cat = new Categoria();
            cat.setIdCategoria(idCategoria);
            cat.setNombreCategoria(nombre);
            cat.setIdAdminModifico(idAdmin);

            boolean actualizada = categoriaDao.update(cat);
            response.getWriter().write("{\"success\": " + actualizada + ", \"message\": \"" + (actualizada ? "Actualizada" : "Error al actualizar") + "\"}");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID inválido\"}");
        }
    }

    private void desactivarCategoriaJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean ok = categoriaDao.desactivar(id);
            response.getWriter().write("{\"success\": " + ok + ", \"message\": \"" + (ok ? "Desactivada" : "Error") + "\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Error al desactivar\"}");
        }
    }

    private void activarCategoriaJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean ok = categoriaDao.activar(id);
            response.getWriter().write("{\"success\": " + ok + ", \"message\": \"" + (ok ? "Activada" : "Error") + "\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Error al activar\"}");
        }
    }
}