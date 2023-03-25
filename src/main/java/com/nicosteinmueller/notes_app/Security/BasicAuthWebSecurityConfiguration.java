package com.nicosteinmueller.notes_app.Security;

import com.nicosteinmueller.notes_app.Security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class BasicAuthWebSecurityConfiguration {

  @Autowired
  private AppBasicAuthenticationEntryPoint authenticationEntryPoint;
  @Autowired
  private CustomUserDetailsService customUserDetailsService;
  @Value("${app.api.version}")
  private String apiVersion;
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html
    http.authorizeHttpRequests()
      .requestMatchers(apiVersion+"/auth/register").permitAll()
      .anyRequest().authenticated()
      .and()
      .httpBasic()
      .authenticationEntryPoint(authenticationEntryPoint);
    http.csrf().disable(); //TODO remove workaround for enable post methode
    return http.build();
  }
  @Bean
  public AuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider =
            new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(this.customUserDetailsService);
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10,new SecureRandom());
  }
}
