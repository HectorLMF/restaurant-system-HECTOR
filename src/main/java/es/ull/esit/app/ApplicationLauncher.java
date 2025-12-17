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
   *        Creates and displays the application's Login screen using an
   *        injectable
   *        factory (test seam) so tests can avoid creating real UI components.
   *
   * @param args [String[]] Command-line arguments (unused).
   */
  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> getLoginFactory().get().setVisible(true));
  }

  /**
   * @brief Factory (test seam) used to create Login instances.
   *
   *        Tests can override this supplier to inject a stub/mocked Login and
   *        avoid creating real Swing components (e.g., in headless CI).
   */
  private static java.util.function.Supplier<Login> loginFactory = Login::new;

  /**
   * @brief Sets the factory used to create Login instances (test seam).
   *        Allows tests to inject mock implementations.
   * 
   * @param factory [java.util.function.Supplier<Login>] Factory to create Login
   *                instances.
   */
  static void setLoginFactory(java.util.function.Supplier<Login> factory) {
    loginFactory = factory;
  }

  /**
   * @brief Gets the current factory used to create Login instances (test seam).
   *        Allows tests to verify or use the current factory.
   * 
   * @return [java.util.function.Supplier<Login>] Current factory for Login
   *         instances.
   */
  static java.util.function.Supplier<Login> getLoginFactory() {
    return loginFactory;
  }
}
