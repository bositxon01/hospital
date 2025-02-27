package hospital.hospital_system.security;

import hospital.hospital_system.entity.User;
import hospital.hospital_system.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static hospital.hospital_system.security.JWTProvider.PERMISSIONS;
import static hospital.hospital_system.utils.AuthConstants.BASIC_PREFIX;
import static hospital.hospital_system.utils.AuthConstants.BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {

            String token = authorization.substring(7);

            Claims claims = jwtProvider.extractClaims(token);

            String username = claims.getSubject();

            List<String> permissions = extractPermissions(claims);

            Optional<User> optionalUser = Optional.ofNullable((User) authService.loadUserByUsername(username));

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                List<SimpleGrantedAuthority> authorities = permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var authentication =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // Basic Auth handling
        else if (Objects.nonNull(authorization) && authorization.startsWith(BASIC_PREFIX)) {
            String basicToken = authorization.substring(6);

            String decodedCredentials = new String(Base64.getDecoder().decode(basicToken));

            String[] credentials = decodedCredentials.split(":");

            if (credentials.length == 2) {
                String username = credentials[0];
                String password = credentials[1];

                Optional<User> optionalUser = Optional.ofNullable((User) authService.loadUserByUsername(username));

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    if (passwordEncoder.matches(password, user.getPassword())) {
                        var authentication =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private List<String> extractPermissions(Claims claims) {
        Object permissionsObject = claims.get(PERMISSIONS);

        if (permissionsObject instanceof List<?>) {
            return ((List<?>) permissionsObject).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}