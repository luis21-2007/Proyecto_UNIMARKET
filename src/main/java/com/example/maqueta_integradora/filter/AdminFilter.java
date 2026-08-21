package com.example.maqueta_integradora.filter;

import com.example.maqueta_integradora.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Protege tanto servlets como jsps dentro de la sección admin
@WebFilter(urlPatterns = {"/adminDashboard",
        "/gestionProductos",
        "/gestionUsuarios",
        "/categorias",
        "/adminReportes",
        "/index_admin.jsp",
        "/agregarCategorias.jsp",
        "/GestionProductos.jsp",
        "/gestionCategorias.jsp",
        "/GestionUsuarios.jsp",
        "/adminReportes.jsp"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario != null && "ADMIN".equals(usuario.getRol())) {
            chain.doFilter(request, response); // Acceso permitido: continúa a la ruta deseada
        } else {
            res.sendRedirect(req.getContextPath() + "/login.jsp"); // Acceso denegado
        }
    }
}