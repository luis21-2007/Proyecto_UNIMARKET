package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ReporteDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/guardarReporte")
public class GuardarReporteServlet extends HttpServlet {

    private ReporteDao reporteDao;

    @Override
    public void init() throws ServletException {
        reporteDao = new ReporteDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User reportador = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (reportador == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener el origen y el idProducto para construir la redirección correcta
        String origen = request.getParameter("origen"); // p. ej. "detalleProducto" o "misCompras"
        String idProducto = request.getParameter("idProducto");

        try {
            int idReportado = Integer.parseInt(request.getParameter("idReportado"));
            String motivo = request.getParameter("motivo");
            String descripcion = request.getParameter("descripcion");

            // Evitar autoreporte
            if (reportador.getId() == idReportado) {
                redirigir(response, origen, idProducto, "error=autoReporte");
                return;
            }

            String idTransStr = request.getParameter("idTransaccion");
            Integer idTransaccion = null;
            if (idTransStr != null && !idTransStr.isBlank()) {
                idTransaccion = Integer.parseInt(idTransStr);
            }

            if (motivo == null || motivo.isBlank() || descripcion == null || descripcion.isBlank()) {
                redirigir(response, origen, idProducto, "error=camposVacios");
                return;
            }

            boolean guardado = reporteDao.guardarReporte(reportador.getId(), idReportado, idTransaccion, motivo, descripcion);

            if (guardado) {
                redirigir(response, origen, idProducto, "msg=reporteExitoso");
            } else {
                redirigir(response, origen, idProducto, "error=errorReporte");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error en formato de parámetros al reportar: " + e.getMessage());
            redirigir(response, origen, idProducto, "error=datosInvalidos");
        }
    }

    // Método auxiliar para responder hacia la vista de origen
    private void redirigir(HttpServletResponse response, String origen, String idProducto, String param) throws IOException {
        if ("detalleProducto".equals(origen) && idProducto != null && !idProducto.isBlank()) {
            response.sendRedirect("detalleProducto?id=" + idProducto + "&" + param);
        } else {
            response.sendRedirect("misCompras?" + param);
        }
    }
}