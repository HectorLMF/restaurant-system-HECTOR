package es.ull.esit.app;

/**
 * @brief Auxiliary entry point used to start the application's UI.
 *
 *        Serves as a simple launcher responsible for opening
 *        the Login window when the program is executed directly.
 *
 */
public class ApplicationLauncher {

  /**
   * @brief Alternate entry point used to launch the Login window.
   *
   *        Creates and displays the application's Login screen.
   *
   * @param args [String[]] Command-line arguments (unused).
   */
  public static void main(String[] args) {
    new Login().setVisible(true);
  }
}
