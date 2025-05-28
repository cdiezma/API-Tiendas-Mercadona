package es.mercadona.api_tiendas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrabajadorDto {

    private String nombre;

    private String apellidos;

    private String identificacion;

    private String codigoTienda;

    private Integer horasTotales;

}
