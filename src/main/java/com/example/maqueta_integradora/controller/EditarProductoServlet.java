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
            producto.setIdUsuario(usuario.getId());

            // =========================================================
            // 5. INICIO DE LA LÓGICA DE LA IMAGEN
            // =========================================================
            Part filePart = request.getPart("imagen");

            // Verificamos si el usuario realmente subió un archivo nuevo
            if (filePart != null && filePart.getSize() > 0) {

                // Obtenemos el nombre original del archivo subido
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                // Le agregamos la fecha actual al nombre para que no se repita con otras imágenes
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                // Creamos la ruta física en el servidor donde se guardará la foto
                String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img";

                // Si la carpeta "img" no existe, la creamos
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                // Guardamos el archivo físicamente en esa carpeta
                filePart.write(uploadPath + File.separator + uniqueFileName);

                // Generamos la ruta relativa que se guardará en la base de datos
                String rutaImagenBD = "assets/img/" + uniqueFileName;

                // Usamos tu método del DAO para guardar esta nueva imagen asociada a este producto
                productoDao.guardarImagenProducto(producto.getIdProducto(), rutaImagenBD);
            }
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