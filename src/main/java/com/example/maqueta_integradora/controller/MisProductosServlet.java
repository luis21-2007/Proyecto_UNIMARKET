package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CategoriaDao;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;


@WebServlet("/misProductos")
public class MisProductosServlet extends HttpServlet {


    private ProductoDao productoDao;
    private CategoriaDao categoriaDao;

    @Override
    public void init() throws ServletException {

        productoDao = new ProductoDao();
        categoriaDao = new CategoriaDao();

    }




    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("usuario")==null){

            response.sendRedirect("login.jsp");
            return;
        }

        User usuario = (User) session.getAttribute("usuario");
        int idUsuario = usuario.getId();
        List<Categoria> listaCategorias = categoriaDao.getAll();

        List<Producto> listaProductos =
                productoDao.obtenerProductosPorUsuario(usuario.getId());

        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listaProductos", listaProductos);

        request.getRequestDispatcher("misProductos.jsp")
                .forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User usuario = (User) session.getAttribute("usuario");

        int idUsuario = usuario.getId();

        String categoria = request.getParameter("categoriaId");

        List<Categoria> listaCategorias = categoriaDao.getAll();

        List<Producto> listaProductos;

        if (categoria == null || categoria.isBlank()) {

            listaProductos = productoDao.obtenerProductosPorUsuario(idUsuario);

        } else {

            listaProductos = productoDao.obtenerProductosPorUsuarioYCategoria(
                            idUsuario,
                            Integer.parseInt(categoria)
                    );

        }

        System.out.println("Productos recibidos del DAO: " + listaProductos.size());
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("listaProductos", listaProductos);

        request.getRequestDispatcher("misProductos.jsp")
                .forward(request, response);
    }


}