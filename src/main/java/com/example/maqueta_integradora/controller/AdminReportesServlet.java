package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Reporte;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ReporteDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/adminReportes")
public class AdminReportesServlet extends HttpServlet {

    private ReporteDao reporteDao;

    @Override
    public void init() throws ServletException {
        reporteDao = new ReporteDao();
    }

    // 1. CARGAR Y MOSTRAR LA LISTA DE REPORTES EN EL JSP
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión de usuario (Asegurar que haya alguien logueado)
        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener reportes con la información de los usuarios desde la BD
        List<Reporte> listaReportes = reporteDao.obtenerTodos();

        // Mandar la lista al JSP
        request.setAttribute("listaReportes", listaReportes);
        request.getRequestDispatcher("adminReportes.jsp").forward(request, response);
    }

    // 2. CAMBIAR EL ESTADO DE UN REPORTE DESDE EL JSP (Puntos de acción)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int idReporte = Integer.parseInt(request.getParameter("idReporte"));
            int nuevoEstado = Integer.parseInt(request.getParameter("nuevoEstado"));

            // Actualizar en base de datos
            reporteDao.actualizarEstado(idReporte, nuevoEstado);

            // Redirigir de nuevo con mensaje de éxito
            response.sendRedirect("adminReportes?msg=estadoActualizado");
        } catch (Exception e) {
            response.sendRedirect("adminReportes?error=datosInvalidos");
        }
    }
}