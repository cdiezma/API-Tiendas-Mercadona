package es.mercadona.api_tiendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.mercadona.api_tiendas.dto.AuthRequestDto;
import es.mercadona.api_tiendas.service.AuthApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación", description = "Servicio de autenticación de usuarios")
@RestController
public class AuthController {

    @Autowired
    private AuthApiPort authApiPort;

    @Operation(summary = "Autentica un usuario", description = "Permite autenticar un usuario y obtener un token de acceso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales de autenticación", required = true, content = @Content(schema = @Schema(implementation = AuthRequestDto.class))) @RequestBody AuthRequestDto authDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authApiPort.authenticate(authDto));
    }
}
