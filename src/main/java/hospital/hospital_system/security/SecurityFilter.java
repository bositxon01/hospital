package hospital.hospital_system.security;

import hospital.hospital_system.entity.User;
import hospital.hospital_system.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            String username = jwtProvider.validateToken(token);
            User user = (User) authService.loadUserByUsername(username);

            if (Objects.nonNull(user)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder
                        .getContext().
                        setAuthentication(authentication);
            }

        }//basic auth
        else if (Objects.nonNull(authorization) && authorization.startsWith("Basic ")) {

            //Basic base64(username:password)
            String basicToken = authorization.substring(6);

            Base64.Decoder decoder = Base64.getDecoder();

            String usernamePassword = new String(decoder.decode(basicToken));

            String[] strings = usernamePassword.split(":");

            String username = strings[0];
            String password = strings[1];

            User user = (User) authService.loadUserByUsername(username);

            boolean matches = passwordEncoder.matches(password, user.getPassword());

            if (matches) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder
                        .getContext().
                        setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }
}
