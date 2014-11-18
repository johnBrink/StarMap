/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
/**
 *
 * @author 7101020
 */
public class MainWindow2 extends javax.swing.JFrame {

    
    /**
     * Creates new form MainWindow2
     */
    public MainWindow2() {
        initComponents();
        this.setDefaultCloseOperation(MainWindow2.EXIT_ON_CLOSE);
        map.infoPanel = starInfo1;
        map.loadConstellations(FileLoader.getStars(), FileLoader.getConstellations());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        map = new starmap.StarMapPanel();
        starInfo1 = new starmap.StarInfo();
        jMenuBar1 = new javax.swing.JMenuBar();
        QuitButton = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        ViewMenu = new javax.swing.JMenu();
        PositionButton = new javax.swing.JMenuItem();
        ShowConst = new javax.swing.JCheckBoxMenuItem();
        HelpMenu = new javax.swing.JMenu();
        Usage = new javax.swing.JMenuItem();
        About = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mapLayout = new javax.swing.GroupLayout(map);
        map.setLayout(mapLayout);
        mapLayout.setHorizontalGroup(
            mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapLayout.createSequentialGroup()
                .addGap(0, 340, Short.MAX_VALUE)
                .addComponent(starInfo1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mapLayout.setVerticalGroup(
            mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapLayout.createSequentialGroup()
                .addComponent(starInfo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 203, Short.MAX_VALUE))
        );

        QuitButton.setText("File");

        jMenuItem1.setText("Quit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        QuitButton.add(jMenuItem1);

        jMenuBar1.add(QuitButton);

        ViewMenu.setText("View");

        PositionButton.setText("Set Position");
        PositionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PositionButtonActionPerformed(evt);
            }
        });
        ViewMenu.add(PositionButton);

        ShowConst.setSelected(true);
        ShowConst.setText("Show Constellations");
        ShowConst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowConstActionPerformed(evt);
            }
        });
        ViewMenu.add(ShowConst);

        jMenuBar1.add(ViewMenu);

        HelpMenu.setText("Help");

        Usage.setText("Usage");
        Usage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsageActionPerformed(evt);
            }
        });
        HelpMenu.add(Usage);

        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        HelpMenu.add(About);

        jMenuBar1.add(HelpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(map, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(map, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void UsageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsageActionPerformed
        
    }//GEN-LAST:event_UsageActionPerformed

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        String message = "StarMap v1.0.0\n   Matthew Rames\n   John Brink";
        JOptionPane.showMessageDialog(null, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_AboutActionPerformed

    private void PositionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PositionButtonActionPerformed

        ObserverDialog window = new ObserverDialog(this, true,
            (double latitude, double longitude, double altitude, double azimuth) -> {
                map.setPosition(latitude, longitude, altitude, azimuth);
            });
        window.setVisible(true);
        
    }//GEN-LAST:event_PositionButtonActionPerformed

    private void ShowConstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowConstActionPerformed
        JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem)evt.getSource();
        map.setShowConstellations(checkBox.getState());
    }//GEN-LAST:event_ShowConstActionPerformed

    /**
     * @param args the command line arguments
     */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow2().setVisible(true); 
            }      
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JMenuItem PositionButton;
    private javax.swing.JMenu QuitButton;
    private javax.swing.JCheckBoxMenuItem ShowConst;
    private javax.swing.JMenuItem Usage;
    private javax.swing.JMenu ViewMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private starmap.StarMapPanel map;
    private starmap.StarInfo starInfo1;
    // End of variables declaration//GEN-END:variables
}
