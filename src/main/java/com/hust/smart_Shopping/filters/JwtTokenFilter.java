package com.hust.smart_Shopping.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hust.smart_Shopping.components.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response); // enable bypass
                return;
            }

            String authHeader = request.getHeader("Authorization");
            String token;
            String email;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            token = authHeader.substring(7);
            email = jwtTokenUtil.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(email);
                if (jwtTokenUtil.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    // isByPass: không yêu cầu token
    private boolean isByPassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(

                Pair.of(String.format("%s/roles**", apiPrefix), "GET"),
                Pair.of(String.format("%s/user/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/user", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/refresh-token", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/send-verification-code", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/refresh-token", apiPrefix), "POST"),

                // mvc

                Pair.of("/auth/**", "GET"),
                Pair.of("/image/**", "GET"),
                Pair.of("/**", "GET"),
                Pair.of("/favicon.ico", "GET"),
                Pair.of("/user", "GET"),
                Pair.of("/css/**", "GET"),
                Pair.of("/js/**", "GET"),
                Pair.of("/libs/**", "GET"),
                // sagger-ui
                Pair.of("/v2/api-docs", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/webjars/**", "GET"),
                Pair.of("/swagger-resources/configuration/ui", "GET"),
                Pair.of("/swagger-resources/configuration/security", "GET"),
                Pair.of("/swagger-ui.html/**", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html/**", "GET"));

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> bypassToken : bypassTokens) {
            String path = bypassToken.getFirst();
            String method = bypassToken.getSecond();

            if (requestPath.matches(path.replace("**", ".*")) &&
                    requestMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }

}
