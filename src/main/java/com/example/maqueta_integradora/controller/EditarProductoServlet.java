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

@WebServlet("/editarProducto")
// Configuración obligatoria para poder recibir archivos (imágenes)
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
        User usuario = (User) session.getAttribute("usuario");

        // 2. Recuperar los datos de texto del formulario
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio = Double.parseDouble(request.getParameter("precio"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

        // 3. Buscar el producto actual en la BD para conservar su categoría original
        Producto producto = productoDao.getById(idProducto);

        if (producto != null) {
            // 4. Actualizamos los datos de texto en el objeto
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setIdCategoria(idCategoria);

            // Inyectamos el ID del usuario logueado por seguridad

            boolean subioNuevasImagenes = false;
            for (Part part : request.getParts()) {
                if (part.getName().equals("imagenes") && part.getSize() > 0) {
                    subioNuevasImagenes = true;
                    break; // Con una que detecte, sabemos que quiere reemplazar
                }
            }

            // b) Si detectamos fotos nuevas, ejecutamos el reemplazo
            if (subioNuevasImagenes) {

                // 1. BORRAMOS las imágenes anteriores de la BD
                productoDao.eliminarImagenesPorProducto(producto.getIdProducto());

                // 2. Preparamos la carpeta para guardar las nuevas
                String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();

                int contadorImagenes = 0; // Nuestro vigilante para no pasar de 3

                // 3. Recorremos y GUARDAMOS los archivos nuevos
                for (Part part : request.getParts()) {
                    // Verificamos que sea imagen, que pese algo, y que llevemos MENOS DE 3
                    if (part.getName().equals("imagenes") && part.getSize() > 0 && contadorImagenes < 3) {

                        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                        // Guardamos físicamente en la carpeta
                        part.write(uploadPath + File.separator + uniqueFileName);

                        // Insertamos la nueva ruta en la BD
                        String rutaImagenBD = "assets/img/" + uniqueFileName;
                        productoDao.guardarImagenProducto(producto.getIdProducto(), rutaImagenBD);

                        // Aumentamos el contador
                        contadorImagenes++;
                    }
                }
            }
            // =========================================================
            // FIN DE LA LÓGICA DE IMÁGENES
            // =========================================================
            // =========================================================
            // FIN DE LA LÓGICA DE LA IMAGEN
            // =========================================================

            // 6. Ejecutamos el update para guardar los textos cambiados (nombre, precio, etc.)
            boolean exito = productoDao.update(producto);

            if (exito) {
                // Si todo salió bien, redirigimos a mis productos
                response.sendRedirect("misProductos?msg=actualizacionExitosa");
            } else {
                // Si falló la BD por alguna razón
                response.sendRedirect("misProductos?msg=errorActualizacion");
            }
        } else {
            // Si el producto no se encontró en la BD
            response.sendRedirect("misProductos?msg=productoNoEncontrado");
        }
    }
}