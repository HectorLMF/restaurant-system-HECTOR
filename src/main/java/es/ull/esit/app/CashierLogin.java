package es.ull.esit.app;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.service.ReportService;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * @brief Login window for authenticating cashiers.
 * 
 *        It is displayed after a successful login with role CASHIER.
 *        Shows a Swing form that lets cashiers:
 *        - access to the general menu window used to register orders.
 *        - access to the "About us" information window.
 *        - log out and return to the login window.
 * 
 *        Additionally, some actions perform background calls to the backend
 *        using ReportService to:
 *        - retrieve cashier information.
 *        - check the menu or system status.
 * 
 */
public class CashierLogin extends javax.swing.JFrame {

  /** Service used to retrieve reports and basic status from the backend. */
  private final ReportService reportService;

  /**
   * @brief Default constructor.
   *
   *        Creates a cashier window without a specific name in the welcome
   *        message.
   *        Internally delegates to the constructor that accepts a name.
   */
  public CashierLogin() {
    this((String) null);
  }

  /**
   * @brief Constructor with cashier name.
   *
   *        Creates the cashier window, initializes the GUI, builds an
   *        ApiClient pointing to the backend, creates a ReportService and
   *        updates the welcome message using the cashier name.
   *
   * @param name [String] Name of the cashier returned by the backend
   *             (or null for a generic welcome message).
   */
  public CashierLogin(String name) {
    initComponents();

    ApiClient client = new ApiClient("http://localhost:8080");
    this.reportService = new ReportService(client);

    updateWelcomeMessage(name);
  }

  /**
   * @brief Updates the welcome message label.
   * 
   *        If the name is null or empty, the label shows "Welcome Cashier".
   *        Otherwise the label shows "Welcome " followed by the given name.
   * 
   * @param name [String] Cashier name or null.
   */
  private void updateWelcomeMessage(String name) {
    if (name == null || name.trim().isEmpty()) {
      welcomeTxt.setText("Welcome Cashier");
    } else {
      welcomeTxt.setText("Welcome " + name);
    }
  }

  /**
   * @brief Action handler for the "About us" button.
   *
   *        Action steps:
   *        - Starts a background thread.
   *        - Uses ReportService.getCashierInfo() to retrieve cashier data from
   *        the backend and logs the number of cashiers found.
   *        - Opens the Info window in the Event Dispatch Thread.
   *        - Closes the current Cashier window.
   *
   *        Any error retrieving data is logged to the standard error stream
   *        but does not prevent opening the Info window.
   *
   * @param evt [java.awt.event.ActionEvent] Action event triggered by button
   *            click.
   */
  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    new Thread(() -> {
      try {
        List<es.ull.esit.app.middleware.model.Cashier> cashiers = reportService.getCashierInfo();
        System.out.println("Found " + cashiers.size() + " cashiers in DB.");

      } catch (Exception ex) {
        System.err.println("Warning: Could not fetch cashier stats: " + ex.getMessage());
      }
      SwingUtilities.invokeLater(() -> {
        new AboutUs().setVisible(true);
        dispose();
      });
    }).start();
  }

  /**
   * @brief Action handler for the "Menu" button.
   *
   *        Action steps:
   *        - Starts a background thread.
   *        - Uses ReportService.checkMenuStatus() to verify communication
   *        with the backend and logs the returned status.
   *        - Opens the general menu window (Frame1) in the Event Dispatch
   *        Thread.
   *        - Closes the current Cashier window.
   *
   *        Any error while checking the status is logged to the standard
   *        error stream but does not prevent opening the menu window.
   *
   * @param evt [java.awt.event.ActionEvent] Action event triggered by button
   *            click.
   */
  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
    new Thread(() -> {
      try {
        String status = reportService.checkMenuStatus();
        System.out.println(status);

      } catch (Exception ex) {
        System.err.println("Error checking menu status: " + ex.getMessage());
      }

      SwingUtilities.invokeLater(() -> {
        new Order().setVisible(true);
        dispose();
      });
    }).start();
  }

  /**
   * @brief Action handler for the "LogOut" button.
   *
   *        Action steps:
   *        - Opens the Login window.
   *        - Closes the current Cashier window.
   *
   *        Any server-side logout or token invalidation, if needed, should be
   *        handled outside this class.
   *
   * @param evt [java.awt.event.ActionEvent] Action event triggered by button
   *            click.
   */
  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
    new Login().setVisible(true);
    dispose();
  }

  /**
   * @brief Standalone entry point for testing the Cashier window.
   *
   *        Sets the Nimbus look and feel if available and shows an instance
   *        of Cashier without a specific cashier name. In the normal
   *        application flow this window is started from the Login class after
   *        a successful cashier login.
   *
   * @param args [String[]] Command line arguments (not used).
   */
  public static void main(String args[]) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(CashierLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(() -> new CashierLogin().setVisible(true));
  }

  /**
   * @brief Initializes GUI components.
   *
   *        Generated by the Form Editor: do not modify.
   *        Sets up the welcome label, logo image, buttons for "About us", "Menu",
   *        "LogOut",
   *        and the layout and configuration of the window.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated
  // Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    welcomeTxt = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jButton3 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Cashier");
    setResizable(false);

    jPanel1.setBackground(new java.awt.Color(248, 244, 230));

    welcomeTxt.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
    welcomeTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    welcomeTxt.setText("Welcome Cashier");

    jButton1.setBackground(new java.awt.Color(153, 153, 153));
    jButton1.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
    jButton1.setText("About us");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jButton2.setBackground(new java.awt.Color(153, 153, 153));
    jButton2.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
    jButton2.setText("Menu");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/screenshot.png"))); // NOI18N

    jButton3.setBackground(new java.awt.Color(255, 255, 255));
    jButton3.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
    jButton3.setText("LogOut");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(109, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 299,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(98, 98, 98))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(183, 183, 183))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(190, 190, 190)))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(108, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(99, 99, 99))));
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(welcomeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(290, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(242, 242, 242))));

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

  // Variables declaration - do not modify//GEN-BEGIN:variables
  /** Button that opens the "About us" information window. */
  private javax.swing.JButton jButton1;
  /** Button that opens the general menu window (Frame1). */
  private javax.swing.JButton jButton2;
  /** Button used to log out and return to the login screen. */
  private javax.swing.JButton jButton3;
  /** Label that displays the logo or application image. */
  private javax.swing.JLabel jLabel1;
  /** Main container panel for all UI elements. */
  private javax.swing.JPanel jPanel1;
  /** Label that displays the welcome message for the cashier. */
  private javax.swing.JLabel welcomeTxt;
  // End of variables declaration//GEN-END:variables
}