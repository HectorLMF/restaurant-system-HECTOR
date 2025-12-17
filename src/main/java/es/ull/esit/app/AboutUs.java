package es.ull.esit.app;

/**
 * @brief "About us" window displaying the restaurant's history and info.
 *
 *        Simple Swing window that:
 *        - shows a static text area with the project story ("Our Story").
 *        - includes a navigation button to return to the previous screen
 *        CashierLogin.
 *
 *        Does not perform any network or database operations:
 *        all the content is embedded directly in the text area.
 */
public class AboutUs extends javax.swing.JFrame {

  /** @brief Static HTML content for the "Our Story" section. */
  private String textBlock = """
                  Our Story
                  Collecting flavors from all over the world to give everyone a taste of what they love.
                  Black Plate came in with this vision in mind, to be more of a home rather than an establishment.
                  2021 marked the beginning of the journey of Black Plate, starting from Al Khobar.
                  Because your visit means a lot to us, we would love to hear your suggestions and concerns so we can improve!
            """;

  /**
   * @brief Default constructor.
   *
   *        Creates an instance of the Info window and UI components.
   */
  public AboutUs() {
    initComponents();
  }

  /**
   * @brief Initializes and lays out Swing components.
   * 
   *        Generates by the Form Editor: don't modify manually.
   *
   *        Sets up the JFrame, including the main panel, the static text area
   *        with the
   *        restaurant story, the logo/image label, and the "Go Back" button.
   *        Also configure the layout and event handlers.
   * 
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated
  // Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jButton3 = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    ourStory = new javax.swing.JTextArea();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Info");
    setResizable(false);

    jPanel1.setBackground(new java.awt.Color(248, 244, 230));

    jButton3.setBackground(new java.awt.Color(255, 255, 255));
    jButton3.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
    jButton3.setText("Go Back");
    jButton3.addActionListener(this::jButton3ActionPerformed);

    ourStory.setEditable(false);
    ourStory.setBackground(new java.awt.Color(248, 244, 230));
    ourStory.setColumns(20);
    ourStory.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
    ourStory.setRows(5);
    ourStory.setText(textBlock);
    ourStory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jScrollPane1.setViewportView(ourStory);

    // Updated path to match other UI resources
    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/screenshot.png")));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1004,
                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(489, 489, 489)
                        .addComponent(jLabel1)))
                .addContainerGap(159, Short.MAX_VALUE)));
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(67, 67, 67)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @brief Handler for the "Go Back" button.
   *
   *        Closes the current Info window and opens the CashierLogin
   *        window, returning the user to the cashier login screen.
   *
   * @param evt Action event triggered by button click.
   */
  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
    new CashierLogin().setVisible(true);
    this.dispose(); // Close current window
  }// GEN-LAST:event_jButton3ActionPerformed

  /**
   * @brief Standalone entry point for testing the Info window.
   *
   *        Sets the Nimbus look and feel if available and shows an
   *        instance of Info. In the normal application flow this
   *        window is opened from CashierLogin via the "About us"
   *        button.
   *
   * @param args Command line arguments (not used).
   */
  public static void main(String[] args) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(AboutUs.class.getName())
          .log(java.util.logging.Level.SEVERE, null, ex);
    }
    // </editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(() -> new AboutUs().setVisible(true));
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // Sonarqube rule java:S1450 must be ignored here as these variables are
  // auto-generated by the Form Editor and need to remain as instance variables.
  /** @brief Button that navigates back to the cashier login window. */
  private javax.swing.JButton jButton3;
  /** @brief Logo or image displayed at the top of the Info window. */
  private javax.swing.JLabel jLabel1;
  /** @brief Main container panel for all components in the Info window. */
  private javax.swing.JPanel jPanel1;
  /** @brief Scroll pane that wraps the static "ourStory" text area. */
  private javax.swing.JScrollPane jScrollPane1;
  /** @brief Text area containing the restaurant history and description. */
  private javax.swing.JTextArea ourStory;
  // End of variables declaration//GEN-END:variables
}
