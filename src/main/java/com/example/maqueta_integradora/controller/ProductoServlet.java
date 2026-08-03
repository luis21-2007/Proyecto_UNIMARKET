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

        // 1. Validar sesión del usuario
        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Cargar categorías activas para el select
        List<Categoria> listaCategorias = categoriaDao.getAll();
        request.setAttribute("listaCategorias", listaCategorias);

        // 3. Reenviar al JSP
        request.getRequestDispatcher("subir_productos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Validar Sesión del Usuario
        HttpSession session = request.getSession(false);
        User usuarioLogueado = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Recuperar parámetros del formulario
        String nombre = request.getParameter("nombre_producto");
        String categoriaStr = request.getParameter("categoria");
        String precioStr = request.getParameter("precio");
        String descripcion = request.getParameter("descripcion");

        // ==========================================
        // RESTRICCIÓN 1: CAMPOS OBLIGATORIOS Y VACÍOS
        // ==========================================
        if (nombre == null || nombre.isBlank() ||
                categoriaStr == null || categoriaStr.isBlank() ||
                precioStr == null || precioStr.isBlank() ||
                descripcion == null || descripcion.isBlank()) {

            request.setAttribute("error", "Por favor, completa todos los campos son obligatorios.");
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("categoria", categoriaStr);
            request.setAttribute("precio", precioStr);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
            return;
        }

        double precio = 0;
        int idCategoria = 0;

        // ==========================================
        // RESTRICCIÓN 2: VALIDACIÓN FORMATO NUMÉRICO Y VALORES VÁLIDOS
        // ==========================================
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

        // ==========================================
        // RESTRICCIÓN 3: VALIDACIÓN DE EXACTAMENTE 3 IMÁGENES
        // ==========================================
        List<Part> partesImagenes = new ArrayList<>();

        for (Part part : request.getParts()) {
            if ("imagenes".equals(part.getName()) && part.getSize() > 0) {
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                if (fileName != null && !fileName.trim().isEmpty()) {

                    // Verificar extensión válida
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

        // REGLA CLAVE: Deben ser EXACTAMENTE 3 imágenes
        if (partesImagenes.size() != 3) {
            request.setAttribute("error", "Es obligatorio adjuntar exactamente 3 imágenes del producto.");
            request.setAttribute("nombre_producto", nombre);
            request.setAttribute("categoria", categoriaStr);
            request.setAttribute("precio", precioStr);
            request.setAttribute("descripcion", descripcion);
            doGet(request, response);
            return;
        }

        // ==========================================
        // INSERCIÓN EN LA BASE DE DATOS Y GUARDADO
        // ==========================================
        try {
            // Mapear datos en el objeto Producto
            Producto producto = new Producto();
            producto.setNombre(nombre.trim());
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion.trim());
            producto.setIdCategoria(idCategoria);
            producto.setIdUsuario(usuarioLogueado.getId());

            // 1. Insertar Producto en BD y obtener su ID asignado
            int idProductoGenerado = productoDao.createAndGetId(producto);

            if (idProductoGenerado > 0) {

                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                // 2. Guardar físicamente e insertar en la tabla 'imagen_producto' las 3 fotos
                for (int i = 0; i < partesImagenes.size(); i++) {
                    Part part = partesImagenes.get(i);
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                    String newFileName = System.currentTimeMillis() + "_" + i + fileExtension;

                    // Guardar en servidor
                    File file = new File(uploadDir, newFileName);
                    try (InputStream input = part.getInputStream()) {
                        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    String relativePath = "uploads/" + newFileName;

                    // Guardar en la tabla 'imagen_producto'
                    productoDao.guardarImagenProducto(idProductoGenerado, relativePath);
                }

                // Redireccionar o notificar éxito
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