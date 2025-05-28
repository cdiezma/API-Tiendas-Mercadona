package es.mercadona.api_tiendas.dto;

import lombok.Data;

@Data
public class AsignacionDto {

    private Long idTrabajador;
    private Long idSeccion;
    private Integer horas;
}
