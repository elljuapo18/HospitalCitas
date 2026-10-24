package modelo;

public class Medico {

    private String cedula;
    private String nombre;
    private String telefono;
    private int especialidadId;
    private String especialidadNombre; // solo para mostrar en la tabla

    public Medico() {
    }

    public Medico(String cedula, String nombre, String telefono, int especialidadId) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.especialidadId = especialidadId;
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(int especialidadId) { this.especialidadId = especialidadId; }

    public String getEspecialidadNombre() { return especialidadNombre; }
    public void setEspecialidadNombre(String especialidadNombre) { this.especialidadNombre = especialidadNombre; }
}