package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CalificacionDao;
import com.example.maqueta_integradora.model.dao.OfertaDao;
import com.example.maqueta_integradora.model.dao.ProductoDao;
import com.example.maqueta_integradora.model.dao.TransaccionDao;
import com.example.maqueta_integradora.model.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/detalleProducto", "/comprarProducto"})
public class DetalleProductoServlet extends HttpServlet {

    private ProductoDao productoDao;
    private UserDao userDao;
    private OfertaDao ofertaDao;
    private CalificacionDao calificacionDao;
    private TransaccionDao transaccionDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
        userDao = new UserDao();
        ofertaDao = new OfertaDao();
        calificacionDao = new CalificacionDao();
        transaccionDao = new TransaccionDao();
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

        // 2. Obtener ID del producto
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            response.sendRedirect("inicio");
            return;
        }

        try {
            int idProducto = Integer.parseInt(idStr);

            // 3. Obtener Producto e Imágenes
            Producto producto = productoDao.getById(idProducto);
            List<String> listaImagenes = productoDao.getImagenesByProductoId(idProducto);

            if (producto != null) {
                // 4. Datos del vendedor
                User vendedor = userDao.getById(producto.getIdUsuario());

                // 5. Estado de Oferta del Usuario (1 = Aceptada, 0 = Pendiente, etc.)
                int estadoOferta = ofertaDao.getEstadoOfertaUsuario(usuario.getId(), idProducto);

                // 6. Validar si ya existe una Transacción / Compra directa de este usuario para este producto
                boolean yaComproDirecto = transaccionDao.getComprasByUsuario(usuario.getId())
                        .stream()
                        .anyMatch(t -> t.getIdProducto() == idProducto);

                // 7. Reputación del vendedor
                int idVendedor = producto.getIdUsuario();
                double promedioVendedor = calificacionDao.obtenerPromedioCalificaciones(idVendedor);
                int totalResenasVendedor = calificacionDao.obtenerResenasPorVendedor(idVendedor).size();

                // 8. Atributos a la vista
                request.setAttribute("producto", producto);
                request.setAttribute("listaImagenes", listaImagenes);
                request.setAttribute("vendedor", vendedor);
                request.setAttribute("estadoOferta", estadoOferta);
                request.setAttribute("yaComproDirecto", yaComproDirecto);
                request.setAttribute("promedioVendedor", promedioVendedor);
                request.setAttribute("totalResenasVendedor", totalResenasVendedor);

                request.getRequestDispatcher("DetalleProducto.jsp").forward(request, response);
            } else {
                response.sendRedirect("inicio");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("inicio");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar Sesión
        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Parámetro de Producto
        String idProductoStr = request.getParameter("idProducto");
        if (idProductoStr == null || idProductoStr.isBlank()) {
            response.sendRedirect("inicio");
            return;
        }

        try {
            int idProducto = Integer.parseInt(idProductoStr);
            Producto producto = productoDao.getById(idProducto);

            if (producto == null) {
                response.sendRedirect("inicio");
                return;
            }

            // Evitar auto-compra
            if (usuario.getId() == producto.getIdUsuario()) {
                response.sendRedirect("detalleProducto?id=" + idProducto + "&error=auto_oferta");
                return;
            }

            // Registrar compra en estado pendiente (0)
            boolean registrado = transaccionDao.registrarCompraDirectaPendiente(
                    usuario.getId(),
                    producto.getIdUsuario(),
                    idProducto,
                    producto.getPrecio()
            );

            if (registrado) {
                response.sendRedirect("detalleProducto?id=" + idProducto + "&msg=compraExitosa");
            } else {
                response.sendRedirect("detalleProducto?id=" + idProducto + "&error=compra_fallida");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("inicio");
        }
    }
}