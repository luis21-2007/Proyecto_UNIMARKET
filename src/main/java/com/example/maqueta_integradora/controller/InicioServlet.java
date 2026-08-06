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

        // 1. Obtener todas las categorías activas para el carrusel superior
        List<Categoria> listaCategorias = categoriaDao.getAll();

        // 2. Obtener los productos activos guardados en Oracle
        List<Producto> listaProductos = productoDao.getAll();

        // 3. Enviar las listas al scope del Request
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listaProductos", listaProductos);

        // 4. Reenviar al JSP del catálogo
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Al recibir el POST con la categoría, simplemente ejecuta la lógica del doGet
        doGet(request, response);
    }
}