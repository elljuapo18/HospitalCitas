package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Usuario;

public class UsuarioDAO {

    // CREATE: guarda un usuario nuevo
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (usuario, clave, rol) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getClave());
            ps.setString(3, u.getRol());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // READ: trae todos los usuarios
    public ArrayList<Usuario> listar() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, usuario, clave, rol FROM usuarios";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("clave"),
                    rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // UPDATE: actualiza un usuario (se busca por su id)
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET usuario = ?, clave = ?, rol = ? WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getClave());
            ps.setString(3, u.getRol());
            ps.setInt(4, u.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // DELETE: elimina un usuario por su id
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // Crea un administrador de ejemplo solo si la tabla está vacía
    public void insertarAdminPorDefecto() {
        String contar = "SELECT COUNT(*) FROM usuarios";
        String insertar = "INSERT INTO usuarios (usuario, clave, rol) VALUES ('admin', 'admin123', 'Administrador')";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(contar)) {
            if (rs.next() && rs.getInt(1) == 0) {
                st.executeUpdate(insertar);
            }
        } catch (SQLException e) {
            System.out.println("Error al crear admin: " + e.getMessage());
        }
    }
}