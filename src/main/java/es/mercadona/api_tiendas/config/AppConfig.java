package es.mercadona.api_tiendas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import es.mercadona.api_tiendas.dto.AuthRequestDto;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${auth.username}")
    private String authUsername;

    private static final String AUTH_PASSWORD = "$2a$10$faD9bhQrEOtMRcEI5A7tjurvx4KjYVWvZgY6dGzXvzVCO0OuwM6Py";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            AuthRequestDto authRequest = simularConsultaUsuarioBD(username);
            return User.builder()
                    .username(authRequest.getUsername())
                    .password(authRequest.getPassword())
                    .build();
        };
    }

    private AuthRequestDto simularConsultaUsuarioBD(String username) {
        if (username.equals(authUsername)) {
            return AuthRequestDto.builder()
                    .username(authUsername)
                    .password(AUTH_PASSWORD)
                    .build();
        } else {
            throw new ApiTiendasException("Usuario o contraseña incorrectos");
        }
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}