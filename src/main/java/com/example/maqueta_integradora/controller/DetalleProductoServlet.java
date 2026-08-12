package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CalificacionDao;
import com.example.maqueta_integradora.model.dao.OfertaDao;
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
    private OfertaDao ofertaDao;
    private CalificacionDao calificacionDao; // <-- DAO DE CALIFICACIONES

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
        userDao = new UserDao();
        ofertaDao = new OfertaDao();
        calificacionDao = new CalificacionDao(); // <-- INICIALIZACIÓN
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
                User vendedor = userDao.getById(producto.getIdUsuario());

                // 5. Consultar estado de la oferta del usuario en sesión
                int estadoOferta = ofertaDao.getEstadoOfertaUsuario(usuario.getId(), idProducto);

                // 6. Consultar reputación y calificaciones del vendedor
                int idVendedor = producto.getIdUsuario();
                double promedioVendedor = calificacionDao.obtenerPromedioCalificaciones(idVendedor);
                int totalResenasVendedor = calificacionDao.obtenerResenasPorVendedor(idVendedor).size();

                // 7. Enviar datos al JSP
                request.setAttribute("producto", producto);
                request.setAttribute("listaImagenes", listaImagenes);
                request.setAttribute("vendedor", vendedor);
                request.setAttribute("estadoOferta", estadoOferta);
                request.setAttribute("promedioVendedor", promedioVendedor);       // <-- NUEVO ATRIBUTO
                request.setAttribute("totalResenasVendedor", totalResenasVendedor); // <-- NUEVO ATRIBUTO

                request.getRequestDispatcher("DetalleProducto.jsp").forward(request, response);
            } else {
                response.sendRedirect("inicio");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("inicio");
        }
    }
}