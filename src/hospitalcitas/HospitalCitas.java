/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hospitalcitas;

/**
 *
 * @author F
 */
public class HospitalCitas {
public static void main(String[] args) {
    // Estilo Nimbus (botones redondeados)
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception e) {
        System.out.println("No se pudo aplicar el estilo: " + e.getMessage());
    }

    // Abre la ventana con un tamaño cómodo y centrada
    java.awt.EventQueue.invokeLater(() -> {
        vista.VentanaPrincipal ventana = new vista.VentanaPrincipal();
        ventana.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    });
}
}
