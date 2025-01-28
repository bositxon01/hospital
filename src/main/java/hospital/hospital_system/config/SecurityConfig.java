package hospital.hospital_system.config;

import hospital.hospital_system.service.DbUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DbUserDetailsService dbUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.userDetailsService(dbUserDetailsService);

        http.authorizeHttpRequests(conf -> conf
                .requestMatchers(
                        "/auth/sign-up",
                        "/auth/login",
                        "/auth/verify-email",
                        "/auth/verification-success",
                        "**.html",
                        "/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        http.formLogin(conf -> conf
                .loginPage("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/auth/login"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
