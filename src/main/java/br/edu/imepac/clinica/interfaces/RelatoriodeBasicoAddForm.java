/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.edu.imepac.clinica.interfaces;

/**
 *
 * @author Geovani
 */
public class RelatoriodeBasicoAddForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RelatoriodeBasicoAddForm.class.getName());

   
    public RelatoriodeBasicoAddForm() {
        initComponents();
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setText("Relatorio Basico da Clinica");

        jTextPane1.setText("O aplicativo da clínica tem como objetivo principal facilitar o agendamento de consultas, a gestão de prontuários eletrônicos, e promover a comunicação eficiente entre médicos, pacientes e atendentes. Ele integra diversas funcionalidades para melhorar a experiência do paciente e otimizar a administração da clínica.  Funcionalidades Principais:  Login/Cadastro de Usuário:  Objetivo: Permitir o acesso seguro para pacientes, médicos, atendentes e administradores, com login simples e registro.  Funcionalidade: Cadastro de usuários com diferentes níveis de acesso para cada tipo de usuário da plataforma.  Agenda e Consultas:  Objetivo: Facilitar o agendamento, visualização e cancelamento de consultas.  Funcionalidade: Pacientes podem agendar, visualizar e cancelar consultas, enquanto médicos e atendentes gerenciam a agenda de atendimentos.  Atendimento e Prontuário Eletrônico:  Objetivo: Facilitar o registro e acompanhamento do histórico médico dos pacientes.  Funcionalidade: Médicos podem acessar e atualizar os dados dos pacientes, incluindo histórico de consultas, diagnósticos e exames realizados.  Especialidades Médicas:  Objetivo: Organizar e categorizar as especialidades médicas disponíveis para facilitar a busca de profissionais.  Funcionalidade: Pacientes podem buscar médicos e serviços de acordo com a especialidade médica necessária.  Convênios:  Objetivo: Permitir que pacientes consultem os convênios aceitos pela clínica.  Funcionalidade: Pacientes podem verificar se seus planos de saúde são aceitos na clínica e consultar as opções de convênios disponíveis.  Portal do Paciente:  Objetivo: Oferecer aos pacientes um portal de fácil acesso ao seu histórico médico.  Funcionalidade: Pacientes podem acessar o histórico de consultas, exames e prescrições médicas anteriores.  Pesquisa de Satisfação:  Objetivo: Coletar feedback dos pacientes sobre o atendimento médico e a experiência geral com o sistema.  Funcionalidade: O aplicativo realiza pesquisas de satisfação periódicas, permitindo à clínica melhorar a qualidade do atendimento com base nos dados coletados.");
        jScrollPane2.setViewportView(jTextPane1);

        jButton1.setText("Ok");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(319, 319, 319)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton1)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    // Fecha a janela ao clicar em "Ok"
    this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new RelatoriodeBasicoAddForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
