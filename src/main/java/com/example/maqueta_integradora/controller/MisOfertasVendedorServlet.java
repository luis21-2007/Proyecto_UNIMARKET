package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Oferta;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.OfertaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/misOfertasVendedor")
public class MisOfertasVendedorServlet extends HttpServlet {

    private OfertaDao ofertaDao;

    @Override
    public void init() throws ServletException {
        ofertaDao = new OfertaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Oferta> listaOfertasRecibidas = ofertaDao.getOfertasByVendedor(usuario.getId());
        request.setAttribute("listaOfertasRecibidas", listaOfertasRecibidas);
        request.getRequestDispatcher("misOfertasVendedor.jsp").forward(request, response);
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

        try {
            String accion = request.getParameter("accion");
            int idOferta = Integer.parseInt(request.getParameter("idOferta"));
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            if ("aceptar".equals(accion)) {
                int idComprador = Integer.parseInt(request.getParameter("idComprador"));
                double monto = Double.parseDouble(request.getParameter("monto"));

                // Ejecuta la transacción SQL
                boolean exito = ofertaDao.aceptarOfertaYActualizarProducto(idOferta, idProducto, idComprador, usuario.getId(), monto);

                if (exito) {
                    response.sendRedirect("misOfertasVendedor?msg=ok");
                } else {
                    response.sendRedirect("misOfertasVendedor?msg=error");
                }

            } else if ("rechazar".equals(accion)) {
                // Estado 2 = Rechazada
                boolean exito = ofertaDao.actualizarEstado(idOferta, 2);

                if (exito) {
                    response.sendRedirect("misOfertasVendedor?msg=ok");
                } else {
                    response.sendRedirect("misOfertasVendedor?msg=error");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al procesar la oferta en doPost:");
            e.printStackTrace(); // Imprime el error exacto en la consola de Java/IntelliJ
            response.sendRedirect("misOfertasVendedor?msg=error");
        }
    }
}