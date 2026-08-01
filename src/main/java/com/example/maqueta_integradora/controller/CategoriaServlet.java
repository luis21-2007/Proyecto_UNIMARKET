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

    // GET: Cargar la lista de categorías o preparar formularios
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
        if (action == null) action = "list";

        switch (action) {
            case "delete":
                eliminarCategoria(request, response);
                break;
            case "list":
            default:
                listarCategorias(request, response);
                break;
        }
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
        List<Categoria> listaCategorias = categoriaDao.getAll();
        request.setAttribute("listaCategorias", listaCategorias);
        request.getRequestDispatcher("gestionCategorias.jsp").forward(request, response);
    }

    private void crearCategoria(HttpServletRequest request, HttpServletResponse response, int idAdmin)
            throws IOException, ServletException {

        String nombre = request.getParameter("nombreCategoria");

        // 1. Validar que el campo no esté vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            request.setAttribute("error", "El nombre de la categoría no puede estar vacío.");
            request.getRequestDispatcher("agregarCategoria.jsp").forward(request, response);
            return;
        }

        nombre = nombre.trim(); // Limpiamos espacios al inicio y al final

        // 2. Validar si la categoría ya existe
        if (categoriaDao.existeNombre(nombre)) {
            request.setAttribute("error", "La categoría '" + nombre + "' ya existe.");
            request.setAttribute("nombreCategoria", nombre); // Mantiene el texto en el input
            request.getRequestDispatcher("agregarCategoria.jsp").forward(request, response);
            return;
        }

        // 3. Instanciar y asignar datos
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombreCategoria(nombre);
        nuevaCategoria.setIdAdminCreo(idAdmin); // Usamos directamente el parámetro que recibió la función

        // 4. Guardar en la base de datos
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

    private void eliminarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");

        try {
            int idCategoria = Integer.parseInt(idStr);
            boolean eliminada = categoriaDao.delete(idCategoria);

            if (eliminada) {
                response.sendRedirect("categorias?msg=eliminada");
            } else {
                response.sendRedirect("categorias?error=no_eliminada");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("categorias?error=id_invalido");
        }
    }
}