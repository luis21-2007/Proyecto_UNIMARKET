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

        // 1. Validar campos vacíos
        if (email == null || email.isBlank() || contra == null || contra.isBlank()) {
            request.setAttribute("error", "Por favor, completa todos los campos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 2. Validar dominio institucional
        if (!email.toLowerCase().endsWith("@utez.edu.mx")) {
            request.setAttribute("error", "El correo debe ser institucional con terminación @utez.edu.mx");
            request.setAttribute("contra", contra);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 3. Validar credenciales y cuenta activa (activo = 1)
        boolean esValido = dao.login(email, contra);

        if (esValido) {
            User usuarioLogueado = dao.obtenerPorCorreo(email);

            // 4. NUEVA VALIDACIÓN: Verificar si ya tiene una sesión abierta (sesion_activa == 1)
            if (usuarioLogueado.getSesionActiva() == 1) {
                request.setAttribute("error", "Tiene un dispositivo con una sesión abierta por favor cierrala si quieres iniciar sesión");
                request.setAttribute("correo", email);
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // 5. Marcar la sesión como activa (1) en la Base de Datos
            dao.actualizarSesionActiva(usuarioLogueado.getId(), 1);

            // Actualizamos la propiedad en el objeto que guardamos en sesión
            usuarioLogueado.setSesionActiva(1);

            // 6. Crear la sesión en el servidor
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuarioLogueado);

            // Redirección según el rol
            if ("ADMIN".equalsIgnoreCase(usuarioLogueado.getRol())) {
                response.sendRedirect("adminDashboard");
            } else {
                response.sendRedirect("inicio");
            }

        } else {
            // Si las credenciales fallan o el usuario está inactivo
            HttpSession session = request.getSession(true);
            session.setAttribute("correoPendiente", email);

            request.setAttribute("error", "Credenciales incorrectas o cuenta pendiente de verificación o Tu cuenta fue deshabilitada.");
            request.setAttribute("correo", email);
            request.setAttribute("contra", contra);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}