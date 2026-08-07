package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.dao.OfertaDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/responderOferta")
public class ResponderOfertaServlet extends HttpServlet {
    private OfertaDao ofertaDao = new OfertaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idOferta = Integer.parseInt(request.getParameter("id"));
        String accion = request.getParameter("accion"); // "aceptar" o "rechazar"

        int nuevoEstado = accion.equals("aceptar") ? 1 : 2;

        boolean exito = ofertaDao.actualizarEstado(idOferta, nuevoEstado);

        response.sendRedirect("misOfertasVendedor?msg=" + (exito ? "ok" : "error"));
    }
}