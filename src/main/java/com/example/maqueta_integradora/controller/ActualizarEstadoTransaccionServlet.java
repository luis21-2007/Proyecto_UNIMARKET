package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/actualizarEstadoTransaccion")
public class ActualizarEstadoTransaccionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idTransaccionStr = request.getParameter("idTransaccion");
        String nuevoEstadoStr = request.getParameter("nuevoEstado");

        try {
            int idTransaccion = Integer.parseInt(idTransaccionStr);
            int nuevoEstado = Integer.parseInt(nuevoEstadoStr);

            TransaccionDao transaccionDao = new TransaccionDao();
            boolean actualizado = transaccionDao.actualizarEstado(idTransaccion, nuevoEstado);

            if (actualizado) {
                response.sendRedirect("misVentas?msg=estadoActualizado");
            } else {
                response.sendRedirect("misVentas?error=errorActualizar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("misVentas");
        }
    }
}