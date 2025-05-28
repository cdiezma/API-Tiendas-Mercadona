package es.mercadona.api_tiendas.dto;

import java.util.List;

import lombok.Data;

@Data
public class InformeTiendaSeccionesDto {

    private String nombre;
    private String direccion;
    private List<SeccionHorasLibresDto> secciones;
}
