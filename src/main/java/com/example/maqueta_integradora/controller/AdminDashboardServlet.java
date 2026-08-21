package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ProductoDao;
import com.example.maqueta_integradora.model.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {

    private UserDao userDao;
    private ProductoDao productoDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
        productoDao = new ProductoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar Sesión y Rol de Administrador
        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        // Si no está logueado O su rol no es 1 (Admin), se deniega el acceso
        if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Obtener los conteos desde la base de datos
        int totalUsuarios = userDao.getAll().size();
        int totalProductos = productoDao.getAll().size();

        // 3. Pasar las variables a la vista (JSP)
        request.setAttribute("totalUsuarios", totalUsuarios);
        request.setAttribute("totalProductos", totalProductos);

        // 4. Redirigir la petición al JSP de administrador
        request.getRequestDispatcher("index_admin.jsp").forward(request, response);
    }
}