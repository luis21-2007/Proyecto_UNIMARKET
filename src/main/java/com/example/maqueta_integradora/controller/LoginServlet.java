package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private final UserDao dao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("correo");
        String contra = request.getParameter("contra");

        // Validamos primero que no vengan nulos o vacíos para evitar errores
        if (email == null || email.isBlank() || contra == null || contra.isBlank()) {
            request.setAttribute("error", "Por favor, completa todos los campos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        if (!email.toLowerCase().endsWith("@utez.edu.mx")) {
            request.setAttribute("error", "El correo debe ser institucional con terminación @utez.edu.mx");
            request.setAttribute("contra",contra);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Intenta logear. Esto devolverá TRUE solo si las credenciales son correctas Y activo = 1
        boolean esValido = dao.login(email, contra);
        if (esValido) {
            HttpSession session = request.getSession(true);
            User usuarioLogueado = dao.obtenerPorCorreo(email);
            session.setAttribute("usuario", usuarioLogueado);

            // Redirección según el rol
            if ("ADMIN".equalsIgnoreCase(usuarioLogueado.getRol())) {
                response.sendRedirect("index_admin.jsp");
            } else {
                response.sendRedirect("inicio");
            }
        } else {
            // Si falla, preparamos la sesión con el correo por si el usuario
            // sigue en estado inactivo (activo = 0) y necesita verificar su cuenta.
            HttpSession session = request.getSession(true);
            session.setAttribute("correoPendiente", email);

            request.setAttribute("error", "Credenciales incorrectas o cuenta pendiente de verificación.");
            request.setAttribute("correo",email);
            request.setAttribute("contra",contra);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}