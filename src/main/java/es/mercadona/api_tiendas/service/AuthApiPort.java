package es.mercadona.api_tiendas.service;

import es.mercadona.api_tiendas.dto.AuthRequestDto;
import es.mercadona.api_tiendas.dto.AuthResponseDto;

public interface AuthApiPort {

    AuthResponseDto authenticate(AuthRequestDto loginRequest);
}
