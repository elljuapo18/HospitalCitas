package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Especialidad;

public class EspecialidadDAO {

    // READ: trae todas las especialidades
    public ArrayList<Especialidad> listar() {
        ArrayList<Especialidad> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM especialidades ORDER BY nombre";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Especialidad(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar especialidades: " + e.getMessage());
        }
        return lista;
    }

    // Inserta especialidades de ejemplo solo si la tabla está vacía
    public void insertarPorDefecto() {
        String contar = "SELECT COUNT(*) FROM especialidades";
        String insertar = "INSERT INTO especialidades (nombre) VALUES "
                + "('Medicina General'), ('Pediatría'), ('Cardiología'), "
                + "('Ginecología'), ('Odontología'), ('Dermatología')";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(contar)) {
            if (rs.next() && rs.getInt(1) == 0) {
                st.executeUpdate(insertar);
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar especialidades: " + e.getMessage());
        }
    }
}
