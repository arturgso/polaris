package io.vexis.polaris.application.security;

import io.vexis.polaris.shared.TextUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${app.bootstrap.admin.username:}")
  private String bootstrapAdminUsername;

  @Value("${app.bootstrap.admin.password:}")
  private String bootstrapAdminPassword;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> {})
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .anonymous(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(CorsUtils::isPreFlightRequest)
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(
                        "/auth/login",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/health")
                    .permitAll()
                    .requestMatchers("/vault/unlock")
                    .hasRole("ADMIN")
                    .requestMatchers("/vault/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/gift-lists/**", "/shopping-lists/**", "/statuses/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .hasRole("ADMIN"))
        .authenticationProvider(authenticationProvider(userDetailsService()))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    if (!StringUtils.hasText(bootstrapAdminUsername)) {
      throw new IllegalStateException("BOOTSTRAP_ADMIN_USERNAME is required");
    }

    if (!StringUtils.hasText(bootstrapAdminPassword)) {
      throw new IllegalStateException("BOOTSTRAP_ADMIN_PASSWORD is required");
    }

    String normalizedUsername = TextUtils.normalizeText(bootstrapAdminUsername);

    // TODO: Add a safe credential rotation flow without requiring application restart.
    return new InMemoryUserDetailsManager(
        User.withUsername(normalizedUsername)
            .password(passwordEncoder().encode(bootstrapAdminPassword))
            .roles("ADMIN")
            .build());
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
