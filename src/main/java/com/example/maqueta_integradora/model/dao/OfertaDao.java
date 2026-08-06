package com.example.maqueta_integradora.model.dao;

import com.example.maqueta_integradora.model.Oferta;
import com.example.maqueta_integradora.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OfertaDao {

    public boolean guardarOferta(Oferta oferta) {
        String sql = "INSERT INTO oferta (monto_oferta, estado, id_usuario, id_producto) VALUES (?, 0, ?, ?)";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, oferta.getMontoOferta());
            ps.setInt(2, oferta.getIdUsuario());
            ps.setInt(3, oferta.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar la oferta.");
            e.printStackTrace();
            return false;
        }
    }
}