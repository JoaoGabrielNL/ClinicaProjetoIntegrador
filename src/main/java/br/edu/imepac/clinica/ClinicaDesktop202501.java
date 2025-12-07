
package br.edu.imepac.clinica;
import br.edu.imepac.clinica.screens.LoginForm; 

public class ClinicaDesktop202501 {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClinicaDesktop202501.class.getName());
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }
}