package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String nuevoNombre = request.getParameter("nombre");
        String nuevoTelefono = request.getParameter("telefono");
        String nuevoCorreo = request.getParameter("correo");

        // 1. Validar Nombre: No nulo, no vacío y que no sea solo espacios en blanco
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            response.sendRedirect("perfil?error=nombreInvalido");
            return;
        }

        // 2. Validar Teléfono estrictamente a 10 dígitos numéricos
        if (nuevoTelefono == null || !nuevoTelefono.trim().matches("\\d{10}")) {
            response.sendRedirect("perfil?error=telefonoInvalido");
            return;
        }

        // 3. Validar Correo Institucional (@utez.edu.mx)
        if (nuevoCorreo == null || !nuevoCorreo.toLowerCase().endsWith("@utez.edu.mx")) {
            response.sendRedirect("perfil?error=correoInvalido");
            return;
        }

        try {
            String nombreLimpio = nuevoNombre.trim();
            long telefonoLong = Long.parseLong(nuevoTelefono.trim());
            String correoLimpio = nuevoCorreo.trim();

            // 4. Actualizar la base de datos (asegúrate de incluir el correo en tu DAO si aplica)
            boolean actualizado = userDao.updatePerfil(
                    usuarioLogueado.getId(),
                    nombreLimpio,
                    telefonoLong
            );

            if (actualizado) {
                usuarioLogueado.setNombre(nombreLimpio);
                usuarioLogueado.setTelefono(telefonoLong);
                usuarioLogueado.setCorreo(correoLimpio); // Si manejas el correo en el objeto User
                session.setAttribute("usuario", usuarioLogueado);

                response.sendRedirect("perfil?msg=actualizado");
            } else {
                response.sendRedirect("perfil?error=1");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error al parsear el número telefónico: " + nuevoTelefono);
            response.sendRedirect("perfil?error=telefonoInvalido");
        }
    }
}