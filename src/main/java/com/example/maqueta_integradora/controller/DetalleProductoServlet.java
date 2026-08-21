package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.Transaccion;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CalificacionDao;
import com.example.maqueta_integradora.model.dao.OfertaDao;
import com.example.maqueta_integradora.model.dao.ProductoDao;
import com.example.maqueta_integradora.model.dao.ReporteDao;
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
    private ReporteDao reporteDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
        userDao = new UserDao();
        ofertaDao = new OfertaDao();
        calificacionDao = new CalificacionDao();
        transaccionDao = new TransaccionDao();
        reporteDao = new ReporteDao();
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
                // 4. Datos del vendedor y verificación de autoría
                User vendedor = userDao.getById(producto.getIdUsuario());
                boolean esDuenoProducto = (usuario.getId() == producto.getIdUsuario());

                // 5. Estado de Oferta del Usuario (1 = Aceptada, 0 = Pendiente, etc.)
                int estadoOferta = ofertaDao.getEstadoOfertaUsuario(usuario.getId(), idProducto);

                // 6. Validar Transacciones/Compras directas del usuario filtrando por estado
                List<Transaccion> misCompras = transaccionDao.getComprasByUsuario(usuario.getId());

                // Se considera compra activa solo si NO está cancelada (estado != 0)
                boolean yaComproDirecto = misCompras.stream()
                        .anyMatch(t -> t.getIdProducto() == idProducto && t.getEstado() != 0);

                // Detecta si la transacción previa fue cancelada por el vendedor (estado == 0)
                boolean transaccionCancelada = misCompras.stream()
                        .filter(t -> t.getIdProducto() == idProducto)
                        .anyMatch(t -> t.getEstado() == 0) && !yaComproDirecto;

                // 7. Reputación del vendedor
                int idVendedor = producto.getIdUsuario();
                double promedioVendedor = calificacionDao.obtenerPromedioCalificaciones(idVendedor);
                int totalResenasVendedor = calificacionDao.obtenerResenasPorVendedor(idVendedor).size();

                // 8. Cantidad de reportes sancionados y verificación de reporte previo al VENDEDOR
                int cantidadReportesSancionados = reporteDao.obtenerCantidadSancionesPorUsuario(idVendedor);

                // NUEVO: Validar si el usuario actual ya reportó previamente a este vendedor
                boolean yaReportoVendedor = reporteDao.yaReportoVendedor(usuario.getId(), idVendedor);

                // 9. Atributos a la vista
                request.setAttribute("producto", producto);
                request.setAttribute("listaImagenes", listaImagenes);
                request.setAttribute("vendedor", vendedor);
                request.setAttribute("esDuenoProducto", esDuenoProducto);
                request.setAttribute("estadoOferta", estadoOferta);
                request.setAttribute("yaComproDirecto", yaComproDirecto);
                request.setAttribute("transaccionCancelada", transaccionCancelada);
                request.setAttribute("promedioVendedor", promedioVendedor);
                request.setAttribute("totalResenasVendedor", totalResenasVendedor);
                request.setAttribute("cantidadReportesSancionados", cantidadReportesSancionados);

                // Atributo enviado al JSP para ocultar/deshabilitar el botón de reportar
                request.setAttribute("yaReportoVendedor", yaReportoVendedor);

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

        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

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

            // 1. Evitar auto-compra
            if (usuario.getId() == producto.getIdUsuario()) {
                response.sendRedirect("detalleProducto?id=" + idProducto + "&error=auto_oferta");
                return;
            }

            // 2. Procesar compra con bloqueo pesimista atómico
            boolean registrado = productoDao.procesarCompraDirecta(
                    idProducto,
                    usuario.getId(),
                    producto.getIdUsuario(),
                    producto.getPrecio()
            );

            // 3. Responder según el resultado de la transacción
            if (registrado) {
                response.sendRedirect("detalleProducto?id=" + idProducto + "&msg=compraExitosa");
            } else {
                // Si el producto ya fue ganado por otro hilo o ya no está disponible
                response.sendRedirect("detalleProducto?id=" + idProducto + "&error=ya_vendido");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("inicio");
        }
    }
}