package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;

@WebServlet("/eliminarImagenProducto")
public class EliminarImagenProductoServlet extends HttpServlet {

    private ProductoDao productoDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión del usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Recuperar parámetros enviados por la "X" del JSP
        String idProductoStr = request.getParameter("idProducto");
        String imagenUrl = request.getParameter("imagenUrl");

        if (idProductoStr != null && imagenUrl != null && !imagenUrl.trim().isEmpty()) {
            try {
                int idProducto = Integer.parseInt(idProductoStr);

                // 3. Eliminar el registro en la Base de Datos
                boolean eliminadoBD = productoDao.eliminarImagenEspecifica(idProducto, imagenUrl);

                if (eliminadoBD) {
                    // 4. Borrar el archivo físico en el servidor de forma segura
                    String absolutePath = getServletContext().getRealPath(imagenUrl);
                    if (absolutePath != null) {
                        File file = new File(absolutePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }

                    // 5. Redirigir de nuevo a la vista de detalle/edición
                    response.sendRedirect("detallemiProducto?id=" + idProducto + "&msg=imagenEliminada");
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("ID de producto inválido: " + idProductoStr);
            }
        }

        // Redirección si falla algún parámetro o el borrado en BD
        response.sendRedirect("misProductos?msg=errorEliminarImagen");
    }
}