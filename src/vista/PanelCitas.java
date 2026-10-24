/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

/**
 *
 * @author F
 */
public class PanelCitas extends javax.swing.JPanel {
 // Atributos de la clase
  private javax.swing.table.DefaultTableModel modeloTabla;
  private java.util.List<modelo.Cita> listaCitas = new java.util.ArrayList<>();
  private java.util.List<modelo.Paciente> listaPacientes = new java.util.ArrayList<>();
  private java.util.List<modelo.Medico> listaMedicos = new java.util.ArrayList<>();
  private dao.CitaDAO citaDAO = new dao.CitaDAO();
  private dao.PacienteDAO pacienteDAO = new dao.PacienteDAO();
  private dao.MedicoDAO medicoDAO = new dao.MedicoDAO();
  private int idEditando = -1;
public PanelCitas() {
    initComponents();

    modeloTabla = new javax.swing.table.DefaultTableModel(
        new String[]{"ID", "Paciente", "Médico", "Fecha", "Hora", "Motivo", "Estado"}, 0);
    Tbcitas.setModel(modeloTabla);
    Tbcitas.setDefaultEditor(Object.class, null);

    txFecha.setToolTipText("Escribe la fecha así: 20-10-2026 (día-mes-año)");

    // Texto de ayuda en gris: se ve cuando el campo está vacío y desaparece al escribir
    final javax.swing.border.Border bordeOriginal = txFecha.getBorder();
    txFecha.setBorder(new javax.swing.border.Border() {
        @Override
        public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int w, int h) {
            bordeOriginal.paintBorder(c, g, x, y, w, h);
            if (txFecha.getText().isEmpty()) {
                java.awt.Insets in = bordeOriginal.getBorderInsets(c);
                g.setColor(java.awt.Color.GRAY);
                g.setFont(c.getFont());
                java.awt.FontMetrics fm = g.getFontMetrics();
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString("DD-MM-AAAA", in.left, ty);
            }
        }

        @Override
        public java.awt.Insets getBorderInsets(java.awt.Component c) {
            return bordeOriginal.getBorderInsets(c);
        }

        @Override
        public boolean isBorderOpaque() {
            return bordeOriginal.isBorderOpaque();
        }
    });

    // Pone los guiones automáticamente y no deja escribir días o meses inválidos
    ((javax.swing.text.AbstractDocument) txFecha.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {

        // Revisa que los números escritos hasta ahora sean posibles (d = solo dígitos)
        private boolean valido(String d) {
            if (d.length() >= 1 && d.charAt(0) > '3') {
                return false;
            }
            if (d.length() >= 2) {
                int dia = Integer.parseInt(d.substring(0, 2));
                if (dia < 1 || dia > 31) {
                    return false;
                }
            }
            if (d.length() >= 3 && d.charAt(2) > '1') {
                return false;
            }
            if (d.length() >= 4) {
                int dia = Integer.parseInt(d.substring(0, 2));
                int mes = Integer.parseInt(d.substring(2, 4));
                if (mes < 1 || mes > 12) {
                    return false;
                }
                int maxDia = 31;
                if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                    maxDia = 30;
                } else if (mes == 2) {
                    maxDia = 29;
                }
                if (dia > maxDia) {
                    return false;
                }
            }
            if (d.length() >= 5 && d.charAt(4) != '2') {
                return false;
            }
            return true;
        }

        private void poner(FilterBypass fb, String texto) throws javax.swing.text.BadLocationException {
            String d = texto.replaceAll("\\D", "");
            if (d.length() > 8) {
                d = d.substring(0, 8);
            }
            if (!valido(d)) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < d.length(); i++) {
                if (i == 2 || i == 4) {
                    sb.append('-');
                }
                sb.append(d.charAt(i));
            }
            fb.replace(0, fb.getDocument().getLength(), sb.toString(), null);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevo = new StringBuilder(actual).replace(offset, offset + length, text == null ? "" : text).toString();
            poner(fb, nuevo);
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
            replace(fb, offset, 0, text, attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws javax.swing.text.BadLocationException {
            replace(fb, offset, length, "", null);
        }
    });

    llenarHoras();
    llenarEstados();
    cargarListas();
    llenarTabla();
    limpiarCampos();

    // Cada vez que se muestra este panel se refrescan los combos y la tabla
    addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentShown(java.awt.event.ComponentEvent e) {
            cargarListas();
            llenarTabla();
        }
    });
}

// Llena los combos de paciente y médico desde la base de datos
private void cargarListas() {
    cbPaciente.removeAllItems();
    cbPaciente.addItem("Seleccione un paciente");
    listaPacientes = pacienteDAO.listar();
    for (modelo.Paciente p : listaPacientes) {
        cbPaciente.addItem(p.getNombre() + " - " + p.getCedula());
    }

    cbMedico.removeAllItems();
    cbMedico.addItem("Seleccione un médico");
    listaMedicos = medicoDAO.listar();
    for (modelo.Medico m : listaMedicos) {
        cbMedico.addItem(m.getNombre() + " - " + m.getCedula());
    }
}

// Horas de 08:00 a 17:30, cada 30 minutos
private void llenarHoras() {
    cbHora.removeAllItems();
    cbHora.addItem("Seleccione la hora");
    for (int h = 8; h <= 17; h++) {
        cbHora.addItem(String.format("%02d:00", h));
        cbHora.addItem(String.format("%02d:30", h));
    }
}

private void llenarEstados() {
    cbEstado.removeAllItems();
    cbEstado.addItem("Pendiente");
    cbEstado.addItem("Atendida");
    cbEstado.addItem("Cancelada");
}

// Dibuja la tabla con las citas de la base de datos
private void llenarTabla() {
    modeloTabla.setRowCount(0);
    listaCitas = citaDAO.listar();
    for (modelo.Cita c : listaCitas) {
        modeloTabla.addRow(new Object[]{
            c.getId(),
            c.getPacienteNombre(),
            c.getMedicoNombre(),
            aVista(c.getFecha()),
            c.getHora(),
            c.getMotivo(),
            c.getEstado()
        });
    }
}

private void limpiarCampos() {
    cbPaciente.setSelectedIndex(0);
    cbMedico.setSelectedIndex(0);
    txFecha.setText("");
    cbHora.setSelectedIndex(0);
    txMotivo.setText("");
    cbEstado.setSelectedIndex(0);
    Tbcitas.clearSelection();
    idEditando = -1;
    btEditar.setText("Editar");
}

// Convierte lo que escribe el usuario (DD-MM-AAAA) a AAAA-MM-DD. Devuelve null si es inválida.
private String aIso(String texto) {
    try {
        java.time.format.DateTimeFormatter f = java.time.format.DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(java.time.format.ResolverStyle.STRICT);
        return java.time.LocalDate.parse(texto, f).toString();
    } catch (java.time.format.DateTimeParseException e) {
        return null;
    }
}

// Convierte AAAA-MM-DD (base de datos) a DD-MM-AAAA para mostrarla.
private String aVista(String iso) {
    try {
        return java.time.LocalDate.parse(iso)
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    } catch (Exception e) {
        return iso;
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelCitas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPaciente = new javax.swing.JLabel();
        cbPaciente = new javax.swing.JComboBox<>();
        jFecha = new javax.swing.JLabel();
        txFecha = new javax.swing.JTextField();
        jMedico = new javax.swing.JLabel();
        cbMedico = new javax.swing.JComboBox<>();
        jMotivo = new javax.swing.JLabel();
        txMotivo = new javax.swing.JTextField();
        jHora = new javax.swing.JLabel();
        cbHora = new javax.swing.JComboBox<>();
        jEstado = new javax.swing.JLabel();
        cbEstado = new javax.swing.JComboBox<>();
        btGuardar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btEliminar = new javax.swing.JButton();
        btLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tbcitas = new javax.swing.JTable();

        PanelCitas.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("Gestión de Citas");

        jPaciente.setText("Paciente:");

        cbPaciente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jFecha.setText("Fecha:");

        txFecha.setText("DD-MM-AAAA");
        txFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txFechaActionPerformed(evt);
            }
        });

        jMedico.setText("Médico:");

        cbMedico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jMotivo.setText("Motivo:");

        jHora.setText("Hora:");

        cbHora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jEstado.setText("Estado:");

        cbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btGuardar.setText("Guardar");
        btGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarActionPerformed(evt);
            }
        });

        btEditar.setText("Editar");
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btEliminar.setText("Eliminar");
        btEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEliminarActionPerformed(evt);
            }
        });

        btLimpiar.setText("Limpiar");
        btLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimpiarActionPerformed(evt);
            }
        });

        Tbcitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Paciente ", "Médico", "Fecha", "Hora", "Motivo", "Estado"
            }
        ));
        jScrollPane1.setViewportView(Tbcitas);

        javax.swing.GroupLayout PanelCitasLayout = new javax.swing.GroupLayout(PanelCitas);
        PanelCitas.setLayout(PanelCitasLayout);
        PanelCitasLayout.setHorizontalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(PanelCitasLayout.createSequentialGroup()
                                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPaciente)
                                    .addComponent(jFecha)
                                    .addComponent(jMotivo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txMotivo)
                                    .addComponent(cbPaciente, javax.swing.GroupLayout.Alignment.LEADING, 0, 250, Short.MAX_VALUE)
                                    .addComponent(txFecha, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(62, 62, 62)
                                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCitasLayout.createSequentialGroup()
                                        .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jMedico)
                                            .addComponent(jHora))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(PanelCitasLayout.createSequentialGroup()
                                        .addComponent(jEstado)
                                        .addGap(17, 17, 17)))
                                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cbEstado, 0, 250, Short.MAX_VALUE)
                                    .addComponent(cbMedico, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbHora, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(PanelCitasLayout.createSequentialGroup()
                                .addComponent(btGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btEditar)
                                .addGap(18, 18, 18)
                                .addComponent(btEliminar)
                                .addGap(18, 18, 18)
                                .addComponent(btLimpiar)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelCitasLayout.setVerticalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPaciente)
                    .addComponent(cbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMedico)
                    .addComponent(cbMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFecha)
                    .addComponent(txFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jHora)
                    .addComponent(cbHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jMotivo)
                    .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jEstado)
                        .addComponent(cbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btGuardar)
                    .addComponent(btEditar)
                    .addComponent(btEliminar)
                    .addComponent(btLimpiar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
if (cbPaciente.getSelectedIndex() <= 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un paciente");
    return;
}
if (cbMedico.getSelectedIndex() <= 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un médico");
    return;
}
String fecha = aIso(txFecha.getText().trim());
if (fecha == null) {
    javax.swing.JOptionPane.showMessageDialog(this, "Escribe la fecha con el formato DD-MM-AAAA, por ejemplo 20-10-2026");
    return;
}
if (java.time.LocalDate.parse(fecha).isBefore(java.time.LocalDate.now())) {
    javax.swing.JOptionPane.showMessageDialog(this, "La fecha no puede ser anterior a hoy");
    return;
}
if (cbHora.getSelectedIndex() <= 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "Selecciona la hora");
    return;
}

String pacienteCedula = listaPacientes.get(cbPaciente.getSelectedIndex() - 1).getCedula();
String medicoCedula = listaMedicos.get(cbMedico.getSelectedIndex() - 1).getCedula();
String hora = cbHora.getSelectedItem().toString();

if (citaDAO.existeCruce(medicoCedula, fecha, hora, -1)) {
    javax.swing.JOptionPane.showMessageDialog(this, "Ese médico ya tiene una cita a esa fecha y hora");
    return;
}

modelo.Cita c = new modelo.Cita(
    pacienteCedula,
    medicoCedula,
    fecha,
    hora,
    txMotivo.getText().trim(),
    cbEstado.getSelectedItem().toString()
);
if (citaDAO.insertar(c)) {
    llenarTabla();
    limpiarCampos();
} else {
    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo guardar la cita");
}
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimpiarActionPerformed
     limpiarCampos();
    }//GEN-LAST:event_btLimpiarActionPerformed

    private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
int fila = Tbcitas.getSelectedRow();
if (fila < 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una cita de la tabla");
    return;
}
int respuesta = javax.swing.JOptionPane.showConfirmDialog(
    this, "¿Seguro que deseas eliminar esta cita?", "Confirmar",
    javax.swing.JOptionPane.YES_NO_OPTION);
if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
    citaDAO.eliminar(listaCitas.get(fila).getId());
    llenarTabla();
    limpiarCampos();
}
    }//GEN-LAST:event_btEliminarActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
if (idEditando == -1) {
    // Primer clic: cargar los datos de la cita seleccionada
    int fila = Tbcitas.getSelectedRow();
    if (fila < 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una cita de la tabla");
        return;
    }
    modelo.Cita c = listaCitas.get(fila);

    cbPaciente.setSelectedIndex(0);
    for (int i = 0; i < listaPacientes.size(); i++) {
        if (listaPacientes.get(i).getCedula().equals(c.getPacienteCedula())) {
            cbPaciente.setSelectedIndex(i + 1);
            break;
        }
    }
    cbMedico.setSelectedIndex(0);
    for (int i = 0; i < listaMedicos.size(); i++) {
        if (listaMedicos.get(i).getCedula().equals(c.getMedicoCedula())) {
            cbMedico.setSelectedIndex(i + 1);
            break;
        }
    }
    txFecha.setText(aVista(c.getFecha()));
    cbHora.setSelectedItem(c.getHora());
    txMotivo.setText(c.getMotivo());
    cbEstado.setSelectedItem(c.getEstado());

    idEditando = c.getId();
    btEditar.setText("Actualizar");
} else {
    // Segundo clic: guardar los cambios en la base de datos
    if (cbPaciente.getSelectedIndex() <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un paciente");
        return;
    }
    if (cbMedico.getSelectedIndex() <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un médico");
        return;
    }
    String fecha = aIso(txFecha.getText().trim());
    if (fecha == null) {
        javax.swing.JOptionPane.showMessageDialog(this, "Escribe la fecha con el formato DD-MM-AAAA, por ejemplo 20-10-2026");
        return;
    }
    if (cbHora.getSelectedIndex() <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona la hora");
        return;
    }

    String pacienteCedula = listaPacientes.get(cbPaciente.getSelectedIndex() - 1).getCedula();
    String medicoCedula = listaMedicos.get(cbMedico.getSelectedIndex() - 1).getCedula();
    String hora = cbHora.getSelectedItem().toString();

    if (citaDAO.existeCruce(medicoCedula, fecha, hora, idEditando)) {
        javax.swing.JOptionPane.showMessageDialog(this, "Ese médico ya tiene una cita a esa fecha y hora");
        return;
    }

    modelo.Cita c = new modelo.Cita(
        idEditando,
        pacienteCedula,
        medicoCedula,
        fecha,
        hora,
        txMotivo.getText().trim(),
        cbEstado.getSelectedItem().toString()
    );
    if (citaDAO.actualizar(c)) {
        llenarTabla();
        limpiarCampos();
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "No se pudo actualizar la cita");
    }
}
    }//GEN-LAST:event_btEditarActionPerformed

    private void txFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txFechaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCitas;
    private javax.swing.JTable Tbcitas;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btLimpiar;
    private javax.swing.JComboBox<String> cbEstado;
    private javax.swing.JComboBox<String> cbHora;
    private javax.swing.JComboBox<String> cbMedico;
    private javax.swing.JComboBox<String> cbPaciente;
    private javax.swing.JLabel jEstado;
    private javax.swing.JLabel jFecha;
    private javax.swing.JLabel jHora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jMedico;
    private javax.swing.JLabel jMotivo;
    private javax.swing.JLabel jPaciente;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txFecha;
    private javax.swing.JTextField txMotivo;
    // End of variables declaration//GEN-END:variables
}
