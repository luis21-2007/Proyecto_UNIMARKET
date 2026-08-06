package com.example.maqueta_integradora.controller;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtenemos la sesión actual si existe
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Invalidamos la sesión (elimina al usuario logueado de la memoria)
            session.invalidate();
        }

        // Redirigimos al Servlet principal para refrescar el estado de la página
        response.sendRedirect("inicio");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
