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

@WebServlet("/detallemiProducto")
public class DetalleMiProductoServlet extends HttpServlet {

    private ProductoDao productoDao;
    private CategoriaDao categoriaDao;

    // EL CONSTRUCTOR PROBLEMÁTICO FUE ELIMINADO

    @Override
    public void init() throws ServletException {
        // AQUÍ ES DONDE DEBES INICIALIZAR AMBOS DAOS
        productoDao = new ProductoDao();
        categoriaDao = new CategoriaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Validar sesión de usuario
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User usuario = (User) session.getAttribute("usuario");

        // 2. Obtener parámetro ID de forma segura
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("misProductos");
            return;
        }

        int idProducto = 0;
        try {
            idProducto = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("misProductos");
            return;
        }

        // 3. Buscar el producto en la BD
        Producto producto = productoDao.getById(idProducto);

        // 4 y 5. Validar si existe y si le pertenece al usuario
        if (producto == null || producto.getIdUsuario() != usuario.getId()) {
            response.sendRedirect("misProductos");
            return;
        }

        // 6. Enviar atributos a la vista
        request.setAttribute("esPropietario", true);
        request.setAttribute("producto", producto);

        // --- CARGAR SUS IMÁGENES REALES ---
        List<String> listaImagenes = productoDao.getImagenesByProductoId(idProducto);
        request.setAttribute("listaImagenes", listaImagenes);

        // --- CARGAR CATEGORÍAS ---
        List<Categoria> listaCategorias = categoriaDao.getAll();
        request.setAttribute("listaCategorias", listaCategorias);

        // 7. Redireccionar a la vista JSP
        request.getRequestDispatcher("DetalleMiProducto.jsp").forward(request, response);
    }
}