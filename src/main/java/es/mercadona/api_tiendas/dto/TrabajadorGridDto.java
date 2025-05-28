package es.mercadona.api_tiendas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrabajadorGridDto {

    private Long id;

    private String nombre;

    private String apellidos;

    private String identificacion;

    private int horasTotales;

    private TiendaGridDto tienda;

    // private List<Asignacion> asignaciones;

}
