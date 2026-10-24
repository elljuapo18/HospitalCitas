package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    private static final String URL = "jdbc:sqlite:hospital.db";

    // Abre la conexión con la base de datos (si el archivo no existe, lo crea)
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Crea las tablas la primera vez que se ejecuta el programa
        public static void crearTablas() {
        String[] sentencias = {
            "CREATE TABLE IF NOT EXISTS pacientes ("
                + "cedula TEXT PRIMARY KEY, "
                + "nombre TEXT NOT NULL, "
                + "telefono TEXT, "
                + "direccion TEXT)",

            "CREATE TABLE IF NOT EXISTS especialidades ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL UNIQUE)",

            "CREATE TABLE IF NOT EXISTS medicos ("
                + "cedula TEXT PRIMARY KEY, "
                + "nombre TEXT NOT NULL, "
                + "telefono TEXT, "
                + "especialidad_id INTEGER, "
                + "FOREIGN KEY (especialidad_id) REFERENCES especialidades(id))",

            "CREATE TABLE IF NOT EXISTS citas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "paciente_cedula TEXT NOT NULL, "
                + "medico_cedula TEXT NOT NULL, "
                + "fecha TEXT NOT NULL, "
                + "hora TEXT NOT NULL, "
                + "motivo TEXT, "
                + "estado TEXT DEFAULT 'Pendiente', "
                + "FOREIGN KEY (paciente_cedula) REFERENCES pacientes(cedula), "
                + "FOREIGN KEY (medico_cedula) REFERENCES medicos(cedula))",

            "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "usuario TEXT NOT NULL UNIQUE, "
                + "clave TEXT NOT NULL, "
                + "rol TEXT NOT NULL)"
        };

        try (Connection con = conectar(); Statement st = con.createStatement()) {
            for (String sql : sentencias) {
                st.execute(sql);
            }
            System.out.println("Base de datos lista");
        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }
    // Prueba rápida de la conexión
    public static void main(String[] args) {
        crearTablas();
    }
}
