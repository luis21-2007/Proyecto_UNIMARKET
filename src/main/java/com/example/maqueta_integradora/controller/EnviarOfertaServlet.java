package com.example.maqueta_integradora.controller;

import com.example.maqueta_integradora.model.Oferta;
import com.example.maqueta_integradora.model.Producto;
import com.example.maqueta_integradora.model.User;
import com.example.maqueta_integradora.model.dao.OfertaDao;

import com.example.maqueta_integradora.model.dao.ProductoDao;
import com.example.maqueta_integradora.model.dao.UserDao;
import com.example.maqueta_integradora.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.MessageFormat;

@WebServlet("/enviarOferta")
public class EnviarOfertaServlet extends HttpServlet {

    private OfertaDao ofertaDao;

    @Override
    public void init() throws ServletException {
        ofertaDao = new OfertaDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User usuario = (session != null) ? (User) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idProductoStr = request.getParameter("idProducto");
        String montoOfertaStr = request.getParameter("montoOferta");

        try {
            int idProducto = Integer.parseInt(idProductoStr);
            double montoOferta = Double.parseDouble(montoOfertaStr);

            ProductoDao productoDao = new ProductoDao();
            Producto producto = productoDao.getById(idProducto);

            if (producto != null && producto.getIdUsuario() == usuario.getId()) {
                // Si el dueño del producto es el mismo usuario logueado, redirigir con error
                response.sendRedirect("detalleProducto?id=" + idProducto + "&error=auto_oferta");
                return;
            }
            Oferta oferta = new Oferta(montoOferta, usuario.getId(), idProducto);
            boolean registrada = ofertaDao.guardarOferta(oferta);
            UserDao userDao = new UserDao();

            if (registrada) {
                // 1. Obtener la información del producto y del vendedor/dueño
                User vendedor = userDao.getById(producto.getIdUsuario()); // O el DAO que use tu modelo de usuario

                if (vendedor != null && vendedor.getCorreo() != null) {
                    // 2. Plantilla HTML adaptada para la Oferta
                    String plantillaHtml = """
                <html>
                    <body style="font-family: Arial, sans-serif; color: #333333; line-height: 1.5;">
                        <h2 style="color: #8B0000;">¡Hola, {0}! 🎉</h2>
                        <p>El usuario <strong>{1}</strong> ha realizado una oferta por tu producto:</p>
                        <div style="background-color: #f8f9fa; padding: 15px; border-left: 4px solid #8B0000; margin: 15px 0;">
                            <h3 style="margin: 0; color: #333;">{2}</h3>
                            <p style="font-size: 1.2rem; font-weight: bold; color: #28a745; margin: 5px 0;">
                                Monto ofrecido: ${3} MXN
                            </p>
                        </div>
                        <p>Ingresa al marketplace para aceptar o rechazar la propuesta.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="font-size: 0.8rem; color: #777;">Marketplace Universitario</p>
                    </body>
                </html>
                """;

                    // Formatear el precio a 2 decimales para que se vea limpio (ej: 4500.00)
                    String montoFormateado = String.format("%.2f", montoOferta);

                    // 3. Rellenar la plantilla con los datos del vendedor, comprador y producto
                    String cuerpoCorreo = MessageFormat.format(
                            plantillaHtml,
                            vendedor.getNombre(),         // {0}
                            usuario.getNombre(),  // {1}
                            producto.getNombre(),         // {2}
                            montoFormateado               // {3}
                    );

                    // 4. Enviar usando tu EmailSender existente
                    EmailSender.sendMail(
                            vendedor.getCorreo(),
                            "¡Has recibido una oferta por " + producto.getNombre() + "!",
                            cuerpoCorreo
                    );
                }

                // 5. Redirigir con mensaje de exito
                response.sendRedirect("detalleProducto?id=" + idProducto + "&msg=ofertaExitosa");
            }else {
                response.sendRedirect("detalleProducto?id=" + idProducto + "&msg=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("inicio");
        }
    }
}