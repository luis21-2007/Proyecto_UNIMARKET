package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ProductoDao;
import com.example.maqueta_integradora.model.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/detalleProducto")
public class DetalleProductoServlet extends HttpServlet {

    private ProductoDao productoDao;
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
        userDao = new UserDao();
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

        // 2. Obtener el ID del producto
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            response.sendRedirect("inicio");
            return;
        }

        try {
            int idProducto = Integer.parseInt(idStr);

            // 3. Obtener Producto y su lista de fotos
            Producto producto = productoDao.getById(idProducto);
            List<String> listaImagenes = productoDao.getImagenesByProductoId(idProducto);

            if (producto != null) {
                // 4. Consultar los datos del usuario vendedor
                User vendedor = userDao.getById(producto.getIdUsuario()); // O el método que use tu UserDao para buscar por ID

                // 5. Enviar producto, imágenes y vendedor al JSP
                request.setAttribute("producto", producto);
                request.setAttribute("listaImagenes", listaImagenes);
                request.setAttribute("vendedor", vendedor);

                request.getRequestDispatcher("DetalleProducto.jsp").forward(request, response);
            } else {
                response.sendRedirect("inicio");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("inicio");
        }
    }
}