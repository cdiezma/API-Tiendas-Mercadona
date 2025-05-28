package es.mercadona.api_tiendas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeccionGridDto {

    private String nombre;
    private List<AsignacionGridDto> trabajadores;

}
