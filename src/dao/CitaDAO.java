package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Cita;

public class CitaDAO {

    // CREATE: guarda una cita nueva
    public boolean insertar(Cita c) {
        String sql = "INSERT INTO citas (paciente_cedula, medico_cedula, fecha, hora, motivo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getPacienteCedula());
            ps.setString(2, c.getMedicoCedula());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getMotivo());
            ps.setString(6, c.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar cita: " + e.getMessage());
            return false;
        }
    }

    // READ: trae todas las citas con el nombre del paciente y del médico
    public ArrayList<Cita> listar() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.paciente_cedula, c.medico_cedula, c.fecha, c.hora, c.motivo, c.estado, "
                + "p.nombre AS paciente, m.nombre AS medico "
                + "FROM citas c "
                + "LEFT JOIN pacientes p ON c.paciente_cedula = p.cedula "
                + "LEFT JOIN medicos m ON c.medico_cedula = m.cedula "
                + "ORDER BY c.fecha, c.hora";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cita c = new Cita(
                    rs.getInt("id"),
                    rs.getString("paciente_cedula"),
                    rs.getString("medico_cedula"),
                    rs.getString("fecha"),
                    rs.getString("hora"),
                    rs.getString("motivo"),
                    rs.getString("estado")
                );
                c.setPacienteNombre(rs.getString("paciente"));
                c.setMedicoNombre(rs.getString("medico"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }

    // UPDATE: actualiza una cita (se busca por su id)
    public boolean actualizar(Cita c) {
        String sql = "UPDATE citas SET paciente_cedula = ?, medico_cedula = ?, fecha = ?, hora = ?, "
                + "motivo = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getPacienteCedula());
            ps.setString(2, c.getMedicoCedula());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getMotivo());
            ps.setString(6, c.getEstado());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }

    // DELETE: elimina una cita por su id
    public boolean eliminar(int id) {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cita: " + e.getMessage());
            return false;
        }
    }

    // VALIDACIÓN: ¿el médico ya tiene una cita a esa fecha y hora?
    // idExcluir sirve al editar, para no compararla consigo misma (usar -1 si la cita es nueva)
    public boolean existeCruce(String medicoCedula, String fecha, String hora, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM citas "
                + "WHERE medico_cedula = ? AND fecha = ? AND hora = ? "
                + "AND estado <> 'Cancelada' AND id <> ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medicoCedula);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            ps.setInt(4, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al validar cruce: " + e.getMessage());
            return false;
        }
    }
}