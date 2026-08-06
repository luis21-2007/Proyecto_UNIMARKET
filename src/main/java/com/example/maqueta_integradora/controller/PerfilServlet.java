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

        // 1. Validar y actualizar el Nombre
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            usuarioLogueado.setNombre(nuevoNombre.trim());
        }

        // 2. Limpiar y convertir el Teléfono a tipo 'long'
        if (nuevoTelefono != null && !nuevoTelefono.isBlank()) {
            try {
                // Remueve espacios, guiones o signos '+' dejando únicamente los dígitos
                String soloNumeros = nuevoTelefono.replaceAll("[^0-9]", "");

                if (!soloNumeros.isBlank()) {
                    long telefonoLong = Long.parseLong(soloNumeros);
                    usuarioLogueado.setTelefono(telefonoLong);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear el número telefónico: " + nuevoTelefono);
                e.printStackTrace();
            }
        }

        boolean actualizado = userDao.updatePerfil(usuarioLogueado.getId(), usuarioLogueado.getNombre(), usuarioLogueado.getTelefono());

        if (actualizado) {
            session.setAttribute("usuario", usuarioLogueado);
            response.sendRedirect("perfil?msg=actualizado");
        } else {
            response.sendRedirect("perfil?error=1");
        }
    }
}