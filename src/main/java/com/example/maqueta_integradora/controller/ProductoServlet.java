package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.CategoriaDao;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/subirProducto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB de buffer
        maxFileSize = 1024 * 1024 * 10,       // 10MB tamaño máximo por imagen
        maxRequestSize = 1024 * 1024 * 50     // 50MB tamaño máximo de la petición
)
public class ProductoServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Categoria> listaCategorias = categoriaDao.getAll();
        request.setAttribute("listaCategorias", listaCategorias);

        request.getRequestDispatcher("subir_productos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String nombre = request.getParameter("nombre_producto");
        String categoriaStr = request.getParameter("categoria");
        String precioStr = request.getParameter("precio");
        String descripcion = request.getParameter("descripcion");

        if (nombre == null || nombre.isBlank() ||
                categoriaStr == null || categoriaStr.isBlank() ||
                precioStr == null || precioStr.isBlank() ||
                descripcion == null || descripcion.isBlank()) {

            request.setAttribute("error", "Por favor, completa todos los campos del formulario.");
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("categoria", categoriaStr);
            request.setAttribute("precio", precioStr);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
            return;
        }

        double precio = 0;
        int idCategoria = 0;

        try {
            precio = Double.parseDouble(precioStr.trim());
            idCategoria = Integer.parseInt(categoriaStr.trim());

            if (precio <= 0) {
                request.setAttribute("error", "El precio del producto debe ser mayor a $0.");
                request.setAttribute("nombre_producto", nombre);
                request.setAttribute("categoria", categoriaStr);
                request.setAttribute("precio", precioStr);
                request.setAttribute("descripcion", descripcion);
                doGet(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Por favor, ingresa un precio o categoría válida.");
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
            return;
        }

        // VALIDACIÓN DE IMÁGENES
        List<Part> partesImagenes = new ArrayList<>();

        for (Part part : request.getParts()) {
            if ("imagenes".equals(part.getName()) && part.getSize() > 0) {
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                if (fileName != null && !fileName.trim().isEmpty()) {

                    String lowerName = fileName.toLowerCase();
                    if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") ||
                            lowerName.endsWith(".png") || lowerName.endsWith(".webp")) {
                        partesImagenes.add(part);
                    } else {
                        request.setAttribute("error", "Solo se admiten formatos de imagen (.jpg, .jpeg, .png, .webp).");
                        request.setAttribute("nombre_producto", nombre);
                        request.setAttribute("categoria", categoriaStr);
                        request.setAttribute("precio", precioStr);
                        request.setAttribute("descripcion", descripcion);
                        doGet(request, response);
                        return;
                    }
                }
            }
        }

        // EVALUAR CANTIDAD DE IMÁGENES
        if (partesImagenes.isEmpty()) {
            request.setAttribute("error", "Hace falta adjuntar las imágenes del producto.");
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("categoria", categoriaStr);
            request.setAttribute("precio", precioStr);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
            return;
        } else if (partesImagenes.size() != 3) {
            request.setAttribute("error", "Es obligatorio adjuntar exactamente 3 imágenes del producto.");
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("categoria", categoriaStr);
            request.setAttribute("precio", precioStr);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
            return;
        }

        try {
            Producto producto = new Producto();
            producto.setNombre(nombre.trim());
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion.trim());
            producto.setIdCategoria(idCategoria);
            producto.setIdUsuario(usuarioLogueado.getId());

            int idProductoGenerado = productoDao.createAndGetId(producto);

            if (idProductoGenerado > 0) {

                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                for (int i = 0; i < partesImagenes.size(); i++) {
                    Part part = partesImagenes.get(i);
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                    String newFileName = System.currentTimeMillis() + "_" + i + fileExtension;

                    File file = new File(uploadDir, newFileName);
                    try (InputStream input = part.getInputStream()) {
                        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    String relativePath = "uploads/" + newFileName;
                    productoDao.guardarImagenProducto(idProductoGenerado, relativePath);
                }

                response.sendRedirect("inicio?msg=exito");
            } else {
                request.setAttribute("error", "No se pudo registrar el producto en la base de datos.");
                request.setAttribute("nombre_producto", nombre);
                request.setAttribute("categoria", categoriaStr);
                request.setAttribute("precio", precioStr);
                request.setAttribute("descripcion", descripcion);
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error inesperado al procesar la solicitud: " + e.getMessage());
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("categoria", categoriaStr);
            request.setAttribute("precio", precioStr);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
        }
    }
}