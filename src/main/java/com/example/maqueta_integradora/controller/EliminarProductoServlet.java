package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/eliminarProducto")
public class EliminarProductoServlet extends HttpServlet {

    private ProductoDao productoDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User usuario = (User) session.getAttribute("usuario");
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("misProductos");
            return;
        }
        int idProducto = 0;
        try {
            idProducto = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("misProductos");
            return;
        }
        Producto producto = productoDao.getById(idProducto);
        if (producto == null || producto.getIdUsuario() != usuario.getId()) {
            response.sendRedirect("misProductos");
            return;
        }
        boolean eliminado = productoDao.delete(idProducto);
        if (eliminado) {
            response.sendRedirect("misProductos?msg=eliminadoExitoso");
        } else {
            response.sendRedirect("detallemiProducto?id=" + idProducto + "&error=errorEliminacion");
        }
    }
}