package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Medico;

public class MedicoDAO {

    // CREATE: guarda un médico nuevo
    public boolean insertar(Medico m) {
        String sql = "INSERT INTO medicos (cedula, nombre, telefono, especialidad_id) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getCedula());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getTelefono());
            ps.setInt(4, m.getEspecialidadId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar médico: " + e.getMessage());
            return false;
        }
    }

    // READ: trae todos los médicos con el nombre de su especialidad
    public ArrayList<Medico> listar() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "SELECT m.cedula, m.nombre, m.telefono, m.especialidad_id, e.nombre AS especialidad "
                + "FROM medicos m LEFT JOIN especialidades e ON m.especialidad_id = e.id";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medico m = new Medico(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getInt("especialidad_id")
                );
                m.setEspecialidadNombre(rs.getString("especialidad"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar médicos: " + e.getMessage());
        }
        return lista;
    }

    // UPDATE: actualiza un médico (se busca por la cédula anterior)
    public boolean actualizar(Medico m, String cedulaAnterior) {
        String sql = "UPDATE medicos SET cedula = ?, nombre = ?, telefono = ?, especialidad_id = ? WHERE cedula = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getCedula());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getTelefono());
            ps.setInt(4, m.getEspecialidadId());
            ps.setString(5, cedulaAnterior);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar médico: " + e.getMessage());
            return false;
        }
    }

    // DELETE: elimina un médico por su cédula
    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM medicos WHERE cedula = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar médico: " + e.getMessage());
            return false;
        }
    }
}