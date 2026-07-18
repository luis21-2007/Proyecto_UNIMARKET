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
import java.util.ArrayList;

@WebServlet(name="RegisterServlet", value = "/register")
public class RegisterServlet  extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellido");
        String email1 = request.getParameter("correo");
        String contra1 = request.getParameter("contra1");
        String contra2 = request.getParameter("contra2");
        String carrera = request.getParameter("carrera");

        if (nombre == null || nombre.isBlank() || email1 == null || email1.isBlank() || contra1 == null || contra1.isBlank() || contra2 == null || contra2.isBlank() || carrera == null || carrera.isBlank() || apellidos.isBlank() || apellidos == null) {
            request.setAttribute("error", "Por favor, completa todos los campos obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!contra1.equals(contra2)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        UserDao dao = new UserDao();

        User nuevoDueno = new User();
        nuevoDueno.setNombre(nombre);
        nuevoDueno.setApellido(apellidos);
        nuevoDueno.setCorreo(email1);
        nuevoDueno.setContrasena(contra1);
        nuevoDueno.setCarrera(carrera);
        boolean creado = dao.create(nuevoDueno);

        if (creado) {
            String tokenGenerado = nuevoDueno.getToken();

            // Guardamos el correo en sesión
            HttpSession session = request.getSession();
            session.setAttribute("correoPendiente", nuevoDueno.getCorreo());

            // Tu plantilla de correo con {2} para el token se queda igual
            String plantillaHtml = """
                    <html>
                        <body style="font-family: Arial, sans-serif; color: #333333;">
                            <h2>¡Hola, {0} {1}!</h2>
                            <p>Tu código de verificación de 8 dígitos es: <strong>{2}</strong></p>
                        </body>
                    </html>
                    """;

            String cuerpoCorreo = MessageFormat.format(
                    plantillaHtml,
                    nuevoDueno.getNombre(),
                    nuevoDueno.getApellido(),
                    tokenGenerado // <-- Aquí pasamos el token recuperado
            );

            EmailSender.sendMail(
                    nuevoDueno.getCorreo(),
                    "Código de verificación",
                    cuerpoCorreo
            );

            response.sendRedirect("verificacion.jsp");

        } else {
            request.setAttribute("error", "Hubo un problema interno.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}