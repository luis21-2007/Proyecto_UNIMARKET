package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/gestionUsuarios")
public class GestionUsuariosServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String idStr = request.getParameter("id");

        // Si viene una acción de activar o desactivar, la procesamos y REDIRIGIMOS
        if (accion != null && idStr != null) {
            try {
                int idUsuario = Integer.parseInt(idStr);
                boolean resultado = false;

                if ("desactivar".equals(accion)) {
                    resultado = userDao.desactivarUsuario(idUsuario);
                    if (resultado) {
                        response.sendRedirect("gestionUsuarios?msg=desactivado");
                        return;
                    }
                } else if ("activar".equals(accion)) {
                    resultado = userDao.activarUsuario(idUsuario);
                    if (resultado) {
                        response.sendRedirect("gestionUsuarios?msg=activado");
                        return;
                    }
                }

                // Si falló la operación en la BD
                if (!resultado) {
                    response.sendRedirect("gestionUsuarios?error=true");
                    return;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("gestionUsuarios?error=true");
                return;
            }
        }

        // Cargar la lista actualizada de usuarios cuando se entra de forma normal
        List<User> usuarios = userDao.getAll();
        request.setAttribute("listaUsuarios", usuarios);

        // Renderizar el JSP
        request.getRequestDispatcher("GestionUsuario.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}