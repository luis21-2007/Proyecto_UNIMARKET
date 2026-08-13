package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.dao.OfertaDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/responderOferta")
public class ResponderOfertaServlet extends HttpServlet {
    private OfertaDao ofertaDao = new OfertaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idOferta = Integer.parseInt(request.getParameter("id"));
        String accion = request.getParameter("accion"); // "aceptar" o "rechazar"

        // Parámetros adicionales para identificar la redirección
        String idProductoStr = request.getParameter("idProducto");
        String origen = request.getParameter("origen");

        boolean exito = false;

        if ("aceptar".equalsIgnoreCase(accion)) {
            // Ejecuta el flujo completo de aceptación + registro de transacción
            exito = ofertaDao.aceptarOfertaYRegistrarVenta(idOferta);
        } else if ("rechazar".equalsIgnoreCase(accion)) {
            // Solo actualiza el estado de la oferta a 2 (Rechazada)
            exito = ofertaDao.actualizarEstado(idOferta, 2);
        }

        // Lógica de redirección dinámica según la procedencia
        if ("detalle".equalsIgnoreCase(origen) && idProductoStr != null && !idProductoStr.isEmpty()) {
            String msg = exito ? "respuestaExitosa" : "errorActualizacion";
            response.sendRedirect("detallemiProducto?id=" + idProductoStr + "&msg=" + msg);
        } else {
            response.sendRedirect("misOfertasVendedor?msg=" + (exito ? "ok" : "error"));
        }
    }
}