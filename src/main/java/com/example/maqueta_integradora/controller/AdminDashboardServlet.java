package com.example.maqueta_integradora.controller;

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

        // 1. Obtener los conteos desde la base de datos
        int totalUsuarios = userDao.getAll().size();     // O usa userDao.contarUsuarios() si lo creaste
        int totalProductos = productoDao.getAll().size(); // O usa productoDao.contarProductos() si lo creaste

        // 2. Pasar las variables a la vista (JSP)
        request.setAttribute("totalUsuarios", totalUsuarios);
        request.setAttribute("totalProductos", totalProductos);

        // 3. Redirigir la petición al JSP de la vista de administrador
        request.getRequestDispatcher("index_admin.jsp").forward(request, response);
    }
}