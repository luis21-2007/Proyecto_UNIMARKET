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

@WebServlet(name="RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellido");
        String email1 = request.getParameter("correo");
        String contra1 = request.getParameter("contra1");
        String contra2 = request.getParameter("contra2");
        String carrera = request.getParameter("carrera");
        String telefonoStr = request.getParameter("telefono");

        // 1. Validar campos obligatorios
        if (nombre == null || nombre.isBlank() || email1 == null || email1.isBlank()
                || contra1 == null || contra1.isBlank() || contra2 == null || contra2.isBlank()
                || carrera == null || carrera.isBlank() || apellidos == null || apellidos.isBlank()
                || telefonoStr == null || telefonoStr.isBlank()) {
            request.setAttribute("error", "Por favor, completa todos los campos son obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // 2. Validar formato de teléfono (10 dígitos)
        if (!telefonoStr.trim().matches("\\d{10}")) {
            request.setAttribute("error", "El número de teléfono debe contener exactamente 10 dígitos numéricos.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellido", apellidos);
            request.setAttribute("carrera", carrera);
            request.setAttribute("correo", email1);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        long telefono = Long.parseLong(telefonoStr.trim());

        // 3. Validar correo institucional (@utez.edu.mx)
        if (!email1.toLowerCase().endsWith("@utez.edu.mx")) {
            request.setAttribute("error", "El correo debe ser institucional con terminación @utez.edu.mx");
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellido", apellidos);
            request.setAttribute("carrera", carrera);
            request.setAttribute("telefono", telefono);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        UserDao dao = new UserDao();

        // 4. Validar que no exista el correo en la base de datos
        if (dao.existeCorreo(email1)) {
            request.setAttribute("error", "El correo institucional ya está registrado.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellido", apellidos);
            request.setAttribute("carrera", carrera);
            request.setAttribute("telefono", telefono);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // 5. Validar contraseña segura (Mínimo 8 caracteres y al menos una letra mayúscula)
        if (contra1.length() < 8 || !contra1.matches(".*[A-Z].*")) {
            request.setAttribute("error", "La contraseña debe tener al menos 8 caracteres y contener mínimo una letra mayúscula.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellido", apellidos);
            request.setAttribute("carrera", carrera);
            request.setAttribute("correo", email1);
            request.setAttribute("telefono", telefono);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // 6. Validar coincidencia de contraseñas
        if (!contra1.equals(contra2)) {
            request.setAttribute("error", "Las contraseñas no coinciden. Inténtalo de nuevo.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellido", apellidos);
            request.setAttribute("carrera", carrera);
            request.setAttribute("correo", email1);
            request.setAttribute("telefono", telefono);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // 7. Instanciar objeto Usuario con las nuevas propiedades (activo = 1, esVerificado = 0 manejados por DAO)
        User nuevoDueno = new User();
        nuevoDueno.setNombre(nombre);
        nuevoDueno.setApellido(apellidos);
        nuevoDueno.setCorreo(email1);
        nuevoDueno.setContrasena(contra1);
        nuevoDueno.setCarrera(carrera);
        nuevoDueno.setTelefono(telefono);
        nuevoDueno.setActivo(1);
        nuevoDueno.setEsVerificado(0);

        boolean creado = dao.create(nuevoDueno);

        if (creado) {
            String tokenGenerado = nuevoDueno.getToken();

            // Guardamos el correo en la sesión para completar la verificación
            HttpSession session = request.getSession();
            session.setAttribute("correoPendiente", nuevoDueno.getCorreo());

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
                    tokenGenerado
            );

            EmailSender.sendMail(
                    nuevoDueno.getCorreo(),
                    "Código de verificación",
                    cuerpoCorreo
            );

            response.sendRedirect("verificacion.jsp");

        } else {
            request.setAttribute("error", "Hubo un problema interno al registrar tu cuenta.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}