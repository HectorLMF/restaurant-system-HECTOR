package es.ull.esit.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @brief Spring Security configuration for the backend.
 *        Defines:
 *        - Password encoder used for hashing passwords.
 *        - HTTP endpoint security policies: all endpoints are currently open
 *        (no authentication required).
 * 
 *        The login logic is handled manually in the AuthController.
 */
@Configuration
public class SecurityConfig {

  /**
   * @brief Creates a password encoder using BCrypt hashing algorithm.
   * 
   * @return [PasswordEncoder] The BCrypt-based password encoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * @brief Configures the HTTP security settings:
   *        - Disables CSRF protection.
   *        - Permits unauthenticated access to specified endpoints (currently
   *        all).
   *        - Enables HTTP Basic auth (username and password in headers).
   * 
   * @param http [HttpSecurity] The HttpSecurity builder to define security
   *             policies.
   * @return [SecurityFilterChain] The configured security filter chain.
   * @throws Exception If an error occurs during configuration.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable();

    http
        .authorizeRequests()
        .anyRequest().permitAll();

    http
        .httpBasic().disable()
        .formLogin().disable();

    return http.build();
  }
}
