package es.mercadona.api_tiendas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import es.mercadona.api_tiendas.config.JwtService;
import es.mercadona.api_tiendas.dto.AuthRequestDto;
import es.mercadona.api_tiendas.dto.AuthResponseDto;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.service.AuthApiPort;

@Service
public class AuthService implements AuthApiPort {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto loginRequest) {
        validateLoginRequest(loginRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = jwtService.generateToken(loginRequest.getUsername());
        return AuthResponseDto.builder().accessToken(token).build();
    }

    private void validateLoginRequest(AuthRequestDto loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw new ApiTiendasException("Usuario y contraseña son obligatorios");
        }
    }
}
