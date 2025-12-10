package es.ull.esit.app.middleware;

/**
 * @brief Custom exception class for API client errors.
 * 
 * Inherits from RuntimeException to represent exceptions that occur
 * during API client operations.
 */
public class ApiClientException extends RuntimeException {

  /**
   * @brief Constructs a new ApiClientException with the specified detail message.
   *
   * @param message [String] Detail message for the exception.
   */
  public ApiClientException(String message) {
    super(message);
  }

  /**
   * @brief Constructs a new ApiClientException with the specified detail message and cause.
   * 
   * @param message [String] Detail message for the exception.
   * @param cause [Throwable] The cause of the exception.
   */
  public ApiClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
