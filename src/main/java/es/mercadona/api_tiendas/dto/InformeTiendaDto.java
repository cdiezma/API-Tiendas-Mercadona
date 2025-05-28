package es.mercadona.api_tiendas.dto;

import java.util.List;

import lombok.Data;

@Data
public class InformeTiendaDto {

    private String nombre;
    private String direccion;
    private List<SeccionGridDto> secciones;

}
