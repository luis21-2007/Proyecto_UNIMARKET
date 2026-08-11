package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CalificacionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/guardarCalificacion")
public class GuardarCalificacionServlet extends HttpServlet {

    private CalificacionDao calificacionDao;

    @Override
    public void init() throws ServletException {
        calificacionDao = new CalificacionDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión del comprador
        HttpSession session = request.getSession(false);
        User comprador = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (comprador == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Obtener parámetros del formulario
            int idVendedor = Integer.parseInt(request.getParameter("idVendedor"));
            int idTransaccion = Integer.parseInt(request.getParameter("idTransaccion"));
            int puntuacion = Integer.parseInt(request.getParameter("puntuacion"));
            String comentario = request.getParameter("comentario");

            // 3. Validar si esta compra ya cuenta con una calificación
            if (calificacionDao.existeCalificacion(idTransaccion)) {
                response.sendRedirect("misCompras?error=yaCalificado");
                return;
            }

            // 4. Guardar en la base de datos
            boolean guardado = calificacionDao.guardarCalificacion(comprador.getId(), idVendedor, idTransaccion, puntuacion, comentario);

            if (guardado) {
                response.sendRedirect("misCompras?msg=calificacionExitosa");
            } else {
                response.sendRedirect("misCompras?error=errorCalificacion");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error en formato de parámetros al calificar: " + e.getMessage());
            response.sendRedirect("misCompras?error=datosInvalidos");
        }
    }
}