package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/actualizarEstadoTransaccion")
public class ActualizarEstadoTransaccionServlet extends HttpServlet {

    private TransaccionDao transaccionDao;

    @Override
    public void init() throws ServletException {
        transaccionDao = new TransaccionDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión de usuario
        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Obtener parámetros de la petición
        String idTransaccionStr = request.getParameter("idTransaccion");
        String nuevoEstadoStr = request.getParameter("nuevoEstado");

        if (idTransaccionStr == null ||  nuevoEstadoStr == null) {
            response.sendRedirect("misVentas?error=errorActualizar");
            return;
        }

        try {
            int idTransaccion = Integer.parseInt(idTransaccionStr);
            int nuevoEstado = Integer.parseInt(nuevoEstadoStr);

            // 3. Ejecutar actualización de la transacción y el producto en el DAO
            boolean actualizado = transaccionDao.actualizarEstado(idTransaccion, nuevoEstado);

            if (actualizado) {
                response.sendRedirect("misVentas?msg=estadoActualizado");
            } else {
                response.sendRedirect("misVentas?error=errorActualizar");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("misVentas?error=errorActualizar");
        }
    }
}