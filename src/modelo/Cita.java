package modelo;

public class Cita {

    private int id;
    private String pacienteCedula;
    private String medicoCedula;
    private String fecha;
    private String hora;
    private String motivo;
    private String estado;

    // Solo para mostrar en la tabla
    private String pacienteNombre;
    private String medicoNombre;

    public Cita() {
    }

    public Cita(int id, String pacienteCedula, String medicoCedula,
                String fecha, String hora, String motivo, String estado) {
        this.id = id;
        this.pacienteCedula = pacienteCedula;
        this.medicoCedula = medicoCedula;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }

    public Cita(String pacienteCedula, String medicoCedula,
                String fecha, String hora, String motivo, String estado) {
        this.pacienteCedula = pacienteCedula;
        this.medicoCedula = medicoCedula;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPacienteCedula() { return pacienteCedula; }
    public void setPacienteCedula(String pacienteCedula) { this.pacienteCedula = pacienteCedula; }

    public String getMedicoCedula() { return medicoCedula; }
    public void setMedicoCedula(String medicoCedula) { this.medicoCedula = medicoCedula; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
}