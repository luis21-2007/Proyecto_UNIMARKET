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

        // 1. Validar sesión del usuario denunciante
        HttpSession session = request.getSession(false);
        User reportador = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (reportador == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Obtener parámetros del formulario
            int idReportado = Integer.parseInt(request.getParameter("idReportado"));
            String motivo = request.getParameter("motivo");
            String descripcion = request.getParameter("descripcion");

            // 3. Obtener idTransaccion opcional (maneja valores vacíos o nulos)
            String idTransStr = request.getParameter("idTransaccion");
            Integer idTransaccion = null;
            if (idTransStr != null && !idTransStr.isBlank()) {
                idTransaccion = Integer.parseInt(idTransStr);
            }

            // Validar que los campos requeridos no lleguen vacíos
            if (motivo == null || motivo.isBlank() || descripcion == null || descripcion.isBlank()) {
                response.sendRedirect("misCompras?error=camposVacios");
                return;
            }

            // 4. Guardar el reporte pasando idTransaccion
            boolean guardado = reporteDao.guardarReporte(reportador.getId(), idReportado, idTransaccion, motivo, descripcion);

            if (guardado) {
                response.sendRedirect("misCompras?msg=reporteExitoso");
            } else {
                response.sendRedirect("misCompras?error=errorReporte");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error en formato de parámetros al reportar: " + e.getMessage());
            response.sendRedirect("misCompras?error=datosInvalidos");
        }
    }
}