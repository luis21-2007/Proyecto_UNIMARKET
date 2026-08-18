package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.UserDao;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    private final UserDao dao = new UserDao();

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        User usuario = (User) se.getSession().getAttribute("usuario");
        if (usuario != null) {
            dao.actualizarSesionActiva(usuario.getId(), 0);
        }
    }
}