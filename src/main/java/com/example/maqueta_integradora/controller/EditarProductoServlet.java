package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.ProductoDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/editarProducto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class EditarProductoServlet extends HttpServlet {

    private ProductoDao productoDao;

    @Override
    public void init() throws ServletException {
        productoDao = new ProductoDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión del usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Recuperar datos del formulario
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio = Double.parseDouble(request.getParameter("precio"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

        // 3. Buscar el producto actual en la BD
        Producto producto = productoDao.getById(idProducto);

        if (producto != null) {
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setIdCategoria(idCategoria);

            // 4. Lógica de acumulación de imágenes (Límite máximo: 3 fotos por producto)
            List<String> fotosActuales = productoDao.getImagenesByProductoId(idProducto);
            int totalFotosActuales = (fotosActuales != null) ? fotosActuales.size() : 0;

            if (totalFotosActuales < 3) {
                String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                for (Part part : request.getParts()) {
                    if (part.getName().equals("imagenes") && part.getSize() > 0) {
                        // Verificamos no exceder el máximo de 3 imágenes en total
                        if (totalFotosActuales < 3) {
                            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                            // Guardar archivo físico
                            part.write(uploadPath + File.separator + uniqueFileName);

                            // Guardar nueva ruta en la BD
                            String rutaImagenBD = "assets/img/" + uniqueFileName;
                            productoDao.guardarImagenProducto(producto.getIdProducto(), rutaImagenBD);

                            totalFotosActuales++;
                        }
                    }
                }
            }

            // 5. Guardar los cambios del texto del producto
            boolean exito = productoDao.update(producto);

            if (exito) {
                response.sendRedirect("detallemiProducto?id=" + idProducto + "&msg=actualizacionExitosa");
            } else {
                response.sendRedirect("detallemiProducto?id=" + idProducto + "&error=errorActualizacion");
            }
        } else {
            response.sendRedirect("misProductos?msg=productoNoEncontrado");
        }
    }
}