package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Transaccion;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/misCompras")
public class MisComprasServlet extends HttpServlet {
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

        List<Transaccion> listaCompras = transaccionDao.getComprasByUsuario(usuario.getId());
        request.setAttribute("listaCompras", listaCompras);
        request.getRequestDispatcher("misCompras.jsp").forward(request, response);
    }
}