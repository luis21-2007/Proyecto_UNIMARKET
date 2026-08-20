package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Transaccion;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/misVentas")
public class MisVentasServlet extends HttpServlet {
    private TransaccionDao transaccionDao = new TransaccionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Transaccion> listaVentas = transaccionDao.getVentasByUsuario(usuario.getId());
        request.setAttribute("listaVentas", listaVentas);
        request.getRequestDispatcher("misVentas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idTxStr = request.getParameter("idTransaccion");
        String idProdStr = request.getParameter("idProducto");
        String accion = request.getParameter("accion"); // Ejemplo: "cancelar"

        if ("cancelar".equalsIgnoreCase(accion) && idTxStr != null && idProdStr != null) {
            try {
                int idTransaccion = Integer.parseInt(idTxStr);
                int idProducto = Integer.parseInt(idProdStr);

                boolean cancelado = transaccionDao.cancelarTransaccion(idTransaccion, idProducto);

                if (cancelado) {
                    response.sendRedirect("misVentas?msg=canceladoExito");
                    return;
                } else {
                    response.sendRedirect("misVentas?error=cancelarError");
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("misVentas");
    }
}