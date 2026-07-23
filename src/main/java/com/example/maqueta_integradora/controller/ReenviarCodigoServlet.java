package com.example.maqueta_integradora.controller;

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
import java.util.UUID;

@WebServlet(name="ReenviarCodigoServlet", value = "/ReenviarCodigoServlet")
public class ReenviarCodigoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String correo = (String) session.getAttribute("correoPendiente");

        if (correo != null && !correo.isBlank()) {

            java.security.SecureRandom random = new java.security.SecureRandom();
            int numero = 10000000 + random.nextInt(90000000);
            String nuevoToken = String.valueOf(numero);
            
            // 2. Actualizamos la tabla 'tokens_verificacion' mediante el DAO
            UserDao dao = new UserDao();
            boolean actualizado = dao.actualizarTokenPorCorreo(correo, nuevoToken);

            if (actualizado) {
                // 3. Enviamos el correo con el nuevo token
                String plantillaHtml = """
                    <html>
                        <body style="font-family: Arial, sans-serif; color: #333333;">
                            <h2>¡Nuevo código de verificación!</h2>
                            <p>Tu nuevo código de 8 dígitos es: <strong>{0}</strong></p>
                        </body>
                    </html>
                    """;

                String cuerpoCorreo = MessageFormat.format(plantillaHtml, nuevoToken);
                EmailSender.sendMail(correo, "Nuevo Código de Verificación", cuerpoCorreo);

                // 4. Respondemos OK (200) para AJAX/Fetch
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Si por alguna razón no encontró la relación en la BD
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            // Si no hay correo en sesión
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}