package com.ai_project.dataart.security;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.UserRepository;
import com.ai_project.dataart.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Перевіряємо, чи є заголовок Authorization і чи починається він з "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Відкидаємо "Bearer " (7 символів)
        username = jwtService.extractUsername(jwt);

        // Якщо ім'я є в токені, а користувач ще не автентифікований у поточному контексті Spring
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Завантажуємо юзера з БД
            User user = userRepository.findByUsername(username).orElse(null);

            // Якщо юзер існує і токен валідний
            if (jwtService.isTokenValid(jwt, user.getUsername())) {
                String roleFromToken = jwtService.extractRole(jwt); // Дістаємо ADMIN із токена
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleFromToken));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities // Тепер тут точно ROLE_ADMIN, бо ми взяли це з токена
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Зберігаємо інфу про юзера в контексті
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}