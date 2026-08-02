package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Categoria;
import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User; // O la clase de Usuario que manejes en sesión
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

    /**
     * Carga el formulario de subida de producto llenando el combo de categorías
     */
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

        try {
            // 2. Obtener datos de texto del formulario
            String nombre = request.getParameter("nombre_producto");
            int idCategoria = Integer.parseInt(request.getParameter("categoria"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            String descripcion = request.getParameter("descripcion");

            // 3. Mapear datos en el objeto Producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setIdCategoria(idCategoria);
            producto.setIdUsuario(usuarioLogueado.getId());

            // 4. PASO 1: Guardar el producto en la BD y obtener el ID generado
            int idProductoGenerado = productoDao.createAndGetId(producto);

            if (idProductoGenerado > 0) {

                // Definir carpeta física para guardar imágenes
                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                int contador = 0;

                // 5. PASO 2 y 3: Iterar sobre las partes subidas (name="imagenes" en el JSP)
                for (Part part : request.getParts()) {
                    if ("imagenes".equals(part.getName()) && part.getSize() > 0 && contador < 3) {

                        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();

                        if (fileName != null && !fileName.trim().isEmpty()) {
                            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                            String newFileName = System.currentTimeMillis() + "_" + contador + fileExtension;

                            // Guardar archivo físico en el servidor
                            File file = new File(uploadDir, newFileName);
                            try (InputStream input = part.getInputStream()) {
                                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }

                            String relativePath = "uploads/" + newFileName;

                            // Guardar el registro en la tabla 'imagen_producto'
                            productoDao.guardarImagenProducto(idProductoGenerado, relativePath);

                            contador++;
                        }
                    }
                }

                // Redirigir al formulario con mensaje de éxito
                response.sendRedirect("inicio");

            } else {
                request.setAttribute("error", "No se pudo registrar el producto en la base de datos.");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al procesar el producto: " + e.getMessage());
            doGet(request, response);
        }
    }
}