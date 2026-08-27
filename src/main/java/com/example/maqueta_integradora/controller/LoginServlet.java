package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.UserDao;
import com.example.maqueta_integradora.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.MessageFormat;

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

        // 3. Validar coincidencia de correo y contraseña
        boolean esValido = dao.login(email, contra);

        if (esValido) {
            User usuarioLogueado = dao.obtenerPorCorreo(email);

            if (usuarioLogueado != null) {

                // PASO A: ¿Está dado de baja por Admin/Sistema? (activo == 0)
                if (usuarioLogueado.getActivo() == 0) {
                    request.setAttribute("error", "Tu cuenta se encuentra deshabilitada. Contacta al administrador.");
                    request.setAttribute("correo", email);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                // PASO B: Cuenta activa pero sin verificar correo (es_verificado == 0)
                if (usuarioLogueado.getEsVerificado() == 0) {
                    // Genera o actualiza el token en la base de datos
                    String nuevoToken = dao.reagenerarToken(usuarioLogueado.getId());

                    if (nuevoToken != null) {
                        // Construimos el correo y lo enviamos vía EmailSender
                        String plantillaHtml = """
                                <html>
                                    <body style="font-family: Arial, sans-serif; color: #333333;">
                                        <h2>¡Hola, {0} {1}!</h2>
                                        <p>Tu nuevo código de verificación de 8 dígitos es: <strong>{2}</strong></p>
                                    </body>
                                </html>
                                """;

                        String cuerpoCorreo = MessageFormat.format(
                                plantillaHtml,
                                usuarioLogueado.getNombre(),
                                usuarioLogueado.getApellido(),
                                nuevoToken
                        );

                        EmailSender.sendMail(
                                usuarioLogueado.getCorreo(),
                                "Nuevo Código de Verificación",
                                cuerpoCorreo
                        );
                    }

                    HttpSession session = request.getSession(true);
                    session.setAttribute("correoPendiente", usuarioLogueado.getCorreo());

                    request.setAttribute("info", "No has verificado tu cuenta. Te hemos reenviado un nuevo código a tu correo.");
                    request.getRequestDispatcher("verificacion.jsp").forward(request, response);
                    return;
                }

                // PASO C: Usuario Activo y Verificado (es_verificado == 1 y activo == 1)

                // Validar si ya tiene una sesión abierta
                if (usuarioLogueado.getSesionActiva() == 1) {
                    request.setAttribute("error", "Tiene un dispositivo con una sesión abierta, por favor ciérrala si quieres iniciar sesión.");
                    request.setAttribute("correo", email);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                dao.actualizarSesionActiva(usuarioLogueado.getId(), 1);
                usuarioLogueado.setSesionActiva(1);

                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuarioLogueado);

                // Redirección según el rol
                if ("ADMIN".equalsIgnoreCase(usuarioLogueado.getRol())) {
                    response.sendRedirect("adminDashboard");
                } else {
                    response.sendRedirect("inicio");
                }
            } else {
                request.setAttribute("error", "No se pudo recuperar la información del usuario.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } else {
            // Si el correo o la contraseña no coinciden
            request.setAttribute("error", "Credenciales incorrectas.");
            request.setAttribute("correo", email);
            request.setAttribute("contra", contra);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}