package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Calificacion;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CalificacionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/misResenas")
public class MisResenasServlet extends HttpServlet {

    private CalificacionDao calificacionDao;

    @Override
    public void init() throws ServletException {
        calificacionDao = new CalificacionDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar la sesión del usuario actual
        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idVendedor = usuarioLogueado.getId();

        // 2. Obtener lista de reseñas y métricas asociadas al vendedor
        List<Calificacion> listaResenas = calificacionDao.obtenerResenasPorVendedor(idVendedor);
        double promedioCalificaciones = calificacionDao.obtenerPromedioCalificaciones(idVendedor);

        // 3. Enviar datos a la vista JSP
        request.setAttribute("listaResenas", listaResenas);
        request.setAttribute("promedioCalificaciones", promedioCalificaciones);
        request.setAttribute("totalResenas", listaResenas.size());

        // 4. Redirigir a misResenas.jsp
        request.getRequestDispatcher("misResenas.jsp").forward(request, response);
    }
}