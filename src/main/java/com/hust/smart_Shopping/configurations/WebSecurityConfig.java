package com.hust.smart_Shopping.configurations;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.hust.smart_Shopping.filters.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

        private final JwtTokenFilter jwtTokenFilter;
        private final AuthenticationProvider authenticationProvider;

        @Value("${api.prefix}")
        private String apiPrefix;

        @Value("${domain.protocol}")
        private String domainProtocol;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(AbstractHttpConfigurer::disable) // Enable CORS

                                .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                                .authorizeHttpRequests(request -> request
                                                .requestMatchers("/error", "/css/**", "/js/**", "/libs/**",
                                                                "/images/**",
                                                                "/auth/**", "/**", "/user", "/favicon.ico")
                                                .permitAll() // MVC

                                                .requestMatchers(HttpMethod.GET,
                                                                String.format("%s/role/**", apiPrefix),
                                                                // sagger-ui
                                                                "/v2/api-docs",
                                                                "/v3/api-docs",
                                                                "/v3/api-docs/**",
                                                                "/swagger-resources/**",
                                                                "/swagger-ui.html",
                                                                "/webjars/**",
                                                                "/swagger-resources/configuration/ui",
                                                                "/swagger-resources/configuration/security",
                                                                "/swagger-ui.html/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST,
                                                                String.format("%s/user/login", apiPrefix),
                                                                String.format("%s/user", apiPrefix),
                                                                String.format("%s/user/send-verification-code",
                                                                                apiPrefix),
                                                                String.format("%s/user/refresh-token", apiPrefix))
                                                .permitAll()

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/user/account")
                                                .permitAll())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authenticationProvider(authenticationProvider)

                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint((request, response, authException) -> response
                                                                .sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                                                                authException.getMessage()))
                                                .accessDeniedHandler((request, response,
                                                                accessDeniedException) -> response.sendError(
                                                                                HttpServletResponse.SC_FORBIDDEN,
                                                                                accessDeniedException.getMessage())));
                // .headers(headers -> headers
                // .httpStrictTransportSecurity(hsts -> hsts
                // .includeSubDomains(true)
                // .maxAgeInSeconds(31536000))
                // .contentSecurityPolicy(csp -> csp // Remove 'unsafe-inline'
                // .policyDirectives(
                // "default-src 'self'; img-src 'self' data:; script-src 'self'"))
                // .permissionsPolicy(fp -> fp // Adjust microphone/camera if needed
                // .policy("geolocation 'self'; microphone 'none'; camera 'none'")));

                return http.build();
        }

        // @Bean
        // public CorsConfigurationSource corsConfigurationSource() {
        // CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(List.of(domainProtocol)); // Define allowed
        // origins
        // configuration.setAllowedMethods(List.of(
        // "GET",
        // "POST",
        // "PUT",
        // "DELETE",
        // "OPTIONS"));
        // configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control",
        // "Content-Type"));
        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // source.registerCorsConfiguration("/**", configuration);
        // return source;
        // }

}