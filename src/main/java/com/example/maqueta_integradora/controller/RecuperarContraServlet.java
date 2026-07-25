package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.dao.UserDao;
import com.example.maqueta_integradora.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;

@WebServlet(name = "RecuperarContraServlet", value = "/recuperar")
public class RecuperarContraServlet extends HttpServlet {
    private final UserDao dao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo = request.getParameter("correo");
        // MENSAJE DE SEGURIDAD (Se muestra siempre para evitar rastreo de correos activos)
        String mensajeGenerico = "Si el email se encuentra registrado, te llegará un correo electrónico con instrucciones.";

        if (correo == null || correo.isBlank()) {
            request.setAttribute("error", "Todos los campos deben de estar llenos");
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        if (!correo.toLowerCase().endsWith("@utez.edu.mx")) {
            request.setAttribute("error", "El correo debe ser institucional con terminación @utez.edu.mx");
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }

        if (correo != null && !correo.isBlank()) {
            boolean existe = dao.existeCorreo(correo);
            if (existe) {
                // Generamos código aleatorio de 6 caracteres (letras y números)
                String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                SecureRandom random = new SecureRandom();
                StringBuilder codigo = new StringBuilder(6);
                for (int i = 0; i < 6; i++) {
                    codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
                }
                String codigoGenerado = codigo.toString();
                // Lo guardamos en la base de datos
                dao.guardarCodigoRecuperacion(correo, codigoGenerado);
                // Enlace dinámico a tu sistema (ajusta localhost y el puerto si es necesario)
                String enlaceVerificacion = "http://localhost:8080/maqueta_integradora_war_exploded/restablecer.jsp?correo=" + correo;
                String plantillaHtml = "<html>"
                        + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                        + "<h2>Restablecer tu contraseña</h2>"
                        + "<p>Has solicitado restablecer tu contraseña. Tu código de verificación es: <strong>" + codigoGenerado + "</strong></p>"
                        + "<p>Haz clic en el siguiente enlace para ingresar el código y cambiar tu contraseña:</p>"
                        + "<p><a href='" + enlaceVerificacion + "' style='background:#0d6efd;color:white;padding:10px 15px;text-decoration:none;border-radius:5px;'>Restablecer contraseña</a></p>"
                        + "<p>Este código expira en 15 minutos.</p>"
                        + "</body>"
                        + "</html>";
                EmailSender.sendMail(correo, "Recuperación de contraseña", plantillaHtml);
            }
        }
        request.setAttribute("mensaje", mensajeGenerico);
        request.getRequestDispatcher("recuperar.jsp").forward(request, response);
    }
}