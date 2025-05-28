package es.mercadona.api_tiendas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeccionHorasLibresDto {

    private String nombre;
    private int horasNecesarias;
    private int horasDisponibles;
    private int horasAsignadas;

}
