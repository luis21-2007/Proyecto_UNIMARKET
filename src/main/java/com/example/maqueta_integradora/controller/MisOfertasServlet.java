package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Oferta;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.OfertaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/misOfertas")
public class MisOfertasServlet extends HttpServlet {

    private OfertaDao ofertaDao;

    @Override
    public void init() throws ServletException {
        ofertaDao = new OfertaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar Sesión
        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Obtener ofertas enviadas por el comprador en sesión
        List<Oferta> listaOfertas = ofertaDao.getOfertasByComprador(usuario.getId());

        // 3. Enviar la lista a la vista
        request.setAttribute("listaOfertas", listaOfertas);
        request.getRequestDispatcher("misOfertas.jsp").forward(request, response);
    }
}