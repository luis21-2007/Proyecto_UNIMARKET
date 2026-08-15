package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.dao.CategoriaDao;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/inicio", "/marketplace", ""})
public class InicioServlet extends HttpServlet {

    private ProductoDao productoDao;
    private CategoriaDao categoriaDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
        categoriaDao = new CategoriaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Obtener todas las categorías activas para el menú / carrusel superior
        List<Categoria> listaCategorias = categoriaDao.getAll();

        // 2. Obtener el parámetro opcional de filtrado por categoría
        String idCatParam = request.getParameter("idCategoria");
        List<Producto> listaProductos;

        // 3. Evaluar si el usuario seleccionó una categoría específica
        if (idCatParam != null && !idCatParam.isBlank()) {
            try {
                int idCategoria = Integer.parseInt(idCatParam.trim());
                // Trae solo los productos asignados a esa categoría
                listaProductos = productoDao.getByCategoria(idCategoria);
            } catch (NumberFormatException e) {
                // Si viene un valor inválido, carga todos los productos
                listaProductos = productoDao.getAll();
            }
        } else {
            // Cargar el catálogo completo si no hay filtro
            listaProductos = productoDao.getAll();
        }

        // 4. Enviar las listas al scope del Request
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listaProductos", listaProductos);

        // 5. Reenviar al JSP del catálogo
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}