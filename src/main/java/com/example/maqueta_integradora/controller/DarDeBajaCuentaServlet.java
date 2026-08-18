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

@WebServlet("/darDeBajaCuenta")
public class DarDeBajaCuentaServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            User usuario = (User) session.getAttribute("usuario");

            if (usuario != null) {
                userDao.darDeBaja(usuario.getId());
                // 2. Liberar la sesión activa en la BD (sesion_activa = 0)
                userDao.actualizarSesionActiva(usuario.getId(), 0);
                session.invalidate();
            }
        }
        response.sendRedirect("login.jsp?msg=cuenta_desactivada");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}