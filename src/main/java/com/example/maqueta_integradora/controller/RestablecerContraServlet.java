package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.dao.UserDao;
import com.example.maqueta_integradora.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RestablecerContraServlet", value = "/restablecer")
public class RestablecerContraServlet extends HttpServlet {
    private final UserDao dao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String codigo = request.getParameter("codigo");
        String contra1 = request.getParameter("contra1");
        String contra2 = request.getParameter("contra2");

        // 1. Validar que el código ingresado coincida en la BD y no haya expirado
        boolean codigoEsValido = dao.validarCodigoYObtenerUsuario(correo, codigo);

        if (!codigoEsValido) {
            request.setAttribute("error", "Código incorrecto o expirado, intenta de nuevo.");
            request.getRequestDispatcher("restablecer.jsp?correo=" + correo).forward(request, response);
            return;
        }

        // 2. Validar que las dos contraseñas ingresadas coincidan
        if (contra1 == null || !contra1.equals(contra2)) {
            request.setAttribute("error", "Las contraseñas ingresadas no coinciden.");
            request.getRequestDispatcher("restablecer.jsp?correo=" + correo).forward(request, response);
            return;
        }

        // 3. Todo bien, actualizamos la BD (establece la contra y limpia el código)
        boolean actualizado = dao.actualizarContrasena(correo, contra1);

        if (actualizado) {
            // 4. Enviar correo de confirmación final de que el proceso concluyó con éxito
            String mensajeHtml = "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h3>¡Tu contraseña ha sido restablecida!</h3>"
                    + "<p>Hola,</p>"
                    + "<p>Te confirmamos que la contraseña de tu cuenta se ha actualizado exitosamente el día de hoy.</p>"
                    + "<p>Si no realizaste esta acción, por favor ponte en contacto de inmediato con soporte.</p>"
                    + "</body>"
                    + "</html>";

            EmailSender.sendMail(correo, "Contraseña actualizada exitosamente", mensajeHtml);

            // Redirigir al login informándole que ya puede iniciar sesión
            request.setAttribute("mensaje", "Contraseña cambiada con éxito. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Hubo un error interno al intentar cambiar la contraseña.");
            request.getRequestDispatcher("restablecer.jsp?correo=" + correo).forward(request, response);
        }
    }
}