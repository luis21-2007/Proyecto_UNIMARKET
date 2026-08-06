package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/gestionProductos")
public class GestionProductosServlet extends HttpServlet {

    private ProductoDao productoDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar Sesión
        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Procesar acción si vienen parámetros
        String idStr = request.getParameter("id");
        String accion = request.getParameter("accion");

        if (idStr != null && accion != null) {
            try {
                int idProducto = Integer.parseInt(idStr);
                boolean exito = false;

                if (accion.equalsIgnoreCase("activar")) {
                    exito = productoDao.activar(idProducto);
                } else if (accion.equalsIgnoreCase("desactivar")) {
                    exito = productoDao.desactivar(idProducto);
                }

                if (exito) {
                    response.sendRedirect("gestionProductos?msg=" + (accion.equalsIgnoreCase("activar") ? "activado" : "desactivado"));
                } else {
                    response.sendRedirect("gestionProductos?error=1");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("gestionProductos?error=1");
            }
            return;
        }

        // 3. Cargar la lista completa
        List<Producto> listaProductos = productoDao.getAllAdmin();
        request.setAttribute("listaProductos", listaProductos);
        request.getRequestDispatcher("GestionProductos.jsp").forward(request, response);
    }
}