package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "VerificarCodigoServlet", value = "/Verificar")
public class VerificarCodigoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Recuperamos el correo pendiente desde la sesión
        HttpSession session = request.getSession();
        String correo = (String) session.getAttribute("correoPendiente");

        if (correo == null) {
            // Sesión expiró o intentó entrar directo. Lo mandamos a registrarse
            response.sendRedirect("registro.jsp");
            return;
        }

        // 2. Recuperamos los 8 dígitos independientes enviados desde el JSP
        String c1 = request.getParameter("c1");
        String c2 = request.getParameter("c2");
        String c3 = request.getParameter("c3");
        String c4 = request.getParameter("c4");
        String c5 = request.getParameter("c5");
        String c6 = request.getParameter("c6");
        String c7 = request.getParameter("c7");
        String c8 = request.getParameter("c8");

        // Validamos que ninguno llegue vacío
        if (c1 == null || c2 == null || c3 == null || c4 == null ||
                c5 == null || c6 == null || c7 == null || c8 == null) {
            request.setAttribute("error", "Por favor ingresa el código completo.");
            request.getRequestDispatcher("verificacion.jsp").forward(request, response);
            return;
        }

        // 3. Concatenamos los inputs para armar el token de 8 dígitos
        String tokenCompleto = c1 + c2 + c3 + c4 + c5 + c6 + c7 + c8;

        // 4. Mandamos a verificar a la Base de Datos
        UserDao dao = new UserDao();
        boolean verificado = dao.verificarTokenYActivarUsuario(correo, tokenCompleto);

        if (verificado) {
            // El usuario ya está activo, limpiamos la sesión
            session.removeAttribute("correoPendiente");

            // Redireccionamos a login con mensaje de éxito
            request.setAttribute("mensaje", "¡Cuenta verificada con éxito! Ya puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // El código falló (incorrecto o expirado)
            request.setAttribute("error", "Código incorrecto o ha expirado. Inténtalo de nuevo.");
            request.getRequestDispatcher("verificacion.jsp").forward(request, response);
        }
    }
}